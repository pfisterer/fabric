/** 20.10.2011 23:39 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Converter class for the Simple XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
 */
public class Simple extends XMLLibrary
{
  /**
   * Parameterized constructor.
   *    
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public Simple(final String beanClassName) throws Exception
  {
    super(beanClassName);
  }

  /**
   * This method generates code that translates an annotated
   * Java object to a plain XML document.
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateJavaToXMLCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, this.beanClassName, "beanObject"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
            "instanceToXML", jms, new String[] { "Exception" });

    String methodBody =
            "Format xmlHeader = new Format(\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\");\n" +
            "Serializer serializer = new Persister(xmlHeader);\n" +
            "StringWriter xmlDocument = new StringWriter();\n\n" +
            "serializer.write(beanObject, xmlDocument);\n\n" +
            "return xmlDocument.toString();";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("org.simpleframework.xml.stream.Format");
    this.addRequiredImport("org.simpleframework.xml.Serializer");
    this.addRequiredImport("org.simpleframework.xml.core.Persister");
    this.addRequiredImport("java.io.StringWriter");
  }

  /**
   * This method generates code that translates a plain XML
   * document to a Java class instance.
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateXMLToInstanceCode() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, this.beanClassName,
            "xmlToInstance", jms, new String[] { "Exception" });
    
    String methodBody = String.format(
            "Serializer serializer = new Persister();\n" +
            "%s beanObject = serializer.read(%s.class, xmlDocument);\n\n" +
            "return beanObject;",
            this.beanClassName, this.beanClassName);

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));
    
    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("org.simpleframework.xml.Serializer");
    this.addRequiredImport("org.simpleframework.xml.core.Persister");
  }
}
