/** 08.11.2011 14:53 */
package fabric.module.exi.java;

/**
 * The FixValueContainer class wraps a couple of static inner classes
 * that are being used for fixing the value-tag problem in generated
 * XML documents. The inner classes hold information about elements,
 * arrays and lists, which need further treatment later on.
 * 
 * @author seidel
 */
abstract public class FixValueContainer
{  
  /*****************************************************************
   * ElementData inner class
   *****************************************************************/
  
  public static class ElementData
  {
    /** Name of the XML element */
    private String name;

    /**
     * Parameterized constructor.
     * 
     * @param name Name of the XML element
     */
    public ElementData(final String name)
    {
      this.name = name;
    }

    /**
     * Set name of the XML element.
     * 
     * @param name Name of the XML element
     */
    public void setName(final String name)
    {
      this.name = name;
    }

    /**
     * Get name of the XML element.
     * 
     * @return Name of the XML element
     */
    public String getName()
    {
      return name;
    }

    /**
     * Method was overridden to allow equality check on two ElementData
     * objects. Equality is based on the 'name' member variable, because
     * element names should be unique.
     * 
     * @param object ElementData object for comparision
     * 
     * @return True on equality, false otherwise
     */
    @Override
    public boolean equals(Object object)
    {
      // Objects can never be equal to null
      if (null == object)
      {
        return false;
      }
      
      // Early exit, if objects are identical
      if (object == this)
      {
        return true;
      }
      
      // Exit, if object types do not match
      if (!object.getClass().equals(this.getClass()))
      {
        return false;
      }
      
      // Now we can be sure to have an ElementData object
      ElementData otherElement = (ElementData)object;

      // Check equality based on element name
      return otherElement.name.equals(this.name);
    }

    /**
     * Hash code method for equality checking.
     * 
     * @return Hash code of ElementData object
     */
    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
      
      return hash;
    }
  }

  /*****************************************************************
   * ArrayData inner class
   *****************************************************************/
  
  public static class ArrayData
  {
    /** Name of the container class in which array resides */
    private String parentContainerName;

    /** Name of the XML array */
    private String arrayName;

    /** Type of the XML array */
    private String arrayType;

    /** Name of the array items */
    private String itemName;

    /** Type of the array items (usually equal to array type) */
    private String itemType;
    
    /** Flag to tell whether array is custom-typed or not */
    private boolean isCustomTyped;

    /**
     * Parameterized constructor.
     *
     * @param parentContainerName Name of the container class in which array resides
     * @param name Name of the XML array
     * @param type Type of the XML array
     * @param itemName Name of the array items
     * @param itemType Type of the array items (usually equal to array type)
     * @param isCustomTyped Flag to tell whether array is custom-typed
     */
    public ArrayData(final String parentContainerName, final String name, final String type,
            final String itemName, final String itemType, final boolean isCustomTyped)
    {
      this.parentContainerName = parentContainerName;
      this.arrayName = name;
      this.arrayType = type;
      this.itemName = itemName;
      this.itemType = itemType;
      this.isCustomTyped = isCustomTyped;
    }

    /**
     * Set name of the parent container class in which array resides.
     *
     * @param parentContainerName Name of parent container class
     */
    public void setParentContainerName(final String parentContainerName)
    {
      this.parentContainerName = parentContainerName;
    }

    /**
     * Get name of the parent container class in which array resides.
     *
     * @return Name of parent container class
     */
    public String getParentContainerName()
    {
      return parentContainerName;
    }

    /**
     * Set name of the XML array.
     * 
     * @param arrayName Name of the XML array
     */
    public void setArrayName(final String arrayName)
    {
      this.arrayName = arrayName;
    }

    /**
     * Get name of the XML array.
     * 
     * @return Name of the XML array
     */
    public String getArrayName()
    {
      return arrayName;
    }

    /**
     * Set type of the XML array.
     * 
     * @param arrayType Type of the XML array
     */
    public void setArrayType(final String arrayType)
    {
      this.arrayType = arrayType;
    }

    /**
     * Get type of the XML array.
     * 
     * @return Type of the XML array
     */
    public String getArrayType()
    {
      return arrayType;
    }

    /**
     * Set name of the array items.
     * 
     * @param itemName Name of the array items
     */
    public void setItemName(final String itemName)
    {
      this.itemName = itemName;
    }

    /**
     * Get name of the array items.
     * 
     * @return Name of the array items
     */
    public String getItemName()
    {
      return itemName;
    }

    /**
     * Set type of the array items (usually equal to array type).
     * 
     * @param itemType Type of the array items
     */
    public void setItemType(final String itemType)
    {
      this.itemType = itemType;
    }

    /**
     * Get type of the array items.
     * 
     * @return Type of the array items
     */
    public String getItemType()
    {
      return itemType;
    }

    /**
     * Set flag to tell whether array is custom-typed or not.
     * 
     * @param isCustomTyped True if custom-typed, false otherwise
     */
    public void setIsCustomTyped(boolean isCustomTyped)
    {
      this.isCustomTyped = isCustomTyped;
    }

    /**
     * Check whether array is custom-typed or not.
     *
     * @return True if custom-typed, false otherwise
     */
    public boolean isCustomTyped()
    {
      return this.isCustomTyped;
    }

    /**
     * Method was overridden to allow equality check on two ArrayData
     * objects. Equality is based on the 'arrayName' member variable,
     * because array names should be unique.
     * 
     * @param object ArrayData object for comparision
     * 
     * @return True on equality, false otherwise
     */
    @Override
    public boolean equals(Object object)
    {
      // Objects can never be equal to null
      if (null == object)
      {
        return false;
      }
      
      // Early exit, if objects are identical
      if (object == this)
      {
        return true;
      }
      
      // Exit, if object types do not match
      if (!object.getClass().equals(this.getClass()))
      {
        return false;
      }
      
      // Now we can be sure to have an ArrayData object
      ArrayData otherArray = (ArrayData)object;

      // Check equality based on element name
      return otherArray.arrayName.equals(this.arrayName);
    }

    /**
     * Hash code method for equality checking.
     * 
     * @return Hash code of ArrayData object
     */
    @Override
    public int hashCode()
    {
      int hash = 5;
      hash = 23 * hash + (this.arrayName != null ? this.arrayName.hashCode() : 0);
      
      return hash;
    }
  }

  /*****************************************************************
   * ListData inner class
   *****************************************************************/
  
  public static class ListData
  {
    /** Name of the XML list */
    protected String listName;

    /** Type of the XML list */
    protected String listType;

    /** Type of the list items */
    protected String itemType;
    
    /** Flag to tell whether list is custom-typed or not */
    private boolean isCustomTyped;

    /**
     * Parameterized constructor.
     *
     * @param name Name of the XML list
     * @param type Type of the XML list
     * @param itemType Type of the list items
     * @param isCustomTyped Flag to tell whether list is custom-typed
     */
    public ListData(final String name, final String type, final String itemType, final boolean isCustomTyped)
    {
      this.listName = name;
      this.listType = type;
      this.itemType = itemType;
      this.isCustomTyped = isCustomTyped;
    }

    /**
     * Set name of the XML list.
     * 
     * @param itemName Name of the XML list
     */
    public void setListName(final String listName)
    {
      this.listName = listName;
    }

    /**
     * Get name of the XML list.
     * 
     * @return Name of the XML list
     */
    public String getListName()
    {
      return listName;
    }

    /**
     * Set type of the XML list.
     * 
     * @param listType Type of the XML list
     */
    public void setListType(final String listType)
    {
      this.listType = listType;
    }

    /**
     * Get type of the XML list.
     * 
     * @return Type of the XML list
     */
    public String getListType()
    {
      return listType;
    }

    /**
     * Set type of the list items.
     * 
     * @param itemType Type of the list items
     */
    public void setItemType(final String itemType)
    {
      this.itemType = itemType;
    }

    /**
     * Get type of the list items.
     * 
     * @return Type of the list items
     */
    public String getItemType()
    {
      return itemType;
    }

    /**
     * Set flag to tell whether list is custom-typed or not.
     * 
     * @param isCustomTyped True if custom-typed, false otherwise
     */
    public void setIsCustomTyped(boolean isCustomTyped)
    {
      this.isCustomTyped = isCustomTyped;
    }

    /**
     * Check whether list is custom-typed or not.
     * 
     * @return True if custom-typed, false otherwise
     */
    public boolean isCustomTyped()
    {
      return this.isCustomTyped;
    }

    /**
     * Method was overridden to allow equality check on two ListData
     * objects. Equality is based on the 'listName' member variable,
     * because list names should be unique.
     * 
     * @param object ListData object for comparision
     * 
     * @return True on equality, false otherwise
     */
    @Override
    public boolean equals(Object object)
    {
      // Objects can never be equal to null
      if (null == object)
      {
        return false;
      }
      
      // Early exit, if objects are identical
      if (object == this)
      {
        return true;
      }
      
      // Exit, if object types do not match
      if (!object.getClass().equals(this.getClass()))
      {
        return false;
      }
      
      // Now we can be sure to have an ListData object
      ListData otherList = (ListData)object;

      // Check equality based on element name
      return otherList.listName.equals(this.listName);
    }

    /**
     * Hash code method for equality checking.
     * 
     * @return Hash code of ListData object
     */
    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 53 * hash + (this.listName != null ? this.listName.hashCode() : 0);
      
      return hash;
    }
  }
}
