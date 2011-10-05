package fabric.module.exi.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JavaWorkspace;

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
  }

  @Override
  public void generateCode() throws Exception
  {
    JavaBeanConverter beanConverter = new JavaBeanConverter(this.properties);
    JavaEXIConverter exiConverter = new JavaEXIConverter(this.properties);

    /*****************************************************************
     * Create code for Java to XML conversion
     *****************************************************************/
    beanConverter.generateXMLToInstanceCode();

    /*****************************************************************
     * Create code for XML to Java conversion
     *****************************************************************/
    beanConverter.generateJavaToXMLCode();

    /*****************************************************************
     * Create code for EXI serialization
     *****************************************************************/
    exiConverter.generateSerializeCode();

    /*****************************************************************
     * Create code for EXI deserialization
     *****************************************************************/
    exiConverter.generateDeserializeCode();

    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeSourceFile() throws Exception
  {
    JavaWorkspace javaWorkspace = this.workspace.getJava();
    JSourceFile jsf = null;

    jsf.add(this.classObject);

    // TODO: Add required imports

    // TODO: Add name of source file here
    LOGGER.debug(String.format("Generated new source file '%s'.", "TODO name"));
  }
}
