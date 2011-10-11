/** 11.10.2011 12:02 */
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
import java.util.ArrayList;

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
  private String applicationClassName;
  
  /** Class with EXI serializer and deserializer */
  private JClass applicationClass;
  
  /** Name of the package in which the bean class resides */
  private String packageName;
  
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
    
    this.applicationClassName = this.properties.getProperty(FabricEXIModule.APPLICATION_CLASS_NAME_KEY);
    this.applicationClass = JClass.factory.create(JModifier.PUBLIC, this.applicationClassName);
    this.applicationClass.setComment(new JClassCommentImpl("The application's main class."));
    
    this.packageName = this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY);
    if (this.properties.containsKey(FabricEXIModule.PACKAGE_NAME_ALT_KEY))
    {
      this.packageName = this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_ALT_KEY);
    }
    
    this.qualifiedBeanClassName = String.format("%s.%s",
            this.packageName,
            this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY));

    this.converterClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY) + "Converter";
  }

  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * 
   * @param fixElements Elements where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateCode(final ArrayList<String> fixElements) throws Exception
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
    beanConverter.generateConverterClass(jsf, fixElements);
    
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

    // TODO: JavaEXIConverter exiConverter = new JavaEXIConverter(this.properties);

    // TODO: Create EXI converter class
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
    
    // Only import bean class, if it resides in a different package
    if (!this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY).equals(this.packageName))
    {
      jsf.addImport(this.qualifiedBeanClassName);
    }
    
    LOGGER.debug(String.format("Generated new source file '%s' for main application.", this.applicationClassName));
  }

  /**
   * Private helper method to create a main function for the application.
   * We add some example code here to demonstrate the usage of the XML
   * converter and the EXI de-/serialization class.
   * 
   * @return JMethod with main function
   * 
   * @throws Exception Error during code generation
   */
  private JMethod createMainFunction() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create("String[]", "args"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "main", jms);

    String beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);
    
    // TODO: Add code with usage example here
    String methodBody = String.format(
            "// Instanziate application\n" +
            "%s application = new %s();\n\n" +
            "// Create instance of the Java bean class\n" +
            "%s %s = new %s();\n\n" +
            "try {\n" +
            "\t// Convert bean instance to XML document\n" +
            "\tString xmlDocument = application.toXML(%s);\n\n" +
            "\tSystem.out.println(xmlDocument);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            applicationClassName, applicationClassName,
            beanClassName, beanClassName.toLowerCase(), beanClassName, beanClassName.toLowerCase());
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Main function of the application."));

    return jm;
  }
}
