package fabric.module.exi.java;

import org.junit.Test;
import org.junit.Ignore; // TODO: Remove import when unused
import static org.junit.Assert.*;

import de.uniluebeck.sourcegen.java.JClass;

import fabric.module.exi.java.lib.exi.EXILibrary;
import fabric.module.exi.java.lib.exi.EXILibraryFactory;

/**
 * Unit test for EXILibrary classes.
 *
 * @author seidel
 */
public class EXILibraryTest
{
  /**
   * Test creation of EXILibrary object.
   */
  @Test(timeout = 1000)
  public void testCreation() throws Exception
  {
    // Check constructor
    EXILibrary exiLibrary = EXILibraryFactory.getInstance().createEXILibrary("fabric.module.exi.java.lib.exi.EXIficient", null);
    assertNotNull("EXILibrary object must not be null.", exiLibrary);
  }

  /**
   * Test EXILibrary implementation for the EXIficient library.
   */
  @Test(timeout = 1000)
  public void testEXIficientLibrary() throws Exception
  {
    this.testEXILibrary("fabric.module.exi.java.lib.exi.EXIficient");
  }

  /**
   * Test EXILibrary implementation for the OpenEXI library.
   */
  @Test(timeout = 1000)
  @Ignore // TODO: Remove line, when OpenEXI implementation is ready
  public void testOpenEXILibrary() throws Exception
  {
    this.testEXILibrary("fabric.module.exi.java.lib.exi.OpenEXI");
  }

  /**
   * Private helper method to check EXILibrary implementations.
   *
   * @param exiLibraryClassName Fully qualified class name of
   * the EXILibrary implementation
   */
  private void testEXILibrary(final String exiLibraryClassName) throws Exception
  {
    EXILibrary exiLibrary = EXILibraryFactory.getInstance().createEXILibrary(exiLibraryClassName, null);

    // Check library initialization
    JClass classObject = exiLibrary.init();
    assertNotNull("Returned JClass object must not be null.", classObject);
    assertEquals("JClass name must be 'EXIConverter'.", "EXIConverter", classObject.getName());

    // Check existence of method serialize()
    TestHelper.checkMethodExistence(classObject, "serialize", "byte[]", "String");

    // Check existence of method deserialize()
    TestHelper.checkMethodExistence(classObject, "deserialize", "String", "byte[]");
  }
}
