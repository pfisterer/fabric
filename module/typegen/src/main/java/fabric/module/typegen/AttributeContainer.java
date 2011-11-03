/** 17.10.2011 18:00 */
package fabric.module.typegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.module.typegen.base.ClassGenerationStrategy;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * The AttributeContainer class provides a high-level, language
 * independent interface for the creation of Bean-like classes.
 * This is classes that hold private member variables and allow
 * access to them through public setter and getter methods.
 *
 * AttributeContainer objects are immutable and can be constructed
 * with an inner builder class. Write-out is externalized and relies
 * on the so-called ClassGenerationStrategy interface. This way class
 * file creation is handled in an external strategy object, allowing
 * language-independent data management.
 *
 * Usage:
 *   AttributeContainer.Builder.newBuilder(prototype)
 *                             .setName("Car")
 *                             .addAttribute("String", "manufacturer", "Audi")
 *                             .addAttribute("String", "model", "TT")
 *                             .addElement("String", "color", "red")
 *                             .addConstantElement("int", "maxSpeed", "220")
 *                             .addElementArray("String", "trunkItems")
 *                             .addElementArray("String", "passengers", 1, 2)
 *                             .build();
 *
 * @author seidel
 */
public class AttributeContainer
{
  /*****************************************************************
   * Builder inner class
   *****************************************************************/
  
  public static final class Builder
  {
    /** Name of the container class */
    private String name;

    /** Member variables (e.g. attributes, elements, constants or arrays) */
    private Map<String, MemberVariable> members;

    /**
     * Private constructor. Use create() instead.
     */
    private Builder()
    {
      this.reset();
    }

    /**
     * Create new Builder object.
     *
     * @return Builder object
     */
    private static Builder create()
    {
      return new Builder();
    }

    /**
     * Private helper method to reset Builder.
     */
    private void reset()
    {
      this.name = AttributeContainer.DEFAULT_CONTAINER_NAME;
      this.members = new HashMap<String, MemberVariable>();
    }

    /**
     * Public method to reset Builder, that is clear all members.
     *
     * @return Cleared Builder object
     */
    public Builder clear()
    {
      this.reset();

      return this;
    }

    /**
     * Clone Builder object by merging an empty, new container
     * with the current one.
     *
     * @return Cloned Builder object
     */
    @Override
    public Builder clone()
    {
      return Builder.create().mergeWith(this.build());
    }

    /**
     * Create AttributeContainer from Builder object.
     *
     * @return AttributeContainer object
     */
    public AttributeContainer build()
    {
      AttributeContainer result = new AttributeContainer();

      result.name = this.name;

      // Copy member variables
      Iterator iterator = this.members.entrySet().iterator();
      while (iterator.hasNext())
      {
        try
        {
          Map.Entry<String, MemberVariable> member = (Map.Entry)iterator.next();

          result.members.put(member.getKey(), this.copyOf(member.getValue()));
        }
        catch (Exception e)
        {
          LOGGER.error(e.getMessage());
        }
      }

      return result;
    }

    /**
     * Merge this Builder object with another AttributeContainer, that
     * is copy all values from the other container if they are set.
     *
     * @param otherContainer Other AttributeContainer object
     *
     * @return Merged Builder instance
     */
    public Builder mergeWith(final AttributeContainer otherContainer)
    {
      if (otherContainer == AttributeContainer.getDefaultInstance())
      {
        return this;
      }

      if (otherContainer != null)
      {
        // Copy name, if it is set
        if (!otherContainer.getName().equals(""))
        {
          this.setName(otherContainer.getName());
        }

        // Copy members, if any are set
        if (otherContainer.getMembers().size() > 0)
        {
          Iterator iterator = otherContainer.getMembers().entrySet().iterator();
          while (iterator.hasNext())
          {
            try
            {
              Map.Entry<String, MemberVariable> member = (Map.Entry)iterator.next();

              this.members.put(member.getKey(), this.copyOf(member.getValue()));
            }
            catch (Exception e)
            {
              LOGGER.error(e.getMessage());
            }
          }
        }
      }

      return this;
    }

    /**
     * Return a deep copy of the given MemberVariable object.
     *
     * @param master Master copy with data
     *
     * @return New MemberVariable object with copy of the data
     *
     * @throws Error while creating copy of member variable
     */
    private MemberVariable copyOf(MemberVariable master) throws Exception
    {
      MemberVariable copy = null;

      if (master.getClass() == AttributeContainer.Element.class)
      {
        AttributeContainer.Element e = (AttributeContainer.Element)master;
        copy = new AttributeContainer.Element(e.type, e.name, e.value, e.restrictions.clone());
      }
      else if (master.getClass() == AttributeContainer.ConstantElement.class)
      {
        AttributeContainer.ConstantElement c = (AttributeContainer.ConstantElement)master;
        copy = new AttributeContainer.ConstantElement(c.type, c.name, c.value);
      }
      else if (master.getClass() == AttributeContainer.Attribute.class)
      {
        AttributeContainer.Attribute a = (AttributeContainer.Attribute)master;
        copy = new AttributeContainer.Attribute(a.type, a.name, a.value, a.restrictions.clone());
      }
      else if (master.getClass() == AttributeContainer.EnumElement.class)
      {
        AttributeContainer.EnumElement ee = (AttributeContainer.EnumElement)master;
        copy = new AttributeContainer.EnumElement(ee.type, ee.name, ee.enumConstants);
      }
      else if (master.getClass() == AttributeContainer.ElementArray.class)
      {
        AttributeContainer.ElementArray ea = (AttributeContainer.ElementArray)master;
        copy = new AttributeContainer.ElementArray(ea.type, ea.name, ea.minSize, ea.maxSize);
      }
      else if (master.getClass() == AttributeContainer.ElementList.class)
      {
        AttributeContainer.ElementList el = (AttributeContainer.ElementList)master;
        copy = new AttributeContainer.ElementList(el.type, el.name, el.minSize, el.maxSize);
      }
      else
      {
        throw new FabricTypeGenException("Cannot copy member variable, because it has unknown type.");
      }

      return copy;
    }

    /**
     * Set the name of the container class.
     *
     * @param name Name of container class
     *
     * @return Builder object
     */
    public Builder setName(final String name)
    {
      this.name = name;

      return this;
    }

    /**
     * Get the name of the container class.
     *
     * @return Container name
     */
    public String getName()
    {
      return this.name;
    }

    /**
     * Set the member variables of the container.
     *
     * @param members Map of member variables
     *
     * @return Builder object
     */
    public Builder setMembers(final Map<String, MemberVariable> members)
    {
      this.members = members;

      return this;
    }

    /**
     * Get the map of member variables.
     *
     * @return Map of member variables
     */
    public Map<String, MemberVariable> getMembers()
    {
      return this.members;
    }

    /**
     * Add XML element to container. Initial value can be predefined.
     * Existing entries will be overridden by new element definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param value Initial value of member variable
     * @param restrictions Restrictions on element content
     *
     * @return Builder object
     */
    public Builder addElement(final String type, final String name, final String value, final Restriction restrictions)
    {
      this.members.put(name, new AttributeContainer.Element(type, name, value, restrictions));

      return this;
    }

    /**
     * Add XML element to container. Initial value can be predefined.
     * Existing entries will be overridden by new element definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param value Initial value of member variable
     *
     * @return Builder object
     */
    public Builder addElement(final String type, final String name, final String value)
    {
      this.members.put(name, new AttributeContainer.Element(type, name, value));

      return this;
    }

    /**
     * Add XML element to container. Existing entries will be
     * overridden by new element definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param restrictions Restrictions on element content
     *
     * @return Builder object
     */
    public Builder addElement(final String type, final String name, final Restriction restrictions)
    {
      this.members.put(name, new AttributeContainer.Element(type, name, restrictions));

      return this;
    }

    /**
     * Add XML element to container. Existing entries will be
     * overridden by new element definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     *
     * @return Builder object
     */
    public Builder addElement(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.Element(type, name));

      return this;
    }
    
    /**
     * Add constant XML element to container. The value of this
     * element is fixed and cannot be changed. Existing entries
     * will be overridden by new element definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param value Fixed value of member variable
     *
     * @return Builder object
     */
    public Builder addConstantElement(final String type, final String name, final String value)
    {
      this.members.put(name, new AttributeContainer.ConstantElement(type, name, value));

      return this;
    }

    /**
     * Add XML attribute to container. Initial value can be predefined.
     * Existing entries will be overridden by new attribute definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param value Initial value of member variable
     * @param restrictions Restrictions on attribute content
     *
     * @return Builder object
     */
    public Builder addAttribute(final String type, final String name, final String value, final Restriction restrictions)
    {
      this.members.put(name, new AttributeContainer.Attribute(type, name, value, restrictions));

      return this;
    }

    /**
     * Add XML attribute to container. Initial value can be predefined.
     * Existing entries will be overridden by new attribute definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param value Initial value of member variable
     *
     * @return Builder object
     */
    public Builder addAttribute(final String type, final String name, final String value)
    {
      // Member class Attribute cannot exist without instance of outer class AttributeContainer
      this.members.put(name, new AttributeContainer.Attribute(type, name, value));

      return this;
    }

    /**
     * Add XML attribute to container. Existing entries will be
     * overridden by new attribute definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param restrictions Restrictions on attribute content
     *
     * @return Builder object
     */
    public Builder addAttribute(final String type, final String name, final Restriction restrictions)
    {
      this.members.put(name, new AttributeContainer.Attribute(type, name, restrictions));

      return this;
    }

    /**
     * Add XML attribute to container. Existing entries will be
     * overridden by new attribute definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     *
     * @return Builder object
     */
    public Builder addAttribute(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.Attribute(type, name));

      return this;
    }

    /**
     * Add XML element and enum type to container. Existing entries will
     * be overridden by new element definitions.
     * 
     * @param type Name of XML enum type (e.g. MyEnumType)
     * @param name Name of XML enum element
     * @param enumConstants Array if enum constants
     * 
     * @return Builder object
     *
     * @throws IllegalArgumentException Enum array is empty
     */
    public Builder addEnumElement(final String type, final String name, final String[] enumConstants) throws IllegalArgumentException
    {
      if (null != enumConstants && enumConstants.length < 1)
      {
        throw new IllegalArgumentException("Enum must at least contain one element.");
      }
      
      this.members.put(name, new AttributeContainer.EnumElement(type, name, enumConstants));
      
      return this;
    }

    /**
     * Add XML element array to container. Minimum and maximum size of
     * the array can be predefined. Existing entries will be overridden
     * by new array definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param minSize Minimal size of array
     * @param maxSize Maximum size of array
     *
     * @return Builder object
     *
     * @throws IllegalArgumentException Invalid array size provided
     */
    public Builder addElementArray(final String type, final String name, final int minSize, final int maxSize) throws IllegalArgumentException
    {
      if (maxSize < 0)
      {
        throw new IllegalArgumentException("Array size must be positive.");
      }

      this.members.put(name, new AttributeContainer.ElementArray(type, name, minSize, maxSize));

      return this;
    }

    /**
     * Add XML element array to container. Maximum size of the array
     * can be predefined. Existing entries will be overridden by new
     * array definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param maxSize Maximum size of array
     *
     * @return Builder object
     */
    public Builder addElementArray(final String type, final String name, final int maxSize)
    {
      return this.addElementArray(type, name, 0, maxSize);
    }

    /**
     * Add XML element array to container. Existing entries will be
     * overridden by new array definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     *
     * @return Builder object
     */
    public Builder addElementArray(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.ElementArray(type, name));

      return this;
    }

    /**
     * Add XML element list to container. Minimum and maximum size of
     * the list can be predefined. Existing entries will be overridden
     * by new list definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param minSize Minimal size of list
     * @param maxSize Maximum size of list
     *
     * @return Builder object
     *
     * @throws IllegalArgumentException Invalid list size provided
     */
    public Builder addElementList(final String type, final String name, final int minSize, final int maxSize) throws IllegalArgumentException
    {
      if (maxSize < 0)
      {
        throw new IllegalArgumentException("List size must be positive.");
      }

      this.members.put(name, new AttributeContainer.ElementList(type, name, minSize, maxSize));

      return this;
    }

    /**
     * Add XML element list to container. Maximum size of the list
     * can be predefined. Existing entries will be overridden by new
     * list definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param maxSize Maximum size of list
     *
     * @return Builder object
     */
    public Builder addElementList(final String type, final String name, final int maxSize)
    {
      return this.addElementList(type, name, 0, maxSize);
    }

    /**
     * Add XML element list to container. Existing entries will be
     * overridden by new list definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     *
     * @return Builder object
     */
    public Builder addElementList(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.ElementList(type, name));

      return this;
    }

    /**
     * Delete member variable from container class. Member name acts
     * as a key for deletion here.
     *
     * @param name Name of the member to delete
     *
     * @return Builder object
     */
    public Builder deleteMember(final String name)
    {
      this.members.remove(name);

      return this;
    }
  }

  /*****************************************************************
   * MemberVariable internal classes
   *****************************************************************/

  public static abstract class MemberVariable
  {
    /** Type of member variable */
    public String type;

    /** Name of member variable */
    public String name;
  }

  public static abstract class ElementBase extends MemberVariable
  {
    /** Value of XML element */
    public String value;

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     * @param value Initial value of XML element
     */
    public ElementBase(final String type, final String name, final String value)
    {
      this.type = type;
      this.name = name;
      this.value = value;
    }
  }

  public static abstract class RestrictedElementBase extends ElementBase
  {
    /** Restrictions on element value */
    public Restriction restrictions;

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     * @param value Initial value of XML element
     * @param restrictions Restrictions on XML element
     */
    public RestrictedElementBase(final String type, final String name, final String value, final Restriction restrictions)
    {
      super(type, name, value);

      this.restrictions = restrictions;
    }

    /**
     * Check 'length' restriction for element.
     *
     * @return True or false
     */
    public boolean isLengthRestricted()
    {
      return (null != this.restrictions.length && Integer.parseInt(this.restrictions.length) >= 0);
    }

    /**
     * Check 'minLength' restriction for element.
     *
     * @return True or false
     */
    public boolean isMinLengthRestricted()
    {
      return (null != this.restrictions.minLength && Integer.parseInt(this.restrictions.minLength) >= 0);
    }

    /**
     * Check 'maxLength' restriction for element.
     *
     * @return True or false
     */
    public boolean isMaxLengthRestricted()
    {
      return (null != this.restrictions.maxLength && Integer.parseInt(this.restrictions.maxLength) >= 0);
    }

    /**
     * Check 'minInclusive' restriction for element.
     *
     * @return True or false
     */
    public boolean isMinInclusiveRestricted()
    {
      return (null != this.restrictions.minInclusive);
    }

    /**
     * Check 'maxInclusive' restriction for element.
     *
     * @return True or false
     */
    public boolean isMaxInclusiveRestricted()
    {
      return (null != this.restrictions.maxInclusive);
    }

    /**
     * Check 'minExclusive' restriction for element.
     *
     * @return True or false
     */
    public boolean isMinExclusiveRestricted()
    {
      return (null != this.restrictions.minExclusive);
    }

    /**
     * Check 'maxExclusive' restriction for element.
     *
     * @return True or false
     */
    public boolean isMaxExclusiveRestricted()
    {
      return (null != this.restrictions.maxExclusive);
    }

    /**
     * Check 'pattern' restriction for element.
     *
     * @return True or false
     */
    public boolean isPatternRestricted()
    {
      return (null != this.restrictions.pattern);
    }

    /**
     * Check 'whiteSpace' restriction for element.
     */
    public boolean isWhiteSpaceRestricted()
    {
      return (null != this.restrictions.whiteSpace);
    }

    /**
     * Check 'totalDigits' restriction for element.
     */
    public boolean isTotalDigitsRestricted()
    {
      return (null != this.restrictions.totalDigits && Integer.parseInt(this.restrictions.totalDigits) >= 1); // Value must be positive integer
    }

    /**
     * Check 'fractionDigits' restriction for element.
     */
    public boolean isFractionDigitsRestricted()
    {
      return (null != this.restrictions.fractionDigits && Integer.parseInt(this.restrictions.fractionDigits) >= 0);
    }
  }

  public static class Element extends RestrictedElementBase
  {
    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     * @param value Initial value of XML attribute
     * @param restrictions Restrictions on XML element
     */
    public Element(final String type, final String name, final String value, final Restriction restrictions)
    {
      super(type, name, value, restrictions);
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     * @param value Initial value of XML element
     */
    public Element(final String type, final String name, final String value)
    {
      this(type, name, value, new Restriction());
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     * @param restrictions Restrictions on XML element
     */
    public Element(final String type, final String name, final Restriction restrictions)
    {
      this(type, name, "", restrictions);
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     */
    public Element(final String type, final String name)
    {
      this(type, name, "", new Restriction());
    }
  }

  public static class ConstantElement extends ElementBase
  {    
    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     * @param value Fixed value of XML element
     */
    public ConstantElement(final String type, final String name, final String value)
    {
      super(type, name, value);
    }
  }

  public static class Attribute extends RestrictedElementBase
  {
    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     * @param value Initial value of XML attribute
     * @param restrictions Restrictions on XML element
     */
    public Attribute(final String type, final String name, final String value, final Restriction restrictions)
    {
      super(type, name, value, restrictions);
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     * @param value Initial value of XML attribute
     */
    public Attribute(final String type, final String name, final String value)
    {
      this(type, name, value, new Restriction());
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     * @param restrictions Restrictions on XML element
     */
    public Attribute(final String type, final String name, final Restriction restrictions)
    {
      this(type, name, "", restrictions);
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     */
    public Attribute(final String type, final String name)
    {
      this(type, name, "", new Restriction());
    }
  }
  
  public static class EnumElement extends MemberVariable
  { 
    /** Constants of the enumeration */
    public String[] enumConstants;
    
    /**
     * Parameterized constructor.
     * 
     * @param type Name of XML enum type (e.g. MyEnumType)
     * @param name Name of XML enum element
     * @param enumConstants Array if enum constants
     */
    public EnumElement(final String type, final String name, final String[] enumConstants)
    {
      this.type = type;
      this.name = name;
      this.enumConstants = Arrays.copyOf(enumConstants, enumConstants.length, String[].class);
    }
  }

  public static abstract class ElementCollection extends MemberVariable
  {
    /** Minimum size of XML element collection */
    public int minSize;

    /** Maximum size of XML element collection */
    public int maxSize;

  }

  public static class ElementArray extends ElementCollection
  {
    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element array
     * @param name Name of XML element array
     * @param minSize Minimum size of XML element array
     * @param maxSize Maximum size of XML element array
     */
    public ElementArray(final String type, final String name, final int minSize, final int maxSize)
    {
      this.type = type;
      this.name = name;
      this.minSize = minSize;
      this.maxSize = maxSize;
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element array
     * @param name Name of XML element array
     */
    public ElementArray(final String type, final String name)
    {
      this(type, name, 0, Integer.MAX_VALUE);
    }
  }

  public static class ElementList extends ElementCollection
  {
    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element list
     * @param name Name of XML element list
     * @param minSize Minimum size of XML element list
     * @param maxSize Maximum size of XML element list
     */
    public ElementList(final String type, final String name, final int minSize, final int maxSize)
    {
      this.type = type;
      this.name = name;
      this.minSize = minSize;
      this.maxSize = maxSize;
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element list
     * @param name Name of XML element list
     */
    public ElementList(final String type, final String name)
    {
      this(type, name, 0, Integer.MAX_VALUE);
    }
  }
  
  /*****************************************************************
   * Restriction internal class
   *****************************************************************/

  public static class Restriction
  {
    /** Length of content (e.g. amount of characters) or null */
    public String length;

    /** Minimal content length or null */
    public String minLength;

    /** Maximum content length or null */
    public String maxLength;

    /** Lower bound on element value (including boundary) or null */
    public String minInclusive;

    /** Upper bound on element value (including boundary) or null */
    public String maxInclusive;

    /** Lower bound on element value (excluding boundary) or null */
    public String minExclusive;

    /** Upper bound on element value (excluding boundary) or null */
    public String maxExclusive;

    /** Pattern of content or null */
    public String pattern;

    /** Handling of whitespaces in content or null */
    public String whiteSpace;

    /** Number of digits for numeric content */
    public String totalDigits;

    /** Number of fractional digits for numeric content  */
    public String fractionDigits;

    /**
     * Parameterless constructor. Member variable value of 'null'
     * means that the restriction is not set at all. Value of 'length',
     * 'minLength', 'maxLength', 'totalDigits' and 'fractionDigits' must
     * not be negative. The remaining boundaries can either be negative,
     * zero or positive integer values. The values of 'pattern' and
     * 'whiteSpace' are strings.
     */
    public Restriction()
    {
      this.length = null;
      this.minLength = null;
      this.maxLength = null;

      this.minInclusive = null;
      this.maxInclusive = null;
      this.minExclusive = null;
      this.maxExclusive = null;

      this.pattern = null;
      this.whiteSpace = null;

      this.totalDigits = null;
      this.fractionDigits = null;
    }

    /**
     * Parameterized constructor. Values must not be negative.
     * A value of 'null' means that the restriction is not set.
     *
     * @param length Length of element content
     * @param minLength Minimal content length
     * @param maxLength Maximal content length
     */
    public Restriction(final String length, final String minLength, final String maxLength)
    {
      this.length = length;
      this.minLength = minLength;
      this.maxLength = maxLength;
    }

    /**
     * Parameterized constructor. Values must either be negative,
     * zero or positive integers. A value of 'null' means that the
     * restriction is not set.
     *
     * @param lowerBound Lower bound on element value
     * @param upperBound Upper bound on element value
     * @param includeBound Flag to include or exclude boundary
     */
    public Restriction(final String lowerBound, final String upperBound, final boolean includeBound)
    {
      if (includeBound)
      {
        this.minInclusive = lowerBound;
        this.maxInclusive = upperBound;
      }
      else
      {
        this.minExclusive = lowerBound;
        this.maxExclusive = upperBound;
      }
    }

    /**
     * Parameterized constructor. Value of 'pattern' must be a valid
     * XML schema regular expression. Value of 'whiteSpace' must be
     * one of 'preserve', 'replace' or 'collapse'. Value of parameter
     * 'totalDigits' must be positive integer. Value of 'fractionDigits'
     * must be either zero or positive integers. A value of 'null'
     * means that the restriction is not set.
     *
     * @param pattern Pattern with XML schema regular expression
     * @param whiteSpace Handling of white space characters
     * @param totalDigits Maximum number of total digits
     * @param fractionDigits Maximum number of fraction digits
     */
    public Restriction(final String pattern, final String whiteSpace, final String totalDigits, final String fractionDigits)
    {
      this.pattern = pattern;
      this.whiteSpace = whiteSpace;
      
      this.totalDigits = totalDigits;
      this.fractionDigits = fractionDigits;
    }

    /**
     * Clone Restriction object and return a deep copy.
     *
     * @return Cloned Restriction object
     */
    @Override
    public Restriction clone()
    {
      Restriction clone = new Restriction();

      clone.length = this.length;
      clone.minLength = this.minLength;
      clone.maxLength = this.maxLength;

      clone.minInclusive = this.minInclusive;
      clone.maxInclusive = this.maxInclusive;
      clone.minExclusive = this.minExclusive;
      clone.maxExclusive = this.maxExclusive;

      clone.pattern = this.pattern;
      clone.whiteSpace = this.whiteSpace;

      clone.totalDigits = this.totalDigits;
      clone.fractionDigits = this.fractionDigits;

      return clone;
    }
  }

  /*****************************************************************
   * AttributeContainer outer class
   *****************************************************************/

  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(AttributeContainer.class);

  /** Constant for default container name */
  private static final String DEFAULT_CONTAINER_NAME = "DefaultAttributeContainer";

  /** Constant for default AttributeContainer instance */
  private static final AttributeContainer DEFAULT_INSTANCE = new AttributeContainer();

  /** Name of the container class */
  private String name;

  /** Member variables (e.g. attributes, elements or arrays) */
  private Map<String, MemberVariable> members;

  /**
   * Private constructor. Use newBuilder() instead.
   */
  private AttributeContainer()
  {
    this.initFields();
  }

  /**
   * Return default container name.
   *
   * @return Default container name
   */
  public static String getDefaultContainerName()
  {
    return AttributeContainer.DEFAULT_CONTAINER_NAME;
  }

  /**
   * Return default AttributeContainer instance.
   *
   * @return Default container instance
   */
  public static AttributeContainer getDefaultInstance()
  {
    return AttributeContainer.DEFAULT_INSTANCE;
  }

  /**
   * Private method for AttributeContainer initialization.
   */
  private void initFields()
  {
    this.name = AttributeContainer.DEFAULT_CONTAINER_NAME;
    this.members = new HashMap<String, MemberVariable>();
  }

  /**
   * Create new Builder object for AttributeContainer creation.
   *
   * @return Builder object
   */
  public static Builder newBuilder()
  {
    return Builder.create();
  }

  /**
   * Create new Builder object from prototype for AttributeContainer creation.
   *
   * @param prototype AttributeContainer object as prototype
   *
   * @return Builder object
   */
  public static Builder newBuilder(final AttributeContainer prototype)
  {
    // Create container from prototype
    return AttributeContainer.newBuilder().mergeWith(prototype);
  }

  /**
   * Convert AttributeContainer to Builder object.
   *
   * @return Builder object
   */
  public Builder toBuilder()
  {
    return AttributeContainer.newBuilder(this);
  }

  /**
   * Return content of AttributeContainer as class object. Return value
   * is of type WorkspaceElement and should be casted before further use
   * (e.g. to JClass or CppClass, depending on ClassGenerationStrategy).
   *
   * @param strategy Strategy object for external class generation
   *
   * @return Class object (as WorkspaceElement)
   *
   * @throws Exception Error during class object creation
   */
  public WorkspaceElement asClassObject(ClassGenerationStrategy strategy) throws Exception
  {
    return strategy.generateClassObject(this);
  }

  /**
   * Get the name of the container class.
   *
   * @return Container name
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Get the map of member variables.
   *
   * @return Map of member variables
   */
  public Map<String, MemberVariable> getMembers()
  {
    return this.members;
  }
}
