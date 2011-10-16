package fabric.module.exi.java;

import org.junit.Test;
import org.junit.Ignore; // TODO: Remove import when unused
import static org.junit.Assert.*;

import java.util.List;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

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
  @Ignore // TODO: Remove line, when XSteam implementation is ready
  public void testXStreamLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.XStream");
  }

  /**
   * Test XMLLibrary implementation for the JAXB library.
   */
  @Test(timeout = 1000)
  @Ignore // TODO: Remove line, when JAXB implementation is ready
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

    // Check library initialization
    JClass classObject = xmlLibrary.init(null);
    assertNotNull("Returned JClass object must not be null.", classObject);
    assertEquals(String.format("Bean class name must be '%sConverter'.", BEAN_CLASS_NAME), BEAN_CLASS_NAME + "Converter", classObject.getName());

    // Check existence of method instanceToXML()
    this.checkMethodExistence(classObject, "instanceToXML", "String", BEAN_CLASS_NAME);

    // Check existence of method xmlToInstance()
    this.checkMethodExistence(classObject, "xmlToInstance", BEAN_CLASS_NAME, "String");
  }

  /**
   * Private helper method to check the existence of a method with
   * the given properties.
   *
   * @param classObject JClass object for checking
   * @param methodName Name of method that the class must contain
   * @param returnValueType Expected type of return value
   * @param argumentType Expected type of method argument
   */
  private void checkMethodExistence(final JClass classObject, final String methodName, final String returnValueType, final String argumentType)
  {
    // Check if method exists at all
    List<JMethod> methods = classObject.getJMethodsByName(methodName);
    assertNotNull("List of methods must not be null.", methods);
    assertTrue(String.format("Class must contain at least one method named '%s'.", methodName), methods.size() >= 1);

    // Check type of method parameter
    JMethod method = methods.get(0);
    JMethodSignature signature = method.getSignature();
    assertNotNull("Method signature must not be null.", signature);
    JParameter parameter = signature.getParameters().get(0);
    assertNotNull("Method parameter must not be null.", parameter);
    assertTrue(String.format("Argument must have type '%s'.", argumentType), parameter.typeEquals(argumentType));

    // Check modifier, return value and throws-statement
    assertEquals("Modifiers of method must be 'public' and 'static'.", JModifier.PUBLIC | JModifier.STATIC, method.getModifiers());
    assertEquals(String.format("Type of return value must be '%s'.", returnValueType), returnValueType, method.getReturnType());
    assertTrue("Method must throw exception of type 'Exception'.", method.containsException("Exception"));
  }
}
