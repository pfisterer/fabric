/** 06.01.2012 18:13 */
package fabric.module.typegen.java;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.java.*;

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
    jc.setComment(new JClassCommentImpl(String.format("The '%s' container class.", container.getName())));
    
    // Add annotations
    for (String annotation: this.xmlMapper.getRootAnnotations(this.firstLetterCapital(container.getName())))
    {
      jc.addAnnotation(new JClassAnnotationImpl(annotation));
    }

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
       * Create enum type (optional)
       *****************************************************************/
      if (member.getClass() == AttributeContainer.EnumElement.class)
      {
        AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;

        JEnum je = JEnum.factory.create(JModifier.PUBLIC | JModifier.STATIC, ee.type, this.fixEnumConstants(ee.enumConstants));
        je.setComment(new JEnumCommentImpl(String.format("The '%s' enumeration.", ee.type)));
        
        // Add annotations
        for (String annotation: this.xmlMapper.getEnumAnnotations(ee.type))
        {
          je.addAnnotation(new JEnumAnnotationImpl(annotation));
        }
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
      JMethod getter = this.createGetterMethod(member, container.getName());
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
  private JField createMemberVariable(final MemberVariable member) throws Exception
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

      jf.setComment(new JFieldCommentImpl(String.format("The '%s' element.", e.name)));

      // Add annotations
      for (String annotation: this.xmlMapper.getElementAnnotations(e.name))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
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
      jf.setComment(new JFieldCommentImpl(String.format("The '%s' constant.", ce.name)));
      
      // Add annotations
      for (String annotation: this.xmlMapper.getElementAnnotations(ce.name))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
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

      jf.setComment(new JFieldCommentImpl(String.format("The '%s' attribute.", a.name)));

      // Add annotations
      for (String annotation: this.xmlMapper.getAttributeAnnotations(a.name))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
    }

    /*****************************************************************
     * Handle XML element of type enum
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.EnumElement.class)
    {
      AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;
      
      jf = JField.factory.create(JModifier.PRIVATE, ee.type, ee.name);
      jf.setComment(new JFieldCommentImpl(String.format("The '%s' enum element.", ee.name)));
      
      // Add annotations
      for (String annotation: this.xmlMapper.getElementAnnotations(ee.name))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
    }

    /*****************************************************************
     * Handle XML element arrays
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ElementArray.class)
    {
      AttributeContainer.ElementArray ea = (AttributeContainer.ElementArray)member;
      String type = String.format("java.util.ArrayList<%s>", this.fixPrimitiveTypes(ea.type));

      // No array size is given
      if (ea.maxSize == Integer.MAX_VALUE)
      {
        jf = JField.factory.create(JModifier.PRIVATE, type, ea.name, "new " + type + "()");
      }
      // Array size is given
      else
      {
        jf = JField.factory.create(JModifier.PRIVATE, type, ea.name, "new " + type + "(" + ea.maxSize + ")");
      }

      jf.setComment(new JFieldCommentImpl(String.format("The '%s' element array.", ea.name)));
      
      // Add annotations
      for (String annotation: this.xmlMapper.getArrayAnnotations(ea.name, ea.type, ea.name, ea.type))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
    }

    /*****************************************************************
     * Handle XML element lists
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ElementList.class)
    {
      AttributeContainer.ElementList el = (AttributeContainer.ElementList)member;
      String type = String.format("java.util.ArrayList<%s>", this.fixPrimitiveTypes(el.type));

      // No list size is given
      if (el.maxSize == Integer.MAX_VALUE)
      {
        jf = JField.factory.create(JModifier.PRIVATE, type, el.name, "new " + type + "()");
      }
      // List size is given
      else
      {
        jf = JField.factory.create(JModifier.PRIVATE, type, el.name, "new " + type + "(" + el.maxSize + ")");
      }

      jf.setComment(new JFieldCommentImpl(String.format("The '%s' element list.", el.name)));

      // Add annotations
      for (String annotation: this.xmlMapper.getListAnnotations(el.name, "value", el.type))
      {
        jf.addAnnotation(new JFieldAnnotationImpl(annotation));
      }
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
  private JMethod createSetterMethod(final MemberVariable member) throws Exception
  {
    // No setter for constants
    if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      return null;
    }

    String methodBody = "";
    String type = member.type;
    int modifiers = JModifier.FINAL;
    
    // Member variable is an element or attribute
    if (member.getClass() == AttributeContainer.Element.class || member.getClass() == AttributeContainer.Attribute.class)
    {
      AttributeContainer.RestrictedElementBase e = (AttributeContainer.RestrictedElementBase)member;

      // Create code to check restrictions
      methodBody += this.generateRestrictionChecks(e);

      // Remove 'final' modifier, if member variable has 'whiteSpace' restriction
      if (e.isWhiteSpaceRestricted() && ("String").equals(e.type)) // Java can only enforce 'whiteSpace' on strings
      {
        modifiers &= ~JModifier.FINAL;
      }
    }
    // Member variable is an ElementArray or ElementList
    else if (member instanceof AttributeContainer.ElementCollection)
    {
      AttributeContainer.ElementCollection ec = (AttributeContainer.ElementCollection)member;
      type = String.format("java.util.ArrayList<%s>", this.fixPrimitiveTypes(member.type));

      // Create code to check array or list size
      methodBody += JavaRestrictionHelper.createCheckCode(
              String.format("%s.size() < %d || %s.size() > %d", member.name, ec.minSize, member.name, ec.maxSize),
              String.format("Illegal size for array '%s'.", member.name),
              "Check the occurrence indicators");
    }

    JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create(modifiers, type, member.name));
    JMethod setter = JMethod.factory.create(JModifier.PUBLIC, "void", "set" + this.firstLetterCapital(member.name), jms);

    methodBody += String.format("this.%s = %s;", member.name, member.name);
    setter.getBody().appendSource(methodBody);
    setter.setComment(new JMethodCommentImpl(String.format("Set the '%s' member variable.", member.name)));

    return setter;
  }

  /**
   * Private helper method to create code for restriction checking.
   * The function determines, if any restrictions are set on the
   * given member variable, and generates check-code accordingly.
   *
   * @param member Element object with restrictions
   *
   * @return String with code that includes restriction checks
   *
   * @throws Exception Error during check code generation
   */
  private String generateRestrictionChecks(final AttributeContainer.RestrictedElementBase member) throws Exception
  {
    String result = "";

    // Create code to check restrictions
    AttributeContainer.Restriction r = member.restrictions;
    String operand = member.name;
    String message = "Restriction '%s' violated for member variable '%s'.";
    String comment = "Check the '%s' restriction";

    // If member type is QName, enforce restriction on local part
    if (member.type.endsWith("QName"))
    {
      operand = String.format("(%s.getNamespaceURI() + \":\" + %s.getLocalPart())", member.name, member.name);
    }

    if (member.isLengthRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.lengthExpression(operand, Long.parseLong(r.length)),
              String.format(message, "length", member.name),
              String.format(comment, "length"));
    }

    if (member.isMinLengthRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.minLengthExpression(operand, Long.parseLong(r.minLength)),
              String.format(message, "minLength", member.name),
              String.format(comment, "minLength"));
    }

    if (member.isMaxLengthRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.maxLengthExpression(operand, Long.parseLong(r.maxLength)),
              String.format(message, "maxLength", member.name),
              String.format(comment, "maxLength"));
    }

    if (member.isMinInclusiveRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.minInclusiveExpression(member),
              String.format(message, "minInclusive", member.name),
              String.format(comment, "minInclusive"));
    }

    if (member.isMaxInclusiveRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.maxInclusiveExpression(member),
              String.format(message, "maxInclusive", member.name),
              String.format(comment, "maxInclusive"));
    }

    if (member.isMinExclusiveRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.minExclusiveExpression(member),
              String.format(message, "minExclusive", member.name),
              String.format(comment, "minExclusive"));
    }

    if (member.isMaxExclusiveRestricted())
    {
      result += JavaRestrictionHelper.createCheckCode(
              JavaRestrictionHelper.maxExclusiveExpression(member),
              String.format(message, "maxExclusive", member.name),
              String.format(comment, "maxExclusive"));
    }

    if (member.isPatternRestricted())
    {
      result += JavaRestrictionHelper.createPatternCheckCode(
              operand,
              r.pattern,
              String.format(message, "pattern", member.name));
    }

    if (member.isWhiteSpaceRestricted() && ("String").equals(member.type)) // Java can only enforce 'whiteSpace' on strings
    {
      result += JavaRestrictionHelper.createWhiteSpaceCheckCode(
              member.name,
              r.whiteSpace);
    }

    if (member.isTotalDigitsRestricted())
    {
      result += JavaRestrictionHelper.createTotalDigitsCheckCode(
              member.name,
              r.totalDigits);
    }

    if (member.isFractionDigitsRestricted())
    {
      result += JavaRestrictionHelper.createFractionDigitsCheckCode(
              member.name,
              r.fractionDigits);
    }

    return result;
  }

  /**
   * Private helper method to create getter methods. This function
   * will create a JMethod object with a comment.
   *
   * @param member MemberVariable object for creation
   * @param outerClass Name of surrounding container class
   *
   * @return Generated JMethod object
   *
   * @throws Exception Error during JMethod creation
   */
  private JMethod createGetterMethod(final MemberVariable member, final String outerClass) throws Exception
  {
    // Member variable is an ElementArray or ElementList
    String type = member.type;
    if (member instanceof AttributeContainer.ElementCollection)
    {
      type = String.format("java.util.ArrayList<%s>", this.fixPrimitiveTypes(member.type));
    }

    // Member variable is a constant
    String reference = "this";
    if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      reference = outerClass;
    }

    JMethod getter = JMethod.factory.create(JModifier.PUBLIC, type, "get" + this.firstLetterCapital(member.name));

    getter.getBody().appendSource(String.format("return %s.%s;", reference, member.name));
    getter.setComment(new JMethodCommentImpl(String.format("Get the '%s' member variable.", member.name)));

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
   * Private helper method to fix Java primitives for ArrayList.
   * Most Java collections rely on entries to be derived from
   * Object. Java primitive types (boolean, int etc.), however,
   * are not. So we use this function to translate Java primitive
   * types to the corresponding wrapper classes (Boolean, Integer
   * etc.).
   *
   * @param typeName Name of (possibly primitive) type
   *
   * @return Type name that is compatible to ArrayList
   */
  private String fixPrimitiveTypes(final String typeName)
  {
    // Initial assumption: Type is not a Java primitive
    String result = typeName;

    // Map of wrapper classes for Java primitives
    HashMap<String, String> wrapperClasses = new HashMap<String, String>();
    wrapperClasses.put("byte", "Byte");
    wrapperClasses.put("short", "Short");
    wrapperClasses.put("int", "Integer");
    wrapperClasses.put("long", "Long");
    wrapperClasses.put("float", "Float");
    wrapperClasses.put("double", "Double");
    wrapperClasses.put("char", "Character");
    wrapperClasses.put("boolean", "Boolean");

    // If we have a Java primitive, translate it to wrapper class
    if (wrapperClasses.containsKey(typeName))
    {
      result = wrapperClasses.get(typeName);
    }

    return result;
  }

  /**
   * Private helper method to fix the name of enum constants
   * for Java. XML schema allows names that start with a
   * number or include dashes, whereas in Java enum constants
   * must start with a character and must not contain dashes.
   *
   * This function replaces all invalid characters and adds
   * a replace character to the beginning of a name, if it
   * starts with a non-character.
   *
   * To prevent the creation of duplicate names, we always
   * have to check that a newly created name not already
   * exists in the enum constants array.
   *
   * @param enumConstants Array with XSD enum constant names
   *
   * @return Array with cleaned, Java-compatible constant names
   */
  private String[] fixEnumConstants(final String[] enumConstants)
  {
    final char REPLACE_CHAR = '_';

    // Check all enum constants
    for (int i = 0; i < enumConstants.length; ++i)
    {
      String newName = enumConstants[i];

      // First character must not be a number
      if (newName.substring(0, 1).matches("[0-9]"))
      {
        newName = REPLACE_CHAR + newName;
      }

      // Replace all invalid characters
      newName = newName.replaceAll("[^A-Za-z0-9]", String.valueOf(REPLACE_CHAR));

      // If name was changed, make sure we created no duplicate
      if (!newName.equals(enumConstants[i]) && Arrays.asList(enumConstants).contains(newName))
      {
        do
        {
          // Create new name
          newName = REPLACE_CHAR + newName;

          // Check again
        } while (Arrays.asList(enumConstants).contains(newName));
      }

      enumConstants[i] = newName;
    }

    return enumConstants;
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
