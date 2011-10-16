package fabric.codegeneration;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Unit test for the JClassImpl class.
 *
 * @author seidel
 */
public class JClassImplTest
{
  /**
   * Test getJMethodsByName() function.
   */
  @Test(timeout = 1000)
  public void testGetJMethodsByName() throws Exception
  {
    // Create JClass object
    JClass classObject = JClass.factory.create("Test");

    // Create method signatures
    JMethodSignature jmsInt = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, "int", "foo"));
    JMethodSignature jmsString = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, "String", "foo"));

    // Create two methods with different signature
    classObject.add(JMethod.factory.create(JModifier.PUBLIC, "void", "testMethod", jmsInt));
    classObject.add(JMethod.factory.create(JModifier.PUBLIC, "void", "testMethod", jmsString));

    // Get all methods and count them
    List<JMethod> methods = classObject.getJMethodsByName("testMethod");
    assertNotNull("List of methods must not be null.", methods);
    assertTrue("Class object must contain two methods called 'testMethod'.", methods.size() == 2);
  }
}
