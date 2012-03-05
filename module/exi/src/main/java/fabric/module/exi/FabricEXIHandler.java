/** 11.11.2011 20:18 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.SchemaHelper;

import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.base.TypeGenHelper;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.FabricTypeGenModule;

import fabric.module.exi.base.EXICodeGen;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * Fabric handler class for the EXI module. This class defines
 * a couple of callback methods which get called by the treewalker
 * while processing an XSD file. The FabricEXIHandler acts upon
 * those function calls and generates a class for EXI serialization
 * and deserialization accordingly.
 * 
 * @author seidel
 */
public class FabricEXIHandler extends FabricDefaultHandler
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(FabricEXIHandler.class);

  /** EXICodeGen object for EXI class generation */
  private EXICodeGen exiGenerator;

  /** Mapper object for simple data types */
  private Mapper mapper;

  /** List of XML elements, where value-tags need to be fixed */
  private ArrayList<ElementData> fixElements;

  /** List of XML arrays, where value-tags need to be fixed */
  private ArrayList<ArrayData> fixArrays;

  /** List of XML lists, where value-tags need to be fixed */
  private ArrayList<ListData> fixLists;

  /**
   * Constructor initializes the language-specific EXI code generator.
   * 
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   * 
   * @throws Exception Error during EXI generator creation
   */
  public FabricEXIHandler(Workspace workspace, Properties properties) throws Exception
  {
    this.exiGenerator = EXICodeGenFactory.getInstance().createEXICodeGen(
            properties.getProperty(FabricEXIModule.EXICODEGEN_NAME_KEY), workspace, properties);

    this.mapper = MapperFactory.getInstance().createMapper(properties.getProperty(FabricTypeGenModule.MAPPER_CLASS_KEY));

    this.fixElements = new ArrayList<ElementData>();
    this.fixArrays = new ArrayList<ArrayData>();
    this.fixLists = new ArrayList<ListData>();
  }

  /**
   * Handle end of an XML schema document. After processing the
   * entire XSD file, we create the EXI serializer and deserializer
   * class and write it to the language-specific workspace.
   *
   * @param schema FSchema object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void endSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called endSchema().");

    this.exiGenerator.generateCode(this.fixElements, this.fixArrays, this.fixLists);
    this.exiGenerator.writeSourceFile();
  }

  /**
   * Handle start of a top-level schema element. As each top-level
   * element is equivalent to a member variable in the corresponding
   * container class, we have to check whether value-tags need to
   * be fixed for the specific XML element.
   * 
   * @param element FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startTopLevelElement(FElement element) throws Exception
  {
    LOGGER.debug("Called startTopLevelElement().");

    exiGenerator.handleElement(element);

    if (null != element)
    {
      this.fixElementIfRequired(element);
    }
  }

  /**
   * Handle start of a local schema element. Local elements only
   * apply to complex types. Each local element is equivalent to
   * a member variable in the corresponding container class, so
   * we have to check whether value-tags need to be fixed for the
   * specific XML element.
   *
   * @param element FElement object
   * @param parent Parent FComplexType object
   * 
   * @throws Exception Error during processing
   */
  @Override
  public void startLocalElement(FElement element, FComplexType parent) throws Exception
  {
    LOGGER.debug("Called startLocalElement().");

    exiGenerator.handleElement(element);

    if (null != element)
    {
      this.fixElementIfRequired(element);
    }
  }

  /**
   * This method checks whether value-tags need to be fixed for
   * a particular XML element or not. In case the element needs
   * to be fixed, its data is added to the corresponding field
   * (e.g. fixElements, fixArrays or fixLists) and true is
   * returned. Otherwise simply nothing happens and the method
   * returns false.
   * 
   * @param element FElement object
   * 
   * @return True if element was fixed, false otherwise
   */
  private boolean fixElementIfRequired(FElement element)
  {
    boolean elementWasFixed = false;
    
    // Determine element type
    String typeName = "";
    boolean isCustomTyped;
    
    // Element is XSD base-typed (e.g. xs:string, xs:short, ...)
    if (SchemaHelper.isBuiltinTypedElement(element))
    {
      FSchemaType schemaType = null;
      
      // Get item type, if element is a list
      if (FSchemaTypeHelper.isList(element))
      {
        FList listType = (FList)element.getSchemaType();
        schemaType = listType.getItemType();
      }
      else
      {
        schemaType = element.getSchemaType();
      }
      
      typeName = this.mapper.lookup(TypeGenHelper.getFabricTypeName(schemaType));
      LOGGER.debug(String.format("Type '%s' is an XSD built-in type.", typeName));
      
      // Convert Java primitives to belonging wrapper class
      typeName = this.fixPrimitiveTypes(typeName);
      
      isCustomTyped = false;
    }
    // Element is custom-typed (e.g. itm:Simple02)
    else
    {
      typeName = element.getSchemaType().getName();
      LOGGER.debug(String.format("Type '%s' is a custom type.", typeName));
      
      // Create artificial name for local complex type (i.e. an inner class)
      if (!element.getSchemaType().isTopLevel() && !element.getSchemaType().isSimple())
      {
        typeName += "Type";
      }

      isCustomTyped = true;
    }
    
    LOGGER.debug(String.format("Checking element '%s' for value-tag fixing...", element.getName()));
    
    // Save data of simple-typed elements for later fixing
    if (null != element.getSchemaType() && element.getSchemaType().isSimple() && !FSchemaTypeHelper.isEnum(element.getSchemaType()))
    {
      // Element is an array
      if (FSchemaTypeHelper.isArray(element))
      {
        ArrayData arrayToFix = new ArrayData(element.getName(), typeName, "values", typeName, isCustomTyped);
        if (!this.fixArrays.contains(arrayToFix))
        {
          LOGGER.debug(String.format("Fixing%s array '%s' within complex type.", (isCustomTyped ? " custom-typed" : ""), element.getName()));
          this.fixArrays.add(arrayToFix);
          elementWasFixed = true;
        }
      }
      // Element is a list
      else if (FSchemaTypeHelper.isList(element))
      {
        ListData listToFix = new ListData(element.getName(), typeName, typeName, isCustomTyped);
        if (!this.fixLists.contains(listToFix))
        {
          LOGGER.debug(String.format("Fixing%s list '%s'.", (isCustomTyped ? " custom-typed" : ""), element.getName()));
          this.fixLists.add(listToFix);
          elementWasFixed = true;
        }
      }
      // Element is custom-typed
      else if (isCustomTyped)
      {
        ElementData elementToFix = new ElementData(element.getName());
        if (!this.fixElements.contains(elementToFix))
        {
          LOGGER.debug(String.format("Fixing custom-types element '%s'.", element.getName()));
          this.fixElements.add(elementToFix);
          elementWasFixed = true;
        }
      }
    }

    return elementWasFixed;
  }

  /**
   * Private helper method to convert Java primitives (e.g. int,
   * double or boolean) to their belonging wrapper classes (e.g.
   * Integer, Double or Boolean). This is useful when handling
   * Java collections, for example, since they rely on entries
   * to be derived from Object.
   *
   * @param typeName Name of (possibly primitive) type
   *
   * @return Belonging wrapper class name or unchanged argument,
   * if it was not the name of a Java primitive type
   */
  private String fixPrimitiveTypes(final String typeName)
  {
    // Initial assumption: Type is not a Java primitive
    String result = typeName;

    // Map of wrapper classes for Java primitives
    HashMap<String, String> wrapperClasses = new HashMap<String, String>();
    wrapperClasses.put("byte", "Byte");
    wrapperClasses.put("short", "Short");
    wrapperClasses.put("int", "Integer");
    wrapperClasses.put("long", "Long");
    wrapperClasses.put("float", "Float");
    wrapperClasses.put("double", "Double");
    wrapperClasses.put("char", "Character");
    wrapperClasses.put("boolean", "Boolean");

    // If we have a Java primitive, translate it to wrapper class
    if (wrapperClasses.containsKey(typeName))
    {
      result = wrapperClasses.get(typeName);
    }

    return result;
  }
}
