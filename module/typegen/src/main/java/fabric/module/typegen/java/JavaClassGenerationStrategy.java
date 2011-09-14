package fabric.module.typegen.java;

import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassAnnotationImpl;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JEnumCommentImpl;
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
   * The returned class has public visibility.
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
    return this.generateJClassObject(container, JModifier.PUBLIC, null);
  }

  /**
   * This method returns a class object that can be added to a source
   * file. Return value should be casted to JClass before further use.
   * The returned class has custom visibility, depending on modifiers
   * argument (e.g. JModifier.PUBLIC | JModifier.STATIC for static
   * nested inner classes).
   *
   * @param container AttributeContainer for conversion
   * @param modifiers Modifiers for class object (e.g. visibility)
   *
   * @return Generated WorkspaceElement object
   *
   * @throws Exception Error during class object generation
   */
  public WorkspaceElement generateClassObject(final AttributeContainer container, final int modifiers) throws Exception
  {
    return this.generateJClassObject(container, modifiers, null);
  }

  /**
   * This method returns a class object that can be added to a source
   * file. Return value should be casted to JClass before further use.
   * The returned class will be an extended version of the given parent
   * class.
   * 
   * @param container AttributeContainer for conversion
   * @param parent Name of parent class for the extends-directive
   *
   * @return Generated WorkspaceElement object
   *
   * @throws Exception Error during class object generation
   */
  public WorkspaceElement generateClassObject(final AttributeContainer container, final String parent) throws Exception
  {
    return this.generateJClassObject(container, JModifier.PUBLIC, parent);
  }

  /**
   * This method returns a class object that can be added to a source
   * file. Return value should be casted to JClass before further use.
   * The returned class has custom visibility, depending on modifiers
   * argument (e.g. JModifier.PUBLIC | JModifier.STATIC for static
   * nested inner classes). Furthermore the name of the parent class
   * for the extends-directive can be set.
   *
   * @param container AttributeContainer for conversion
   * @param modifiers Modifiers for class object (e.g. visibility)
   * @param parent Name of parent class for the extends-directive
   *
   * @return Generated WorkspaceElement object
   *
   * @throws Exception Error during class object generation
   */
  public WorkspaceElement generateClassObject(final AttributeContainer container, final int modifiers, final String parent) throws Exception
  {
    return this.generateJClassObject(container, modifiers, parent);
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
   * @param modifiers Modifiers for JClass object (e.g. visibility)
   * @param parent Parent class name for extends-directive (set to
   * null or empty string if class has no parent)
   *
   * @return Generated JClass object
   *
   * @throws Exception Error during class object generation
   */
  private JClass generateJClassObject(final AttributeContainer container, final int modifiers, final String parent) throws Exception
  {
    /*****************************************************************
     * Create surrounding container class
     *****************************************************************/
    JClass jc = JClass.factory.create(modifiers, this.firstLetterCapital(container.getName()));
    jc.setComment(new JClassCommentImpl("The '" + container.getName() + "' container class."));

    // Annotation pattern e.g. @Root(name = "value") or @XStreamAlias("value")
    String annotation = this.xmlMapper.getAnnotation("root", this.firstLetterCapital(container.getName()));
    jc.addAnnotation(new JClassAnnotationImpl(annotation));

    // Set extends-directive
    if (null != parent && parent.length() > 0)
    {
      jc.setExtends(parent);
    }

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
       * Create enum type
       *****************************************************************/
      if (member.getClass() == AttributeContainer.EnumElement.class)
      {
        AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;
        
        JEnum je = JEnum.factory.create(JModifier.PRIVATE, ee.type, ee.enumConstants);
        je.setComment(new JEnumCommentImpl("The '" + ee.type + "' enumeration."));
        jc.add(je);
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
     * Handle XML elements
     *****************************************************************/
    if (member.getClass() == AttributeContainer.Element.class)
    {
      AttributeContainer.Element e = (AttributeContainer.Element)member;

      // No initial value set
      if (("").equals(e.value))
      {
        jf = JField.factory.create(JModifier.PRIVATE, e.type, e.name);
      }
      // Initial value is set
      else
      {
        // Add quotation marks to initial value, if type is String
        String value = e.value;
        if (("String").equals(e.type))
        {
          value = "\"" + value + "\"";
        }

        jf = JField.factory.create(JModifier.PRIVATE, e.type, e.name, value);
      }

      jf.setComment(new JFieldCommentImpl("The '" + e.name + "' element."));

      // Annotation pattern e.g. @Element or @XStreamAlias("value")
      String annotation = this.xmlMapper.getAnnotation("element", e.name);
      jf.addAnnotation(new JFieldAnnotationImpl(annotation));
    }

    /*****************************************************************
     * Handle constant XML elements
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      AttributeContainer.ConstantElement ce = (AttributeContainer.ConstantElement)member;

      // Add quotation marks to value, if type is String
      String value = ce.value;
      if (("String").equals(ce.type))
      {
        value = "\"" + value + "\"";
      }

      jf = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC | JModifier.FINAL, ce.type, ce.name, value);
      jf.setComment(new JFieldCommentImpl("The '" + ce.name + "' constant."));

      // Annotation pattern e.g. @Element or @XStreamAlias("value")
      String annotation = this.xmlMapper.getAnnotation("element", ce.name);
      jf.addAnnotation(new JFieldAnnotationImpl(annotation));
    }

    /*****************************************************************
     * Handle XML attributes
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.Attribute.class)
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

      // Annotation pattern e.g. @Attribute or @XStreamAsAttribute
      String annotation = this.xmlMapper.getAnnotation("attribute", a.name);
      jf.addAnnotation(new JFieldAnnotationImpl(annotation));
    }

    /*****************************************************************
     * Handle XML element of type enum
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.EnumElement.class)
    {
      AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;
      
      jf = JField.factory.create(JModifier.PRIVATE, ee.type, ee.name);
      jf.setComment(new JFieldCommentImpl("The '" + ee.name + "' enum element."));
      
      // Annotation pattern e.g. @XmlEnum
      String annotation = this.xmlMapper.getAnnotation("element", ee.name); // TODO: Change key to enum
      jf.addAnnotation(new JFieldAnnotationImpl(annotation));
    }

    /*****************************************************************
     * Handle XML element arrays
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ElementArray.class)
    {
      AttributeContainer.ElementArray ea = (AttributeContainer.ElementArray)member;

      // No array size is given
      if (ea.size == Integer.MAX_VALUE)
      {
        jf = JField.factory.create(JModifier.PRIVATE, ea.type, ea.name + "[]");
      }
      // Array size is given
      else
      {
        jf = JField.factory.create(JModifier.PRIVATE, ea.type, ea.name + "[]", "new " + ea.type + "[" + ea.size + "]");
      }

      jf.setComment(new JFieldCommentImpl("The '" + ea.name + "' element array."));

      // Annotation pattern e.g. @ElementArray or @XStreamImplicit(itemFieldName="value")
      String annotation = this.xmlMapper.getAnnotation("elementArray", ea.name);
      jf.addAnnotation(new JFieldAnnotationImpl(annotation));
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
   * will create a JMethod object with a comment or return null,
   * if member variable is a constant.
   *
   * @param member MemberVariable object for creation
   *
   * @return Generated JMethod object or null
   *
   * @throws Exception Error during JMethod creation
   */
  private JMethod createSetterMethod(MemberVariable member) throws Exception
  {
    // No setter for constants
    if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      return null;
    }

    String methodBody = "";
    String name = member.name;
    
    // Member variable is an element or attribute
    if (member.getClass() == AttributeContainer.Element.class || member.getClass() == AttributeContainer.Attribute.class)
    {
      // Create code to check restrictions
      methodBody += this.generateRestrictionChecks(member);
    }
    // Member variable is an array    
    else if (member.getClass() == AttributeContainer.ElementArray.class)
    {
      name = name + "[]";

      // Create code to check array length
      methodBody += this.createCheckString(
              String.format("%s.length < 0 || %s.length > %d", member.name, member.name, ((AttributeContainer.ElementArray)member).size),
              String.format("Illegal size for array '%s'.", member.name));
    }

    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create(JModifier.FINAL, member.type, name));
    JMethod setter = JMethod.factory.create(JModifier.PUBLIC, "void", "set" + this.firstLetterCapital(member.name), jms);

    methodBody += "this." + member.name + " = " + member.name + ";";
    setter.getBody().appendSource(methodBody);
    setter.setComment(new JMethodCommentImpl("Set the '" + member.name + "' member variable."));

    return setter;
  }

  /**
   * Private helper method to create code for restriction checking.
   * The function determines, if any restrictions are set on the
   * given member variable, and generates check-code accordingly.
   *
   * @param member MemberVariable object with restrictions
   *
   * @return String with code that includes restriction checks
   */
  private String generateRestrictionChecks(AttributeContainer.MemberVariable member)
  {
    String result = "";
    
    // Create code to check restrictions
    AttributeContainer.Element e = (AttributeContainer.Element)member;
    AttributeContainer.Restriction r = e.restrictions;
    String message = "Restriction '%s' violated for member variable '%s'.";

    if (e.isLengthRestricted())
    {
      result += this.createCheckString(
              String.format("%s.length() != %d", member.name, Integer.parseInt(r.length)),
              String.format(message, "length", member.name));
    }

    if (e.isMinLengthRestricted())
    {
      result += this.createCheckString(
              String.format("%s.length() < %d", member.name, Integer.parseInt(r.minLength)),
              String.format(message, "minLength", member.name));
    }

    if (e.isMaxLengthRestricted())
    {
      result += this.createCheckString(
              String.format("%s.length() > %d", member.name, Integer.parseInt(r.maxLength)),
              String.format(message, "maxLength", member.name));
    }

    if (e.isMinInclusiveRestricted())
    {
      result += this.createCheckString(
              String.format("%s < %d", member.name, Integer.parseInt(r.minInclusive)),
              String.format(message, "minInclusive", member.name));
    }

    if (e.isMaxInclusiveRestricted())
    {
      result += this.createCheckString(
              String.format("%s > %d", member.name, Integer.parseInt(r.maxInclusive)),
              String.format(message, "maxInclusive", member.name));
    }

    if (e.isMinExclusiveRestricted())
    {
      result += this.createCheckString(
              String.format("%s <= %d", member.name, Integer.parseInt(r.minExclusive)),
              String.format(message, "minExclusive", member.name));
    }

    if (e.isMaxExclusiveRestricted())
    {
      result += this.createCheckString(
              String.format("%s >= %d", member.name, Integer.parseInt(r.maxExclusive)),
              String.format(message, "maxExclusive", member.name));
    }

    return result;
  }

  /**
   * Private helper method to create a string that checks a boolean
   * expression and throws an IllegalArgumentException with predefined
   * message on failure.
   *
   * This function is used to add parameter and restriction checks
   * to setter methods.
   *
   * @param expression Boolean expression for if-statement
   * @param message Error message to show in exception
   *
   * @return String with parameter check code
   */
  private String createCheckString(final String expression, final String message)
  {
    String result;

    result = String.format("if (%s)", expression);
    result += "\n{";
    result += String.format("\n\tthrow new IllegalArgumentException(\"%s\");", message);
    result += "\n}\n\n";

    return result;
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
    if (member.getClass() == AttributeContainer.ElementArray.class)
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
   * Function will return null, if argument was null.
   *
   * @param text Text to process
   *
   * @return Text with first letter capitalized or null
   */
  private String firstLetterCapital(final String text) throws Exception
  {
    return (null == text ? null : text.substring(0, 1).toUpperCase() + text.substring(1, text.length()));
  }
}
