/** 05.03.2012 15:04 */
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
      this.generateSerializeCode();
      this.generateDeserializeCode();
        
        // Add member variables to EXIConverter class
        serializerClass.add(CppVar.factory.create(Cpp.PRIVATE, Cpp.CHAR, "buf[BUFFER_SIZE]"));
        serializerClass.add(CppVar.factory.create(Cpp.PRIVATE, "IOStream", "ioStrm"));
        serializerClass.add(CppVar.factory.create(Cpp.PRIVATE, "EXITypeEncoder", "encoder"));
        serializerClass.add(CppVar.factory.create(Cpp.PRIVATE, "EXITypeDecoder", "decoder"));

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
      cpphf.addInclude(CppEXITypeDecoderGenerator.FILE_NAME + ".hpp");

      // Add include guards to header file
      cpphf.addBeforeDirective("ifndef " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addBeforeDirective("define " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addAfterDirective("endif // " + CppEXIConverter.createIncludeGuardName(cpphf.getFileName()));

      // Add constant definition
      cpphf.addBeforeDirective("define BUFFER_SIZE 100");

      LOGGER.debug(String.format("Generated new header file '%s'.", this.serializerClassName));

      /*****************************************************************
       * Create C++ source file
       *****************************************************************/
      CppSourceFile cppsf = workspace.getC().getCppSourceFile(this.serializerClassName);

      cppsf.setComment(new CCommentImpl(String.format("The '%s' source file.", this.serializerClassName)));

      // Add includes
      cppsf.addInclude(cpphf);
      cppsf.addLibInclude("cstdio");

      LOGGER.debug(String.format("Generated new source file '%s'.", this.serializerClassName));
    }
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
    CppVar streamObject = CppVar.factory.create(Cpp.NONE, "EXIStream*", "strm");
    CppVar functionPointer = CppVar.factory.create(Cpp.NONE, "mySize_t", "(*outputFunction)(void*, mySize_t)");
    CppFun serialize = CppFun.factory.create(Cpp.INT, "serialize", typeObject, streamObject, functionPointer);
    
    // TODO: Add code to encode the elements in the object.
    String methodBody = String.format(
            "// Set exit code to zero\n" +
                    "int exit_code = 0;\n\n" +
                    "// Define method to write on the stream\n" +
                    "ioStrm.readWriteToStream = outputFunction;\n\n" +
                    "// Open EXI stream\n" +
                    "strm->initStream(buf, BUFFER_SIZE, ioStrm);\n\n" +
                    "// Write header to the EXI stream\n" +
                    "exit_code += strm->writeHeader();\n\n" +
                    "// Encode elements\n" +
                    "// TODO: Implement!\n" +
                    "// unsigned int event_code = 0;\n" +
                    "// exit_code += strm->writeNBits(3, &event_code);\n" +
                    "// exit_code += encoder.encodeX(strm, &value);\n\n" +
                    "// Close EXI stream\n" +
                    "exit_code += strm->closeStream();\n\n" +
                    "// Return the exit code\n" +
                    "return exit_code;",
            CppEXITypeEncoderGenerator.FILE_NAME);
    
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
      CppVar typeObject = CppVar.factory.create(Cpp.NONE, this.beanClassName + "*", "typeObject");
      CppVar streamObject = CppVar.factory.create(Cpp.NONE, "EXIStream*", "strm");
      CppVar functionPointer = CppVar.factory.create(Cpp.NONE, "mySize_t", "(*inputFunction)(void*, mySize_t)");
      CppFun deserialize = CppFun.factory.create(Cpp.INT, "deserialize", typeObject, streamObject, functionPointer);
    
    // TODO: Add code to decode the elements.
    String methodBody =
            "// Set exit code to zero\n" +
                    "int exit_code = 0;\n\n" +
                    "// Define method to read from the stream\n" +
                    "ioStrm.readWriteToStream = inputFunction;\n\n" +
                    "// Open EXI stream\n" +
                    "strm->initStream(buf, BUFFER_SIZE, ioStrm);\n\n" +
                    "// Read header from the EXI stream\n" +
                    "exit_code += strm->readHeader();\n\n" +
                    "// Decode elements\n" +
                    "// TODO: Implement!\n" +
                    "// unsigned int event_code = 0;\n" +
                    "// exit_code += strm->readNBits(3, &event_code);\n" +
                    "// exit_code += decoder.decodeX(strm, &value);\n\n" +
                    "// Return the exit code\n" +
                    "return exit_code;";
    
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
    CParam converterObject = CParam.factory.create(this.serializerClassName + "*", "exiConverter");
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CParam streamObject = CParam.factory.create(CppEXIStreamGenerator.FILE_NAME + "*", "exiStream");
    CParam functionPointer = CParam.factory.create("mySize_t", "(*outputFunction)(void*, mySize_t)");
    CFunSignature cfs = CFunSignature.factory.create(converterObject, typeObject, streamObject, functionPointer);
    CFun cf = CFun.factory.create("toEXIStream", "void", cfs);
    
    String methodBody = "exiConverter->serialize(typeObject, exiStream, outputFunction);";
    
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
    CParam converterObject = CParam.factory.create(this.serializerClassName + "*", "exiConverter");
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CParam streamObject = CParam.factory.create(CppEXIStreamGenerator.FILE_NAME + "*", "exiStream");
    CParam functionPointer = CParam.factory.create("mySize_t", "(*inputFunction)(void*, mySize_t)");
    CFunSignature cfs = CFunSignature.factory.create(converterObject, typeObject, streamObject, functionPointer);
    CFun cf = CFun.factory.create("fromEXIStream", "void", cfs);
    
    String methodBody = "exiConverter->deserialize(typeObject, exiStream, inputFunction);";
    
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
