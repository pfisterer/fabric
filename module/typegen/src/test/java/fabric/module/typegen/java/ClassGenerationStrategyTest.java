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

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;

/**
 * Unit test for ClassGenerationStrategy interface and classes.
 *
 * @author seidel
 */
public class ClassGenerationStrategyTest
{
  /**
   * Unit test for JavaClassGenerationStrategy.
   */
  @Test(timeout = 1000)
  public void testJavaClassGenerationStrategy() throws Exception
  {
    // Check parameterless constructor
    JavaClassGenerationStrategy javaStrategy = new JavaClassGenerationStrategy();
    assertNotNull("JavaClassGenerationStrategy object must not be null.", javaStrategy);

    // Check parameterized constructor
    javaStrategy = new JavaClassGenerationStrategy(new AnnotationMapper());
    assertNotNull("JavaClassGenerationStrategy object with AnnotationMapper must not be null.", javaStrategy);

    // Check generateClassObject()
    JClass jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build());
    assertNotNull("JClass object must not be null.", jClassObject);
    assertFalse("JClass content must not be empty string.", ("").equals(jClassObject.toString()));
    System.out.println(jClassObject.toString());

    // Check generateClassObject() with custom modifiers
    int modifiers = JModifier.PUBLIC | JModifier.STATIC;
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), modifiers);
    assertNotNull("JClass object with custom modifiers must not be null.", jClassObject);
    assertTrue("Text representation of JClass object must contain 'public static'.", jClassObject.toString().contains("public static"));
    System.out.println(jClassObject.toString());

    // Check generateClassObject with extends-directive
    String parent = "ExtendedClassName";
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), parent);
    assertNotNull("JClass object with extends-directive must not be null.", jClassObject);
    assertTrue(String.format("Text representation of JClass object must contain 'extends %s'.", parent),
            jClassObject.toString().contains(String.format("extends %s", parent)));
    System.out.println(jClassObject.toString());

    // Check generateClassObject with custom modifiers and extends-directive
    jClassObject = (JClass)javaStrategy.generateClassObject(AttributeContainer.newBuilder().addAttribute("int", "foo").build(), modifiers, parent);
    assertNotNull("JClass object with custom modifiers and extends-directive must not be null.", jClassObject);
    assertTrue("Text representation of JClass object must contain 'public static'.", jClassObject.toString().contains("public static"));
    assertTrue(String.format("Text representation of JClass object must contain 'extends %s'.", parent),
            jClassObject.toString().contains(String.format("extends %s", parent)));
    System.out.println(jClassObject.toString());

    // Check getRequiredImports()
    ArrayList<String> imports = javaStrategy.getRequiredDependencies();
    assertTrue("List of required imports must contain two items.", imports.size() == 2);
    assertTrue("Required imports must contain 'org.simpleframework.xml.Root'.", imports.contains("org.simpleframework.xml.Root"));
    assertTrue("Required imports must contain 'org.simpleframework.xml.Attribute'.", imports.contains("org.simpleframework.xml.Attribute"));
  }

  /**
   * Unit test for JavaClassGenerationStrategy with restricted elements.
   */
  @Test(timeout = 1000)
  public void testJavaClassGenerationStrategyWithRestrictions() throws Exception
  {
    // Create container builder and strategy object
    AttributeContainer.Builder builder = AttributeContainer.newBuilder();
    JavaClassGenerationStrategy javaStrategy = new JavaClassGenerationStrategy();

    // Populate map with all possible restrictions
    HashMap<String, Restriction> restrictions = new HashMap<String, Restriction>();
    restrictions.put("length", new AttributeContainer.Restriction("5", null, null));
    restrictions.put("minLength", new AttributeContainer.Restriction(null, "3", null));
    restrictions.put("maxLength", new AttributeContainer.Restriction(null, null, "7"));    
    restrictions.put("minInclusive", new AttributeContainer.Restriction("10", null, true));
    restrictions.put("maxInclusive", new AttributeContainer.Restriction(null, "50", true));
    restrictions.put("minExclusive", new AttributeContainer.Restriction("110", null, false));
    restrictions.put("maxExclusive", new AttributeContainer.Restriction(null, "150", false));
    restrictions.put("pattern", new AttributeContainer.Restriction("(SS|WS)20[0-9][0-9]", null, null, null));
    restrictions.put(/* whiteSpace */"preserve", new AttributeContainer.Restriction(null, "preserve", null, null));
    restrictions.put(/* whiteSpace */"replace", new AttributeContainer.Restriction(null, "replace", null, null));
    restrictions.put(/* whiteSpace */"collapse", new AttributeContainer.Restriction(null, "collapse", null, null));
    restrictions.put("totalDigits", new AttributeContainer.Restriction(null, null, "5", null));
    restrictions.put("fractionDigits", new AttributeContainer.Restriction(null, null, null, "2"));

    // Check generateClassObject() for all restrictions
    Iterator iterator = restrictions.entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry<String, Restriction> restriction = (Map.Entry)iterator.next();
      AttributeContainer container = builder.clear().addElement("String", "restrictedElement", restriction.getValue()).build();

      JClass jClassObject = (JClass)javaStrategy.generateClassObject(container);
      assertNotNull("JClass object must not be null.", jClassObject);
      assertFalse("JClass content must not be empty string.", ("").equals(jClassObject.toString()));
      assertTrue(String.format("JClass content must contain check for restriction on '%s'.", restriction.getKey()),
              jClassObject.toString().contains(restriction.getKey()));
      System.out.println(jClassObject.toString());
    }
  }
}
