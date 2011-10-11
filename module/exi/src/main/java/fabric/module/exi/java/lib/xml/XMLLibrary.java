/** 11.10.2011 02:14 */
package fabric.module.exi.java.lib.xml;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JModifier;

/**
 * Abstract base class for XML libraries. Derived classes
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
   * @return List of requires imports
   */
  public ArrayList<String> getRequiredImports()
  {
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
  public JClass init(ArrayList<String> fixElements) throws Exception
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
    // TODO: Generate method 'String removeValueTags(final String xmlDocument, final ArrayList<String> affectedElements)'
    
    // TODO: Generate method 'String addValueTags(final String xmlDocument, final ArrayList<String> affectedElements)'

    // TODO: Add both methods to 'this.converterClass'
  }
}
