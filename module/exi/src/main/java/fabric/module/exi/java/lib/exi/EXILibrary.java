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
/** 11.11.2011 20:57 */
package fabric.module.exi.java.lib.exi;

import java.util.ArrayList;
import java.util.Collections;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JModifier;

/**
 * Abstract base class for EXI libraries. Derived classes
 * generate code to serialize and deserialize XML documents
 * with EXI. Each implementation of the interface may operate
 * a different XML library.
 * 
 * @author seidel
 */
abstract public class EXILibrary
{
  /** List of required Java imports */
  protected ArrayList<String> requiredImports;

  /** Class for EXI serialization and deserialization */
  protected JClass serializerClass;

  /** Path to the input XSD document */
  protected String xsdDocumentPath;

  /**
   * Parameterized constructor initializes EXI converter class
   * and sets bean class name.
   * 
   * @param xsdDocumentPath Path to the input XSD document
   *
   * @throws Exception Error during code generation
   */
  public EXILibrary(final String xsdDocumentPath) throws Exception
  {
    this.requiredImports = new ArrayList<String>();

    this.serializerClass = JClass.factory.create(JModifier.PUBLIC, "EXIConverter");
    this.serializerClass.setComment(new JClassCommentImpl("The EXI de-/serializer class."));

    this.xsdDocumentPath = xsdDocumentPath;
  }
  
  /**
   * Private helper method to add an import to the internal list
   * of required imports. All entries will later be written to
   * the Java source file.
   *
   * Multiple calls to this function with the same argument will
   * result in a single import statement on source code write-out.
   *
   * @param requiredImport Name of required import
   */
  protected void addRequiredImport(final String requiredImport)
  {
    if (null != requiredImport && !this.requiredImports.contains(requiredImport))
    {
      this.requiredImports.add(requiredImport);
    }
  }

  /**
   * Helper method to get a list of required imports
   * for the XML converter class.
   *
   * @return List of required imports
   */
  public ArrayList<String> getRequiredImports()
  {
    Collections.sort(this.requiredImports);

    return this.requiredImports;
  }

  /**
   * Initialize the EXILibrary implementation. This means we run
   * all methods that the interface defines, in order to create
   * the EXI de-/serializer class we return eventually.
   *
   * @return JClass object with EXI de-/serializer class
   *
   * @throws Exception Error during code generation
   */
  public JClass init() throws Exception
  {
    // Generate code for EXI serialization
    this.generateSerializeCode();
    
    // Generate code for EXI deserializer
    this.generateDeserializeCode();

    return this.serializerClass;
  }

  /**
   * Method that creates code to serialize an XML document with EXI.
   *
   * @throws Exception Error during code generation
   */
  abstract public void generateSerializeCode() throws Exception;

  /**
   * Method that creates code to deserialize an EXI byte stream.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateDeserializeCode() throws Exception;
}
