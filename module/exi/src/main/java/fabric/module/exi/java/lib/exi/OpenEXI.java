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
/** 19.11.2011 01:35 */
package fabric.module.exi.java.lib.exi;

import de.uniluebeck.sourcegen.java.JField;
import de.uniluebeck.sourcegen.java.JFieldCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * EXI converter class for the OpenEXI library. This class
 * provides means to create code that de-/serializes XML
 * documents with EXI.
 * 
 * @author widderich, seidel
 */
public class OpenEXI extends EXILibrary
{
  /**
   * Parameterized constructor.
   *    
   * @param xsdDocumentPath Path to the input XSD document
   *
   * @throws Exception Error during code generation
   */
  public OpenEXI(final String xsdDocumentPath) throws Exception
  {
    super(xsdDocumentPath);

    this.generateSerializerObjects();
  }

  /**
   * Private helper method to generate static member variables and
   * setup framework-specific objects for EXI de-/serialization.
   *
   * @throws Exception Error during code generation
   */
  private void generateSerializerObjects() throws Exception
  {    
    // Create member variable for EXI encoder
    JField encoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Transmogrifier", "encoder", "null");
    encoder.setComment(new JFieldCommentImpl("The EXI encoder member variable."));
    this.serializerClass.add(encoder);

    // Create member variable for EXI decoder
    JField decoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXIReader", "decoder", "null");
    decoder.setComment(new JFieldCommentImpl("The EXI decoder member variable."));
    this.serializerClass.add(decoder);

    // Initialize EXI de-/serializer objects
    this.serializerClass.appendStaticCode(String.format("%s.setupEXISerializer();", this.serializerClass.getName()));

    // Create method for EXI de-/serializer setup
    JMethod setupEXISchemaFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupEXISerializer");

    String methodBody = String.format(
            "try {\n" +
            "\t// Create grammar cache for EXI compression\n" +
            "\tEXISchemaFactory exiSchemaFactory = new EXISchemaFactory();\n" +
            "\tEXISchema exiSchema = exiSchemaFactory.compile(new InputSource(new FileInputStream(new File(\"%s\"))));\n" +
            "\tGrammarCache grammarCache = new GrammarCache(exiSchema);\n\n" +
            "\t// Initialize EXI encoder\n" +
            "\t%s.encoder = new Transmogrifier();\n" +
            "\t%s.encoder.setEXISchema(grammarCache);\n" +
            "\t%s.encoder.setPreserveWhitespaces(true);\n\n" +
            "\t// Initialize EXI decoder\n" +
            "\t%s.decoder = new EXIReader();\n" +
            "\t%s.decoder.setEXISchema(grammarCache);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            this.xsdDocumentPath, this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName());

    setupEXISchemaFactory.getBody().appendSource(methodBody);
    setupEXISchemaFactory.setComment(new JMethodCommentImpl("Setup EXI de-/serializer objects."));

    this.serializerClass.add(setupEXISchemaFactory);

    // Add required Java imports
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.FileInputStream");
    this.addRequiredImport("org.openexi.fujitsu.proc.grammars.GrammarCache");
    this.addRequiredImport("org.openexi.fujitsu.sax.EXIReader");
    this.addRequiredImport("org.openexi.fujitsu.sax.Transmogrifier");
    this.addRequiredImport("org.openexi.fujitsu.schema.EXISchema");
    this.addRequiredImport("org.openexi.fujitsu.scomp.EXISchemaFactory");
    this.addRequiredImport("org.xml.sax.InputSource");
  }

  /**
   * This method generates code to serialize a plain
   * XML document with EXI.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateSerializeCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "byte[]",
            "serialize", jms, new String[] { "Exception" });

    String methodBody = String.format(
            "// Prepare output stream for serialization\n" +
            "ByteArrayOutputStream baos = new ByteArrayOutputStream();\n\n" +
            "// Parse XML document and serialize\n" +
            "%s.encoder.setOutputStream(baos);\n" +
            "%s.encoder.encode(new InputSource(new StringReader(xmlDocument)));\n\n" +
            "// Write output to EXI byte stream\n" +
            "byte[] result = baos.toByteArray();\n" +
            "baos.close();\n\n" +
            "return result;",
            this.serializerClass.getName(), this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Compress XML document with EXI."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("java.io.ByteArrayOutputStream");
    this.addRequiredImport("java.io.StringReader");
    this.addRequiredImport("org.openexi.fujitsu.sax.EXIReader");
    this.addRequiredImport("org.openexi.fujitsu.sax.Transmogrifier");
    this.addRequiredImport("org.xml.sax.InputSource");
  }

  /**
   * This method generates code to deserialize an EXI stream.
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateDeserializeCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "byte[]", "exiStream"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
            "deserialize", jms, new String[] { "Exception" });

    String methodBody = String.format(
            "// Create SAX transformer for pretty output\n" +
            "SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();\n" +
            "TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler();\n" +
            "Transformer transformer = transformerHandler.getTransformer();\n" +
            "transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "transformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "transformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "transformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n\n" +
            "// Prepare StringWriter for output\n" +
            "StringWriter stringWriter = new StringWriter();\n" +
            "transformerHandler.setResult(new StreamResult(stringWriter));\n\n" +
            "// Parse EXI stream and deserialize\n" +
            "%s.decoder.setContentHandler(transformerHandler);\n" +
            "%s.decoder.parse(new InputSource(new ByteArrayInputStream(exiStream)));\n\n" +
            "// Write output to XML document\n" +
            "stringWriter.flush();\n" +
            "String result = stringWriter.getBuffer().toString();\n\n" +
            "return result;",
            this.serializerClass.getName(), this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("java.io.ByteArrayInputStream");
    this.addRequiredImport("java.io.StringWriter");
    this.addRequiredImport("javax.xml.transform.OutputKeys");
    this.addRequiredImport("javax.xml.transform.Transformer");
    this.addRequiredImport("javax.xml.transform.sax.SAXTransformerFactory");
    this.addRequiredImport("javax.xml.transform.sax.TransformerHandler");
    this.addRequiredImport("javax.xml.transform.stream.StreamResult");
    this.addRequiredImport("org.openexi.fujitsu.sax.EXIReader");
    this.addRequiredImport("org.openexi.fujitsu.sax.Transmogrifier");
    this.addRequiredImport("org.xml.sax.InputSource");
  }
}
