package fabric.module.typegen.java;

import java.util.Map;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassAnnotationImpl;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JField;
import de.uniluebeck.sourcegen.java.JFieldAnnotationImpl;
import de.uniluebeck.sourcegen.java.JFieldCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import fabric.module.typegen.AttributeContainer;

import fabric.module.typegen.AttributeContainer.MemberVariable;
import fabric.module.typegen.base.ClassGenerationStrategy;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * @author seidel
 */
public class JavaClassGenerationStrategy implements ClassGenerationStrategy
{
  // TODO: Annotation-Mapper einbauen

  /**
   * Returns class object that can be added to a source file. Return
   * value should be cast to JClass before further use.
   *
   * @return JClass object
   */
  @Override
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception
  {
    return this.generateJClassObject(container);
  }

  private JClass generateJClassObject(AttributeContainer container) throws Exception
  {
    /*****************************************************************
     * Create container class
     *****************************************************************/
    JClass jc = JClass.factory.create(JModifier.PUBLIC, this.firstLetterCapital(container.getName()));
    jc.setComment(new JClassCommentImpl("The '" + container.getName() + "' container class."));
    jc.addAnnotation(new JClassAnnotationImpl("Root(name = \"" + this.firstLetterCapital(container.getName()) + "\")"));

    // Process all members
    Iterator iterator = container.getMembers().entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry<String, MemberVariable> item = (Map.Entry)iterator.next();
      MemberVariable member = item.getValue();

      /*****************************************************************
       * Create member variable
       *****************************************************************/
      JField jf = this.createMemberVariable(member);
      if (null != jf)
      {
        jc.add(jf);
      }

      /*****************************************************************
       * Create setter
       *****************************************************************/
      JMethod setter = this.createSetterMethod(member);
      if (null != setter)
      {
        jc.add(setter);
      }

      /*****************************************************************
       * Create getter
       *****************************************************************/
      JMethod getter = this.createGetterMethod(member);
      if (null != getter)
      {
        jc.add(getter);
      }
    }

    return jc;
  }

  private JField createMemberVariable(MemberVariable member) throws Exception
  {
    JField jf = null;

    // Handle XML attributes
    if (member instanceof AttributeContainer.Attribute)
    {
      AttributeContainer.Attribute a = (AttributeContainer.Attribute)member;

      // No initial value set
      if (("").equals(a.value))
      {
        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name);
      }
      // Initial value is set
      else
      {
        // Add quotation marks to initial value, if type is String
        String value = a.value;
        if (("String").equals(a.type))
        {
          value = "\"" + value + "\"";
        }

        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name, value);
      }

      jf.setComment(new JFieldCommentImpl("The '" + a.name + "' attribute."));
      jf.addAnnotation(new JFieldAnnotationImpl("Attribute"));
    }
    // Handle XML elements
    else if (member instanceof AttributeContainer.Element)
    {
      AttributeContainer.Element a = (AttributeContainer.Element)member;

      // No initial value set
      if (("").equals(a.value))
      {
        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name);
      }
      // Initial value is set
      else
      {
        // Add quotation marks to initial value, if type is String
        String value = a.value;
        if (("String").equals(a.type))
        {
          value = "\"" + value + "\"";
        }

        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name, value);
      }

      jf.setComment(new JFieldCommentImpl("The '" + a.name + "' element."));
      jf.addAnnotation(new JFieldAnnotationImpl("Element"));
    }
    // Handle XML element arrays
    else if (member instanceof AttributeContainer.ElementArray)
    {
      AttributeContainer.ElementArray a = (AttributeContainer.ElementArray)member;

      // No array size is given
      if (a.size == Integer.MAX_VALUE)
      {
        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name + "[]");
      }
      // Array size is given
      else
      {
        jf = JField.factory.create(JModifier.PRIVATE, a.type, a.name + "[]", "new " + a.type + "[" + a.size + "]");
      }

      jf.setComment(new JFieldCommentImpl("The '" + a.name + "' element array."));
      jf.addAnnotation(new JFieldAnnotationImpl("ElementArray"));
    }
    else
    {
      throw new FabricTypeGenException("Member variable in attribute container has unknown type.");
    }

    return jf;
  }

  private JMethod createSetterMethod(MemberVariable member) throws Exception
  {
    String name = member.name;
    if (member instanceof AttributeContainer.ElementArray)
    {
      name = name + "[]";
    }

    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, member.type, name));
    JMethod setter = JMethod.factory.create(JModifier.PUBLIC, "void", "set" + this.firstLetterCapital(member.name), jms);

    setter.getBody().appendSource("container." + member.name + " = " + member.name + ";");
    setter.setComment(new JMethodCommentImpl("Set the '" + member.name + "' member variable."));

    return setter;
  }

  private JMethod createGetterMethod(MemberVariable member) throws Exception
  {
    String type = member.type;
    if (member instanceof AttributeContainer.ElementArray)
    {
      type = type + "[]";
    }

    JMethod getter = JMethod.factory.create(JModifier.PUBLIC, type, "get" + this.firstLetterCapital(member.name));

    getter.getBody().appendSource("return container." + member.name + ";");
    getter.setComment(new JMethodCommentImpl("Get the '" + member.name + "' member variable."));

    return getter;
  }

  private String firstLetterCapital(final String text)
  {
    return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
  }
}
