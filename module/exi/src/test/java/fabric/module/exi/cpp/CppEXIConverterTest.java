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
package fabric.module.exi.cpp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

/**
 * Unit test for CppEXIConverter class.
 *
 * @author seidel
 */
public class CppEXIConverterTest
{
  /**
   * Test method for nice comment delimiters.
   */
  @Test(timeout = 1000)
  public void testNiceCommentDelimiter() throws Exception
  {
    // Test comment with even text length
    String text = "Even";
    this.testComment(text);
    
    // Test comment with odd text length
    text = "Odd";
    this.testComment(text);
    
    // Test comment with text length > 52 characters
    text = "";
    for (int i = 0; i < 100; ++i)
    {
      text += "A";
    }
    this.testComment(text);
  }

  /**
   * Private helper method to test text length of return value.
   * 
   * @param comment Comment to test
   */
  private void testComment(final String comment) throws Exception
  {
    // Use reflection API to get method for nice comment delimiters
    Method mapping = CppEXIConverter.class.getDeclaredMethod("createNiceCommentDelimiter", String.class);
    mapping.setAccessible(true); // Make private method accessible

    // Output return value and test length
    System.out.println(mapping.invoke(null, comment));
    assertEquals("Message", mapping.invoke(null, comment).toString().length(), 52);
  }
}
