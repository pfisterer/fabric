/** 09.03.2012 14:11 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;

/**
 * @author seidel
 */
public class ElementMetadata
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(ElementMetadata.class);
  
  /** Mapping from Fabric type names (FInt, FString etc.) to EXI built-in type names */
  private HashMap<String, String> types = new HashMap<String, String>();
  
  public static final int XML_ATOMIC_VALUE = 0;
  public static final int XML_LIST = 1;
  public static final int XML_ARRAY = 2;
  
  private String elementName;
  private String elementType;
  private int type;
  private int exiEventCode;

  private ElementMetadata()
  {
  }

  public ElementMetadata(final FElement element)
  {
    // Initialize mapping of type names
    this.createMapping(); // TODO: Better use static initializer?

    // Set element name
    this.elementName = element.getName();

    // Set element type (boolean, int, double etc.)
    this.elementType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());

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
    return this.elementName;
  }

  public void setElementName(final String elementName)
  {
    this.elementName = elementName;
  }

  public String getElementType()
  {
    return this.elementType;
  }

  public void setElementType(final String elementType)
  {
    this.elementType = elementType;
  }

  public int getType()
  {
    return this.type;
  }

  public void setType(final int type)
  {
    this.type = type;
  }

  public int getEXIEventCode()
  {
    return this.exiEventCode;
  }

  public void setEXIEventCode(int exiEventCode)
  {
    this.exiEventCode = exiEventCode;
  }
  
  @Override
  public ElementMetadata clone()
  {
    ElementMetadata element = new ElementMetadata();
    
    element.elementName = this.elementName;
    element.elementType = this.elementType;
    element.type = this.type;
    element.exiEventCode = this.exiEventCode;
    
    return element;
  }

  /**
   * TODO: Add comment
   * 
   * @param schemaTypeName
   * @return 
   * @throws IllegalArgumentException No matching mapping found
   */
  private String getEXITypeName(final String schemaTypeName) throws IllegalArgumentException
  {
    // TODO: Output a meaningful debug message
    LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> Mapping type: " + schemaTypeName);

    if (types.containsKey(schemaTypeName))
    {
      return types.get(schemaTypeName);
    }

    throw new IllegalArgumentException(String.format("No mapping found for datatype '%s'.", schemaTypeName));
  }

  // TODO: Add comment
  private void createMapping()
  {
    // TODO: Add mapping for EXI built-in type names here:
    this.types.put("FBoolean", "Boolean");
    this.types.put("FFloat", "float");
    this.types.put("FDouble", "double");
    this.types.put("FByte", "int8");
    this.types.put("FUnsignedByte", "uint8");
    this.types.put("FShort", "int32");
    this.types.put("FUnsignedShort", "uint16");
    this.types.put("FInt", "Integer");
    this.types.put("FInteger", "Integer*");
    this.types.put("FPositiveInteger", "Integer");
    this.types.put("FUnsignedInt", "Integer");
    this.types.put("FLong", "int64");
    this.types.put("FUnsignedLong", "uint64");
    this.types.put("FDecimal", "char*");
    this.types.put("FString", "String");
    this.types.put("FHexBinary", "xsd_hexBinary_t");
    this.types.put("FBase64Binary", "xsd_base64Binary_t");
    this.types.put("FDateTime", "xsd_dateTime_t");
    this.types.put("FTime", "xsd_time_t");
    this.types.put("FDate", "xsd_date_t");
    this.types.put("FDay", "xsd_gDay_t");
    this.types.put("FMonth", "xsd_gMonth_t");
    this.types.put("FMonthDay", "xsd_gMonthDay_t");
    this.types.put("FYear", "xsd_gYear_t");
    this.types.put("FYearMonth", "xsd_gYearMonth_t");
    this.types.put("FDuration", "xsd_duration_t");
    this.types.put("FNOTATION", "xsd_notation_t");
    this.types.put("FQName", "xsd_qName_t");
    this.types.put("FName", "char*");
    this.types.put("FNCName", "char*");
    this.types.put("FNegativeInteger", "char*");
    this.types.put("FNMTOKEN", "char*");
    this.types.put("FNonNegativeInteger", "char*");
    this.types.put("FNonPositiveInteger", "char*");
    this.types.put("FNormalizedString", "char*");
    this.types.put("FToken", "char*");
    this.types.put("FAnyURI", "char*");
    this.types.put("FAny", "char*");
  }
}
