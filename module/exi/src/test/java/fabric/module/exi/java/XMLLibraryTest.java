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
package fabric.module.exi.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;

import fabric.module.exi.java.lib.xml.XMLLibrary;
import fabric.module.exi.java.lib.xml.XMLLibraryFactory;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * Unit test for XMLLibrary classes.
 *
 * @author seidel
 */
public class XMLLibraryTest
{
  /** Name of the Java bean class */
  private static final String BEAN_CLASS_NAME = "Car";

  /**
   * Test creation of XMLLibrary object.
   */
  @Test(timeout = 1000)
  public void testCreation() throws Exception
  {
    // Check constructor
    XMLLibrary xmlLibrary = XMLLibraryFactory.getInstance().createXMLLibrary("fabric.module.exi.java.lib.xml.Simple", BEAN_CLASS_NAME);
    assertNotNull("XMLLibrary object must not be null.", xmlLibrary);
  }

  /**
   * Test XMLLibrary implementation for the Simple library.
   */
  @Test(timeout = 1000)
  public void testSimpleLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.Simple");
  }

  /**
   * Test XMLLibrary implementation for the XStream library.
   */
  @Test(timeout = 1000)
  public void testXStreamLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.XStream");
  }

  /**
   * Test XMLLibrary implementation for the JAXB library.
   */
  @Test(timeout = 1000)
  public void testJAXBLibrary() throws Exception
  {
    this.testXMLLibrary("fabric.module.exi.java.lib.xml.JAXB");
  }

  /**
   * Private helper method to check XMLLibrary implementations.
   *
   * @param xmlLibraryClassName Fully qualified class name of
   * the XMLLibrary implementation
   */
  private void testXMLLibrary(final String xmlLibraryClassName) throws Exception
  {
    XMLLibrary xmlLibrary = XMLLibraryFactory.getInstance().createXMLLibrary(xmlLibraryClassName, BEAN_CLASS_NAME);

    // Create empty lists for value-tag fixing
    ArrayList<ElementData> fixElements = new ArrayList<ElementData>();
    ArrayList<ArrayData> fixArrays = new ArrayList<ArrayData>();
    ArrayList<ListData> fixLists = new ArrayList<ListData>();

    // Check library initialization
    JClass classObject = xmlLibrary.init(fixElements, fixArrays, fixLists);
    assertNotNull("Returned JClass object must not be null.", classObject);
    assertEquals(String.format("JClass name must be '%sConverter'.", BEAN_CLASS_NAME), BEAN_CLASS_NAME + "Converter", classObject.getName());

    // Check existence of method instanceToXML()
    TestHelper.checkMethodExistence(classObject, "instanceToXML", "String", BEAN_CLASS_NAME);

    // Check existence of method xmlToInstance()
    TestHelper.checkMethodExistence(classObject, "xmlToInstance", BEAN_CLASS_NAME, "String");

    // Check existence of method removeValueTags()
    TestHelper.checkMethodExistence(classObject, JModifier.PRIVATE | JModifier.STATIC, "removeValueTags", "String", "String", null);

    // Check existence of method addValueTags()
    TestHelper.checkMethodExistence(classObject, JModifier.PRIVATE | JModifier.STATIC, "addValueTags", "String", "String", null);
  }
}
