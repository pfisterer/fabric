/** 15.11.2011 20:43 */
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
    // TODO: Fix this block to allow schema-informed de-/serialization
//    // Create member variable for EXI schema factory
//    JField exiSchemaFactory = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchemaFactory", "exiSchemaFactory", "new EXISchemaFactory()");
//    exiSchemaFactory.setComment(new JFieldCommentImpl("The OpenEXI schema factory member variable."));
//    this.serializerClass.add(exiSchemaFactory);
    // TODO: Block end
    
    // Create member variable for EXI encoder
    JField encoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Transmogrifier", "encoder", "null");
    encoder.setComment(new JFieldCommentImpl("The EXI encoder member variable."));
    this.serializerClass.add(encoder);

    // Create member variable for EXI decoder
    JField decoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXIDecoder", "decoder", "null");
    decoder.setComment(new JFieldCommentImpl("The EXI decoder member variable."));
    this.serializerClass.add(decoder);

    // Initialize EXI de-/serializer objects
    this.serializerClass.appendStaticCode(String.format("%s.setupEXISerializer();", this.serializerClass.getName()));

    // Create method for EXI de-/serializer setup
    JMethod setupEXISchemaFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupEXISerializer");

    String methodBody = String.format(
            "try {\n" +
            "\t// Create grammar cache for EXI compression\n" +
            "\tGrammarCache grammarCache = new GrammarCache(EmptySchema.getEXISchema());\n\n" +
            "\t// Initialize EXI encoder\n" +
            "\t%s.encoder = new Transmogrifier();\n" +
            "\t%s.encoder.setEXISchema(grammarCache);\n" +
            "\t%s.encoder.setPreserveWhitespaces(true);\n\n" +
            "\t// Initialize EXI decoder\n" +
            "\t%s.decoder = new EXIDecoder();\n" +
            "\t%s.decoder.setEXISchema(grammarCache);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName());

    setupEXISchemaFactory.getBody().appendSource(methodBody);
    setupEXISchemaFactory.setComment(new JMethodCommentImpl("Setup EXI de-/serializer objects."));

    this.serializerClass.add(setupEXISchemaFactory);

    // Add required Java imports
    // TODO: Remove unused imports
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.FileInputStream");
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("org.openexi.fujitsu.sax.Transmogrifier");
    this.addRequiredImport("org.openexi.fujitsu.schema.EmptySchema");
    this.addRequiredImport("org.openexi.fujitsu.proc.grammars.GrammarCache");
    this.addRequiredImport("org.openexi.fujitsu.proc.EXIDecoder");
    this.addRequiredImport("org.openexi.fujitsu.proc.io.Scanner");
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
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("java.io.OutputStream");
    this.addRequiredImport("java.io.StringReader");
    this.addRequiredImport("java.io.ByteArrayInputStream");
    this.addRequiredImport("java.io.ByteArrayOutputStream");
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
            "String result = \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\\n\";\n\n" +
            "// Set input stream\n" +
            "%s.decoder.setInputStream(new ByteArrayInputStream(exiStream));\n\n" +
            "// Create scanner object for deserialization\n" +
            "Scanner scanner = %s.decoder.processHeader();\n\n" +
            "// Create objects for event processing\n" +
            "EXIEvent exiEvent;\n" +
            "Stack<String> elementStack = new Stack<String>();\n\n" +
            "// Collect all EXI events in a list\n" +
            "ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();\n" +
            "while ((exiEvent = scanner.nextEvent()) != null) {\n" +
            "\texiEventList.add(exiEvent);\n" +
            "}\n\n" +
            "// Rebuild initial XML document from EXI events\n" +
            "for (int i = 0; i < exiEventList.size(); ++i) {\n" +
            "\texiEvent = exiEventList.get(i);\n\n" +
            "\t// Handle start of element\n" +
            "\tif (exiEvent.getEventVariety() == EXIEvent.EVENT_SE) {\n" +
            "\t\tresult += \"<\" + exiEvent.getName() + \">\";\n" +
            "\t\telementStack.push(exiEvent.getName());\n" +
            "\t}\n\n" +
            "\t// Handle characters (element content)\n" +
            "\tif (exiEvent.getEventVariety() == EXIEvent.EVENT_CH) {\n" +
            "\t\tif (null != exiEvent.getCharacters()) {\n" +
            "\t\t\tresult += exiEvent.getCharacters().makeString();\n" +
            "\t\t}\n" +
            "\t}\n\n" +
            "\t// Handle end of element\n" +
            "\tif (exiEvent.getEventVariety() == EXIEvent.EVENT_EE) {\n" +
            "\t\tresult += \"</\" + elementStack.pop() + \">\";\n" +
            "\t}\n" +
            "}\n\n" +
            "return result;",
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("java.util.ArrayList");
    this.addRequiredImport("java.util.Stack");
    this.addRequiredImport("org.openexi.fujitsu.proc.common.EXIEvent");
  }
}
