package fabric.module.exi.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JavaWorkspace;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.base.EXICodeGen;

/**
 * EXI code generator for Java.
 * 
 * @author seidel
 */
public class JavaEXICodeGen implements EXICodeGen
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaEXICodeGen.class);

  /** Workspace object for source code write-out */
  private Workspace workspace;

  /** Properties object with module configuration */
  private Properties properties;

  /** Class with EXI serializer and deserializer */
  private JClass classObject;

  /**
   * Constructor creates mapper for simple data types and initializes
   * various member variables.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public JavaEXICodeGen(Workspace workspace, Properties properties) throws Exception
  {
    this.workspace = workspace;
    this.properties = properties;
    
    this.classObject = JClass.factory.create(JModifier.PUBLIC, "EXIficator"); // TODO: Set proper class name
  }

  @Override
  public void generateCode() throws Exception
  {
    /*****************************************************************
     * Create class and method calls for XML converter
     *****************************************************************/
    JavaBeanConverter beanConverter = new JavaBeanConverter(this.properties);
    
    // TODO: Handle return values
    workspace.getJava().getJSourceFile(
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.properties.getProperty(FabricEXIModule.GENERATOR_NAME_KEY)) // TODO: Create proper file name
            .add(beanConverter.generateConverterClass());
    
    this.classObject.add(beanConverter.generateSerializeCall());
    this.classObject.add(beanConverter.generateDeserializeCall());

    /*****************************************************************
     * Create class and method calls for EXI converter
     *****************************************************************/
    // TODO JavaEXIConverter exiConverter = new JavaEXIConverter(this.properties);
    
    // TODO: Handle return values
    // exiConverter.generateSerializeCode();
    // exiConverter.generateDeserializeCode();
  }

  @Override
  public void writeSourceFile() throws Exception
  {
    JavaWorkspace javaWorkspace = this.workspace.getJava();
    JSourceFile jsf = javaWorkspace.getJSourceFile(
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            "EXIficator"); // TODO: Set proper file name
    
    jsf.add(this.classObject);

    // TODO: Add required imports

    // TODO: Add name of source file here
    LOGGER.debug(String.format("Generated new source file '%s'.", "TODO name"));
  }
}
