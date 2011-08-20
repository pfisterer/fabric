package fabric.module.typegen.java;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author seidel
 */
public class AnnotationMapperTest
{
  @Test(timeout = 1000)
  public void testMapperCreation()
  {
    AnnotationMapper mapper = new AnnotationMapper();
    assertNotNull("Mapper object must not be null.", mapper);
    assertEquals("Mapper must use 'Simple' XML library as default.", "Simple", mapper.getUsedFramework());

    mapper = new AnnotationMapper("Simple");
    assertEquals("Mapper must use 'Simple' XML library.", "Simple", mapper.getUsedFramework());

    mapper = new AnnotationMapper("XStream");
    assertEquals("Mapper must use 'XStream' XML library.", "XStream", mapper.getUsedFramework());

    mapper = new AnnotationMapper("JAXB");
    assertEquals("Mapper must use 'JAXB' XML library.", "JAXB", mapper.getUsedFramework());
  }

  @Test(timeout = 1000)
  public void testImportCreation()
  {
    AnnotationMapper mapper = new AnnotationMapper();
    ArrayList<String> imports = mapper.getUsedImports();

    mapper.getAnnotation("root");
    assertTrue("One import must be required.", imports.size() == 1);
    assertTrue("Imports must contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));

    mapper.getAnnotation("attribute");
    assertTrue("Two imports must be required.", imports.size() == 2);
    assertTrue("Imports must still contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));
    assertTrue("Imports must also contain 'org.simpleframework.xml.Attribute'.", imports.contains("org.simpleframework.xml.Attribute"));

    mapper.getAnnotation("attribute");
    assertTrue("Still only two imports must be required.", imports.size() == 2);
  }

  @Test(timeout = 1000)
  public void testAnnotationLookup()
  {
    AnnotationMapper mapper = new AnnotationMapper();
    assertEquals("Returned annotation must be 'Attribute'.", "Attribute", mapper.getAnnotation("attribute"));
  }
}
