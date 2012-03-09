/** 09.03.2012 14:11 */
package fabric.module.exi;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;

/**
 * @author seidel
 */
public class ElementMetadata
{
  private String elementName;
  private String elementType;
  private int type;
  private int exiEventCode;
  
  public static final int XML_ATOMIC_VALUE = 0;
  public static final int XML_LIST = 1;
  public static final int XML_ARRAY = 2;
  
  public ElementMetadata(final String elementName, final String elementType, final int type, final int exiEventCode)
  {
    this.elementName = elementName;
    this.elementType = elementType;
    this.type = type;
    this.exiEventCode = 0;
  }
  
  public ElementMetadata(final FElement element)
  {
    // Set element name
    this.elementName = element. getName();
    
    // Set element type (boolean, int, double etc.)
    this.elementType = this.getEXITypeName(element.getSchemaType());
    
    // Set element type (atomic value, list or array)
    if (FSchemaTypeHelper.isList(element))
    {
      this.type = ElementMetadata.XML_LIST;
    }
    else if (FSchemaTypeHelper.isArray(element))
    {
      this.type = ElementMetadata.XML_ARRAY;
    }
    else
    {
      this.type = ElementMetadata.XML_ATOMIC_VALUE;
    }
    
    // Set EXI event code
    this.exiEventCode = 0;
  }

  public String getElementName()
  {
    return elementName;
  }

  public void setElementName(final String elementName)
  {
    this.elementName = elementName;
  }

  public String getElementType()
  {
    return elementType;
  }

  public void setElementType(final String elementType)
  {
    this.elementType = elementType;
  }

  public int getType()
  {
    return type;
  }

  public void setType(final int type)
  {
    this.type = type;
  }

  public int getEXIEventCode()
  {
    return exiEventCode;
  }

  public void setEXIEventCode(int exiEventCode)
  {
    this.exiEventCode = exiEventCode;
  }

  private String getEXITypeName(final FSchemaType schemaTypeName)
  {
    // TODO: Map Fabric's XSD type names (FInt, FBoolean etc.) to EXI type names
    
    return null; // TODO: Remove
  }
}
