/** 31.10.2011 19:34 */
package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;
import java.util.Collections;
import de.uniluebeck.sourcegen.java.*;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

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
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   *
   * @return JClass object with XML converter class
   *
   * @throws Exception Error during code generation
   */
  public JClass init(final ArrayList<ElementData> fixElements,
                     final ArrayList<ArrayData> fixArrays,
                     final ArrayList<SimpleListData> fixSimpleLists,
                     final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    // Generate code for XML serialization
    this.generateJavaToXMLCode(fixArrays, fixSimpleLists, fixNonSimpleLists);

    // Generate code for XML deserialization
    this.generateXMLToInstanceCode(fixArrays, fixSimpleLists, fixNonSimpleLists);

    // Generate code to fix value-tags
    this.generateFixValueCode(fixElements, fixArrays, fixSimpleLists, fixNonSimpleLists);

    return this.converterClass;
  }

  /**
   * Method that creates code to convert an annotated Java
   * object to a plain XML document.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateJavaToXMLCode(final ArrayList<ArrayData> fixArrays,
                                             final ArrayList<SimpleListData> fixSimpleLists,
                                             final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception;

  /**
   * Method that creates code to convert an XML document
   * to a class instance of the corresponding Java bean.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateXMLToInstanceCode(final ArrayList<ArrayData> fixArrays,
                                                 final ArrayList<SimpleListData> fixSimpleLists,
                                                 final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception;

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
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  public void generateFixValueCode(final ArrayList<ElementData> fixElements,
                                   final ArrayList<ArrayData> fixArrays,
                                   final ArrayList<SimpleListData> fixSimpleLists,
                                   final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    // Generate Code for removing unnecessary tags
    JMethod removeTagFromElement    = generateRemoveTagFromElement();
    JMethod removeTagFromList       = generateRemoveTagFromList();
    JMethod removeValueTags         = generateRemoveValueTags(fixElements,fixArrays,fixSimpleLists,fixNonSimpleLists);
    if (null != removeValueTags && null != removeTagFromElement && null != removeTagFromList)
    {
      this.converterClass.add(removeValueTags);
      this.converterClass.add(removeTagFromElement);
      this.converterClass.add(removeTagFromList);
    }

    // Generate Code for adding tags
    JMethod addTagToElement = generateAddTagToElement();
    JMethod addTagToList    = generateAddTagToList();
    JMethod addValueTags    = generateAddValueTags(fixElements, fixArrays, fixSimpleLists, fixNonSimpleLists);
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
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateRemoveValueTags(final ArrayList<ElementData> fixElements,
                                          final ArrayList<ArrayData> fixArrays,
                                          final ArrayList<SimpleListData> fixSimpleLists,
                                          final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
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
              ad.getItemType());
    }

    for (SimpleListData sld: fixSimpleLists)
    {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> SimpleList: " +
              sld.getListName() + ", " +
              sld.getListType() + ", " +
              sld.getItemType());
    }

    for (NonSimpleListData nsld: fixNonSimpleLists)
    {
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> NonSimpleList: " +
              nsld.getListName() + ", " +
              nsld.getListType() + ", " +
              nsld.getItemType());
    }
    // TODO: Remove this block after test END

    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "String",
            "removeValueTags", jms);

    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance()\n" +
            "\t\t      .newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements, lists and element arrays\n";
    // Remove tag from elements
    for (ElementData element : fixElements) {
        methodBody += "\tremoveTagFromElement(" + element.getName() + ", doc);\n";
    }
    // Remove tag from element arrays
    methodBody += "\t// Fix tags in element arrays\n";
    for (ArrayData array : fixArrays) {
        methodBody += "\tremoveTagFromElement(" + array.getArrayName() + ", doc);\n";
    }
    // Remove tag from lists of simple type
    methodBody += "\t// Fix tags in lists of simple type\n";
    for (SimpleListData list : fixSimpleLists) {
        methodBody += "\tremoveTagFromList(" + list.getListName() + ", doc);\n";
    }
    // Remove tag from lists of restricted simple type
    methodBody += "\t// Fix tags in lists of restricted simple type\n";
    for (NonSimpleListData list : fixNonSimpleLists) {
        methodBody += "\tremoveTagFromList(" + list.getListName() + ", doc);\n";
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
   * Private helper method to generate code that removes unnecessary
   * value-tags from an element or element array in an XML document.
   *
   * @throws Exception Error during code generation
   */
  protected JMethod generateRemoveTagFromElement() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "element"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "void", "removeTagFromElement", jms);

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
            "\t\t}\n" +
            "\t\troot.setTextContent(newContent.trim());\n" +
            "\t}\n" +
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
  protected JMethod generateRemoveTagFromList() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "list"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "void", "removeTagFromList", jms);

    String methodBody =
            "NodeList rootNodes = doc.getElementsByTagName(list);\n" +
            "for (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\tElement root = (Element) rootNodes.item(i);\n" +
            "\t// Get all child nodes of root with a value-tag\n" +
            "\tNodeList children = root.getElementsByTagName(\"values\");\n" +
            "\tif (children.getLength() == 1) {\n" +
            "\t\tElement valueList = (Element) children.item(0);\n"+
            "\t\twhile (valueList.hasChildNodes()) {\n" +
            "\t\t\troot.appendChild(valueList.getFirstChild().cloneNode(true));\n" +
            "\t\t\tvalueList.removeChild(valueList.getFirstChild());\n" +
            "\t\t}\n" +
            "\t\troot.removeChild(valueList);\n" +
            "\t}\n" +
            "\tfixElement(list, doc);\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tag from the XML element."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }

  /**
   * Private helper method to generate code that adds value-tags
   * to XML documents.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  private JMethod generateAddValueTags(final ArrayList<ElementData> fixElements,
                                       final ArrayList<ArrayData> fixArrays,
                                       final ArrayList<SimpleListData> fixSimpleLists,
                                       final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "String",
            "addValueTags", jms);
    String methodBody =
            "try {\n" +
            "\t// Create document\n" +
            "\tDocument doc = DocumentBuilderFactory.newInstance()\n" +
            "\t\t      .newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n" +
            "\t// Fix tags in elements\n";
    // Add tag to elements
    for (ElementData element : fixElements) {
        methodBody += "\taddTagToElement(" + element.getName() + ", doc);\n";
    }
    // Add tag to element arrays
    methodBody += "\t// Fix tags in element arrays\n";
    for (ArrayData array : fixArrays) {
        methodBody += "\taddTagToElement(" + array.getArrayName() + ", doc);\n";
    }
    // Add tag to lists of simple type
    methodBody += "\t// Fix tags in lists of simple type\n";
    for (SimpleListData list : fixSimpleLists) {
        methodBody += "\taddTagToList(" + list.getListName() + ", doc, true);\n";
    }
    // Add tag to lists of restricted simple type
    methodBody += "\t// Fix tags in lists of restricted simple type\n";
    for (NonSimpleListData list : fixNonSimpleLists) {
        methodBody += "\taddTagToList(" + list.getListName() + ", doc, false);\n";
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
   * Private helper method to generate code that adds
   * value-tags to one single element in an XML document.
   *
   * @throws Exception Error during code generation
   */
  protected JMethod generateAddTagToElement() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "element"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "void", "addTagToElement", jms);

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
  protected JMethod generateAddTagToList() throws Exception {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "list"),
            JParameter.factory.create(JModifier.FINAL, "Document", "doc"),
            JParameter.factory.create(JModifier.FINAL, "boolean", "isSimple"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE, "void", "addTagToList", jms);

    String methodBody =
            "NodeList rootNodes  = doc.getElementsByTagName(list);\n" +
            "for (int i = 0; i < rootNodes.getLength(); i++) {\n" +
            "\tElement root        = (Element) rootNodes.item(i);\n" +
            "\tString[] content    = root.getTextContent().split(\" \");\n" +
            "\tif (! isSimple) {\n" +
            "\t\t// Insert values-tag\n" +
            "\t\tElement child = doc.createElement(\"values\");\n" +
            "\t\tchild.setTextContent(root.getTextContent());\n" +
            "\t\troot.removeChild(root.getFirstChild());\n" +
            "\t\troot.appendChild(child);\n" +
            "\t\troot = child;\n" +
            "\t}\n" +
            "\t// Each value has to get its own value-tag\n" +
            "\tfor (int j = 0; j < content.length; j++) {\n" +
            "\t\tElement child = doc.createElement(\"value\");\n" +
            "\t\tchild.appendChild(doc.createTextNode(content[j]));\n" +
            "\t\troot.appendChild(child);\n" +
            "\t}\n" +
            "\troot.removeChild(root.getFirstChild());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add values-tag and/or value-tags to the XML list."));

    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");

    return jm;
  }
}
