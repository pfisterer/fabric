/** 02.12.2011 12:32 */
package fabric.module.exi.java.lib.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * Abstract base class for XML libraries. Derived files
 * generate code to translate annotated Java object to XML
 * and vice versa. Each implementation of the interface may
 * operate a different XML library.
 *
 * @author reichart, seidel
 */
abstract public class XMLLibrary
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(XMLLibrary.class);
  
  /** List of required Java imports */
  protected ArrayList<String> requiredImports;

  /** Class for XML serialization and deserialization */
  protected JClass converterClass;

  /** Name of the target Java bean class */
  protected String beanClassName;

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
    /*****************************************************************
     * Generate code to remove value-tags
     *****************************************************************/
    
    // Generate code to remove value-tag from XML element
    JMethod removeTagFromElement = generateRemoveTagFromElement();
    if (null != removeTagFromElement)
    {
      this.converterClass.add(removeTagFromElement);
    }
    
    // Generate code to remove value-tag from XML list
    JMethod removeTagFromList = generateRemoveTagFromList();
    if (null != removeTagFromList)
    {
      this.converterClass.add(removeTagFromList);
    }
    
    // Generate code to remove value-tags
    JMethod removeValueTags = generateRemoveValueTags(fixElements, fixArrays, fixLists);
    if (null != removeValueTags)
    {
      this.converterClass.add(removeValueTags);
    }
    
    /*****************************************************************
     * Generate code to add value-tags
     *****************************************************************/
    
    // Generate code to add value-tag to XML element
    JMethod addTagToElement = generateAddTagToElement();
    if (null != addTagToElement)
    {
      this.converterClass.add(addTagToElement);
    }
    
    // Generate code to add value-tag to XML list
    JMethod addTagToList = generateAddTagToList();
    if (null != addTagToList)
    {
      this.converterClass.add(addTagToList);
    }
    
    // Generate code to add value-tags
    JMethod addValueTags = generateAddValueTags(fixElements, fixArrays, fixLists);
    if (null != addValueTags)
    {
      this.converterClass.add(addValueTags);
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
    // Trace elements where value-tag was fixed
    this.traceElementFixing(fixElements, fixArrays, fixLists);
    
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "String",
            "removeValueTags", jms, new String[] { "Exception" });
    
    String methodBody =
            "String result = null;\n\n" +
            "// Create document with custom DocumentBuilderFactory\n" +
            "Document document = DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", null)" +
            ".newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n";
    
    // Remove value-tag from XML elements
    if (!fixElements.isEmpty())
    {
      methodBody += "\n// Remove value-tag from XML elements\n";
      for (ElementData elementData: fixElements)
      {
        methodBody += String.format("%s.removeTagFromElement(\"%s\", document);\n",
                this.converterClass.getName(), elementData.getName());
      }
    }
    // Remove value-tag from XML arrays
    if (!fixArrays.isEmpty())
    {
      methodBody += "\n// Remove value-tag from XML arrays\n";
      for (ArrayData arrayData: fixArrays)
      {
        if (arrayData.isCustomTyped())
        {
          methodBody += String.format("%s.removeTagFromElement(\"%s\", document);\n",
                  this.converterClass.getName(), arrayData.getArrayName());
        }
      }
    }
    // Remove value-tag from XML lists
    if (!fixLists.isEmpty())
    {
      methodBody += "\n// Remove value-tag from XML lists\n";
      for (ListData listData: fixLists)
      {
        methodBody += String.format("%s.removeTagFromList(\"%s\", document, %b);\n",
                this.converterClass.getName(), listData.getListName(), listData.isCustomTyped());
      }
    }

    methodBody +=
            "\n" +
            "// Create objects for code write-out\n" +
            "Source source = new DOMSource(document);\n" +
            "StringWriter stringWriter = new StringWriter();\n" +
            "Result resultStream = new StreamResult(stringWriter);\n\n" +
            "// Setup transformer for pretty output\n" +
            "TransformerFactory transformerFactory = TransformerFactory.newInstance();\n" +
            "Transformer transformer = transformerFactory.newTransformer();\n" +
            "transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "transformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "transformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "transformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n" +
            "transformer.transform(source, resultStream);\n\n" +
            "// Return XML document as string\n" +
            "result = stringWriter.getBuffer().toString();\n" +
            "stringWriter.close();\n\n" +
            "return result;";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tags from XML document."));
    
    // Add required Java imports
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
  protected JMethod generateRemoveTagFromElement() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "elementName"),
            JParameter.factory.create(JModifier.FINAL, "Document", "document"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "removeTagFromElement", jms);
    
    String methodBody =
            "NodeList parentNodes = document.getElementsByTagName(elementName);\n\n" +
            "// Process all elements below parent node\n" +
            "for (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\tElement parent = (Element)parentNodes.item(i);\n\n" +
            "\t// Get all child nodes of parent that have a value-tag\n" +
            "\tNodeList children = parent.getElementsByTagName(\"value\");\n\n" +
            "\t// Remove value-tag from XML tree\n" +
            "\tString newContent = \"\";\n" +
            "\twhile (children.getLength() > 0) {\n" +
            "\t\tElement value = (Element)children.item(0);\n" +
            "\t\tnewContent += value.getTextContent() + \" \";\n" +
            "\t\tparent.removeChild(value);\n" +
            "\t}\n" +
            "\tparent.setTextContent(newContent.trim());\n" +
            "}";
    
    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Remove unnecessary value-tag from XML element."));
    
    // Add required Java imports
    addRequiredImport("org.w3c.dom.Document");
    addRequiredImport("org.w3c.dom.Element");
    addRequiredImport("org.w3c.dom.NodeList");
    
    return jm;
  }
  
  /**
   * Private helper method to generate code that removes unnecessary
   * value- and values-tags from a list in an XML document.
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
            "addValueTags", jms, new String[] { "Exception" });
    
    String methodBody =
            "String result = null;\n\n" +
            "// Create document with custom DocumentBuilderFactory\n" +
            "Document document = DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", null)." +
            "newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));\n";

    // Add value-tag to XML elements
    if (!fixElements.isEmpty())
    {
      methodBody += "\n// Add value-tag to XML elements\n";
      for (ElementData elementData: fixElements)
      {
        methodBody += String.format("%s.addTagToElement(\"%s\", document);\n",
                this.converterClass.getName(), elementData.getName());
      }
    }
    // Add value-tag to XML arrays
    if (!fixArrays.isEmpty())
    {
      methodBody += "\n// Add value-tag to XML arrays\n";
      for (ArrayData arrayData: fixArrays)
      {
        if (arrayData.isCustomTyped())
        {
          methodBody += String.format("%s.addTagToElement(\"%s\", document);\n",
                  this.converterClass.getName(), arrayData.getArrayName());
        }
      }
    }
    // Add value-tag to XML lists
    if (!fixLists.isEmpty())
    {
      methodBody += "\n// Add value-tag to XML lists\n";
      for (ListData listData: fixLists)
      {
        methodBody += String.format("%s.addTagToList(\"%s\", document, %b);\n",
                this.converterClass.getName(), listData.getListName(), listData.isCustomTyped());
      }
    }

    methodBody +=
            "\n" +
            "// Create objects for code write-out\n" +
            "Source source = new DOMSource(document);\n" +
            "StringWriter stringWriter = new StringWriter();\n" +
            "Result resultStream = new StreamResult(stringWriter);\n\n" +
            "// Setup transformer for pretty output\n" +
            "TransformerFactory transformerFactory = TransformerFactory.newInstance();\n" +
            "Transformer transformer = transformerFactory.newTransformer();\n" +
            "transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.METHOD, \"xml\");\n" +
            "transformer.setOutputProperty(OutputKeys.INDENT, \"yes\");\n" +
            "transformer.setOutputProperty(OutputKeys.STANDALONE, \"no\");\n" +
            "transformer.setOutputProperty(OutputKeys.ENCODING, \"UTF-8\");\n" +
            "transformer.setOutputProperty(\"{http://xml.apache.org/xslt}indent-amount\", \"2\");\n" +
            "transformer.transform(source, resultStream);\n\n" +
            "// Return XML document as string\n" +
            "result = stringWriter.getBuffer().toString();\n" +
            "stringWriter.close();\n\n" +
            "return result;";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add value-tags to XML document."));

    // Add required Java imports
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
  protected JMethod generateAddTagToElement() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "elementName"),
            JParameter.factory.create(JModifier.FINAL, "Document", "document"));
    JMethod jm = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "addTagToElement", jms);

    String methodBody =
            "NodeList parentNodes = document.getElementsByTagName(elementName);\n\n" +
            "// Process all elements below parent node\n" +
            "for (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\tElement parent = (Element)parentNodes.item(i);\n" +
            "\tElement child = document.createElement(\"value\");\n\n" +
            "\tchild.appendChild(parent.getFirstChild().cloneNode(true));\n" +
            "\tparent.replaceChild(child, parent.getFirstChild());\n" +
            "}";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Add value-tag to XML element."));
    
    // Add required Java imports
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
  abstract protected JMethod generateAddTagToList() throws Exception;

  /**
   * Private helper method to print some debug information. This
   * way it is easier to trace the value-tag fixing process.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   */
  private void traceElementFixing(final ArrayList<ElementData> fixElements,
                                  final ArrayList<ArrayData> fixArrays,
                                  final ArrayList<ListData> fixLists)
  {
    // Output fixed XML elements
    for (ElementData elementData: fixElements)
    {
      LOGGER.debug(String.format("Fixed value-tags for element: %s", elementData.getName()));
    }

    // Output fixed XML arrays
    for (ArrayData arrayData: fixArrays)
    {
      LOGGER.debug(String.format("Fixing value-tags for array: %s, %s, %s, %s, %b",
              arrayData.getArrayName(), arrayData.getArrayType(), arrayData.getItemName(),
              arrayData.getItemType(), arrayData.isCustomTyped()));
    }

    // Output fixed XML lists
    for (ListData listData: fixLists)
    {
      LOGGER.debug(String.format("Fixed value-tag for list: %s, %s, %s, %b",
              listData.getListName(), listData.getListType(),
              listData.getItemType(), listData.isCustomTyped()));
    }
  }
}
