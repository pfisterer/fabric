/** 09.10.2011 23:18 */
package fabric.module.exi.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import de.uniluebeck.sourcegen.java.JSourceFile;

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
  
  /** Name for main application */
  private String applicationClassName = "Application";
  
  /** Class with EXI serializer and deserializer */
  private JClass applicationClass;

  /** Fully qualified name of the Java bean class (incl. package) */
  private String qualifiedBeanClassName;

  /** Name of the XML converter class */
  private String converterClassName;

  /**
   * Constructor creates class object for EXI serializer and
   * deserializer code.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public JavaEXICodeGen(Workspace workspace, Properties properties) throws Exception
  {
    this.workspace = workspace;
    this.properties = properties;
    
    // TODO: Set application name
    // this.applicationName = this.properties.getProperty(FabricEXIModule.APPLICATION_NAME_KEY);
    this.applicationClass = JClass.factory.create(JModifier.PUBLIC, this.applicationClassName);
    this.applicationClass.setComment(new JClassCommentImpl("The application's main class."));

    this.qualifiedBeanClassName = String.format("%s.%s",
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY));

    this.converterClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY) + "Converter";
  }

  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateCode() throws Exception
  {
    /*****************************************************************
     * Create main function for application
     *****************************************************************/
    
    JMethod main = this.createMainFunction();
    if (null != main)
    {
      this.applicationClass.add(main);
    }

    /*****************************************************************
     * Create class and method calls for XML converter
     *****************************************************************/

    JavaBeanConverter beanConverter = new JavaBeanConverter(this.properties);

    // Create source file for XML converter class
    JSourceFile jsf = workspace.getJava().getJSourceFile(
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.converterClassName);

    LOGGER.debug(String.format("Generated new source file '%s' for XML converter.", this.converterClassName));

    // Create XML converter class
    beanConverter.generateConverterClass(jsf);
    // Create method for XML serialization
    JMethod serialize = beanConverter.generateSerializeCall();
    if (null != serialize)
    {
      this.applicationClass.add(serialize);
    }

    // Create method for XML deserialization
    JMethod deserialize = beanConverter.generateDeserializeCall();
    if (null != deserialize)
    {
      this.applicationClass.add(deserialize);
    }

    /*****************************************************************
     * Create class and method calls for EXI converter
     *****************************************************************/

    // TODO: Create EXIConverter object
    // JavaEXIConverter exiConverter = new JavaEXIConverter(this.properties);

    // TODO: Generate class
    // ...

    // TODO: Create method calls and handle return values
    // ...
  }

  /**
   * Create source file and write it to language-specific workspace.
   *
   * @throws Exception Error during source file write-out
   */
  @Override
  public void writeSourceFile() throws Exception
  {
    // Create source file for main class of application
    JSourceFile jsf = workspace.getJava().getJSourceFile(
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.applicationClassName);
    
    // Add application class
    jsf.add(this.applicationClass);

    // Import bean class
    jsf.addImport(this.qualifiedBeanClassName);

    LOGGER.debug(String.format("Generated new source file '%s' for main application.", this.applicationClassName));
  }

  // TODO: Add comment
  private JMethod createMainFunction() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create("String[]", "args"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "main", jms);

    String methodBody = "// TODO: Add code with usage example here.";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Main function of the application."));

    return jm;
  }
}
