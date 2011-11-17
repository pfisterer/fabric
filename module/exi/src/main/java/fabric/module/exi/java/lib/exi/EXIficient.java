/** 12.11.2011 01:31 */
package fabric.module.exi.java.lib.exi;

import de.uniluebeck.sourcegen.java.JField;
import de.uniluebeck.sourcegen.java.JFieldCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * EXI converter class for the EXIficient library. This class
 * provides means to create code that de-/serializes XML
 * documents with EXI.
 *
 * @author seidel
 */
public class EXIficient extends EXILibrary
{
  /**
   * Parameterized constructor.
   *    
   * @param xsdDocumentPath Path to the input XSD document
   *
   * @throws Exception Error during code generation
   */
  public EXIficient(final String xsdDocumentPath) throws Exception
  {
    super(xsdDocumentPath);

    this.generateEXIFactoryCode();
  }

  /**
   * Private helper method to generate code that creates and
   * initializes the framework-specific EXIFactory object.
   *
   * @throws Exception Error during code generation
   */
  private void generateEXIFactoryCode() throws Exception
  {
    // Create member variable for EXI factory
    JField exiFactory = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXIFactory", "exiFactory", "null");
    exiFactory.setComment(new JFieldCommentImpl("The EXI factory member variable."));
    this.serializerClass.add(exiFactory);

    // Initialize EXI factory member variable
    this.serializerClass.appendStaticCode(String.format("%s.setupEXIFactory();", this.serializerClass.getName()));

    // Create method for EXI factory setup
    JMethod setupEXIFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupEXIFactory");

    String methodBody = String.format(
            "try {\n" +
            "\t// Create EXI factory\n" +
            "\t%s.exiFactory = DefaultEXIFactory.newInstance();\n\n" +
            "\t// Set grammar for current XML schema\n" +
            "\tGrammarFactory grammarFactory = GrammarFactory.newInstance();\n" +
            "\tGrammar grammar = grammarFactory.createGrammar(new FileInputStream(new File(\"%s\")));\n\n" +
            "\t%s.exiFactory.setGrammar(grammar);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            this.serializerClass.getName(), xsdDocumentPath, this.serializerClass.getName());

    setupEXIFactory.getBody().appendSource(methodBody);
    setupEXIFactory.setComment(new JMethodCommentImpl("Setup EXI factory member variable."));

    this.serializerClass.add(setupEXIFactory);

    // Add required Java imports
    this.addRequiredImport("com.siemens.ct.exi.EXIFactory");
    this.addRequiredImport("com.siemens.ct.exi.GrammarFactory");
    this.addRequiredImport("com.siemens.ct.exi.grammar.Grammar");
    this.addRequiredImport("com.siemens.ct.exi.helpers.DefaultEXIFactory");
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.FileInputStream");
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
            "OutputStream exiOS = new ByteArrayOutputStream();\n" +
            "EXIResult exiResult = new EXIResult(exiOS, %s.exiFactory);\n\n" +
            "XMLReader xmlReader = XMLReaderFactory.createXMLReader();\n" +
            "xmlReader.setContentHandler(exiResult.getHandler());\n\n" +
            "// Parse XML document and serialize\n" +
            "InputStream xmlIS = new ByteArrayInputStream(xmlDocument.getBytes());\n" +
            "xmlReader.parse(new InputSource(xmlIS));\n\n" +
            "// Write output to EXI byte stream\n" +
            "byte[] result = ((ByteArrayOutputStream)exiOS).toByteArray();\n" +
            "exiOS.close();\n\n" +
            "return result;",
            this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Compress XML document with EXI."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.siemens.ct.exi.EXIFactory");
    this.addRequiredImport("com.siemens.ct.exi.api.sax.EXIResult");
    this.addRequiredImport("java.io.ByteArrayInputStream");
    this.addRequiredImport("java.io.ByteArrayOutputStream");
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("java.io.OutputStream");
    this.addRequiredImport("org.xml.sax.InputSource");
    this.addRequiredImport("org.xml.sax.XMLReader");
    this.addRequiredImport("org.xml.sax.helpers.XMLReaderFactory");
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
            "// Prepare objects for deserialization\n" +
            "EXISource saxSource = new EXISource(%s.exiFactory);\n" +
            "XMLReader xmlReader = saxSource.getXMLReader();\n\n" +
            "TransformerFactory transformerFactory = TransformerFactory.newInstance();\n" +
            "Transformer transformer = transformerFactory.newTransformer();\n\n" +
            "transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "transformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "transformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "transformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n" +
            "// Parse EXI stream and deserialize\n" +
            "InputStream exiIS = new ByteArrayInputStream(exiStream);\n" +
            "SAXSource exiSource = new SAXSource(new InputSource(exiIS));\n" +
            "exiSource.setXMLReader(xmlReader);\n\n" +
            "// Write output to XML document\n" +
            "OutputStream xmlOS = new ByteArrayOutputStream();\n" +
            "transformer.transform(exiSource, new StreamResult(xmlOS));\n\n" +
            "String result = xmlOS.toString();\n" +
            "xmlOS.close();\n\n" +
            "return result;",
            this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.siemens.ct.exi.EXIFactory");
    this.addRequiredImport("com.siemens.ct.exi.api.sax.EXISource");
    this.addRequiredImport("java.io.ByteArrayInputStream");
    this.addRequiredImport("java.io.ByteArrayOutputStream");
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("java.io.OutputStream");
    this.addRequiredImport("javax.xml.transform.OutputKeys");
    this.addRequiredImport("javax.xml.transform.Transformer");
    this.addRequiredImport("javax.xml.transform.TransformerFactory");
    this.addRequiredImport("javax.xml.transform.sax.SAXSource");
    this.addRequiredImport("javax.xml.transform.stream.StreamResult");
    this.addRequiredImport("org.xml.sax.InputSource");
    this.addRequiredImport("org.xml.sax.XMLReader");
  }
}
