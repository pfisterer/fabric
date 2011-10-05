package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Converter class for the Simple XML framework. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
 */
public class Simple extends XMLFramework
{
  public Simple(final String beanClassName) throws Exception
  {
    super(beanClassName);
  }
  
  /**
   * This method generates code that translates an annotated
   * Java object to a plain XML document.
   *
   * @return String with code for Java to XML conversion
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateJavaToXMLCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, BEAN_NAME, "beanObject"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "javaToXML", jms, new String[] { "Exception" });
    
    String methodBody =
            "Format fileHeader = new Format(\"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\");\n" +
            "Serializer serializer = new Persister(fileHeader);\n" +
            "File xmlFile = new File(\"document.xml\");\n\n" +
            "serializer.write(%s, xmlFile);";
    
    jm.getBody().appendSource(String.format(methodBody, BEAN_NAME));
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));
    
    this.converterClass.add(jm);
  }

  /**
   * This method generates code that translates a plain XML
   * document to a Java class instance.
   *
   * @return String with code for XML to instance conversion
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateXMLToInstanceCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "java.io.File", "xmlDocument"));
    
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, BEAN_NAME, "xmlToInstance", jms, new String[] { "Exception" });
    
    String methodBody =
            "Serializer serializer = new Persister();\n" +
            "%s beanObject = serializer.read(%s.class, xmlDocument);\n\n" +
            "return beanObject;";
    
    jm.getBody().appendSource(String.format(methodBody, BEAN_NAME, BEAN_NAME));
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));
    
    this.converterClass.add(jm);
  }
}
