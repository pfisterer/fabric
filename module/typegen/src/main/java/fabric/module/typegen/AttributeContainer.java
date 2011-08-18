package fabric.module.typegen;

import fabric.module.typegen.base.ClassGenerationStrategy;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import de.uniluebeck.sourcegen.WorkspaceElement;

/**
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
 * @author seidel
 */
public class AttributeContainer
{ 
  /*****************************************************************
   * Builder inner class
   *****************************************************************/

  public static final class Builder
  {
    private String name;

    private Map<String, MemberVariable> members;

    /**
     * Private constructor. Use create() instead.
     */
    private Builder()
    {
      this.reset();
    }

    private static Builder create()
    {
      return new Builder();
    }

    private void reset()
    {
      this.name = AttributeContainer.DEFAULT_CONTAINER_NAME;
      this.members = new HashMap<String, MemberVariable>();
    }

    public Builder clear()
    {
      this.reset();

      return this;
    }

    @Override
    public Builder clone()
    {
      return Builder.create().mergeWith(this.build());
    }

    public AttributeContainer build()
    {
      AttributeContainer result = new AttributeContainer();

      result.name = this.name;

      Iterator iterator = this.members.entrySet().iterator();
      while (iterator.hasNext())
      {
        Map.Entry<String, MemberVariable> member = (Map.Entry)iterator.next();

        result.members.put(member.getKey(), member.getValue());
      }

      return result;
    }

    public Builder mergeWith(final AttributeContainer otherContainer)
    {
      if (otherContainer == AttributeContainer.DEFAULT_INSTANCE)
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

            this.members.put(member.getKey(), member.getValue());
          }
        }
      }

      return this;
    }

    public Builder setName(final String name)
    {
      this.name = name;

      return this;
    }

    public String getName()
    {
      return this.name;
    }

    public Builder setMembers(final Map<String, MemberVariable> members)
    {
      this.members = members;

      return this;
    }

    public Map<String, MemberVariable> getMembers()
    {
      return this.members;
    }

    /**
     * Existing entries will be overridden by new element definition.
     */
    public Builder addAttribute(final String type, final String name, final String value)
    {
      // Member class Attribute cannot exist without instance of outer class AttributeContainer
      this.members.put(name, (new AttributeContainer()).new Attribute(type, name, value));

      return this;
    }

    public Builder addAttribute(final String type, final String name)
    {
      this.members.put(name, (new AttributeContainer()).new Attribute(type, name));

      return this;
    }

    public Builder addElement(final String type, final String name, final String value)
    {
      this.members.put(name, (new AttributeContainer()).new Element(type, name, value));

      return this;
    }
    
    public Builder addElement(final String type, final String name)
    {
      this.members.put(name, (new AttributeContainer()).new Element(type, name));

      return this;
    }

    public Builder addElementArray(final String type, final String name)
    {
      this.members.put(name, (new AttributeContainer()).new ElementArray(type, name));
      
      return this;
    }

    public Builder addElementArray(final String type, final String name, final int size)
    {
      if (size < 0)
      {
        throw new IllegalArgumentException("Array size should be positive.");
      }

      this.members.put(name, (new AttributeContainer()).new ElementArray(type, name, size));

      return this;
    }

    public Builder deleteMember(final String name)
    {
      this.members.remove(name);

      return this;
    }
  }

  /*****************************************************************
   * MemberVariable internal classes
   *****************************************************************/

  public abstract class MemberVariable
  {
    public String type;
    public String name;
  }

  public class Attribute extends MemberVariable
  {
    public String value;

    public Attribute(final String type, final String name, final String value)
    {
      this.type = type;
      this.name = name;
      this.value = value;
    }

    public Attribute(final String type, final String name)
    {
      this(type, name, "");
    }
  }

  public class Element extends MemberVariable
  {
    public String value;

    public Element(final String type, final String name, final String value)
    {
      this.type = type;
      this.name = name;
      this.value = value;
    }

    public Element(final String type, final String name)
    {
      this(type, name, "");
    }
  }

  public class ElementArray extends MemberVariable
  {
    public int size;

    public ElementArray(final String type, final String name, final int size)
    {
      this.type = type;
      this.name = name;
      this.size = size;
    }

    public ElementArray(final String type, final String name)
    {
      this(type, name, Integer.MAX_VALUE);
    }    
  }

  /*****************************************************************
   * AttributeContainer outer class
   *****************************************************************/

  private static final String DEFAULT_CONTAINER_NAME = "DefaultAttributeContainer";

  private static final AttributeContainer DEFAULT_INSTANCE = new AttributeContainer();

  private String name;

  private Map<String, MemberVariable> members;

  private AttributeContainer()
  {
    this.initFields();
  }

  public static String getDefaultContainerName()
  {
    return AttributeContainer.DEFAULT_CONTAINER_NAME;
  }

  public static AttributeContainer getDefaultInstance()
  {
    return AttributeContainer.DEFAULT_INSTANCE;
  }

  private void initFields()
  {
    this.name = AttributeContainer.DEFAULT_CONTAINER_NAME;
    this.members = new HashMap<String, MemberVariable>();
  }

  public static Builder newBuilder()
  {
    return Builder.create();
  }

  public static Builder newBuilder(final AttributeContainer prototype)
  {
    // Create container from prototype
    return AttributeContainer.newBuilder().mergeWith(prototype);
  }

  public Builder toBuilder()
  {
    return AttributeContainer.newBuilder(this);
  }

  // Return container content as JClass or CppClass
  public WorkspaceElement asClassObject(ClassGenerationStrategy strategy) throws Exception
  {
    return strategy.generateClassObject(this);
  }  

  public String getName()
  {
    return this.name;
  }

  public Map<String, MemberVariable> getMembers()
  {
    return this.members;
  }
}
