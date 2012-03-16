/** 09.03.2012 14:11 */
package fabric.module.exi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;

/**
 * This class is a container for XML element metadata. While
 * the treewalker processes an XML Schema file, information
 * about XML elements is collected in a Queue of ElementMetadata
 * objects. The data is later used to generate the serialization
 * and deserialization methods in EXIConverter class dynamically.
 * 
 * @author seidel
 */
public class ElementMetadata
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(ElementMetadata.class);
  
  /** Mapping from Fabric type names (FInt, FString etc.) to EXI built-in type names */
  private static HashMap<String, String> types = new HashMap<String, String>();  
  static
  {
    // Initialize type mapping
    ElementMetadata.createMapping();
  }
  
  /** XML element is a single value */
  public static final int XML_ATOMIC_VALUE = 0;
  
  /** XML element is a list that may contain multiple values */
  public static final int XML_LIST = 1;
  
  /** XML element is an array that may contain multiple values */
  public static final int XML_ARRAY = 2;
  
  /** Name of the XML element */
  private String elementName;
  
  /** Type of the XML element content (e.g. Boolean, Integer or String) */
  private String elementType;
  
  /** Type of the XML element (e.g. atomic value, list or array) */
  private int type;
  
  /** EXI event code within the XML Schema document structure */
  private int exiEventCode;
  
  /**
   * Parameterized constructor.
   * 
   * @param elementName XML element name
   * @param elementType XML element content type (e.g. Boolean,
   * Integer or String)
   * @param type XML element type (atomic value, list or array)
   * @param exiEventCode EXI event code
   */
  public ElementMetadata(final String elementName, final String elementType, final int type, final int exiEventCode)
  {
    this.elementName = elementName;
    this.elementType = elementType;
    this.type = type;
    this.exiEventCode = exiEventCode;
  }
  
  /**
   * Parameterized constructor creates ElementMetadata object
   * from an FElement object passed through from the Fabric
   * treewalker.
   * 
   * @param element FElement object passed through from treewalker
   */
  public ElementMetadata(final FElement element)
  {            
    // Set XML element name
    this.elementName = element.getName();

    // Set XML element type (e.g. Boolean, Integer or String)
    this.elementType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());

    // Set XML element type (e.g. atomic value, list or array)
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
  
  /**
   * Setter for XML element name.
   * 
   * @param elementName XML element name
   */
  public void setElementName(final String elementName)
  {
    this.elementName = elementName;
  }
  
  /**
   * Getter for XML element name.
   * 
   * @return XML element name
   */  
  public String getElementName()
  {
    return this.elementName;
  }
  
  /**
   * Setter for XML element type (e.g. Boolean, Integer or String).
   * 
   * @param elementType XML element type
   */
  public void setElementType(final String elementType)
  {
    this.elementType = elementType;
  }
  
  /**
   * Getter for XML element type.
   * 
   * @return XML element type
   */
  public String getElementType()
  {
    return this.elementType;
  }
  
  /**
   * Setter for XML element type (e.g. atomic value, list or array).
   * 
   * @param type XML element type
   */
  public void setType(final int type)
  {
    this.type = type;
  }
  
  /**
   * Getter for XML element type.
   * 
   * @return XML element type
   */
  public int getType()
  {
    return this.type;
  }
  
  /**
   * Setter for EXI event code.
   * 
   * @param exiEventCode EXI event code
   */
  public void setEXIEventCode(int exiEventCode)
  {
    this.exiEventCode = exiEventCode;
  }
  
  /**
   * Getter for EXI event code.
   * 
   * @return EXI event code
   */
  public int getEXIEventCode()
  {
    return this.exiEventCode;
  }
  
  /**
   * Clone ElementMetadata object and return a deep copy.
   * 
   * @return Cloned ElementMetadata object
   */
  @Override
  public ElementMetadata clone()
  {
    return new ElementMetadata(this.elementName, this.elementType, this.type, this.exiEventCode);
  }
  
  /**
   * Private helper method to get the EXI built-in type name
   * (e.g. Boolean, Integer or String) for one of Fabric's
   * XML Schema type names (e.g. FBoolean, FInt or FString).
   * 
   * @param schemaTypeName Fabric type name
   * 
   * @return EXI built-in type name
   * 
   * @throws IllegalArgumentException No matching mapping found
   */
  private String getEXITypeName(final String schemaTypeName) throws IllegalArgumentException
  {
    // Return mapping if available
    if (types.containsKey(schemaTypeName))
    {
      LOGGER.debug(String.format("Mapped Fabric data type '%s' to EXI built-in type '%s'.", schemaTypeName, types.get(schemaTypeName)));

      return types.get(schemaTypeName);
    }

    throw new IllegalArgumentException(String.format("No mapping found for datatype '%s'.", schemaTypeName));
  }
  
  /**
   * Private helper method to populate the mapping of Fabric's
   * XML Schema type names to EXI built-in type names.
   */
  private static void createMapping()
  {
    // TODO: Insert all EXI type names here (right parameter in each line)
    ElementMetadata.types.put("FBoolean", "Boolean");
    ElementMetadata.types.put("FFloat", "float");
    ElementMetadata.types.put("FDouble", "double");
    ElementMetadata.types.put("FByte", "int8");
    ElementMetadata.types.put("FUnsignedByte", "uint8");
    ElementMetadata.types.put("FShort", "int32");
    ElementMetadata.types.put("FUnsignedShort", "uint16");
    ElementMetadata.types.put("FInt", "Integer");
    ElementMetadata.types.put("FInteger", "Integer*");
    ElementMetadata.types.put("FPositiveInteger", "Integer");
    ElementMetadata.types.put("FUnsignedInt", "Integer");
    ElementMetadata.types.put("FLong", "int64");
    ElementMetadata.types.put("FUnsignedLong", "uint64");
    ElementMetadata.types.put("FDecimal", "char*");
    ElementMetadata.types.put("FString", "String");
    ElementMetadata.types.put("FHexBinary", "xsd_hexBinary_t");
    ElementMetadata.types.put("FBase64Binary", "xsd_base64Binary_t");
    ElementMetadata.types.put("FDateTime", "xsd_dateTime_t");
    ElementMetadata.types.put("FTime", "xsd_time_t");
    ElementMetadata.types.put("FDate", "xsd_date_t");
    ElementMetadata.types.put("FDay", "xsd_gDay_t");
    ElementMetadata.types.put("FMonth", "xsd_gMonth_t");
    ElementMetadata.types.put("FMonthDay", "xsd_gMonthDay_t");
    ElementMetadata.types.put("FYear", "xsd_gYear_t");
    ElementMetadata.types.put("FYearMonth", "xsd_gYearMonth_t");
    ElementMetadata.types.put("FDuration", "xsd_duration_t");
    ElementMetadata.types.put("FNOTATION", "xsd_notation_t");
    ElementMetadata.types.put("FQName", "xsd_qName_t");
    ElementMetadata.types.put("FName", "char*");
    ElementMetadata.types.put("FNCName", "char*");
    ElementMetadata.types.put("FNegativeInteger", "char*");
    ElementMetadata.types.put("FNMTOKEN", "char*");
    ElementMetadata.types.put("FNonNegativeInteger", "char*");
    ElementMetadata.types.put("FNonPositiveInteger", "char*");
    ElementMetadata.types.put("FNormalizedString", "char*");
    ElementMetadata.types.put("FToken", "char*");
    ElementMetadata.types.put("FAnyURI", "char*");
    ElementMetadata.types.put("FAny", "char*");
  }
}
