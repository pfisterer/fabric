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
package fabric.codegeneration;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

/**
 * Unit test for the JClassImpl class.
 *
 * @author seidel
 */
public class JClassImplTest
{
  /**
   * Test getJMethodsByName() function.
   */
  @Test(timeout = 1000)
  public void testGetJMethodsByName() throws Exception
  {
    // Create JClass object
    JClass classObject = JClass.factory.create("Test");

    // Create method signatures
    JMethodSignature jmsInt = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, "int", "foo"));
    JMethodSignature jmsString = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, "String", "foo"));

    // Create two methods with different signature
    classObject.add(JMethod.factory.create(JModifier.PUBLIC, "void", "testMethod", jmsInt));
    classObject.add(JMethod.factory.create(JModifier.PUBLIC, "void", "testMethod", jmsString));

    // Get all methods and count them
    List<JMethod> methods = classObject.getJMethodsByName("testMethod");
    assertNotNull("List of methods must not be null.", methods);
    assertTrue("Class object must contain two methods called 'testMethod'.", methods.size() == 2);
  }
}
