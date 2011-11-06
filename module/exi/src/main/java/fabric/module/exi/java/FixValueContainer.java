/** 06.11.2011 17:46 */
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
  }
}
