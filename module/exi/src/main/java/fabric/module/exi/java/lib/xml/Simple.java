/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/** 02.12.2011 12:35 */
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
            "NodeList parentNodes = document.getElementsByTagName(listName);\n\n" +
            "// Process all elements below parent node\n" +
            "for (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\tElement parent = (Element)parentNodes.item(i);\n\n" +
            "\t// Get all child nodes of parent that have a values-tag\n" +
            "\tNodeList children = parent.getElementsByTagName(\"values\");\n\n" +
            "\tif (children.getLength() == 1) {\n" +
            "\t\tElement valueList = (Element)children.item(0);\n"+
            "\t\twhile (valueList.hasChildNodes()) {\n" +
            "\t\t\tparent.appendChild(valueList.getFirstChild().cloneNode(true));\n" +
            "\t\t\tvalueList.removeChild(valueList.getFirstChild());\n" +
            "\t\t}\n" +
            "\t\tparent.removeChild(valueList);\n" +
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
            "NodeList parentNodes = document.getElementsByTagName(listName);\n\n" +
            "// Process all elements below parent node\n" +
            "for (int i = 0; i < parentNodes.getLength(); ++i) {\n" +
            "\tElement parent = (Element)parentNodes.item(i);\n" +
            "\tString[] content = parent.getTextContent().split(\" \");\n\n" +
            "\tif (isCustomTyped) {\n" +
            "\t\t// Insert new values-tag\n" +
            "\t\tElement child = document.createElement(\"values\");\n" +
            "\t\tchild.setTextContent(parent.getTextContent());\n" +
            "\t\tparent.removeChild(parent.getFirstChild());\n" +
            "\t\tparent.appendChild(child);\n" +
            "\t\tparent = child;\n" +
            "\t}\n\n" +
            "\t// Each list item has to get its own value-tag\n" +
            "\tfor (int j = 0; j < content.length; ++j) {\n" +
            "\t\tElement child = document.createElement(\"value\");\n" +
            "\t\tchild.appendChild(document.createTextNode(content[j]));\n" +
            "\t\tparent.appendChild(child);\n" +
            "\t}\n" +
            "\tparent.removeChild(parent.getFirstChild());\n" +
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
