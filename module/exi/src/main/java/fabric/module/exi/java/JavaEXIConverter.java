/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
