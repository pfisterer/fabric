package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import fabric.module.typegen.exceptions.UnsupportedXMLFrameworkException;

/**
 * Unit test for Java AnnotationMapper class.
 *
 * @author seidel
 */
public class AnnotationMapperTest
{
  /**
   * Test creation of AnnotationMapper object.
   */
  @Test(timeout = 1000)
  public void testMapperCreation() throws Exception
  {
    // Check parameterless constructor
    AnnotationMapper mapper = new AnnotationMapper();
    assertNotNull("Mapper object must not be null.", mapper);
    assertEquals("Mapper must use 'Simple' XML library as default.", "Simple", mapper.getUsedFramework());

    // Check parameterized constructor for Simple library
    mapper = new AnnotationMapper("Simple");
    assertEquals("Mapper must use 'Simple' XML library.", "Simple", mapper.getUsedFramework());

    // Check parameterized constructor for XStream library
    mapper = new AnnotationMapper("XStream");
    assertEquals("Mapper must use 'XStream' XML library.", "XStream", mapper.getUsedFramework());

    // Check parameterized constructor for JAXB library
    mapper = new AnnotationMapper("JAXB");
    assertEquals("Mapper must use 'JAXB' XML library.", "JAXB", mapper.getUsedFramework());

    // Check parameterized constructor with unsupported library
    Exception exception = null;
    try
    {
      mapper = new AnnotationMapper("SomeUnknownMapper");
    }
    catch (Exception e)
    {
      exception = e;
    }
    assertTrue("Mapper must throw 'UnsupportedXMLFrameworkException'.", exception instanceof UnsupportedXMLFrameworkException);
  }

  /**
   * Test import mechanism, which automatically returns only
   * those imports that are currently required.
   */
  @Test(timeout = 1000)
  public void testImportCreation() throws Exception
  {
    AnnotationMapper mapper = new AnnotationMapper();
    ArrayList<String> imports = mapper.getUsedImports();

    // Check general import return
    mapper.getRootAnnotations(null);
    assertTrue("One import must be required.", imports.size() == 1);
    assertTrue("Imports must contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));

    // Check requirement-based import return
    mapper.getAttributeAnnotations(null);
    assertTrue("Two imports must be required.", imports.size() == 2);
    assertTrue("Imports must still contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));
    assertTrue("Imports must also contain 'org.simpleframework.xml.Attribute'.", imports.contains("org.simpleframework.xml.Attribute"));

    // Check that no imports are added twice
    mapper.getAttributeAnnotations(null);
    assertTrue("Still only two imports must be required.", imports.size() == 2);
  }

  /**
   * Test lookup of framework-specific Java annotations.
   */
  @Test(timeout = 1000)
  public void testAnnotationLookup() throws Exception
  {
    AnnotationMapper mapper = new AnnotationMapper();
    assertEquals("Returned annotation must be 'Attribute'.", "Attribute", mapper.getAttributeAnnotations(null)[0]);
  }
}
