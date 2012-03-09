/** 09.03.2012 12:17 */
package fabric.module.exi.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ArrayList;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import de.uniluebeck.sourcegen.java.JSourceFile;

import fabric.wsdlschemaparser.schema.FElement;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.base.EXICodeGen;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

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
  
  /** Class with main application */
  private JClass applicationClass;
  
  /** Name of the package in which the bean class resides */
  private String packageName;
  
  /** Fully qualified name of the Java bean class (incl. package) */
  private String qualifiedBeanClassName;

  /** Name of the XML converter class */
  private String converterClassName;
  
  /** Name of the EXI de-/serializer class */
  private String serializerClassName;

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
    
    this.serializerClassName = "EXIConverter";
  }

  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateCode(ArrayList<ElementData> fixElements,
                           ArrayList<ArrayData> fixArrays,
                           ArrayList<ListData> fixLists) throws Exception
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
    beanConverter.generateConverterClass(jsf, fixElements, fixArrays, fixLists);
    
    // Create method for XML serialization
    JMethod xmlSerialize = beanConverter.generateSerializeCall();
    if (null != xmlSerialize)
    {
      this.applicationClass.add(xmlSerialize);
    }

    // Create method for XML deserialization
    JMethod xmlDeserialize = beanConverter.generateDeserializeCall();
    if (null != xmlDeserialize)
    {
      this.applicationClass.add(xmlDeserialize);
    }

    /*****************************************************************
     * Create class and method calls for EXI converter
     *****************************************************************/
    
    JavaEXIConverter exiConverter = new JavaEXIConverter(this.properties);
    
    // Create source file for the EXI de-/serializer class
    jsf = workspace.getJava().getJSourceFile(
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.serializerClassName);
    
    LOGGER.debug(String.format("Generated new source file '%s' for EXI de-/serializer.", this.serializerClassName));
    
    // Create EXI de-/serializer class
    exiConverter.generateSerializerClass(jsf);
    
    // Create method for EXI serialization
    JMethod exiSerialize = exiConverter.generateSerializeCall();
    if (null != exiSerialize)
    {
      this.applicationClass.add(exiSerialize);
    }
    
    // Create method for EXI deserialization
    JMethod exiDeserialize = exiConverter.generateDeserializeCall();
    if (null != exiDeserialize)
    {
      this.applicationClass.add(exiDeserialize);
    }
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
    
    // Import bean class, if it resides in a different package
    if (!this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY).equals(this.packageName))
    {
      jsf.addImport(this.qualifiedBeanClassName);
    }
    
    LOGGER.debug(String.format("Generated new source file '%s' for main application.", this.applicationClassName));
  }

  /**
   * Handle top level element from XML Schema document. We
   * do not need to build any EXI gramma in the Java code
   * generator, so this method is left empty.
   * 
   * @param element Top level element to handle
   */
  @Override
  public void handleTopLevelElement(FElement element)
  {
    // Empty implementation
  }

  /**
   * Handle local element from XML Schema document. We
   * do not need to build any EXI gramma in the Java code
   * generator, so this method is left empty.
   * 
   * @param element Local element to handle
   */
  @Override
  public void handleLocalElement(FElement element)
  {
    // Empty implementation
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
    
    // Add code with usage example
    String methodBody = String.format(
            "// Instantiate application\n" +
            "%s application = new %s();\n\n" +
            "// Create instance of the Java bean class\n" +
            "%s %s = new %s();\n\n" +
            "// TODO: Add your custom initialization code here\n\n" +
            "try {\n" +
            "\t// Convert bean instance to XML document\n" +
            "\tString xmlDocument = application.toXML(%s);\n\n" +
            "\t// Print XML document for debug purposes\n" +
            "\tSystem.out.println(xmlDocument);\n" +
            "\n\t// TODO: Add your custom EXI de-/serialization code here\n" +
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
