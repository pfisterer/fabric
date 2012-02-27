/** 27.02.2012 15:59 */
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

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.exceptions.FabricEXIException;

/**
 * @author seidel
 */
public class CppEXIConverter
{
  // TODO: Add documentation to class
  // TODO: Check and update all comments in this class

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
  
  // TODO: Add comment
  public CppEXIConverter(Properties properties)
  {
    this.properties = properties;

    this.beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);
        
    this.serializerClassName = "EXIConverter";
  }

  /**
   * Public callback method that generates the EXI de-/serializer class
   * and adds it to the provided Java source file.
   * 
   * @param sourceFile Java source file for code write-out
   * 
   * @throws Exception Source file was null or error during code generation
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
      // TODO: CppEXIStreamGenerator.init(workspace);
      
      // Generate EXITypeEncoder class
      // TODO: CppEXITypeEncoder.init(workspace);
      
      // Generate EXIConverter class
      this.serializerClass = CppClass.factory.create(this.serializerClassName);
      this.generateSerializeCode();
      this.generateDeserializeCode();
      
      /*****************************************************************
       * Create C++ header file
       *****************************************************************/      
      CppHeaderFile cpphf = workspace.getC().getCppHeaderFile(this.serializerClassName);
      
      cpphf.setComment(new CCommentImpl(String.format("The '%s' header file.", this.serializerClassName)));
      cpphf.add(this.serializerClass);

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
      cppsf.addInclude(cpphf);
      
      LOGGER.debug(String.format("Generated new source file '%s'.", this.serializerClassName));
    }
  }
  
  // TODO: Add implementation and comment
  public void generateSerializeCode() throws Exception
  {
    CppFun serialize = CppFun.factory.create("void", "serialize");
    this.serializerClass.add(Cpp.PUBLIC, serialize);
  }
  
  // TODO: Add implementation and comment
  public void generateDeserializeCode() throws Exception
  {
    CppFun deserialize = CppFun.factory.create("void", "deserialize");
    this.serializerClass.add(Cpp.PUBLIC, deserialize);
  }

  /**
   * Public callback method that creates code to operate the
   * EXI de-/serializer class. The generated code demonstrates
   * how to serialize a plain XML document with EXI.
   * 
   * @return JMethod object with serialization code
   * 
   * @throws Exception Error during code generation
   */
  public CFun generateSerializeCall() throws Exception
  {
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CParam streamObject = CParam.factory.create("EXIStream*", "exiStream");
    CFunSignature cfs = CFunSignature.factory.create(typeObject, streamObject);
    CFun cf = CFun.factory.create("toEXIStream", "void", cfs);

    String methodBody =
            "// TODO: Add implementation\n\n" +
            "return 0;";
    
    cf.appendCode(methodBody);
    cf.setComment(new CCommentImpl("Serialize type object to EXI byte stream."));

    return cf;
  }

  /**
   * Public callback method that creates code to operate the
   * EXI de-/serializer class. The generated code demonstrates
   * how to deserialize a byte stream with EXI.
   * 
   * @return JMethod object with deserializer code
   * 
   * @throws Exception Error during code generation
   */
  public CFun generateDeserializeCall() throws Exception
  {
    CParam streamObject = CParam.factory.create("EXIStream*", "exiStream");
    CParam typeObject = CParam.factory.create(this.beanClassName + "*", "typeObject");
    CFunSignature cfs = CFunSignature.factory.create(streamObject, typeObject);
    CFun cf = CFun.factory.create("fromEXIStream", "void", cfs);

    String methodBody =
            "// TODO: Add implementation\n\n" +
            "return 0;";
    
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
