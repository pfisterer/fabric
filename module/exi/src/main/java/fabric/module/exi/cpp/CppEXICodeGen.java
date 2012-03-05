/** 05.03.2012 15:27 */
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

  /** Name of the main application */
  private String applicationName;

  /** Source file with main application */
  private CppSourceFile application;

  /**
   * Constructor sets various class names and creates source
   * file for main application to do EXI serialization and
   * deserialization.
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

  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * The method arguments are used in the JavaEXICodeGen and should
   * not be relevant for the CppEXICodeGen implementation.
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
     * Create callback method that writes EXI stream to file
     *****************************************************************/

    CFun outputStream = this.generateOutputStreamFunction();
    if (null != outputStream)
    {
      this.application.add(outputStream);
      
      // Define name of EXI outfile as "BeanClassName.exi"
      this.application.addBeforeDirective(String.format("define OUTFILE_NAME \"%s_serialized.exi\"", this.beanClassName.toLowerCase()));
    }

    /*****************************************************************
     * Create class and method calls for EXI converter
     *****************************************************************/

    CppEXIConverter exiConverter = new CppEXIConverter(this.properties);
    
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

  /**
   * Create source file and write it to language-specific workspace.
   * In CppEXICodeGen this is entirely done in the generateCode()
   * method, because C++ main applications are no classes, but
   * contain procedural code.
   *
   * @throws Exception Error during source file write-out
   */
  @Override
  public void writeSourceFile() throws Exception
  {
    // Empty implementation
  }

  /**
   * Create main application in the C++ workspace and return
   * source file. This method adds all necessary comments,
   * includes and namespaces to the file.
   * 
   * @param applicationName Desired application name
   * 
   * @return CppSourceFile with main application
   * 
   * @throws Exception Error during code generation
   */
  private CppSourceFile createMainApplication(final String applicationName) throws Exception
  {
    CppSourceFile cppsf = workspace.getC().getCppSourceFile(applicationName);
    cppsf.setComment(new CCommentImpl("The EXI application's main file."));

    LOGGER.debug(String.format("Generated application source file '%s'.", cppsf.getFileName()));

    // Add includes and namespace
    cppsf.addLibInclude("cstdlib");
    cppsf.addLibInclude("cstdio");
    cppsf.addLibInclude("iostream");
    cppsf.addInclude(this.beanClassName + ".hpp");
    cppsf.addInclude("EXIStream.hpp");
    cppsf.addInclude(this.serializerClassName + ".hpp");
    cppsf.addUsingNamespace("std");

    return cppsf;
  }
  
  /**
   * Create callback method that is used as a function pointer,
   * when the EXI converter wants to write data to an output
   * stream.
   * 
   * @return CFun with definition of callback method
   * 
   * @throws Exception Error during code generation
   */
  private CFun generateOutputStreamFunction() throws Exception
  {
    CParam buffer = CParam.factory.create("void*", "buffer");
    CParam readSize = CParam.factory.create("mySize_t", "readSize");
    CFunSignature cfs = CFunSignature.factory.create(buffer, readSize);
    CFun writeFileOutputStream = CFun.factory.create("writeFileOutputStream", "mySize_t", cfs);
    
    String methodBody =
            "FILE *outfile = fopen(OUTFILE_NAME, \"wb\");\n\n" +
            "mySize_t result = fwrite(buffer, 1, readSize, outfile);\n" +
            "fclose(outfile);\n\n" +
            "return result;";
    
    writeFileOutputStream.appendCode(methodBody);
    writeFileOutputStream.setComment(new CCommentImpl("Write EXI stream to an output file."));
    
    return writeFileOutputStream;
  }

  /**
   * Create main function for application. The method initializes
   * the root container and an EXIStream object, so that one can
   * easily test the C++ EXI module or create a new application
   * based on the source file's code.
   * 
   * @return CFun with definition of the main function
   * 
   * @throws Exception Error during code generation
   */
  private CFun createMainFunction() throws Exception
  {    
    CParam argc = CParam.factory.create("int", "argc");
    CParam argv = CParam.factory.create("char*", "argv[]");
    CFunSignature mainSignature = CFunSignature.factory.create(argc, argv);
    CFun mainMethod = CFun.factory.create("main", "int", mainSignature);
    mainMethod.setComment(new CCommentImpl("Main function of the application."));

    String methodBody = String.format(
            "%s* %s = new %s();\n" +
            "%s* exiConverter = new %s();\n" +
            "EXIStream exiStream;\n\n" +
            "try {\n" +
            "\t// TODO: Add your custom initialization code here\n\n" +
            "\t// Write EXI header to stream\n" +
            "\t// TODO: exiConverter->generateHeader(exiStream);\n\n" +
            "\t// Serialize bean object with EXI\n" +
            "\ttoEXIStream(exiConverter, %s, &exiStream, writeFileOutputStream);\n" +
            "}\n" +
            "catch (const char* e) {\n" +
            "\tcout << e << endl;\n" +
            "}\n\n" +
            "delete %s;\n" +
            "delete exiConverter;\n\n" +
            "return EXIT_SUCCESS;",
            this.firstLetterCapital(this.beanClassName), this.beanClassName.toLowerCase(),
            this.firstLetterCapital(this.beanClassName), this.serializerClassName,
            this.serializerClassName, this.beanClassName.toLowerCase(),
            this.beanClassName.toLowerCase());
    
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
