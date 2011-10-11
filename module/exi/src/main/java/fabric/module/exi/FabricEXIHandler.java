/** 09.10.2011 22:35 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.wsdlschemaparser.schema.FSchema;

import fabric.module.exi.base.EXICodeGen;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.FSimpleType;
import java.util.ArrayList;

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
  
  /** List of elements where value-tags need to be fixed */
  private ArrayList<String> fixElements;

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
    
    this.fixElements = new ArrayList<String>();
  }

  /**
   * Handle start of a top-level simple type. We need to collect
   * the names of all simple types here, to fix the corresponding
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

    if (null != type)
    {
      this.fixElements.add(type.getName());
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

    this.exiGenerator.generateCode(this.fixElements);
    this.exiGenerator.writeSourceFile();
  }
}
