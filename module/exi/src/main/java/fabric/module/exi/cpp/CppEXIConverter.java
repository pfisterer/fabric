/** 05.03.2012 14:42 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CFunSignature;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppHeaderFile;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppVar;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.exceptions.FabricEXIException;

/**
 * CppEXIConverter class creates the EXI de-/serializer class
 * and generates methods for the application's main function
 * to demonstrate the usage of the converter.
 * 
 * @author seidel
 */
public class CppEXIConverter
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(CppEXIConverter.class);
  
  /** Properties object with module configuration */
  private Properties properties;
  
  /** Name of the bean class */
  private String beanClassName;
  
  /** Name of the EXI de-/serializer class */
  private String serializerClassName;
  
  /** EXI de-/serializer class */
  private CppClass serializerClass;

  /**
   * Parameterized constructor initializes properties object
   * and various other member variables.
   * 
   * @param properties Properties object with module options
   */
  public CppEXIConverter(Properties properties)
  {
    this.properties = properties;

    this.beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);
        
    this.serializerClassName = "EXIConverter";
  }

  /**
   * Public callback method that generates the EXI de-/serializer class
   * and adds the corresponding header and source file to the provided
   * Fabric workspace.
   * 
   * @param workspace Fabric workspace for code write-out
   * 
   * @throws Exception Workspace was null or error during code generation
   */
  public void generateSerializerClass(final Workspace workspace) throws Exception
  {
    if (null == workspace)
    {
      throw new FabricEXIException("Cannot create EXI de-/serializer class. Workspace is null.");
    }
    else
    {
      // Generate EXIStream class
      CppEXIStreamGenerator.init(workspace);

      // Generate EXITypeEncoder class
      CppEXITypeEncoderGenerator.init(workspace);

      // Generate EXITypeDecoder class
      CppEXITypeDecoderGenerator.init(workspace);

      // Generate EXIConverter class
      this.serializerClass = CppClass.factory.create(this.serializerClassName);
      this.generateEXIHeaderGenerationCode();
      this.generateSerializeCode();
      this.generateDeserializeCode();

      /*****************************************************************
       * Create C++ header file
       *****************************************************************/
      CppHeaderFile cpphf = workspace.getC().getCppHeaderFile(this.serializerClassName);

      cpphf.setComment(new CCommentImpl(String.format("The '%s' header file.", this.serializerClassName)));
      cpphf.add(this.serializerClass);

      // Add includes
      cpphf.addInclude(this.beanClassName + ".hpp");
      cpphf.addInclude(CppEXIStreamGenerator.FILE_NAME + ".hpp");
      cpphf.addInclude(CppEXITypeEncoderGenerator.FILE_NAME + ".hpp");

      // Add include guards to header file
      cpphf.addBeforeDirective("ifndef " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addBeforeDirective("define " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addAfterDirective("endif // " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));

      LOGGER.debug(String.format("Generated new header file '%s'.", this.serializerClassName));

      /*****************************************************************
       * Create C++ source file
       *****************************************************************/
      CppSourceFile cppsf = workspace.getC().getCppSourceFile(this.serializerClassName);

      cppsf.setComment(new CCommentImpl(String.format("The '%s' source file.", this.serializerClassName)));

      // Add includes
      cppsf.addInclude(cpphf);
      cppsf.addLibInclude("cstdio");

      // Add constant definition
      cppsf.addBeforeDirective("define OUTPUT_BUFFER_SIZE 100");

      LOGGER.debug(String.format("Generated new source file '%s'.", this.serializerClassName));
    }
  }

  /**
   * Generate code that adds an EXI header to the EXI stream
   * in the C++ EXI converter class.
   * 
   * @throws Exception Error during code generation
   */
  private void generateEXIHeaderGenerationCode() throws Exception
  {
    CppVar streamObject = CppVar.factory.create(Cpp.NONE, "EXIStream*", "exiStream");
    CppFun generateHeader = CppFun.factory.create("void", "generateHeader", streamObject);
    
    // TODO: Add method body here
    String methodBody =
            "// TODO: Add code to write EXI header to stream here";
    
    generateHeader.appendCode(methodBody);
    generateHeader.setComment(new CCommentImpl("Write header to EXI byte stream."));
    
    this.serializerClass.add(Cpp.PUBLIC, generateHeader);
  }

  /**
   * Generate code for EXI serialization with C++. The code is
   * created individually for each bean object.
   * 
   * @throws Exception Error during code generation
   */
  private void generateSerializeCode() throws Exception
  {
    CppVar typeObject = CppVar.factory.create(Cpp.NONE, this.beanClassName + "*", "typeObject");
    CppVar streamObject = CppVar.factory.create(Cpp.NONE, "EXIStream*", "exiStream");
    CppVar functionPointer = CppVar.factory.create(Cpp.NONE, "mySize_t", "(*outputFunction)(void*, mySize_t)");
    CppFun serialize = CppFun.factory.create("void", "serialize", typeObject, streamObject, functionPointer);
    
    String methodBody =
            "char buffer[OUTPUT_BUFFER_SIZE];\n" +
            "IOStream outputStream;\n" +
            "EXITypeEncoder encoder;\n\n" +
            "// Use function pointer to define external output stream\n" +
            "outputStream.readWriteToStream = outputFunction;\n\n" +
            "exiStream->initStream(buffer, OUTPUT_BUFFER_SIZE, outputStream);\n\n" +
            "encoder.encodeInteger(exiStream, 3000);\n\n" +
            "exiStream->closeStream();";
    
    serialize.appendCode(methodBody);
    serialize.setComment(new CCommentImpl(String.format("Serialize %s object to EXI byte stream.", this.beanClassName)));
    
    this.serializerClass.add(Cpp.PUBLIC, serialize);
  }

  /**
   * Generate code for EXI deserialization with C++. The code is
   * created individually for each bean object.
   * 
   * @throws Exception Error during code generation
   */
  private void generateDeserializeCode() throws Exception
  {
    CppVar streamObject = CppVar.factory.create(Cpp.NONE, "EXIStream*", "exiStream");
    CppVar typeObject = CppVar.factory.create(Cpp.NONE, this.beanClassName + "*", "typeObject");
    CppFun deserialize = CppFun.factory.create("void", "deserialize", streamObject, typeObject);
    
    // TODO: Add method body here
    String methodBody =
            "// TODO: Add code to deserialize object here";
    
    deserialize.appendCode(methodBody);
    deserialize.setComment(new CCommentImpl(String.format("Deserialize EXI byte stream to %s object.", this.beanClassName)));
    
    this.serializerClass.add(Cpp.PUBLIC, deserialize);
  }

  /**
   * Public callback method that creates code to operate the
   * EXI de-/serializer class. The generated code demonstrates
   * how to serialize a bean object with EXI.
   * 
   * @return CFun object with serialization code
   * 
   * @throws Exception Error during code generation
   */
  public CFun generateSerializeCall() throws Exception
  {
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CParam streamObject = CParam.factory.create("EXIStream*", "exiStream");
    CParam functionPointer = CParam.factory.create("mySize_t", "(*outputFunction)(void*, mySize_t)");
    CFunSignature cfs = CFunSignature.factory.create(typeObject, streamObject, functionPointer);
    CFun cf = CFun.factory.create("toEXIStream", "void", cfs);

    String methodBody = String.format(
            "%s* exiConverter = new %s();\n\n" +
            "exiConverter->serialize(typeObject, exiStream, outputFunction);\n\n" +
            "delete exiConverter;",
            this.serializerClassName, this.serializerClassName);
    
    cf.appendCode(methodBody);
    cf.setComment(new CCommentImpl("Serialize type object to EXI byte stream."));

    return cf;
  }

  /**
   * Public callback method that creates code to operate the
   * EXI de-/serializer class. The generated code demonstrates
   * how to deserialize a byte stream with EXI.
   * 
   * @return CFun object with deserialization code
   * 
   * @throws Exception Error during code generation
   */
  public CFun generateDeserializeCall() throws Exception
  {
    CParam streamObject = CParam.factory.create("EXIStream*", "exiStream");
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CFunSignature cfs = CFunSignature.factory.create(streamObject, typeObject);
    CFun cf = CFun.factory.create("fromEXIStream", "void", cfs);

    String methodBody = String.format(
            "%s* exiConverter = new %s();\n\n" +
            "exiConverter->deserialize(exiStream, typeObject);\n\n" +
            "delete exiConverter;",
            this.serializerClassName, this.serializerClassName);
    
    cf.appendCode(methodBody);
    cf.setComment(new CCommentImpl("Deserialize EXI byte stream to type object."));
    
    return cf;
  }

  /**
   * Private helper method to translate a source file name to a string
   * that can be used as C++ include guard (e.g. simple_types.hpp will
   * create SIMPLE_TYPES_HPP as output).
   *
   * @param fileName File name as string (with or without '.hpp')
   *
   * @return String that can be used as include guard name
   */
  private static String createIncludeGuardName(String fileName)
  {
    // Source file objects from Fabric workspace usually have
    // no file extension, so we need to add one here
    if (!fileName.endsWith(".hpp"))
    {
      fileName += ".hpp";
    }

    return fileName.replaceAll("\\.", "_").toUpperCase();
  }
}
