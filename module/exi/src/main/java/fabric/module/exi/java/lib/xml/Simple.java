/** 12.11.2011 01:41 */
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
 * @author seidel, reichart
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

    String methodBody = String.format(
            "Format xmlHeader = new Format(\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\");\n" +
            "Serializer serializer = new Persister(xmlHeader);\n" +
            "StringWriter xmlDocument = new StringWriter();\n\n" +
            "serializer.write(beanObject, xmlDocument);\n\n" +
            "return %s.removeValueTags(xmlDocument.toString());",
            this.converterClass.getName());

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
            "%s beanObject = serializer.read(%s.class, %s.addValueTags(xmlDocument));\n\n" +
            "return beanObject;",
            this.beanClassName, this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));
    
    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("org.simpleframework.xml.Serializer");
    this.addRequiredImport("org.simpleframework.xml.core.Persister");
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

    String methodBody = String.format(
            "NodeList rootNodes = document.getElementsByTagName(listName);\n\n" +
            "// Process all elements below root node\n" +
            "for (int i = 0; i < rootNodes.getLength(); ++i) {\n" +
            "\tElement root = (Element)rootNodes.item(i);\n\n" +
            "\t// Get all child nodes of root that have a values-tag\n" +
            "\tNodeList children = root.getElementsByTagName(\"values\");\n\n" +
            "\tif (children.getLength() == 1) {\n" +
            "\t\tElement valueList = (Element)children.item(0);\n"+
            "\t\twhile (valueList.hasChildNodes()) {\n" +
            "\t\t\troot.appendChild(valueList.getFirstChild().cloneNode(true));\n" +
            "\t\t\tvalueList.removeChild(valueList.getFirstChild());\n" +
            "\t\t}\n" +
            "\t\troot.removeChild(valueList);\n" +
            "\t}\n" +
            "\t%s.removeTagFromElement(listName, document);\n" +
            "}",
            this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary values-tag from XML list."));
    
    // Add required Java import
    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

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
            "NodeList rootNodes = document.getElementsByTagName(listName);\n\n" +
            "// Process all elements below root node\n" +
            "for (int i = 0; i < rootNodes.getLength(); ++i) {\n" +
            "\tElement root = (Element)rootNodes.item(i);\n" +
            "\tString[] content = root.getTextContent().split(\" \");\n\n" +
            "\tif (isCustomTyped) {\n" +
            "\t\t// Insert new values-tag\n" +
            "\t\tElement child = document.createElement(\"values\");\n" +
            "\t\tchild.setTextContent(root.getTextContent());\n" +
            "\t\troot.removeChild(root.getFirstChild());\n" +
            "\t\troot.appendChild(child);\n" +
            "\t\troot = child;\n" +
            "\t}\n\n" +
            "\t// Each list item has to get its own value-tag\n" +
            "\tfor (int j = 0; j < content.length; ++j) {\n" +
            "\t\tElement child = document.createElement(\"value\");\n" +
            "\t\tchild.appendChild(document.createTextNode(content[j]));\n" +
            "\t\troot.appendChild(child);\n" +
            "\t}\n" +
            "\troot.removeChild(root.getFirstChild());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add values-tag to XML list."));
    
    // Add required Java imports
    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }
}
