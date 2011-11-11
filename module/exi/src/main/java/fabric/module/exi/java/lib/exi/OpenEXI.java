/** 30.10.2011 18:55 */
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
 * @author widderich
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
    
    this.generateEXISchemaFactoryCode();
  }

  /**
   * Private helper method to generate code that creates and
   * initializes the framework-specific EXISchemaFactory object.
   *
   * @throws Exception Error during code generation
   */
  private void generateEXISchemaFactoryCode() throws Exception
  {
    // Create member variable for OpenEXI schema factory
   /* JField exiSchemaFactory = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchemaFactory", "exiSchemaFactory", "new EXISchemaFactory()");
    exiSchemaFactory.setComment(new JFieldCommentImpl("The OpenEXI schema factory member variable."));
    this.serializerClass.add(exiSchemaFactory);
    */
    // Create member variable for OpenEXI transmogrifier
    JField encoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Transmogrifier", "encoder");
    encoder.setComment(new JFieldCommentImpl("The OpenEXI encoder member variable."));
    this.serializerClass.add(encoder);
    
    // Create member variable for OpenEXI schema
    JField exiSchema = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchema", "exiSchema", "new EXISchema()");
    exiSchema.setComment(new JFieldCommentImpl("The OpenEXI schema member variable."));
    this.serializerClass.add(exiSchema);
    
    // Create member variable for OpenEXI grammar cache
    JField grammarCache = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "GrammarCache", "grammarCache", "null");
    grammarCache.setComment(new JFieldCommentImpl("The OpenEXI grammar cache member variable."));
    this.serializerClass.add(grammarCache);
    
    // Create member variable for OpenEXI decoder
    JField exiDecoder = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXIDecoder", "exiDecoder", "new EXIDecoder()");
    exiDecoder.setComment(new JFieldCommentImpl("The OpenEXI decoder member variable."));
    this.serializerClass.add(exiDecoder);
    
    // Create member variable for OpenEXI scanner
    JField exiScanner = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Scanner", "exiScanner");
    exiScanner.setComment(new JFieldCommentImpl("The OpenEXI scanner member variable."));
    this.serializerClass.add(exiScanner);
    
    // Initialize OpenEXI schema factory member variable
    this.serializerClass.appendStaticCode(String.format("%s.setupEXISchemaFactory();", this.serializerClass.getName()));

    // Create method for OpenEXI schema factory setup
    JMethod setupOpenEXISchemaFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupEXISchemaFactory");

    String methodBody = String.format(
            "try {\n" +
            "\t%s.exiSchema = EmptySchema.getEXISchema();\n\n" +
            "\t// Set grammar for current XML schema\n" +
            "\t%s.grammarCache = new GrammarCache(%s.exiSchema);\n\n" +
            "\t// Initialize and set grammar cache for the OpenEXI transmogrifier (encoder)\n" +
            "\t%s.encoder = new Transmogrifier();\n" +
            "\t%s.encoder.setEXISchema(%s.grammarCache);\n\n" +
            "\t// Preserve whitspaces contained in the XML documents\n" + 
            "\t%s.encoder.setPreserveWhitespaces(true);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName());

    setupOpenEXISchemaFactory.getBody().appendSource(methodBody);
    setupOpenEXISchemaFactory.setComment(new JMethodCommentImpl("Setup OpenEXI schema factory member variable."));

    this.serializerClass.add(setupOpenEXISchemaFactory);

    // Add required Java imports
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.FileInputStream");
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("org.openexi.fujitsu.sax.Transmogrifier");
    this.addRequiredImport("org.openexi.fujitsu.schema.EXISchema");
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
    		"// Prepare objects for serialization\n" +
            "ByteArrayOutputStream baos = new ByteArrayOutputStream();\n" +
            "// Parse XML document and serialize\n" +
            "try {\n" +
            "\t%s.encoder.setOutputStream(baos);\n\n" +
            "\t// Parse XML document and serialize\n" +
            "\t%s.encoder.encode(new InputSource(new StringReader(xmlDocument)));\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n" +
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
            JParameter.factory.create(JModifier.FINAL, "byte[]", "openEXIStream"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
            "deserialize", jms, new String[] { "Exception" });
    
    String methodBody = String.format(
            "// The resulting XML string\n" +
            "String result = \"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\";\n" +
    		"// Prepare objects for deserialization\n" +
            "try {\n" +
            "\t// Set grammar cache for the OpenEXI decoder\n" +
            "\t%s.exiDecoder.setEXISchema(%s.grammarCache);\n\n" +
            "\t// Set the input stream for the OpenEXI Decoder\n" +
            "\t%s.exiDecoder.setInputStream(new ByteArrayInputStream(openEXIStream));\n\n" +
            "\t// Create the scanner object for deserialization\n" +
            "\t%s.exiScanner = %s.exiDecoder.processHeader();\n\n" +
            "\t// create the EXIEvent list for later processing\n" +
            "\tArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();\n" +
            "\tEXIEvent exiEvent;\n" +
            "\twhile ((exiEvent = %s.exiScanner.nextEvent()) != null) {\n" +
  	        "\t\texiEventList.add(exiEvent);\n" +
  	        "\t}\n" +
  	        "\tEventType eventType;\n" +
	        "\tStack<String> s = new Stack<String>();\n\n" +
	        "\t// Process the event list\n" +
	        "\tfor(int i=0; i<exiEventList.size(); i++){\n" +
	        "\t\texiEvent = exiEventList.get(i);\n" +
	        "\t\teventType = exiEvent.getEventType();\n" +
	        "\t\t// Start Element\n\n" +
	        "\t\tif(exiEvent.getEventVariety() == EXIEvent.EVENT_SE){\n" +
	        "\t\t\tresult += \"<\" + exiEvent.getName();\n" +
	        "\t\t\tif(eventType.getURI() != null && eventType.getURI() != \"\"){\n" +
	        "\t\t\t\tresult += \" xmlns=\" + eventType.getURI();\n" +
	        "\t\t\t}\n" +
	        "\t\t\tresult += \">\";\n" +
	        "\t\t\ts.push(exiEvent.getName());\n" +
	        "\t\t}\n\n" +
	        "\t\t// Characters between tags\n" +
	        "\t\tif(exiEvent.getEventVariety() == EXIEvent.EVENT_CH){\n" +
	        "\t\t\tif(exiEvent.getCharacters() != null){\n" +
	        "\t\t\t\tresult += exiEvent.getCharacters().makeString();\n" +
	        "\t\t\t}\n" +
	        "\t\t}\n\n" +
	        "\t\t// End Element\n" +
	        "\t\tif(exiEvent.getEventVariety() == EXIEvent.EVENT_EE){\n" +
	        "\t\t\tresult += \"</\" + s.pop() + \">\";\n" +
	        "\t\t}\n" +
	        "\t}\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n\n" +
            "return result;",
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);
    
    this.addRequiredImport("java.util.ArrayList");
    this.addRequiredImport("java.util.Stack");
    this.addRequiredImport("org.openexi.fujitsu.proc.common.EXIEvent");
    this.addRequiredImport("org.openexi.fujitsu.proc.common.EventType");
  }
}
