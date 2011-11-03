/** 03.11.2011 13:49 */
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
     * @param name  Name of the XML element
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
    public void setName(String name)
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
    /** Name of the XML array */
    private String arrayName;
    
    /** Type of the XML array */
    private String arrayType;
    
    /** Name of the array items */
    private String itemName;
    
    /** Type of the array items (usually equal array type) */
    private String itemType;
    
    /**
     * Parameterized constructor.
     * 
     * @param name Name of the XML array
     * @param type Type of the XML array
     * @param itemName Name of the array items
     * @param itemType Type of the array items (usually equal to array type)
     */
    public ArrayData(final String name, final String type, final String itemName, final String itemType)
    {
      this.arrayName = name;
      this.arrayType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }
    
    /**
     * Set name of the XML array.
     * 
     * @param arrayName Name of the XML array
     */
    public void setArrayName(String arrayName)
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
    public void setArrayType(String arrayType)
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
    public void setItemName(String itemName)
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
    public void setItemType(String itemType)
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
  }
  
  /*****************************************************************
   * ListData inner class
   *****************************************************************/
  
  private abstract static class ListData
  {
    /** Name of the XML list */
    protected String listName;
    
    /** Type of the XML list */
    protected String listType;
    
    /** Name of the list items */
    protected String itemName; // TODO: Do we need this? Lists don't have item tags.
    
    /** Type of the list items */
    protected String itemType;
    
    /**
     * Set name of the XML list.
     * 
     * @param itemName Name of the XML list
     */
    public void setListName(String listName)
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
    public void setListType(String listType)
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
     * Set name of the list items.
     * 
     * @param itemName Name of the list items
     */
    public void setItemName(String itemName)
    {
      this.itemName = itemName;
    }
    
    /**
     * Get name of the list items.
     * 
     * @return Name of the list items
     */
    public String getItemName()
    {
      return itemName;
    }
    
    /**
     * Set type of the list items.
     * 
     * @param itemType Name of the list items
     */
    public void setItemType(String itemType)
    {
      this.itemType = itemType;
    }
    
    /**
     * Get name of the list items.
     * 
     * @return Name of the list items
     */
    public String getItemType()
    {
      return itemType;
    }
  }

  /*****************************************************************
   * SimpleListData inner class
   *****************************************************************/
  
  // XML list with simple typed elements (e.g. xs:short or xs:string)
  public static class SimpleListData extends ListData
  {
    /**
     * Parameterized constructor.
     * 
     * @param name Name of the XML list
     * @param type Type of the XML list
     * @param itemName Name of the list items
     * @param itemType Type of the list items
     */
    public SimpleListData(final String name, final String type, final String itemName, final String itemType)
    {
      this.listName = name;
      this.listType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }
  }

  /*****************************************************************
   * NonSimpleListData inner class
   *****************************************************************/
  
  // XML list with non-simple typed elements (item type has its own class, e.g. MyString)
  public static class NonSimpleListData extends ListData
  {
    /**
     * Parameterized constructor.
     * 
     * @param name Name of the XML list
     * @param type Type of the XML list
     * @param itemName Name of the list items
     * @param itemType Type of the list items
     */
    public NonSimpleListData(final String name, final String type, final String itemName, final String itemType)
    {
      this.listName = name;
      this.listType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }
  }
}
