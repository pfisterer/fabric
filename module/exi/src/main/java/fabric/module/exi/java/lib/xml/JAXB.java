/** 02.12.2011 12:35 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Converter class for the JAXB XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel, reichart
 */
public class JAXB extends XMLLibrary
{
  /**
   * Parameterized constructor.
   *
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public JAXB(final String beanClassName) throws Exception
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

    String methodBody = String.format(
            "JAXBContext context = JAXBContext.newInstance(%s.class);\n" +
            "Marshaller marshaller = context.createMarshaller();\n" +
            "marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);\n" +
            "marshaller.setProperty(Marshaller.JAXB_ENCODING, \"UTF-8\");\n" +
            "marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);\n\n" +
            "StringWriter xmlDocument = new StringWriter();\n" +
            "marshaller.marshal(beanObject, xmlDocument);\n\n" +
            "return %s.removeValueTags(xmlDocument.toString());",
            this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("javax.xml.bind.JAXBContext");
    this.addRequiredImport("javax.xml.bind.Marshaller");
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
            "JAXBContext context = JAXBContext.newInstance(%s.class);\n" +
            "Unmarshaller unmarshaller = context.createUnmarshaller();\n\n" +
            "return (%s)unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(" +
            "%s.addValueTags(xmlDocument).getBytes())));",
            this.beanClassName, this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("javax.xml.bind.JAXBContext");
    this.addRequiredImport("javax.xml.bind.Unmarshaller");
    this.addRequiredImport("org.xml.sax.InputSource");
    this.addRequiredImport("java.io.ByteArrayInputStream");
  }

  /**
   * Private helper method to generate code that removes unnecessary
   * value- and values-tags from a list in an XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  protected JMethod generateRemoveTagFromList() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "listName"),
            JParameter.factory.create(JModifier.FINAL, "Document", "document"),
            JParameter.factory.create(JModifier.FINAL, "boolean", "isCustomTyped"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "removeTagFromList", jms);

    String methodBody =
            "NodeList parentNodes = document.getElementsByTagName(listName);\n\n" +
            "// Process all elements below parent node\n" +
            "for (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\tElement parent = (Element)parentNodes.item(i);\n\n" +
            "\t// Get all child nodes of parent that have a values-tag\n" +
            "\tNodeList children = parent.getElementsByTagName(\"values\");\n\n" +
            "\tif (children.getLength() == 1) {\n" +
            "\t\tString newContent = children.item(0).getTextContent();\n"+
            "\t\tparent.removeChild(children.item(0));\n" +
            "\t\tparent.setTextContent(newContent);\n" +
            "\t}\n" +
            "}";
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary values-tag from XML list."));

    // Add required Java imports
    this.addRequiredImport("org.w3c.dom.Document");
    this.addRequiredImport("org.w3c.dom.Element");
    this.addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }

  /**
   * Private helper method to generate code that adds
   * value-tags to a single list in an XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  protected JMethod generateAddTagToList() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "listName"),
            JParameter.factory.create(JModifier.FINAL, "Document", "document"),
            JParameter.factory.create(JModifier.FINAL, "boolean", "isCustomTyped"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "addTagToList", jms);
    
    String methodBody =
            "if (isCustomTyped) {\n" +
            "\tNodeList parentNodes = document.getElementsByTagName(listName);\n\n" +
            "\t// Process all elements below parent node\n" +
            "\tfor (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\t\tElement parent = (Element)parentNodes.item(i);\n" +
            "\t\tElement child = document.createElement(\"values\");\n\n" +
            "\t\tchild.setTextContent(parent.getTextContent());\n" +
            "\t\tparent.removeChild(parent.getFirstChild());\n" +
            "\t\tparent.appendChild(child);\n" +
            "\t}\n"+
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add values-tag to XML list."));

    // Add required Java imports
    this.addRequiredImport("org.w3c.dom.Document");
    this.addRequiredImport("org.w3c.dom.Element");
    this.addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }
}
