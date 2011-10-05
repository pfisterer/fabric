package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;

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
  protected String BEAN_NAME;
  
  protected JClass converterClass;
  
  /**
   * Use parameterized constructor instead.
   */
  private XMLFramework()
  {
    // Empty implementation
  }
  
  public XMLFramework(final String beanClassName) throws Exception
  {
    this.BEAN_NAME = beanClassName;
    this.converterClass = JClass.factory.create(JModifier.PUBLIC, "XMLConverter");    
  }
  
  // TODO: Implement method and add documentation
  public JClass init(ArrayList<String> fixableElements) throws Exception
  {
    this.generateFixValueCode(fixableElements); // TODO Pass field to method
    
    this.generateJavaToXMLCode();
    
    this.generateXMLToInstanceCode();
        
    return this.converterClass;
  }
  
  /**
   * Method that creates code to convert an annotated Java
   * object to a plain XML document.
   *
   * @return String with code for Java to XML conversion
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateJavaToXMLCode() throws Exception;

  /**
   * Method that creates code to convert an XML document
   * to a class instance of the corresponding Java bean.
   *
   * @return String with code for XML to instance conversion
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
   * @param affectedElements List with the names of all affected elements
   *
   * @return String with code that fixes the aforementioned problem
   * 
   * @throws Exception Error during code generation
   */
  public void generateFixValueCode(final ArrayList<String> affectedElements) throws Exception
  {
    // TODO: Implement method
  }
}
