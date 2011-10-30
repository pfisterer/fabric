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
    JField exiSchemaFactory = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchemaFactory", "exiSchemaFactory", "new EXISchemaFactory()");
    exiSchemaFactory.setComment(new JFieldCommentImpl("The OpenEXI schema factory member variable."));
    this.serializerClass.add(exiSchemaFactory);

    // Create member variable for OpenEXI transmogrifier
    JField transmogrifier = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Transmogrifier", "transmogrifier");
    transmogrifier.setComment(new JFieldCommentImpl("The OpenEXI transmogrifier member variable."));
    this.serializerClass.add(transmogrifier);
    
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
    JField scanner = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Scanner", "scanner");
    scanner.setComment(new JFieldCommentImpl("The OpenEXI scanner member variable."));
    this.serializerClass.add(scanner);
    
    // Initialize OpenEXI schema factory member variable
    this.serializerClass.appendStaticCode(String.format("%s.setupEXISchemaFactory();", this.serializerClass.getName()));

    // Create method for OpenEXI schema factory setup
    JMethod setupOpenEXISchemaFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupEXISchemaFactory");

    String methodBody = String.format(
            "try {\n" +
            "\t// compile and validate input source schema\n" +
            "\tInputStream inputStream = new FileInputStream(new File(\"%s\"));\n" +
            "\tInputSource inputSource = new InputSource(inputStream);\n" +
            "\t%s.exiSchema = %s.exiSchemaFactory.compile(inputSource);\n\n" +
            "\t// Set grammar for current XML schema\n" +
            "\t%s.grammarCache = new GrammarCache(%s.exiSchema);\n\n" +
            "\t// Initialize and set grammar cache for the OpenEXI transmogrifier\n" +
            "\t%s.transmogrifier = new Transmogrifier();\n" +
            "\t%s.transmogrifier.setEXISchema(%s.grammarCache);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            xsdDocumentPath, this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
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
    this.addRequiredImport("org.openexi.fujitsu.scomp.EXISchemaFactory");
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
            "OutputStream os = new ByteArrayOutputStream();\n" +
            "// Parse XML document and serialize\n" +
            "try {\n" +
            "\t%s.transmogrifier.setOutputStream(os);\n\n" +
            "\t// Parse XML document and serialize\n" +
            "\tInputStream inputStream = new ByteArrayInputStream(xmlDocument.getBytes());\n" +
            "\tInputSource inputSource = new InputSource(inputStream);\n" +
            "\t%s.transmogrifier.encode(inputSource);\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n" +
            "// Write output to EXI byte stream\n" +
            "byte[] result = ((ByteArrayOutputStream)os).toByteArray();\n" +
            "os.close();\n\n" +
            "return result;",
            this.serializerClass.getName(), this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Compress XML document with EXI."));

    this.serializerClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("java.io.InputStream");
    this.addRequiredImport("java.io.OutputStream");
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
            "// Prepare objects for deserialization\n" +
            "try {\n" +
            "\t// compile and validate input source schema\n" +
            "\tInputStream inputStream = new FileInputStream(new File(\"%s\"));\n" +
            "\tInputSource inputSource = new InputSource(inputStream);\n" +
            "\t%s.exiSchema = %s.exiSchemaFactory.compile(inputSource);\n\n" +
            "\t// Set grammar for current XML schema\n" +
            "\t%s.grammarCache = new GrammarCache(%s.exiSchema);\n\n" +
            "\t// Set grammar cache for the OpenEXI decoder\n" +
            "\t%s.exiDecoder.setEXISchema(%s.grammarCache);\n\n" +
            "\t// Parse the OpenEXI input stream\n" +
            "\tInputStream is = new ByteArrayInputStream(openEXIStream);\n\n" +
            "\t// Set the input stream for the OpenEXI Decoder\n" +
            "\t%s.exiDecoder.setInputStream(is);\n\n" +
            "\t// Create the scanner object for deserialization\n" +
            "\t%s.exiScanner = %s.exiDecoder.processHeader();\n\n" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n\n" +
            "return null; // TODO: Return result here",
            xsdDocumentPath, this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(),
            this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Uncompress EXI byte stream to XML document."));

    this.serializerClass.add(jm);
    
    // TODO: Add required Java imports
  }
}
