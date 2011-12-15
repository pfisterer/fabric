/** 15.12.2011 11:54 */
package fabric.module.typegen.cpp;

import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.c.*;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.MemberVariable;
import fabric.module.typegen.base.ClassGenerationStrategy;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * Class generation strategy for C++. This class implements the
 * ClassGenerationStrategy interface to convert AttributeContainer
 * objects to CppClass objects.
 *
 * @author seidel
 */
public class CppClassGenerationStrategy implements ClassGenerationStrategy
{
  /** List of required C++ includes to compile the class */
  private ArrayList<String> requiredIncludes;

  /**
   * Parameterless constructor creates an ArrayList for the
   * required C++ includes.
   */
  public CppClassGenerationStrategy()
  {
    this.requiredIncludes = new ArrayList<String>();
  }

  /**
   * This method returns a class object that can be added to a source
   * file. Return value should be casted to CppClass before further use.
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
    return this.generateCppClassObject(container, null);
  }

  /**
   * This method returns a list of all C++ includes that are
   * required to compile the class file without errors later.
   *
   * @return List of required C++ includes
   */
  @Override
  public ArrayList<String> getRequiredDependencies()
  {
    return this.requiredIncludes;
  }

  /**
   * Private helper method to add a single file to the
   * list of required C++ includes.
   *
   * @param requiredInclude Required C++ include
   */
  private void addRequiredInclude(final String requiredInclude)
  {
    // Add required include, if this was not done before
    if (!this.requiredIncludes.contains(requiredInclude))
    {
      this.requiredIncludes.add(requiredInclude);
    }
  }

  /**
   * Private helper method to create CppClass object from AttributeContainer.
   *
   * @param container AttributeContainer for conversion
   * @param parent Parent class name for extends-directive (set to
   * null or empty string if class has no parent)
   *
   * @return Generated CppClass object
   *
   * @throws Exception Error during class object generation
   */
  private CppClass generateCppClassObject(final AttributeContainer container, final String parent) throws Exception
  {
    /*****************************************************************
     * Create surrounding container class
     *****************************************************************/
    CppClass cppc = CppClass.factory.create(this.firstLetterCapital(container.getName()));
    cppc.setComment(new CCommentImpl(String.format("The '%s' container class.", container.getName())));
    
    // Set extends-directive
    if (null != parent && parent.length() > 0)
    {
      cppc.addExtended(Cpp.PUBLIC, parent);
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
      CppVar cppv = this.createMemberVariable(member);
      if (null != cppv)
      {
        cppc.add(cppv);
      }

      /*****************************************************************
       * Create enum type (optional)
       *****************************************************************/
      if (member.getClass() == AttributeContainer.EnumElement.class)
      {
        AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;

        CEnum ce = CEnum.factory.create(ee.type, "", false, ee.enumConstants); // TODO: Check name/varname
        ce.setComment(new CCommentImpl(String.format("The '%s' enumeration.", ee.type)));
        
//        cppc.add(Cpp.PUBLIC | Cpp.STATIC, ce); // TODO: This should work later on
        cppc.add(Cpp.PUBLIC, ce); // TODO: Remove
      }

      /*****************************************************************
       * Create setter
       *****************************************************************/
      CppFun setter = this.createSetterMethod(member);
      if (null != setter)
      {
        cppc.add(Cpp.PUBLIC, setter);
      }

      /*****************************************************************
       * Create getter
       *****************************************************************/
      CppFun getter = this.createGetterMethod(member, container.getName());
      if (null != getter)
      {
        cppc.add(Cpp.PUBLIC, getter);
      }
    }
    
    return cppc;
  }

  /**
   * Private helper method for member variable creation. This function
   * will create a CppVar object with a comment.
   * 
   * @param member MemberVariable object for creation
   *
   * @return Generated CppVar object
   *
   * @throws Exception Error during CppVar creation
   */
  private CppVar createMemberVariable(MemberVariable member) throws Exception
  {
    CppVar cppv = null;

    /*****************************************************************
     * Handle XML elements and XML attributes
     *****************************************************************/    
    if (member.getClass() == AttributeContainer.Element.class || member.getClass() == AttributeContainer.Attribute.class)
    {
      AttributeContainer.RestrictedElementBase re = (AttributeContainer.RestrictedElementBase)member;
      
      // No initial value set
      if (("").equals(re.value))
      {
        cppv = CppVar.factory.create(Cpp.PRIVATE, re.type, re.name);
      }
      // Initial value is set
      else
      {
        // Add quotation marks to initial value, if type is char*
        String value = re.value;
        if (("char*").equals(re.type))
        {
          value = "\"" + value + "\"";
        }

        cppv = CppVar.factory.create(Cpp.PRIVATE, re.type, re.name, value);
      }

      cppv.setComment(new CCommentImpl(String.format("The '%s' element.", re.name)));
    }

    /*****************************************************************
     * Handle constant XML elements
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      AttributeContainer.ConstantElement ce = (AttributeContainer.ConstantElement)member;

      // Add quotation marks to value, if type is String
      String value = ce.value;
      if (("char*").equals(ce.type))
      {
        value = "\"" + value + "\"";
      }

      cppv = CppVar.factory.create(Cpp.PRIVATE | Cpp.CONST, ce.type, ce.name);
      cppv.setComment(new CCommentImpl(String.format("The '%s' constant.", ce.name)));
    }

    /*****************************************************************
     * Handle XML element of type enum
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.EnumElement.class)
    {
      AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)member;

      cppv = CppVar.factory.create(Cpp.PRIVATE, ee.type, ee.name);
      cppv.setComment(new CCommentImpl(String.format("The '%s' enum element.", ee.name)));
    }

    /*****************************************************************
     * Handle XML element arrays
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ElementArray.class)
    {
      AttributeContainer.ElementArray ea = (AttributeContainer.ElementArray)member;
      String type = String.format("vector<%s>", ea.type);
      this.addRequiredInclude("vector"); // TODO: Check this
      
      // No array size is given
      if (ea.maxSize == Integer.MAX_VALUE)
      {
        cppv = CppVar.factory.create(Cpp.PRIVATE, type, ea.name); // TODO: Check this in generated code
      }
      // Array size is given
      else
      {
        cppv = CppVar.factory.create(Cpp.PRIVATE, type, ea.name, String.valueOf(ea.maxSize)); // TODO: Check this in generated code
      }
      
      cppv.setComment(new CCommentImpl(String.format("The '%s' element array.", ea.name)));
    }

    /*****************************************************************
     * Handle XML element lists
     *****************************************************************/
    else if (member.getClass() == AttributeContainer.ElementList.class)
    {
      AttributeContainer.ElementList el = (AttributeContainer.ElementList)member;
      String type = String.format("vector<%s>", el.type);
      this.addRequiredInclude("vector"); // TODO: Check this

      // No list size is given
      if (el.maxSize == Integer.MAX_VALUE)
      {
        cppv = CppVar.factory.create(Cpp.PRIVATE, type, el.name); // TODO: Check this in generated code
      }
      // List size is given
      else
      {
        cppv = CppVar.factory.create(Cpp.PRIVATE, type, el.name, String.valueOf(el.maxSize)); // TODO: Check this in generated code
      }
      
      cppv.setComment(new CCommentImpl(String.format("The '%s' element list.", el.name)));
    }

    /*****************************************************************
     * Handle unknown types
     *****************************************************************/
    else
    {
      throw new FabricTypeGenException("Member variable in attribute container has unknown type.");
    }

    return cppv;
  }

  /**
   * Private helper method to create setter methods. This function
   * will create a CppFun object with a comment or return null,
   * if member variable is a constant.
   *
   * @param member MemberVariable object for creation
   *
   * @return Generated CppFun object or null
   *
   * @throws Exception Error during CppFun creation
   */
  private CppFun createSetterMethod(MemberVariable member) throws Exception
  {
    // No setter for constants
    if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      return null;
    }

    String methodBody = "";
    String type = member.type;
    long modifiers = Cpp.CONST;
    
    // Member variable is an element or attribute
    if (member.getClass() == AttributeContainer.Element.class || member.getClass() == AttributeContainer.Attribute.class)
    {
      AttributeContainer.RestrictedElementBase e = (AttributeContainer.RestrictedElementBase)member; // TODO: Can we use base class here?

      // Create code to check restrictions
      methodBody += this.generateRestrictionChecks(e);

      // Remove 'const' modifier, if member variable has 'whiteSpace' restriction
      if (e.isWhiteSpaceRestricted() && ("char*").equals(e.type)) // C++ can only enforce 'whiteSpace' on char*
      {
        modifiers &= ~Cpp.CONST;
      }
    }
    // Member variable is an ElementArray or ElementList
    else if (member instanceof AttributeContainer.ElementCollection)
    {
      AttributeContainer.ElementCollection ec = (AttributeContainer.ElementCollection)member;
      type = String.format("vector<%s>", member.type);

      // Create code to check array or list size
      methodBody += CppRestrictionHelper.createCheckCode(
              String.format("%s.size() < %d || %s.size() > %d", member.name, ec.minSize, member.name, ec.maxSize),
              String.format("Illegal size for array '%s'.", member.name),
              "Check the occurrence indicators");
    }
    
    CppVar attribute = CppVar.factory.create(modifiers, type, member.name);
    CppFun setter = CppFun.factory.create("void", "set" + this.firstLetterCapital(member.name), attribute);

    methodBody += String.format("this->%s = %s;", member.name, member.name);
    setter.appendCode(methodBody);
    setter.setComment(new CCommentImpl(String.format("Set the '%s' member variable.", member.name)));

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
  private String generateRestrictionChecks(AttributeContainer.RestrictedElementBase member) throws Exception
  {
    String result = "";

    // Create code to check restrictions
    AttributeContainer.Restriction r = member.restrictions;
    String operandName = member.name;
    String message = "Restriction '%s' violated for member variable '%s'.";
    String comment = "Check the '%s' restriction";

    // If member type is QName, enforce restriction on local part    
    if (member.type.endsWith("xs_qName_t"))
    {
      // TODO: How do we do this on our struct in C++?
      operandName = String.format("(%s.getNamespaceURI() + \":\" + %s.getLocalPart())", member.name, member.name);
    }

    if (member.isLengthRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              String.format("strlen(%s) != %d", operandName, Long.parseLong(r.length)),
              String.format(message, "length", member.name),
              String.format(comment, "length"));
    }

    if (member.isMinLengthRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              String.format("strlen(%s) < %d", operandName, Long.parseLong(r.minLength)),
              String.format(message, "minLength", member.name),
              String.format(comment, "minLength"));
    }

    if (member.isMaxLengthRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              String.format("strlen(%s) > %d", operandName, Long.parseLong(r.maxLength)),
              String.format(message, "maxLength", member.name),
              String.format(comment, "maxLength"));
    }

    if (member.isMinInclusiveRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              CppRestrictionHelper.minInclusiveExpression(member),
              String.format(message, "minInclusive", member.name),
              String.format(comment, "minInclusive"));
    }

    if (member.isMaxInclusiveRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              CppRestrictionHelper.maxInclusiveExpression(member),
              String.format(message, "maxInclusive", member.name),
              String.format(comment, "maxInclusive"));
    }

    if (member.isMinExclusiveRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              CppRestrictionHelper.minExclusiveExpression(member),
              String.format(message, "minExclusive", member.name),
              String.format(comment, "minExclusive"));
    }

    if (member.isMaxExclusiveRestricted())
    {
      result += CppRestrictionHelper.createCheckCode(
              CppRestrictionHelper.maxExclusiveExpression(member),
              String.format(message, "maxExclusive", member.name),
              String.format(comment, "maxExclusive"));
    }

    if (member.isPatternRestricted())
    {
      result += CppRestrictionHelper.createPatternCheckCode(
              operandName,
              r.pattern,
              String.format(message, "pattern", member.name));
    }

    if (member.isWhiteSpaceRestricted() && ("char*").equals(member.type)) // C++ can only enforce 'whiteSpace' on char*
    {
      result += CppRestrictionHelper.createWhiteSpaceCheckCode(
              member.name,
              r.whiteSpace);
    }

    if (member.isTotalDigitsRestricted() || member.isFractionDigitsRestricted())
    {
      result += CppRestrictionHelper.createDigitsCheckCode(member);
    }

    return result;
  }

  /**
   * Private helper method to create getter methods. This function
   * will create a CppFun object with a comment.
   *
   * @param member MemberVariable object for creation
   * @param className Name of surrounding container class
   *
   * @return Generated CppFun object
   *
   * @throws Exception Error during CppFun creation
   */
  private CppFun createGetterMethod(MemberVariable member, String className) throws Exception
  {
    // Member variable is an ElementArray or ElementList
    String type = member.type;
    if (member instanceof AttributeContainer.ElementCollection)
    {
      type = String.format("vector<%s>", member.type);
    }

    // Member variable is a constant
    String reference = "this";
    if (member.getClass() == AttributeContainer.ConstantElement.class)
    {
      reference = className; // TODO: Constants don't work like this in C++!
    }
    
    CppFun getter = CppFun.factory.create(type, "get" + this.firstLetterCapital(member.name));

    getter.appendCode(String.format("return %s->%s;", reference, member.name));
    getter.setComment(new CCommentImpl(String.format("Get the '%s' member variable.", member.name)));

    return getter;
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
