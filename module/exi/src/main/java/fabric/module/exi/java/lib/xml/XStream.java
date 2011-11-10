/** 06.11.2011 02:55 */
package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JField;
import de.uniluebeck.sourcegen.java.JFieldCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

import fabric.module.exi.java.FixValueContainer;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

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
    
    this.generateStreamInitializationCode();
  }

  /**
   * Private helper method to generate code that creates and
   * initializes the XStream de-/serializer member variable.
   *
   * @throws Exception Error during code generation
   */
  private void generateStreamInitializationCode() throws Exception
  {
    // Create member variable for XStream object
    JField streamObject = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "XStream", "stream", "null");
    streamObject.setComment(new JFieldCommentImpl("The XStream de-/serializer member variable."));
    this.converterClass.add(streamObject);

    // Initialize XStream member variable
    this.converterClass.appendStaticCode(String.format("%s.setupStreamObject();", this.converterClass.getName()));

    // Create method for XStream object setup
    JMethod setupStreamObject = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupStreamObject");

    String methodBody = String.format(
            "// Create XStream object\n" +
            "%s.stream = new XStream(new Sun14ReflectionProvider() {\n" +
            "\t@Override\n" +
            "\tprotected boolean fieldModifiersSupported(Field field) {\n" +
            "\t\tint modifiers = field.getModifiers();\n\n" +
            "\t\t// Write static fields to XML document as well when serializing\n" +
            "\t\treturn !(Modifier.isTransient(modifiers));\n" +
            "\t}\n\n" +
            "\t@Override\n" +
            "\tpublic void writeField(Object object, String fieldName, Object value, Class definedIn) {\n" +
            "\t\t// Ignore static fields when deserializing, content is already there!\n" +
            "\t\tif (!Modifier.isStatic(fieldDictionary.field(object.getClass(), fieldName, definedIn).getModifiers())) {\n" +
            "\t\t\tsuper.writeField(object, fieldName, value, definedIn);\n" +
            "\t\t}\n" +
            "\t}\n" +
            "});\n" +
            "stream.autodetectAnnotations(true);",
            this.converterClass.getName());
    
    setupStreamObject.getBody().appendSource(methodBody);
    setupStreamObject.setComment(new JMethodCommentImpl("Setup XStream de-/serializer member variable."));

    this.converterClass.add(setupStreamObject);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
    this.addRequiredImport("com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider");
    this.addRequiredImport("java.lang.reflect.Field");
    this.addRequiredImport("java.lang.reflect.Modifier");
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

    //String methodBody = addAliasForLists(fixLists);

    String methodBody = String.format(
            "%s.stream.alias(\"%s\", %s.class);\n\n" +
            "StringWriter xmlDocument = new StringWriter();\n" +
            "BufferedWriter serializer = new BufferedWriter(xmlDocument);\n" +
            "serializer.write(%s.stream.toXML(beanObject));\n" +
            "serializer.close();\n\n" +
            "return removeValueTags(xmlDocument.toString());",
            this.converterClass.getName(), this.beanClassName.toLowerCase(), this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
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

    //String methodBody = addAliasForLists(fixLists);
    
    String methodBody = String.format(
            "%s.stream.alias(\"%s\", %s.class);\n\n" +
            "return (%s)%s.stream.fromXML(addValueTags(xmlDocument));",
            this.converterClass.getName(), this.beanClassName, this.beanClassName, this.beanClassName,
            this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
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
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"),
            JParameter.factory.create(JModifier.FINAL, "boolean", "isCustomTyped"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "removeTagFromList", jms);

    String methodBody =
            "NodeList rootNodes = doc.getElementsByTagName(list);\n" +
            "if (rootNodes.getLength() > 0) {\n" +
            "\tElement first = (Element) rootNodes.item(0);\n" +
            "\tNodeList values = (isCustomTyped ? first.getElementsByTagName(\"values\") : rootNodes);\n" +
            "\tString newContent = values.item(0).getTextContent();\n" +
            "\twhile (values.getLength() > 1) {\n" +
            "\t\tElement value = (Element) values.item(1);\n" +
            "\t\tnewContent += \" \" + value.getTextContent();\n" +
            "\t\tvalue.getParentNode().removeChild(value.getNextSibling());\n" +
            "\t\tvalue.getParentNode().removeChild(value);\n" +
            "\t}\n" +
            "\tfirst.setTextContent(newContent.trim());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
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
            JParameter.factory.create(JModifier.FINAL, "boolean", "isCustomTyped"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "addTagToList", jms);

    String methodBody =
                "NodeList rootNodes = doc.getElementsByTagName(list);\n" +
		"for (int i = 0; i < rootNodes.getLength();) {\n" +
                "\tElement valueList   = (Element) rootNodes.item(i);\n" +
                "\tString[] content    = valueList.getTextContent().split(\" \");\n" +
                "\tif (isCustomTyped) {\n" +
                "\t\tElement parent = doc.createElement(list);\n" +
                "\t\tfor (int j = 0; j < content.length; j++, i++) {\n" +
                "\t\t\tElement child = doc.createElement(\"values\");\n" +
                "\t\t\tchild.appendChild(doc.createTextNode(content[j]));\n" +
                "\t\t\tparent.appendChild(child);\n" +
                "\t\t}\n" +
                "\t\tvalueList.getParentNode().replaceChild(parent, valueList);\n" +
                "\t} else {\n" +
                "\t\tfor (int j = 0; j < content.length; j++, i++) {\n" +
                "\t\t\tElement child = doc.createElement(list);\n" +
                "\t\t\tchild.appendChild(doc.createTextNode(content[j]));\n" +
                "\t\t\tvalueList.getParentNode().insertBefore(child, valueList);\n" +
                "\t\t}\n" +
                "\t\tvalueList.getParentNode().removeChild(valueList);\n" +
                "\t}\n" +
		"}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add values-tag and/or value-tags to the XML list."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }
}
