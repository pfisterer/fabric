/** 20.10.2011 23:40 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;
import java.util.ArrayList;

/**
 * Converter class for the JAXB XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
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
  public void generateJavaToXMLCode(final ArrayList<ArrayData> fixArrays,
                                    final ArrayList<SimpleListData> fixSimpleLists,
                                    final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
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
            "return removeValueTags(xmlDocument.toString());",
            this.beanClassName);

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
  public void generateXMLToInstanceCode(final ArrayList<ArrayData> fixArrays,
                                        final ArrayList<SimpleListData> fixSimpleLists,
                                        final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, this.beanClassName,
            "xmlToInstance", jms, new String[] { "Exception" });

    String methodBody = String.format(
            "JAXBContext context = JAXBContext.newInstance(%s.class);\n" +
            "Unmarshaller unmarshaller = context.createUnmarshaller();\n\n" +
            "return (%s)unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(addValueTags(xmlDocument).getBytes())));",
            this.beanClassName, this.beanClassName);

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
   * values-tag and value-tags from a list in an XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  protected JMethod generateRemoveTagFromList() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "list"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "removeTagFromList", jms);

    String methodBody_private =
            "NodeList rootNodes = doc.getElementsByTagName(list);\n" +
            "for (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\tElement root = (Element) rootNodes.item(i);\n" +
            "\t// Get all child nodes of root with a value-tag\n" +
            "\tNodeList children = root.getElementsByTagName(\"values\");\n" +
            "\tif (children.getLength() == 1) {\n" +
            "\t\tString newContent = children.item(0).getTextContent();\n"+
            "\t\t// Remove value-tag from root element\n" +
            "\t\troot.removeChild(children.item(0));\n" +
            "\t\troot.setTextContent(newContent);\n" +
            "\t}\n" +
            "}";

    jm.getBody().appendSource(methodBody_private);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tag from the XML element."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }

  /**
   * Private helper method to generate code that adds
   * value-tags to one single list in an XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  protected JMethod generateAddTagToList() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "list"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"),
            JParameter.factory.create(JModifier.FINAL, "boolean", "isSimple"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "addTagToList", jms);

    String methodBody =
            "if (! isSimple) {\n" +
            "\tNodeList rootNodes = doc.getElementsByTagName(list);\n" +
            "\tfor (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\t\tElement root    = (Element) rootNodes.item(i);\n" +
            "\t\tElement child   = doc.createElement(\"values\");\n" +
            "\t\tchild.setTextContent(root.getTextContent());\n" +
            "\t\troot.removeChild(root.getFirstChild());\n" +
            "\t\troot.appendChild(child);\n" +
            "\t}\n"+
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add values-tag and/or value-tags to the XML list."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }
}
