package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;

/**
 * Unit test for ClassGenerationStrategy interface and classes.
 *
 * @author seidel
 */
public class ClassGenerationStrategyTest
{
  /**
   * Unit test for JavaClassGenerationStrategy.
   */
  @Test(timeout = 1000)
  public void testJavaClassGenerationStrategy() throws Exception
  {
    // Check parameterless constructor
    JavaClassGenerationStrategy javaStrategy = new JavaClassGenerationStrategy();
    assertNotNull("JavaClassGenerationStrategy object must not be null.", javaStrategy);

    // Check parameterized constructor
    javaStrategy = new JavaClassGenerationStrategy(new AnnotationMapper());
    assertNotNull("JavaClassGenerationStrategy object with AnnotationMapper must not be null.", javaStrategy);

    // Check generateClassObject()
    JClass jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build());
    assertNotNull("JClass object must not be null.", jClassObject);
    assertFalse("JClass content must not be empty string.", ("").equals(jClassObject.toString()));
    System.out.println(jClassObject.toString());

    // Check generateClassObject() with custom modifiers
    int modifiers = JModifier.PUBLIC | JModifier.STATIC;
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), modifiers);
    assertNotNull("JClass object with custom modifiers must not be null.", jClassObject);
    assertTrue("Text representation of JClass object must contain 'public static'.", jClassObject.toString().contains("public static"));
    System.out.println(jClassObject.toString());

    // Check generateClassObject with extends-directive
    String parent = "ExtendedClassName";
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), parent);
    assertNotNull("JClass object with extends-directive must not be null.", jClassObject);
    assertTrue(String.format("Text representation of JClass object must contain 'extends %s'.", parent),
            jClassObject.toString().contains(String.format("extends %s", parent)));
    System.out.println(jClassObject.toString());

    // Check generateClassObject with custom modifiers and extends-directive
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), modifiers, parent);
    assertNotNull("JClass object with custom modifiers and extends-directive must not be null.", jClassObject);
    assertTrue("Text representation of JClass object must contain 'public static'.", jClassObject.toString().contains("public static"));
    assertTrue(String.format("Text representation of JClass object must contain 'extends %s'.", parent),
            jClassObject.toString().contains(String.format("extends %s", parent)));
    System.out.println(jClassObject.toString());

    // Check getRequiredImports()
    ArrayList<String> imports = javaStrategy.getRequiredDependencies();
    assertTrue("List of required imports must contain two items.", imports.size() == 2);
    assertTrue("Required imports must contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));
    assertTrue("Required imports must contain 'org.simpleframework.xml.Attribute'.", imports.contains("org.simpleframework.xml.Attribute"));
  }
}
