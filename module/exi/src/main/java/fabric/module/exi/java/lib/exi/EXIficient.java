/** 11.10.2011 14:16 */
package fabric.module.exi.java.lib.exi;

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
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public EXIficient(final String beanClassName) throws Exception
  {
    super(beanClassName);
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
    // throw new UnsupportedOperationException("Not supported yet.");
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
    // throw new UnsupportedOperationException("Not supported yet.");
  }
// TODO: Maybe use this code for generation, otherwise delete it  
//  public JMethod createCodeSchemaLessFunction() throws Exception
//  {
//    JMethodSignature jms = JMethodSignature.factory.create(
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "xmlDocument"),
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "exiDocument"));
//    
//    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "codeSchemaLess", jms, new String[] { "Exception" });
//    
//    String methodBody = "";
//    
//    jm.getBody().appendSource(methodBody);
//    jm.setComment(new JMethodCommentImpl("Helper function for schema-less encoding and decoding."));
//    
//    return jm;
//  }
//  
//  public JMethod createCodeSchemaInformedFunction() throws Exception
//  {
//    JMethodSignature jms = JMethodSignature.factory.create(
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "xsdDocument"),
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "xmlDocument"),
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "exiDocument"));
//    
//    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "codeSchemaInformed", jms, new String[] { "Exception" });
//    
//    String methodBody = "";
//    
//    jm.getBody().appendSource(methodBody);
//    jm.setComment(new JMethodCommentImpl("Helper function for schema-informed encoding and decoding."));
//    
//    return jm;
//  }
//  
//  public JMethod createSerializeFunction() throws Exception
//  {
//    JMethodSignature jms = JMethodSignature.factory.create(
//            JParameter.factory.create(JModifier.FINAL, "org.xml.sax.ContentHandler", "handler"),
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "xmlDocument"));
//    
//    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "serialize", jms, new String[] { "IOException", "SAXException" });
//    
//    String methodBody = "";
//    
//    jm.getBody().appendSource(methodBody);
//    jm.setComment(new JMethodCommentImpl("Method for EXI serialization (schema-less AND schema-informed)."));
//    
//    return jm;
//  }
//  
//  public JMethod createDeserializeFunction() throws Exception
//  {
//    JMethodSignature jms = JMethodSignature.factory.create(
//            JParameter.factory.create(JModifier.FINAL, "org.xml.sax.XMLReader", "exiReader"),
//            JParameter.factory.create(JModifier.FINAL, "java.io.File", "exiDocument"));
//    
//    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "deserialize", jms, new String[] { "IOException", "SAXException", "TransformerException" });
//    
//    String methodBody = "";
//    
//    jm.getBody().appendSource(methodBody);
//    jm.setComment(new JMethodCommentImpl("Method for EXI deserialization."));
//    
//    return jm;
//  }  
}
