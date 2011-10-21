/** 11.10.2011 14:12 */
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
 * documents with OpenEXI.
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
    
    this.generateSchemaFactoryCode();    
  }

  /**
   * Private helper method to generate code that creates and
   * initializes the framework-specific OpenEXISchemaFactory object.
   *
   * @throws Exception Error during code generation
   */
  public void generateSchemaFactoryCode() throws Exception
  {
    // Create member variable for OpenEXI schema factory
    JField schemaFactory = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchemaFactory", "exiSchemaFactory", "new EXISchemaFactory()");
    schemaFactory.setComment(new JFieldCommentImpl("The OpenEXI schema factory member variable."));
    this.serializerClass.add(schemaFactory);

    // Create member variable for OpenExi Transmogrifier
    JField transmogrifier = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "Transmogifier", "transmogifier", "new Transmogifier()");
    transmogrifier.setComment(new JFieldCommentImpl("The OpenEXI transmogifier member variable."));
    this.serializerClass.add(transmogrifier);
    
    // Create member variable for OpenExi schema
    JField exiSchema = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "EXISchema", "exiSchema", "new EXISchema()");
    exiSchema.setComment(new JFieldCommentImpl("The OpenEXI schema member variable."));
    this.serializerClass.add(exiSchema);
    
    // Create member variable for the OpenEXI grammar cache
    JField grammarCache = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "GrammarCache", "grammarCache", "null");
    grammarCache.setComment(new JFieldCommentImpl("The OpenEXI grammar cache member variable."));
    this.serializerClass.add(grammarCache);
    
    // Initialize OpenEXI schema factory member variable
    this.serializerClass.appendStaticCode(String.format("%s.setupOpenEXISchemaFactory();", this.serializerClass.getName()));

    // Create method for EXI factory setup
    JMethod setupOpenEXISchemaFactory = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupOpenEXISchemaFactory");

    String methodBody = String.format(
            "try {\n" +
            "\t// compile and validate input source schema\n" +
            "\t%s.exiSchema = schemaFactory.compile(new InputSource(new InputStream(new FileInputStream(new File(\"%s\")))));\n\n" +
            "\t// Set grammar for current XML schema\n" +
            "\t%s.grammarCache = new GrammarCache(%s.exiSchema);\n\n" +
            "\t// Set grammar cache for the OpenEXI transmogrifier\n" +
            "\t%s.transmogrifier.setExiSchema(%s.grammarCache);" +
            "}\n" +
            "catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}",
            this.serializerClass.getName(), xsdDocumentPath, this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName(), this.serializerClass.getName());

    setupOpenEXISchemaFactory.getBody().appendSource(methodBody);
    setupOpenEXISchemaFactory.setComment(new JMethodCommentImpl("Setup OpenEXI schema factory member variable."));

    this.serializerClass.add(setupOpenEXISchemaFactory);

    // Add required Java imports
    this.addRequiredImport("java.io.File");
    this.addRequiredImport("java.io.FileInputStream");
    this.addRequiredImport("org.openexi.fujitsu.schema.EXISchema");
    this.addRequiredImport("org.openexi.fujitsu.scomp.EXISchemaFactory");
    this.addRequiredImport("org.openexi.fujitsu.proc.grammars.GrammarCache");
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
			  "// Parse XML document and serialize\n" +
			  "try {\n" +
			  "\t%s.transmogrifier.setOutputStream(exiOS);\n\n" +
			  "\t// Parse XML document and serialize\n" +
			  "\tInputSource is = new InputSource(new InputStream( new ByteArrayInputStream(xmlDocument.getBytes())));\n" +
			  "\t%s.transmogrifier.encode(is);\n" +
			  "}\n" +
			  "catch (Exception e) {\n" +
			  "\te.printStackTrace();\n" +
			  "}\n" +
			  "// Write output to EXI byte stream\n" +
			  "byte[] result = ((ByteArrayOutputStream)exiOS).toByteArray();\n" +
			  "exiOS.close();\n\n" +
			  "return result;",
			  this.serializerClass.getName(), this.serializerClass.getName());

	    jm.getBody().appendSource(methodBody);
	    jm.setComment(new JMethodCommentImpl("Compress XML document with OpenEXI."));

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
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
