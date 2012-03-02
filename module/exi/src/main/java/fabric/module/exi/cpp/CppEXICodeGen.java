/** 27.02.2012 16:00 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ArrayList;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CFunSignature;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.CppSourceFile;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.base.EXICodeGen;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * EXI code generator for C++.
 *
 * @author seidel
 */
public class CppEXICodeGen implements EXICodeGen
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(CppEXICodeGen.class);

  /** Workspace object for source code write-out */
  private Workspace workspace;

  /** Properties object with module configuration */
  private Properties properties;

  /** Name of the bean class */
  private String beanClassName;

  /** Name of the EXI de-/serializer class */
  private String serializerClassName;

  /** Name for main application */
  private String applicationName;

  /** Source file with main application */
  private CppSourceFile application;

  /**
   * Constructor creates class object for EXI serializer and
   * deserializer code.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public CppEXICodeGen(Workspace workspace, Properties properties) throws Exception
  {
    this.workspace = workspace;
    this.properties = properties;
    
    this.beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);
    
    this.serializerClassName = "EXIConverter";
    
    // Create source file for application
    this.applicationName = this.properties.getProperty(FabricEXIModule.APPLICATION_CLASS_NAME_KEY);
    this.application = this.createMainApplication(this.applicationName);
  }

  // TODO: Add implementation and comment
  @Override
  public void generateCode(ArrayList<ElementData> fixElements,
                           ArrayList<ArrayData> fixArrays,
                           ArrayList<ListData> fixLists) throws Exception
  {
    /*****************************************************************
     * Create class and method calls for EXI converter
     *****************************************************************/

    CppEXIConverter exiConverter = new CppEXIConverter(this.properties);

    // Create source file for EXI converter class
//    TODO: Remove? CppSourceFile cppsf = workspace.getC().getCppSourceFile(this.serializerClassName);

    LOGGER.debug(String.format("Generated new source file '%s' for EXI de-/serializer.", this.serializerClassName));
    
    // Create EXI de-/serializer class
    exiConverter.generateSerializerClass(this.workspace);
    
    // Create method for EXI serialization
    CFun exiSerialize = exiConverter.generateSerializeCall();
    if (null != exiSerialize)
    {
      this.application.add(exiSerialize);
    }
    
    // Create method for EXI deserialization
    CFun exiDeserialize = exiConverter.generateDeserializeCall();
    if (null != exiDeserialize)
    {
      this.application.add(exiDeserialize);
    }

    /*****************************************************************
     * Create main function for application
     *****************************************************************/

    CFun main = this.createMainFunction();
    if (null != main)
    {
      this.application.add(main);
    }      
  }
  
  @Override
  public void writeSourceFile() throws Exception
  {
    // TODO: Add implementation and comment
  }

  // TODO: Add comment
  private CppSourceFile createMainApplication(final String applicationName) throws Exception
  {
    CppSourceFile cppsf = workspace.getC().getCppSourceFile(applicationName);
    cppsf.setComment(new CCommentImpl("The EXI application's main file."));
    
    LOGGER.debug(String.format("Generated application source file '%s'.", cppsf.getFileName()));
    
    // Add includes and namespace
    cppsf.addLibInclude("cstdlib");
    cppsf.addLibInclude("iostream");
    cppsf.addInclude(this.beanClassName + ".hpp");
    cppsf.addInclude(this.serializerClassName + ".hpp");
    cppsf.addUsingNamespace("std");
    
    return cppsf;
  }
  
  /**
   * Create source file for application. It will contain a main
   * method that initializes the root container name, so that
   * one can easily test the C++ type generator or create a
   * new application based on the source file's code.
   *
   * @throws Exception Error during enum generation
   */
  private CFun createMainFunction() throws Exception
  {    
    // Create main method
    CParam argc = CParam.factory.create("int", "argc");
    CParam argv = CParam.factory.create("char*", "argv[]");
    CFunSignature mainSignature = CFunSignature.factory.create(argc, argv);
    CFun mainMethod = CFun.factory.create("main", "int", mainSignature);
    mainMethod.setComment(new CCommentImpl("Main function of the application."));

    String methodBody = String.format(
            "%s* %s = new %s();\n\n"
            + "try {\n"
            + "\t// TODO: Add your custom initialization code here\n"
            + "}\n"
            + "catch (const char* e) {\n"
            + "\tcout << e << endl;\n"
            + "}\n\n"
            + "delete %s;\n\n"
            + "return EXIT_SUCCESS;",
            this.firstLetterCapital(this.beanClassName), this.beanClassName.toLowerCase(),
            this.firstLetterCapital(this.beanClassName), this.beanClassName.toLowerCase());
    
    mainMethod.appendCode(methodBody);
    
    return mainMethod;
  }

  /**
   * Private helper method to capitalize the first letter of a string.
   * Function will return null, if argument was null.
   *
   * @param text Text to process
   *
   * @return Text with first letter capitalized or null
   */
  private String firstLetterCapital(final String text)
  {
    return (null == text ? null : text.substring(0, 1).toUpperCase() + text.substring(1, text.length()));
  }
}
