package fabric.module.typegen.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.lang.reflect.Field;

import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.MapperFactory;

/**
 * Unit test for Mapper classes.
 *
 * @author seidel
 */
public class MapperTest
{
  /** Name of Java mapper class */
  private static final String JAVA_MAPPER_CLASS = "fabric.module.typegen.java.JavaMapper";

  /** Name of C++ mapper class */
  private static final String CPP_MAPPER_CLASS = "fabric.module.typegen.cpp.CppMapper";

  /**
   * Test MapperFactory and Mapper instantiation.
   */
  @Test(timeout = 1000)
  public void testFactory() throws Exception
  {
    // Check getInstance()
    MapperFactory factory = MapperFactory.getInstance();
    assertNotNull("Factory object must not be null.", factory);

    // Check createMapper()
    Mapper mapper = factory.createMapper(JAVA_MAPPER_CLASS);
    assertNotNull("Mapper object must not be null.", mapper);
  }

  /**
   * Test JavaMapper implementation.
   */
  @Test(timeout = 1000)
  public void testJavaMapper() throws Exception
  {
    this.testMapper(JAVA_MAPPER_CLASS);
  }

  /**
   * Test CppMapper implementation.
   */
  @Test(timeout = 1000)
  public void testCppMapper() throws Exception
  {
    this.testMapper(CPP_MAPPER_CLASS);
  }

  /**
   * Helper method for Mapper implementation testing.
   *
   * @param mapperClassName Class name of Mapper implementation
   */
  private void testMapper(final String mapperClassName) throws Exception
  {
    // Create language-specific mapper
    Mapper mapper = MapperFactory.getInstance().createMapper(mapperClassName);
    assertNotNull("Mapper object must not be null.", mapper);

    // Use reflection API to get field with mapping
    Field mapping = Mapper.class.getDeclaredField("types");
    mapping.setAccessible(true); // Make protected field accessible
    HashMap<String, String> types = (HashMap<String, String>)mapping.get(mapper);

    // Check all types
    for (String key: types.keySet())
    {
      assertFalse(String.format("Mapping for key '%s' must not be empty.", key), ("").equals(types.get(key)));
      assertEquals(String.format("Return value from lookup must match mapping for '%s'.", key), types.get(key), mapper.lookup(key));
    }
  }
}
