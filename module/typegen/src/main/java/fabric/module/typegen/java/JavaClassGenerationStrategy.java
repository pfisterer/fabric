package fabric.module.typegen.java;

import java.util.Map;
import java.util.ArrayList;
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
 * Class generation strategy for Java. This class implements the
 * ClassGenerationStrategy interface to convert AttributeContainer
 * objects to JClass objects.
 *
 * @author seidel
 */
public class JavaClassGenerationStrategy implements ClassGenerationStrategy
{
  /** Mapper for framework-specific JavaToXML annotations */
  private AnnotationMapper xmlMapper = null;

  /**
   * Parameterless constructor uses AnnotationMapper with default
   * XML framework.
   *
   * @throws Exception Error during AnnotationMapper creation
   */
  public JavaClassGenerationStrategy() throws Exception
  {
    this(new AnnotationMapper());
  }

  /**
   * Parameterized constructor can be configured with a custom
   * AnnotationMapper object.
   *
   * @param mapper AnnotationMapper object
   */
  public JavaClassGenerationStrategy(final AnnotationMapper mapper)
  {
    this.xmlMapper = mapper;
  }

  /**
   * This method returns a class object that can be added to a source
   * file. Return value should be casted to JClass before further use.
   *
   * @param container AttributeContainer for conversion
   *
   * @return Generated WorkspaceElement object
   *
   * @throws Exception Error during class object generation
   */
  @Override
  public WorkspaceElement generateClassObject(final AttributeContainer container) throws Exception
  {
    return this.generateJClassObject(container);
  }
  
  /**
   * This method returns a list of all Java imports that
   * are needed to support the required XML annotations.
   *
   * @return List of required Java imports
   */
  @Override
  public ArrayList<String> getRequiredDependencies()
  {
    return this.getRequiredImports();
  }

  /**
   * Private helper method to create JClass object from AttributeContainer.
   *
   * @param container AttributeContainer for conversion
   *
   * @return Generated JClass object
   *
   * @throws Exception Error during class object generation
   */
  private JClass generateJClassObject(final AttributeContainer container) throws Exception
  {
    /*****************************************************************
     * Create surrounding container class
     *****************************************************************/
    JClass jc = JClass.factory.create(JModifier.PUBLIC, this.firstLetterCapital(container.getName()));
    jc.setComment(new JClassCommentImpl("The '" + container.getName() + "' container class."));

    // Annotation pattern e.g. @Root(name = "value") or @XStreamAlias("value")
    String annotation = String.format(this.xmlMapper.getAnnotation("root"), this.firstLetterCapital(container.getName()));
    jc.addAnnotation(new JClassAnnotationImpl(annotation));

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

  /**
   * Private helper method for member variable creation. This function
   * will create an annotated JField with a comment.
   *
   * @param member MemberVariable object for creation
   *
   * @return Generated JField object
   *
   * @throws Exception Error during JField creation
   */
  private JField createMemberVariable(MemberVariable member) throws Exception
  {
    JField jf = null;

    /*****************************************************************
     * Handle XML attributes
     *****************************************************************/
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
      jf.addAnnotation(new JFieldAnnotationImpl(this.xmlMapper.getAnnotation("attribute")));
    }

    /*****************************************************************
     * Handle XML elements
     *****************************************************************/
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
      jf.addAnnotation(new JFieldAnnotationImpl(this.xmlMapper.getAnnotation("element")));
    }

    /*****************************************************************
     * Handle XML element arrays
     *****************************************************************/
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
      jf.addAnnotation(new JFieldAnnotationImpl(this.xmlMapper.getAnnotation("elementArray")));
    }

    /*****************************************************************
     * Handle unknown types
     *****************************************************************/
    else
    {
      throw new FabricTypeGenException("Member variable in attribute container has unknown type.");
    }

    return jf;
  }

  /**
   * Private helper method to create setter methods. This function
   * will create a JMethod object with a comment.
   *
   * @param member MemberVariable object for creation
   *
   * @return Generated JMethod object
   *
   * @throws Exception Error during JMethod creation
   */
  private JMethod createSetterMethod(MemberVariable member) throws Exception
  {
    // Member variable is an array
    String name = member.name;
    if (member instanceof AttributeContainer.ElementArray)
    {
      name = name + "[]";
    }

    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, member.type, name));
    JMethod setter = JMethod.factory.create(JModifier.PUBLIC, "void", "set" + this.firstLetterCapital(member.name), jms);

    setter.getBody().appendSource("this." + member.name + " = " + member.name + ";");
    setter.setComment(new JMethodCommentImpl("Set the '" + member.name + "' member variable."));

    return setter;
  }

  /**
   * Private helper method to create getter methods. This function
   * will create a JMethod object with a comment.
   *
   * @param member MemberVariable object for creation
   *
   * @return Generated JMethod object
   *
   * @throws Exception Error during JMethod creation
   */
  private JMethod createGetterMethod(MemberVariable member) throws Exception
  {
    // Member variable is an array
    String type = member.type;
    if (member instanceof AttributeContainer.ElementArray)
    {
      type = type + "[]";
    }

    JMethod getter = JMethod.factory.create(JModifier.PUBLIC, type, "get" + this.firstLetterCapital(member.name));

    getter.getBody().appendSource("return this." + member.name + ";");
    getter.setComment(new JMethodCommentImpl("Get the '" + member.name + "' member variable."));

    return getter;
  }

  /**
   * Private helper method to get a list of all Java imports
   * that are needed to support the required XML annotations.
   *
   * @return List of required Java imports
   */
  private ArrayList<String> getRequiredImports()
  {
    return this.xmlMapper.getUsedImports();
  }

  /**
   * Private helper method to capitalize the first letter of a string.
   *
   * @param text Text to process
   *
   * @return Text with first letter capitalized
   */
  private String firstLetterCapital(final String text)
  {
    return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
  }
}
