/** 08.11.2011 14:25 */
package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;
import java.util.Collections;

import de.uniluebeck.sourcegen.java.*;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

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
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   *
   * @return JClass object with XML converter class
   *
   * @throws Exception Error during code generation
   */
  public JClass init(final ArrayList<ElementData> fixElements,
                     final ArrayList<ArrayData> fixArrays,
                     final ArrayList<ListData> fixLists) throws Exception
  {
    // Generate code for XML serialization
    this.generateJavaToXMLCode();

    // Generate code for XML deserialization
    this.generateXMLToInstanceCode();

    // Generate code to fix value-tags
    this.generateFixValueCode(fixElements, fixArrays, fixLists);

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
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  public void generateFixValueCode(final ArrayList<ElementData> fixElements,
                                   final ArrayList<ArrayData> fixArrays,
                                   final ArrayList<ListData> fixLists) throws Exception
  {
    // Generate Code for removing unnecessary tags
    JMethod removeTagFromElement    = generateRemoveTagFromElement();
    JMethod removeTagFromList       = generateRemoveTagFromList();
    JMethod removeValueTags         = generateRemoveValueTags(fixElements, fixArrays, fixLists);

    if (null != removeValueTags && null != removeTagFromElement && null != removeTagFromList)
    {
      this.converterClass.add(removeValueTags);
      this.converterClass.add(removeTagFromElement);
      this.converterClass.add(removeTagFromList);
    }

    // Generate Code for adding tags
    JMethod addTagToElement = generateAddTagToElement();
    JMethod addTagToList    = generateAddTagToList();
    JMethod addValueTags    = generateAddValueTags(fixElements, fixArrays, fixLists);
    if (null != addValueTags && null != addTagToElement && null != addTagToList)
    {
      this.converterClass.add(addValueTags);
      this.converterClass.add(addTagToElement);
      this.converterClass.add(addTagToList);
    }
  }
  
  /**
   * Private helper method to generate code that removes unnecessary
   * value-tags from XML documents.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateRemoveValueTags(final ArrayList<ElementData> fixElements,
                                          final ArrayList<ArrayData> fixArrays,
                                          final ArrayList<ListData> fixLists) throws Exception
  {
    // TODO: Remove this block after test BEGIN
    for (ElementData ed: fixElements)
    {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Element: " + ed.getName());
    }

    for (ArrayData ad: fixArrays)
    {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Array: " +

              ad.getArrayName() + ", " +
              ad.getArrayType() + ", " +
              ad.getItemName() + ", " +
              ad.getItemType() + ", " +
              ad.isCustomTyped() + ", " +
              ad.getParentContainerName());
    }

    for (ListData ld: fixLists)
    {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> List: " +
              ld.getListName() + ", " +
              ld.getListType() + ", " +
              ld.getItemType() + ", " +
              ld.isCustomTyped());
    }
    // TODO: Remove this block after test END

    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "String",
            "removeValueTags", jms);

    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", null).newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements\n";

    // Remove tag from elements
    for (ElementData element : fixElements) {
        methodBody += String.format("\tremoveTagFromElement(\"%s\", doc);\n", element.getName());
    }
    // Remove tag from element arrays
    methodBody += "\t// Fix tags in element arrays\n";
    for (ArrayData array : fixArrays) {
        if (array.isCustomTyped()) {
            methodBody += String.format("\tremoveTagFromElement(\"%s\", doc);\n", array.getArrayName());
        }
    }
    // Remove tag from lists
    methodBody += "\t// Fix tags in lists\n";
    for (ListData list : fixLists) {
        methodBody += String.format(
                "\tremoveTagFromList(\"%s\", doc, %b);\n",
                list.getListName(), list.isCustomTyped());
    }

    methodBody +=
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
   * Private helper method to generate code that removes unnecessary
   * value-tags from an element or element array in an XML document.
   *
   * @throws Exception Error during code generation
   */
  protected JMethod generateRemoveTagFromElement() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "element"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "removeTagFromElement", jms);

    String methodBody =
            "NodeList rootNodes = doc.getElementsByTagName(element);\n" +
            "// Length of rootNodes is greater than 1 if we have an element array\n" +
            "for (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\tElement root = (Element) rootNodes.item(i);\n" +
            "\t// Get all child nodes of root with a value-tag\n" +
            "\tNodeList children = root.getElementsByTagName(\"value\");\n" +
            "\tString newContent   = \"\";\n" +
            "\twhile (children.getLength() > 0) {\n" +
            "\t\tElement value = (Element) children.item(0);\n" +
            "\t\tnewContent += value.getTextContent() + \" \";\n" +
            "\t\troot.removeChild(value);\n" +
            "\t}\n" +
            "\troot.setTextContent(newContent.trim());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tag from the XML element."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }

  /**
   * Private helper method to generate code that removes unnecessary
   * values-tag and value-tags from a list in an XML document.
   *
   * @throws Exception Error during code generation
   */
  abstract protected JMethod generateRemoveTagFromList() throws Exception;

  /**
   * Private helper method to generate code that adds value-tags
   * to XML documents.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateAddValueTags(final ArrayList<ElementData> fixElements,
                                       final ArrayList<ArrayData> fixArrays,
                                       final ArrayList<ListData> fixLists) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "String",
            "addValueTags", jms);
    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", null).newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements\n";

    // Add tag to elements
    for (ElementData element : fixElements) {
        methodBody += String.format("\taddTagToElement(\"%s\", doc);\n", element.getName());
    }
    // Add tag to element arrays
    methodBody += "\t// Fix tags in element arrays\n";
    for (ArrayData array : fixArrays) {
        if (array.isCustomTyped()) {
            methodBody += String.format("\taddTagToElement(\"%s\", doc);\n", array.getArrayName());
        }
    }
    // Add tag to lists
    methodBody += "\t// Fix tags in lists\n";
    for (ListData list : fixLists) {
        methodBody += String.format(
                "\taddTagToList(\"%s\", doc, %b);\n",
                list.getListName(), list.isCustomTyped());
    }

    methodBody +=
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
   * Private helper method to generate code that adds
   * value-tags to one single element in an XML document.
   *
   * @throws Exception Error during code generation
   */
  protected JMethod generateAddTagToElement() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "element"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "addTagToElement", jms);

    String methodBody =
            "NodeList rootNodes = doc.getElementsByTagName(element);\n" +
            "// Length of rootNodes is greater than 1 if we have an element array\n" +
            "for (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\tElement root    = (Element) rootNodes.item(i);\n" +
            "\tElement child   = doc.createElement(\"value\");\n" +
            "\tchild.appendChild(root.getFirstChild().cloneNode(true));\n" +
            "\troot.replaceChild(child, root.getFirstChild());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add value-tag to the XML element."));

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
  abstract protected JMethod generateAddTagToList() throws Exception;
}
