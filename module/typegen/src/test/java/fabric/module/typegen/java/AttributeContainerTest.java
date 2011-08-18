package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.base.ClassGenerationStrategy;

/**
 * @author seidel
 */
public class AttributeContainerTest
{
  // Implement unit test
  private static final String CONTAINER_NAME = "Car";
  private static final String NEW_CONTAINER_NAME = "NewCar";

  @Test(timeout = 1000)
  public void testAttributeContainer()
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    // Check getDefaultInstance()
    assertNotNull("DefaultInstance must not be null.", AttributeContainer.getDefaultInstance());

    // Check parameterless constructor
    assertNotNull("Attribute container from constructor must not be null.", AttributeContainer.newBuilder());

    // Check prototyped constructor
    assertNotNull("Attribute container created from prototype must not be null.", AttributeContainer.newBuilder(carContainer));

    // Check toBuilder()
    assertNotNull("Builder created from container must not be null.", carContainer.toBuilder());
    
    // Check getName() from container
    assertEquals("Container name must match initial value.", CONTAINER_NAME, carContainer.getName());
  }

  @Test(timeout = 1000)
  public void testBuilder()
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    // Check clear()
    AttributeContainer carContainerClear = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    assertEquals("Container name must match new value.", NEW_CONTAINER_NAME, carContainerClear.getName());
    assertEquals("Cleared builder name must match initial value.", AttributeContainer.getDefaultContainerName(), AttributeContainer.newBuilder(carContainerClear).clear().build().getName());

    // Check clone()
    AttributeContainer.Builder clonedBuilder = carContainer.toBuilder().clone();
    assertNotNull("Cloned builder must not be null.", clonedBuilder);
    assertEquals("Clone name must match initial value.", CONTAINER_NAME, clonedBuilder.build().getName());

    // Check build()
    assertNotNull("Built attribute container must not be null.", clonedBuilder.build());

    // Check mergeWith()
    AttributeContainer carContainerOtherName = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    AttributeContainer carContainerMerged = AttributeContainer.newBuilder(carContainer).mergeWith(carContainerOtherName).build();
    assertNotNull("Merged builder must not be null.", carContainerMerged);
    assertEquals("Merged container name must match new value.", NEW_CONTAINER_NAME, carContainerMerged.toBuilder().getName());

    // Check getName()
    assertEquals("Builder name must match initial value.", CONTAINER_NAME, carContainer.toBuilder().getName());

    // Check setName()
    AttributeContainer carContainerNewName = AttributeContainer.newBuilder(carContainer).setName(NEW_CONTAINER_NAME).build();
    assertEquals("Builder name must match new value.", NEW_CONTAINER_NAME, carContainerNewName.getName());
  }

  @Test(timeout = 1000)
  public void testBuilderAttributes()
  {
    // Create builder
    AttributeContainer.Builder testBuilder = AttributeContainer.newBuilder();

    // Check addElement() with two and three parameters
    testBuilder = testBuilder.clear().addElement("int", "foo");
    assertTrue("Container must contain one attribute.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'foo' must be instance of AttributeSimple.", testBuilder.build().getMembers().get("foo") instanceof AttributeContainer.Element);

    testBuilder = testBuilder.clear().addElement("int", "bar", "5");
    assertTrue("Container must contain one attribute.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'bar' must be instance of AttributeSimple.", testBuilder.build().getMembers().get("bar") instanceof AttributeContainer.Element);

    // Check duplicate addElement()
    testBuilder = testBuilder.clear().addElement("int", "foo").addElement("String", "foo");
    assertTrue("Container must not allow duplicate attribute names.", testBuilder.build().getMembers().size() == 1);

    // Check unbounded addElementArray()
    testBuilder = testBuilder.clear().addElementArray("int", "numbers");
    assertTrue("Container must contain one attribute array.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'numbers' must be instance of AttributeArray.", testBuilder.build().getMembers().get("numbers") instanceof AttributeContainer.ElementArray);
    assertTrue("Attribute array must be unbounded.", ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("numbers"))).size == Integer.MAX_VALUE);

    // Check bounded addElementArray()
    testBuilder = testBuilder.clear().addElementArray("String", "names", 5);
    assertTrue("Container must contain one attribute array.", testBuilder.build().getMembers().size() == 1);
    assertTrue("Element 'names' must be instance of AttributeArray.", testBuilder.build().getMembers().get("names") instanceof AttributeContainer.ElementArray);
    assertTrue("Attribute array must be bounded to a size of 5.", ((AttributeContainer.ElementArray)(testBuilder.build().getMembers().get("names"))).size == 5);

    // Check deleteMember()
    testBuilder = testBuilder.clear().addElement("int", "foo").addElement("boolean", "bar");
    assertNotNull("Container must contain an attribute named 'foo'.", testBuilder.build().getMembers().get("foo"));
    assertNotNull("Container must contain an attribute named 'bar'.", testBuilder.build().getMembers().get("bar"));
    testBuilder = testBuilder.deleteMember("foo");
    assertNull("Container must not contain an attribute named 'foo' anymore.", testBuilder.build().getMembers().get("foo"));
    assertNotNull("Container must still contain an attribute named 'bar'.", testBuilder.build().getMembers().get("bar"));

    // Check duplicate deleteMember()
    testBuilder = testBuilder.clear().addElement("int", "foobar");
    assertNotNull("Container must contain an attribute named 'foobar'.", testBuilder.build().getMembers().get("foobar"));
    testBuilder = testBuilder.deleteMember("foobar");
    assertNull("Container must not contain an attribute named 'foobar' anymore.", testBuilder.build().getMembers().get("foobar"));
    testBuilder = testBuilder.deleteMember("foobar");
    assertNull("Container must still not contain an attribute named 'foobar'.", testBuilder.build().getMembers().get("foobar"));
  }

  @Test(timeout = 1000)
  public void testClassCreation() throws Exception
  {
    // Create attribute container
    AttributeContainer carContainer = AttributeContainerTest.createAttributeContainer();

    JSourceFileImpl jsf = new JSourceFileImpl("test.de", "testFile.java");
    ClassGenerationStrategy strategy = new JavaClassGenerationStrategy();
    jsf.add((JClass)carContainer.asClassObject(strategy));
    System.out.println(jsf.toString());
    assertFalse("String must not be empty.", jsf.toString().equals(""));

    // TODO: Solve problem with exception here
    //assertTrue("Must return string.", carContainer.asJClass().toString() instanceof java.lang.String);
    //assertFalse("String must not be empty.", carContainer.asJClass().toString().equals(""));
  }

  /**
   * Helper method for test-container creation.
   *
   * @return AttributeContainer object
   */
  private static AttributeContainer createAttributeContainer()
  {
    AttributeContainer result = AttributeContainer.newBuilder()
                                  .setName(CONTAINER_NAME)
                                  .addAttribute("String", "manufacturer", "Audi")
                                  .addAttribute("String", "model", "TT")
                                  .addElement("String", "color", "red")
                                  .addElement("int", "maxSpeed", "220")
                                  .addElementArray("TrunkItem", "trunkItems")
                                  .addElementArray("String", "passengers", 2)
                                  .build();

    return result;
  }
}
