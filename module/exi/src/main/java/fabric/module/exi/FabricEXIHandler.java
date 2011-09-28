/** 28.09.2011 14:59 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.wsdlschemaparser.schema.FSchema;

import fabric.module.exi.base.EXICodeGen;

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

  /**
   * Constructor initializes the language-specific EXI code generator.
   * 
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   * 
   * @throws Exception  Error during EXI generator creation
   */
  public FabricEXIHandler(Workspace workspace, Properties properties) throws Exception
  {
    this.exiGenerator = EXICodeGenFactory.getInstance().createEXICodeGen(
            properties.getProperty("exi.generator_name"), workspace, properties);
  }

  /**
   * Handle start of an XML schema document. Here we create the entire
   * EXI serializer and deserializer class, because we do not need any
   * XML schema-specific information (except for the name of the main
   * class, which is provided in the properties file).
   *
   * @param schema FSchema object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called startSchema().");

    this.exiGenerator.generateCode();
  }

  /**
   * Handle end of an XML schema document. Here we write all
   * source files to the language-specific workspace.
   *
   * @param schema FSchema object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void endSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called endSchema().");

    this.exiGenerator.writeSourceFile();
  }
}
