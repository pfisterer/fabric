/** 08.11.2011 14:58 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.SchemaHelper;

import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.FabricTypeGenModule;
import fabric.module.typegen.java.JavaTypeGen;

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

  /** Properties object for module configuration */
  private Properties properties = null;

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
    this.properties = properties;
    
    this.exiGenerator = EXICodeGenFactory.getInstance().createEXICodeGen(
            properties.getProperty(FabricEXIModule.EXICODEGEN_NAME_KEY), workspace, properties);

    this.mapper = MapperFactory.getInstance().createMapper(properties.getProperty(FabricTypeGenModule.MAPPER_CLASS_KEY));

    this.fixElements = new ArrayList<ElementData>();
    this.fixArrays = new ArrayList<ArrayData>();
    this.fixLists = new ArrayList<ListData>();
  }

  /**
   * Handle start of a top-level simple type. We need to collect
   * the names of all simple types here to fix the corresponding
   * value-tags in the XML document later on.
   *
   * @param type FSimpleType object
   * @param parent Parent FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startTopLevelSimpleType().");

    // Element is of simple type and not a list
    if (null != type && !FSchemaTypeHelper.isEnum(type))
    {
      LOGGER.debug("######################################## Checking '" + type.getName() + "' for value-tag fixing."); // TODO: Remove

      if (FSchemaTypeHelper.isList(type))
      {
        FList listType = (FList)type;
        String typeName = this.mapper.lookup(JavaTypeGen.getFabricTypeName(listType.getItemType()));

        // TODO: Check last argument: Simple types are never custom-typed, right?
        ListData listToFix = new ListData(type.getName(), typeName, typeName, true);
        if (!this.fixLists.contains(listToFix))
        {
          LOGGER.debug("######################################## Fixing list within top-level simple type."); // TODO: Remove
          this.fixLists.add(listToFix);
        }
      }
      else
      {
        ElementData elementToFix = new ElementData(parent.getName());
        if (!this.fixElements.contains(elementToFix))
        {
          LOGGER.debug("######################################## Fixing element within top-level simple type."); // TODO: Remove
          this.fixElements.add(elementToFix);
        }
      }
    }
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

  // TODO: Also fix lists in topLevelElements?

  // TODO: Add comment
  @Override
  public void startLocalElement(FElement element, FComplexType parent) throws Exception
  {
    LOGGER.debug("Called startLocalElement().");

    if (null != element && null != parent)
    {
      this.fixElementsInComplexType(element, parent);
    }
  }
  
  // TODO: Add comment
  private void fixElementsInComplexType(FElement element, FComplexType parent)
  {
    // Determine element type
    String typeName = "";
    boolean isCustomTyped;

    // Element is XSD base type (e.g. xs:string, xs:short, ...)
    if (SchemaHelper.isBuiltinTypedElement(element))
    {
      typeName = this.mapper.lookup(JavaTypeGen.getFabricTypeName(element.getSchemaType()));
      LOGGER.debug(String.format("Type '%s' is an XSD built-in type.", typeName));

      isCustomTyped = false;
    }
    // Element is custom type (e.g. some XSD base type itm:Simple02)
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

    LOGGER.debug("######################################## Checking '" + element.getName() + "' for value-tag fixing."); // TODO: Remove

    // Always fix element arrays
    if (FSchemaTypeHelper.isArray(element))
    {
      String parentContainerName = String.format("%s.%sType", properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY), parent.getName());
      ArrayData arrayToFix = new ArrayData(parentContainerName, element.getName(), typeName, "values", typeName, isCustomTyped);
      if (!this.fixArrays.contains(arrayToFix))
      {
        LOGGER.debug("######################################## Fixing array within complex type."); // TODO: Remove
        this.fixArrays.add(arrayToFix);
      }
    }
    else if (FSchemaTypeHelper.isList(element))
    {
      ListData listToFix = new ListData(element.getName(), typeName, typeName, isCustomTyped);
      if (this.fixLists.contains(listToFix))
      {
        LOGGER.debug("######################################## Fixing list within complex type."); // TODO: Remove
        this.fixLists.add(listToFix);
      }
    }
    // Only fix custom-typed elements
    else if (isCustomTyped)
    {
      ElementData elementToFix = new ElementData(element.getName());
      if (!this.fixElements.contains(elementToFix))
      {
        LOGGER.debug("######################################## Fixing local element within complex type."); // TODO: Remove
        this.fixElements.add(elementToFix);
      }
    }
  }
}
