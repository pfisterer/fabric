/** 11.10.2011 16:09 */
package fabric.module.exi.java.lib.exi;

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
    // TODO: Implement method
    
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "byte[]",
            "serialize", jms, new String[] { "Exception" });

    String methodBody =
            "\n// TODO\n\n" +
            "return null;";
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Compress XML document with EXI."));

    this.serializerClass.add(jm);

    // Add required Java imports   
    // TODO ...
  }

  /**
   * This method generates code to deserialize an EXI stream.
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateDeserializeCode() throws Exception
  {
    // TODO: Check and improve implementation
    
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "byte[]", "exiStream"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
            "deserialize", jms, new String[] { "Exception" });

    String methodBody = String.format(
            "// Create factory and set EXI grammar for current XML schema\n" +
            "EXIFactory exiFactory = DefaultEXIFactory.newInstance();\n" +
            "GrammarFactory grammarFactory = GrammarFactory.newInstance();\n" +
            "Grammar grammar = grammarFactory.createGrammar(new FileInputStream(new File(\"%s\")));\n" +
            "exiFactory.setGrammar(grammar);\n\n" +
            "// Decode EXI byte stream\n" +
            "EXISource saxSource = new EXISource(exiFactory);\n" +
            "XMLReader xmlReader = saxSource.getXMLReader();\n\n" +
            "TransformerFactory transformerFactory = TransformerFactory.newInstance();\n" +
            "Transformer transformer = transformerFactory.newTransformer();\n\n" +
            "InputStream exiIS = new ByteArrayInputStream(exiStream);\n" +
            "SAXSource exiSource = new SAXSource(new InputSource(exiIS));\n" +
            "exiSource.setXMLReader(xmlReader);\n" +
            "// Parse EXI file and write output\n" +
            "OutputStream xmlOS = new ByteArrayOutputStream();\n" +
            "transformer.transform(exiSource, new StreamResult(xmlOS));\n" +
            "xmlOS.close();\n\n" +
            "return xmlOS.toString();",
            this.xsdDocumentPath);
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);

    // Add required Java imports   
    this.addRequiredImport("com.siemens.ct.exi.EXIFactory");
    this.addRequiredImport("com.siemens.ct.exi.GrammarFactory");
    this.addRequiredImport("com.siemens.ct.exi.api.sax.EXISource");
    this.addRequiredImport("com.siemens.ct.exi.grammar.Grammar");
    this.addRequiredImport("com.siemens.ct.exi.helpers.DefaultEXIFactory");
    this.addRequiredImport("java.io.ByteArrayInputStream");
    this.addRequiredImport("java.io.ByteArrayOutputStream");
    this.addRequiredImport("java.io.FileInputStream");
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("java.io.OutputStream");
    this.addRequiredImport("javax.xml.transform.Transformer");
    this.addRequiredImport("javax.xml.transform.TransformerFactory");
    this.addRequiredImport("javax.xml.transform.sax.SAXSource");
    this.addRequiredImport("javax.xml.transform.stream.StreamResult");
    this.addRequiredImport("org.xml.sax.InputSource");
    this.addRequiredImport("org.xml.sax.XMLReader");
  }
}
