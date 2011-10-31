package fabric.module.exi.java;

/**
 * @author seidel
 */
abstract public class FixValueContainer
{
  // TODO: Implement and add comments to these classes
  // TODO: List of XML elements
  public static class ElementData
  {
    private String name;

    public ElementData(final String name)
    {
      this.name = name;
    }

    public void setName(String name)
    {
      this.name = name;
    }

    public String getName()
    {
      return name;
    }
  }

  // TODO: List of XML element arrays
  public static class ArrayData
  {
    private String arrayName;

    private String arrayType;

    private String itemName;

    private String itemType;

    public ArrayData(final String name, final String type, final String itemName, final String itemType)
    {
      this.arrayName = name;
      this.arrayType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }

    public void setArrayName(String arrayName)
    {
      this.arrayName = arrayName;
    }

    public String getArrayName()
    {
      return arrayName;
    }

    public void setArrayType(String arrayType)
    {
      this.arrayType = arrayType;
    }

    public String getArrayType()
    {
      return arrayType;
    }

    public void setItemName(String itemName)
    {
      this.itemName = itemName;
    }

    public String getItemName()
    {
      return itemName;
    }

    public void setItemType(String itemType)
    {
      this.itemType = itemType;
    }

    public String getItemType()
    {
      return itemType;
    }
  }

  private static class ListData
  {
    protected String listName;

    protected String listType;

    protected String itemName;

    protected String itemType;

    public void setItemName(String itemName)
    {
      this.itemName = itemName;
    }

    public String getItemName()
    {
      return itemName;
    }

    public void setItemType(String itemType)
    {
      this.itemType = itemType;
    }

    public String getItemType()
    {
      return itemType;
    }

    public void setListName(String listName)
    {
      this.listName = listName;
    }

    public String getListName()
    {
      return listName;
    }

    public void setListType(String listType)
    {
      this.listType = listType;
    }

    public String getListType()
    {
      return listType;
    }
  }

  // TODO: List of simple-typed XML lists
  public static class SimpleListData extends ListData
  {
    public SimpleListData(final String name, final String type, final String itemName, final String itemType)
    {
      this.listName = name;
      this.listType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }
  }

  // TODO: List of non-simple-typed XML lists (item type has its own class, e.g. MyString)
  public static class NonSimpleListData extends ListData
  {
    public NonSimpleListData(final String name, final String type, final String itemName, final String itemType)
    {
      this.listName = name;
      this.listType = type;
      this.itemName = itemName;
      this.itemType = itemType;
    }
  }
}
