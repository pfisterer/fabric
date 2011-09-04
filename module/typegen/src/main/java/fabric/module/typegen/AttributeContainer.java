/** 02.09.2011 01:31:25 */
package fabric.module.typegen;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.module.typegen.base.ClassGenerationStrategy;

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
 *                             .addElement("int", "maxSpeed", "220")
 *                             .addElementArray("String", "trunkItems")
 *                             .addElementArray("String", "passengers", 2)
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

    /** Member variables (e.g. attributes, elements or arrays) */
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
        Map.Entry<String, MemberVariable> member = (Map.Entry)iterator.next();

        result.members.put(member.getKey(), this.copyOf(member.getValue()));
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
            Map.Entry<String, MemberVariable> member = (Map.Entry)iterator.next();

            this.members.put(member.getKey(), this.copyOf(member.getValue()));
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
     */
    private MemberVariable copyOf(MemberVariable master)
    {
      MemberVariable copy = null;

      if (master instanceof AttributeContainer.Attribute)
      {
        AttributeContainer.Attribute a = (AttributeContainer.Attribute)master;
        copy = new AttributeContainer.Attribute(a.type, a.name, a.value);
      }
      else if (master instanceof AttributeContainer.Element)
      {
        AttributeContainer.Element e = (AttributeContainer.Element)master;
        copy = new AttributeContainer.Element(e.type, e.name, e.value);
      }
      else if (master instanceof AttributeContainer.ElementArray)
      {
        AttributeContainer.ElementArray ea = (AttributeContainer.ElementArray)master;
        copy = new AttributeContainer.ElementArray(ea.type, ea.name, ea.size);
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
     *
     * @return Builder object
     */
    public Builder addAttribute(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.Attribute(type, name));

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
     *
     * @return Builder object
     */
    public Builder addElement(final String type, final String name)
    {
      this.members.put(name, new AttributeContainer.Element(type, name));

      return this;
    }

    /**
     * Add XML element array to container. Size of the array can be
     * predefined. Existing entries will be overridden by new array
     * definition.
     *
     * @param type Type of member variable
     * @param name Name of member variable
     * @param size Size of array
     *
     * @return Builder object
     *
     * @throws IllegalArgumentException Invalid array size provided
     */
    public Builder addElementArray(final String type, final String name, final int size) throws IllegalArgumentException
    {
      if (size < 0)
      {
        throw new IllegalArgumentException("Array size must be positive.");
      }

      this.members.put(name, new AttributeContainer.ElementArray(type, name, size));

      return this;
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

  public static class Attribute extends MemberVariable
  {
    /** Value of XML attribute */
    public String value;

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     * @param value Initial value of XML attribute
     */
    public Attribute(final String type, final String name, final String value)
    {
      this.type = type;
      this.name = name;
      this.value = value;
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML attribute
     * @param name Name of XML attribute
     */
    public Attribute(final String type, final String name)
    {
      this(type, name, "");
    }
  }

  public static class Element extends MemberVariable
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
    public Element(final String type, final String name, final String value)
    {
      this.type = type;
      this.name = name;
      this.value = value;
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element
     * @param name Name of XML element
     */
    public Element(final String type, final String name)
    {
      this(type, name, "");
    }
  }

  public static class ElementArray extends MemberVariable
  {
    /** Size of XML element array */
    public int size;

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element array
     * @param name Name of XML element array
     * @param size of XML element array
     */
    public ElementArray(final String type, final String name, final int size)
    {
      this.type = type;
      this.name = name;
      this.size = size;
    }

    /**
     * Parameterized constructor.
     *
     * @param type Type of XML element array
     * @param name Name of XML element array
     */
    public ElementArray(final String type, final String name)
    {
      this(type, name, Integer.MAX_VALUE);
    }
  }

  /*****************************************************************
   * AttributeContainer outer class
   *****************************************************************/

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
