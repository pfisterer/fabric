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
