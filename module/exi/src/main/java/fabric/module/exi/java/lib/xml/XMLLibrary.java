/** 15.10.2011 23:13 */
package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;
import java.util.Collections;

import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
import de.uniluebeck.sourcegen.java.*;

/**
 * Abstract base class for XML libraries. Derived files
 * generate code to translate annotated Java object to XML
 * and vice versa. Each implementation of the interface may
 * operate a different XML library.
 *
 * @author seidel
 */
abstract public class XMLLibrary
{
  /** List of required Java imports */
  protected ArrayList<String> requiredImports;

  /** Class for XML serialization and deserialization */
  protected JClass converterClass;

  /** Name of the target Java bean class */
  protected String beanClassName;

  /**
   * Default constructor is private. Use parameterized version instead.
   */
  private XMLLibrary()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor initializes XML converter class
   * and sets bean class name.
   * 
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public XMLLibrary(final String beanClassName) throws Exception
  {
    this.requiredImports = new ArrayList<String>();

    this.converterClass = JClass.factory.create(JModifier.PUBLIC, beanClassName + "Converter");
    this.converterClass.setComment(new JClassCommentImpl("The XML converter class."));

    this.beanClassName = beanClassName;
  }

  /**
   * Private helper method to add an import to the internal list
   * of required imports. All entries will later be written to
   * the Java source file.
   *
   * Multiple calls to this function with the same argument will
   * result in a single import statement on source code write-out.
   *
   * @param requiredImport Name of required import
   */
  protected void addRequiredImport(final String requiredImport)
  {
    if (null != requiredImport && !this.requiredImports.contains(requiredImport))
    {
      this.requiredImports.add(requiredImport);
    }
  }

  /**
   * Helper method to get a list of required imports for the
   * XML converter class.
   *
   * @return List of required imports
   */
  public ArrayList<String> getRequiredImports()
  {
    Collections.sort(this.requiredImports);

    return this.requiredImports;
  }

  /**
   * Initialize the XMLLibrary implementation. This means we run
   * all methods that the interface defines, in order to create
   * the XML converter class we return eventually.
   *
   * @param fixElements Elements where value-tags need to be fixed
   *
   * @return JClass object with XML converter class
   *
   * @throws Exception Error during code generation
   */
  public JClass init(final ArrayList<String> fixElements) throws Exception
  {
    // Generate code for XML serialization
    this.generateJavaToXMLCode();

    // Generate code for XML deserialization
    this.generateXMLToInstanceCode();

    // Generate code to fix value-tags
    this.generateFixValueCode(fixElements);

    return this.converterClass;
  }

  /**
   * Method that creates code to convert an annotated Java
   * object to a plain XML document.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateJavaToXMLCode() throws Exception;

  /**
   * Method that creates code to convert an XML document
   * to a class instance of the corresponding Java bean.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateXMLToInstanceCode() throws Exception;

  /**
   * This method generates code to fix a problem within the
   * XML documents that most frameworks create. If we have an
   * annotated Java class that represents a custom XML simple
   * type, the XML code will usually look like this:
   *
   * <MyString>
   *   <value>Some text.</value>
   * </MyString>
   *
   * Unfortunately this is not in compliance with the XML schema,
   * so we have to adjust the generated XML code to look like this:
   *
   * <MyString>Some text.</MyString>
   *
   * However, we cannot simply strip the value-tags, because they
   * could be part of the content or the name of a valid XML tag.
   * So we collect all affected elements while treewalking the
   * XML schema document and only adjust the XML code for those.
   *
   * @param affectedElements List of elements with value-tag
   *
   * @throws Exception Error during code generation
   */
  public void generateFixValueCode(final ArrayList<String> affectedElements) throws Exception
  {
    JMethod removeValueTags = this.generateRemoveValueTags(affectedElements);
    if (null != removeValueTags)
    {
      this.converterClass.add(removeValueTags);
    }

    JMethod addValueTags = this.generateAddValueTags(affectedElements);
    if (null != addValueTags)
    {
      this.converterClass.add(addValueTags);
    }
  }
  
  /**
   * Private helper method to generate code that removes unnecessary
   * value-tags from XML documents.
   * 
   * @param affectedElements List of elements with value-tag
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateRemoveValueTags(final ArrayList<String> affectedElements) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"),
            JParameter.factory.create(JModifier.FINAL, "ArrayList<String>", "affectedElements"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "String",
            "removeValueTags", jms);
    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance()\n" +
            "\t\t      .newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements, element lists and element arrays\n" +
            "\tfor (String element : affectedElements) {\n" +
            "\t\tNodeList rootNodes = doc.getElementsByTagName(element);\n" +
            "\t\t// Length of rootNodes is greater than 1 if we have an element array\n" +
            "\t\tfor (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\t\t\tElement root        = (Element) rootNodes.item(i);\n" +
            "\t\t\t// Get all child nodes of root with a value-tag\n" +
            "\t\t\tNodeList children = root.getElementsByTagName(\"value\");\n" +
            "\t\t\t// Check if this is an element array (length = 0) or not (length > 0)\n" +
            "\t\t\tif (children.getLength() > 0) {\n" +
            "\t\t\t\tString newContent   = \"\";\n" +
            "\t\t\t\twhile (children.getLength() > 0) {\n" +
            "\t\t\t\t\tnewContent += children.item(0).getTextContent() + \" \";\n" +
            "\t\t\t\t\t// Remove value-tag from root element\n" +
            "\t\t\t\t\troot.removeChild(children.item(0));\n" +
            "\t\t\t\t}\n" +
            "\t\t\t\troot.setTextContent(newContent.trim());\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\t// Create instances for writing output\n" +
            "\tSource source               = new DOMSource(doc);\n" +
            "\tStringWriter stringWriter   = new StringWriter();\n" +
            "\tResult result               = new StreamResult(stringWriter);\n" +
            "\tTransformerFactory factory  = TransformerFactory.newInstance();\n" +
            "\tTransformer transformer     = factory.newTransformer();\n" +
            "\ttransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "\ttransformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n" +
            "\ttransformer.transform(source, result);\n" +
            "\t// Resulting XML as a string\n" +
            "\tString ret = stringWriter.getBuffer().toString();\n" +
            "\tstringWriter.close();\n" +
            "\treturn ret;\n" +
            "} catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n" +
            "return null;";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tags from the XML document."));

    addRequiredImport("java.io.ByteArrayInputStream");
    addRequiredImport("java.io.StringWriter");
    addRequiredImport("java.util.ArrayList");
    addRequiredImport("javax.xml.parsers.DocumentBuilderFactory");
    addRequiredImport("javax.xml.transform.OutputKeys");
    addRequiredImport("javax.xml.transform.Result");
    addRequiredImport("javax.xml.transform.Source");
    addRequiredImport("javax.xml.transform.Transformer");
    addRequiredImport("javax.xml.transform.TransformerFactory");
    addRequiredImport("javax.xml.transform.dom.DOMSource");
    addRequiredImport("javax.xml.transform.stream.StreamResult");
    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }

  /**
   * Private helper method to generate code that adds value-tags
   * to XML documents.
   * 
   * @param affectedElements List of elements with value-tag
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateAddValueTags(final ArrayList<String> affectedElements) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"),
            JParameter.factory.create(JModifier.FINAL, "ArrayList<String>", "affectedElements"),
            JParameter.factory.create(JModifier.FINAL, "ArrayList<String>", "affectedLists"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "String",
            "addValueTags", jms);
    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance()\n" +
            "\t\t      .newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements and element arrays\n" +
            "\tfor (String element : affectedElements) {\n" +
            "\t\tNodeList rootNodes = doc.getElementsByTagName(element);\n" +
            "\t\t// Length of rootNodes is greater than 1 if we have an element array\n" +
            "\t\tfor (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\t\t\tElement root    = (Element) rootNodes.item(i);\n" +
            "\t\t\tElement child   = doc.createElement(\"value\");\n" +
            "\t\t\tchild.appendChild(root.getFirstChild().cloneNode(true));\n" +
            "\t\t\troot.replaceChild(child, root.getFirstChild());\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\t// Fix tags in element lists\n" +
            "\tfor (String list : affectedLists) {\n" +
            "\t\tNodeList rootNodes  = doc.getElementsByTagName(list);\n" +
            "\t\tfor (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\t\t\tElement root        = (Element) rootNodes.item(i);\n" +
            "\t\t\tString[] content    = root.getTextContent().split(\" \");\n" +
            "\t\t\t// Each value has to get its own value-tag\n" +
            "\t\t\tfor (int j = 0; j < content.length; j++) {\n" +
            "\t\t\t\tElement child = doc.createElement(\"value\");\n" +
            "\t\t\t\tchild.appendChild(doc.createTextNode(content[j]));\n" +
            "\t\t\t\troot.appendChild(child);\n" +
            "\t\t\t}\n" +
            "\t\t\troot.removeChild(root.getFirstChild());\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\t// Create instances for writing output\n" +
            "\tSource source               = new DOMSource(doc);\n" +
            "\tStringWriter stringWriter   = new StringWriter();\n" +
            "\tResult result               = new StreamResult(stringWriter);\n" +
            "\tTransformerFactory factory  = TransformerFactory.newInstance();\n" +
            "\tTransformer transformer     = factory.newTransformer();\n" +
            "\ttransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "\ttransformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "\ttransformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n" +
            "\ttransformer.transform(source, result);\n" +
            "\t// Resulting XML as a string\n" +
            "\tString ret = stringWriter.getBuffer().toString();\n" +
            "\tstringWriter.close();\n" +
            "\treturn ret;\n" +
            "} catch (Exception e) {\n" +
            "\te.printStackTrace();\n" +
            "}\n" +
            "return null;";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add value-tags to the XML document."));

    addRequiredImport("java.io.ByteArrayInputStream");
    addRequiredImport("java.io.StringWriter");
    addRequiredImport("java.util.ArrayList");
    addRequiredImport("javax.xml.parsers.DocumentBuilderFactory");
    addRequiredImport("javax.xml.transform.OutputKeys");
    addRequiredImport("javax.xml.transform.Result");
    addRequiredImport("javax.xml.transform.Source");
    addRequiredImport("javax.xml.transform.Transformer");
    addRequiredImport("javax.xml.transform.TransformerFactory");
    addRequiredImport("javax.xml.transform.dom.DOMSource");
    addRequiredImport("javax.xml.transform.stream.StreamResult");
    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");
    
    return null;
  }
}
