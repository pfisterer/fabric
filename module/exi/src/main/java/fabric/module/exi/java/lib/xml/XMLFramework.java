package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;

/**
 * Abstract base class for XML frameworks. Derived classes
 * generate code, to translate annotated Java object to XML
 * and vice versa. Each implementation of the interface may
 * operate a different XML framework.
 *
 * @author seidel
 */
abstract public class XMLFramework
{
  /**
   * Method that creates code to convert an annotated Java
   * object to a plain XML document.
   *
   * @return String with code for Java to XML conversion
   */
  abstract public String generateJavaToXMLCode();

  /**
   * Method that creates code to convert an XML document
   * to a class instance of the corresponding Java bean.
   *
   * @return String with code for XML to instance conversion
   */
  abstract public String generateXMLToInstanceCode();

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
   * @param affectedElements List with the names of all affected elements
   *
   * @return String with code that fixes the aforementioned problem
   */
  public String generateFixValueCode(final ArrayList<String> affectedElements)
  {
    // TODO: Implement method
    return null;
  }
}
