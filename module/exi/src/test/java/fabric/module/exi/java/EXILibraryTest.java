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

import de.uniluebeck.sourcegen.java.JClass;

import fabric.module.exi.java.lib.exi.EXILibrary;
import fabric.module.exi.java.lib.exi.EXILibraryFactory;

/**
 * Unit test for EXILibrary classes.
 *
 * @author seidel
 */
public class EXILibraryTest
{
  /**
   * Test creation of EXILibrary object.
   */
  @Test(timeout = 1000)
  public void testCreation() throws Exception
  {
    // Check constructor
    EXILibrary exiLibrary = EXILibraryFactory.getInstance().createEXILibrary("fabric.module.exi.java.lib.exi.EXIficient", null);
    assertNotNull("EXILibrary object must not be null.", exiLibrary);
  }

  /**
   * Test EXILibrary implementation for the EXIficient library.
   */
  @Test(timeout = 1000)
  public void testEXIficientLibrary() throws Exception
  {
    this.testEXILibrary("fabric.module.exi.java.lib.exi.EXIficient");
  }

  /**
   * Test EXILibrary implementation for the OpenEXI library.
   */
  @Test(timeout = 1000)
  public void testOpenEXILibrary() throws Exception
  {
    this.testEXILibrary("fabric.module.exi.java.lib.exi.OpenEXI");
  }

  /**
   * Private helper method to check EXILibrary implementations.
   *
   * @param exiLibraryClassName Fully qualified class name of
   * the EXILibrary implementation
   */
  private void testEXILibrary(final String exiLibraryClassName) throws Exception
  {
    EXILibrary exiLibrary = EXILibraryFactory.getInstance().createEXILibrary(exiLibraryClassName, null);

    // Check library initialization
    JClass classObject = exiLibrary.init();
    assertNotNull("Returned JClass object must not be null.", classObject);
    assertEquals("JClass name must be 'EXIConverter'.", "EXIConverter", classObject.getName());

    // Check existence of method serialize()
    TestHelper.checkMethodExistence(classObject, "serialize", "byte[]", "String");

    // Check existence of method deserialize()
    TestHelper.checkMethodExistence(classObject, "deserialize", "String", "byte[]");
  }
}
