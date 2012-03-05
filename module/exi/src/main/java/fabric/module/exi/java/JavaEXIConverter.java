/** 05.03.2012 13:00 */
package fabric.module.exi.java;

import java.util.Properties;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import de.uniluebeck.sourcegen.java.JSourceFile;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.exceptions.FabricEXIException;

import fabric.module.exi.java.lib.exi.EXILibrary;
import fabric.module.exi.java.lib.exi.EXILibraryFactory;

/**
 * JavaEXIConverter class creates the EXI de-/serializer class
 * and generates methods for the application's main function
 * to demonstrate the usage of the converter.
 * 
 * @author seidel
 */
public class JavaEXIConverter
{
  /** Properties object with module configuration */
  private Properties properties;
  
  /** Path to the input XSD document */
  private String xsdDocumentPath;
  
  /** Name of the EXI de-/serializer class */
  private String serializerClassName;

  /**
   * Parameterized constructor initializes properties object
   * and various other member variables.
   * 
   * @param properties Properties object with module options
   */
  public JavaEXIConverter(Properties properties)
  {
    this.properties = properties;
    
    this.xsdDocumentPath = this.properties.getProperty(FabricEXIModule.XSD_DOCUMENT_PATH_KEY);
    
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
  public void generateSerializerClass(final JSourceFile sourceFile) throws Exception
  {
    if (null == sourceFile)
    {
      throw new FabricEXIException("Cannot create EXI de-/serializer class. Source file is null.");
    }
    else
    {
      // Create instance of desired EXI library
      EXILibrary exiLibrary = EXILibraryFactory.getInstance().createEXILibrary(
              this.properties.getProperty(FabricEXIModule.EXILIBRARY_NAME_KEY),
              this.xsdDocumentPath);

      sourceFile.add(exiLibrary.init());

      // Add required imports AFTER initialization
      for (String requiredImport: exiLibrary.getRequiredImports())
      {
        sourceFile.addImport(requiredImport);
      }
    }
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
  public JMethod generateSerializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, "byte[]", "toEXIStream", jms, new String[] { "Exception" });
    
    String methodBody = String.format("return %s.serialize(xmlDocument);", this.serializerClassName);
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Serialize XML document to EXI byte stream."));
    
    return jm;
  }
  
  /**
   * Public callback method that creates code to operate the
   * EXI de-/serializer class. The generated code demonstrates
   * how to deserialize a byte stream with EXI.
   * 
   * @return JMethod object with deserialization code
   * 
   * @throws Exception Error during code generation
   */
  public JMethod generateDeserializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "byte[]", "exiStream"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, "String", "fromEXIStream", jms, new String[] { "Exception" });

    String methodBody = String.format("return %s.deserialize(exiStream);", this.serializerClassName);

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize EXI byte stream to XML document."));

    return jm;
  }
}
