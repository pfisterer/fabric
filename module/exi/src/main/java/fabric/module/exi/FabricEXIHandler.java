/** 28.09.2011 19:47 */
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

    this.exiGenerator.generateCode();
    this.exiGenerator.writeSourceFile();
  }
}
