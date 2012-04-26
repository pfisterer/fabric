/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
