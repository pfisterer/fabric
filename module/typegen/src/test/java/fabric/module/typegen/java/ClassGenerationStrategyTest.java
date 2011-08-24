package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
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
    
    // Output JClass for debug reasons
    System.out.println(jClassObject.toString());

    // Check getRequiredImports()
    ArrayList<String> imports = javaStrategy.getRequiredDependencies();
    assertTrue("List of required imports must contain two items.", imports.size() == 2);
    assertTrue("Required imports must contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));
    assertTrue("Required imports must contain 'org.simpleframework.xml.Attribute'.", imports.contains("org.simpleframework.xml.Attribute"));
  }
}
