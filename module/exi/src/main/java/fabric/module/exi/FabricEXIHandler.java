/** 31.10.2011 19:25 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.SchemaHelper;

import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.FabricTypeGenModule;
import fabric.module.typegen.java.JavaTypeGen;

import fabric.module.exi.base.EXICodeGen;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

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

  /** List of XML lists with simple-typed items, where value-tags need to be fixed */
  private ArrayList<SimpleListData> fixSimpleLists;

  /** List of XML lists with non-simple-typed items, where value-tags need to be fixed */
  private ArrayList<NonSimpleListData> fixNonSimpleLists;

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
    this.fixSimpleLists = new ArrayList<SimpleListData>();
    this.fixNonSimpleLists = new ArrayList<NonSimpleListData>();
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
    if (null != type && !FSchemaTypeHelper.isList(type))
    {
      this.fixElements.add(new ElementData(parent.getName()));
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

    this.exiGenerator.generateCode(this.fixElements, this.fixArrays, this.fixSimpleLists, this.fixNonSimpleLists);
    this.exiGenerator.writeSourceFile();
  }

  // TODO: Add comment
  @Override
  public void startTopLevelElement(FElement element) throws Exception
  {
    LOGGER.debug("Called startTopLevelElement().");

    if (null != element)
    {
      this.fixLocalElementsInComplexTypes(element, true); // TODO: Is this call obsolete?
    }
  }

  // TODO: Add comment
  @Override
  public void startLocalElement(FElement element, FComplexType parent) throws Exception
  {
    LOGGER.debug("Called startLocalElement().");

    if (null != element && null != parent)
    {
      this.fixLocalElementsInComplexTypes(element, parent.isTopLevel()); // TODO: Add parent parameter
    }
  }

  // TODO: Add comment
  @Override
  public void startElementReference(FElement element) throws Exception
  {
    LOGGER.debug("Called startElementReference().");

    if (null != element)
    {
      this.fixLocalElementsInComplexTypes(element, true); // TODO: Is this call obsolete?
    }
  }

  private void fixLocalElementsInComplexTypes(FElement element, boolean isTopLevel)
  {
    if (!isTopLevel) // TODO: Use !isTopLevel here?
    {
      // Determine element type
      String typeName = "";
      boolean isCustomType = false;

      // Element is XSD base type (e.g. xs:string, xs:short, ...)
      if (SchemaHelper.isBuiltinTypedElement(element))
      {
        typeName = this.mapper.lookup(JavaTypeGen.getFabricTypeName(element.getSchemaType()));
        LOGGER.debug(String.format("Type '%s' is an XSD built-in type.", typeName));
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
        
        isCustomType = true;
      }
      
      LOGGER.debug("######################################## Checking " + element.getName() + " for array fixing."); // TODO: Remove
      
      if (isCustomType)
      {
        if (FSchemaTypeHelper.isArray(element))
        {
          LOGGER.debug("######################################## Fixing array within compley type."); // TODO: Remove
          this.fixArrays.add(new ArrayData(element.getName(), typeName, "values", typeName)); // TODO: parent.getName() as first argument
        }
        // TODO: Do we need to collect local elements of complex types as well?
        else
        {
          LOGGER.debug("######################################## Fixing local element within complex type."); // TODO: Remove
          this.fixElements.add(new ElementData(element.getName()));
        }
      }
    }
  }
}
