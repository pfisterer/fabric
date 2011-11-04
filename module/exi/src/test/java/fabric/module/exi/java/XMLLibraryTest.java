package fabric.module.exi.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

import fabric.module.exi.java.lib.xml.XMLLibrary;
import fabric.module.exi.java.lib.xml.XMLLibraryFactory;

/**
 * Unit test for XMLLibrary classes.
 *
 * @author seidel
 */
public class XMLLibraryTest
{
  /** Name of the Java bean class */
  private static final String BEAN_CLASS_NAME = "Car";

  /**
   * Test creation of XMLLibrary object.
   */
  @Test(timeout = 1000)
  public void testCreation() throws Exception
  {
    // Check constructor
    XMLLibrary xmlLibrary = XMLLibraryFactory.getInstance().createXMLLibrary("fabric.module.exi.java.lib.xml.Simple", BEAN_CLASS_NAME);
    assertNotNull("XMLLibrary object must not be null.", xmlLibrary);
  }

  /**
   * Test XMLLibrary implementation for the Simple library.
   */
  @Test(timeout = 1000)
  public void testSimpleLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.Simple");
  }

  /**
   * Test XMLLibrary implementation for the XStream library.
   */
  @Test(timeout = 1000)
  public void testXStreamLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.XStream");
  }

  /**
   * Test XMLLibrary implementation for the JAXB library.
   */
  @Test(timeout = 1000)
  public void testJAXBLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.JAXB");
  }

  /**
   * Private helper method to check XMLLibrary implementations.
   *
   * @param xmlLibraryClassName Fully qualified class name of
   * the XMLLibrary implementation
   */
  private void testXMLLibrary(final String xmlLibraryClassName) throws Exception
  {
    XMLLibrary xmlLibrary = XMLLibraryFactory.getInstance().createXMLLibrary(xmlLibraryClassName, BEAN_CLASS_NAME);

    // Create empty lists for value-tag fixing
    ArrayList<ElementData> fixElements = new ArrayList<ElementData>();
    ArrayList<ArrayData> fixArrays = new ArrayList<ArrayData>();
    ArrayList<SimpleListData> fixSimpleLists = new ArrayList<SimpleListData>();
    ArrayList<NonSimpleListData> fixNonSimpleLists = new ArrayList<NonSimpleListData>();

    // Check library initialization
    JClass classObject = xmlLibrary.init(fixElements, fixArrays, fixSimpleLists, fixNonSimpleLists);
    assertNotNull("Returned JClass object must not be null.", classObject);
    assertEquals(String.format("JClass name must be '%sConverter'.", BEAN_CLASS_NAME), BEAN_CLASS_NAME + "Converter", classObject.getName());

    // Check existence of method instanceToXML()
    TestHelper.checkMethodExistence(classObject, "instanceToXML", "String", BEAN_CLASS_NAME);

    // Check existence of method xmlToInstance()
    TestHelper.checkMethodExistence(classObject, "xmlToInstance", BEAN_CLASS_NAME, "String");

    // Check existence of method removeValueTags()
    TestHelper.checkMethodExistence(classObject, JModifier.PRIVATE | JModifier.STATIC, "removeValueTags", "String", "String", null);

    // Check existence of method addValueTags()
    TestHelper.checkMethodExistence(classObject, JModifier.PRIVATE | JModifier.STATIC, "addValueTags", "String", "String", null);
  }
}
