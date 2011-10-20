/** 11.10.2011 02:18 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Converter class for the XStream XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
 */
public class XStream extends XMLLibrary
{
  /**
   * Parameterized constructor.
   *
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public XStream(final String beanClassName) throws Exception
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
            "XStream stream = new XStream(new Sun14ReflectionProvider());\n" +
            "stream.alias(\"%s\", %s.class);\n\n" +
            "StringWriter xmlDocument = new StringWriter();\n" +
            "BufferedWriter serializer = new BufferedWriter(xmlDocument);\n" +
            "serializer.write(stream.toXML(beanObject));\n" +
            "serializer.close();\n\n" +
            "return xmlDocument.toString();";

// TODO: Remove this code    
//            "Format xmlHeader = new Format(\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\");\n" +
//            "Serializer serializer = new Persister(xmlHeader);\n" +
//            "StringWriter xmlDocument = new StringWriter();\n\n" +
//            "serializer.write(beanObject, xmlDocument);\n\n" +
//            "return xmlDocument.toString();";
    
    jm.getBody().appendSource(String.format(methodBody, this.beanClassName.toLowerCase(), this.beanClassName));
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

    this.converterClass.add(jm);

    // Add required Java imports
    // TODO: Check required imports independently
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
    this.addRequiredImport("com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider");
    this.addRequiredImport("java.io.BufferedWriter");
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
    
    String methodBody =
            "XStream stream = new XStream(new Sun14ReflectionProvider());\n" +
            "stream.alias(\"%s\", %s.class);\n\n" +
            "return (%s)stream.fromXML(xmlDocument);";
    
// TODO: Remove this code
//            "Serializer serializer = new Persister();\n" +
//            "%s beanObject = serializer.read(%s.class, xmlDocument);\n\n" +
//            "return beanObject;";
    
    jm.getBody().appendSource(String.format(methodBody, this.beanClassName.toLowerCase(), this.beanClassName, this.beanClassName));
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));
    
    this.converterClass.add(jm);

    // Add required Java imports
    // TODO: Check required imports independently
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
    this.addRequiredImport("com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider");
  }
}
