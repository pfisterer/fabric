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
  private static HashMap<String, String> typesFabricToEXI = new HashMap<String, String>();

  /** Mapping from EXI built-in type names to C++ type names */
  private static HashMap<String, String> typesEXIToCpp = new HashMap<String, String>();

  static
  {
    // Initialize type mapping
    ElementMetadata.createMappingFabricToEXI();
    ElementMetadata.createMappingEXIToCpp();
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
  private String elementEXIType;

  private String elementCppType;
  
  /** Type of the XML element (e.g. atomic value, list or array) */
  private int type;
  
  /** EXI event code within the XML Schema document structure */
  private int exiEventCode;
  
  /**
   * Parameterized constructor.
   * 
   * @param elementName XML element name
   * @param elementEXIType XML element content type for EXI (e.g. Boolean,
   * Integer or String)
   * @param elementCppType XML element content type for C++ (e.g. bool,
   * int or char*)
   * @param type XML element type (atomic value, list or array)
   * @param exiEventCode EXI event code
   */
  public ElementMetadata(final String elementName, final String elementEXIType, final String elementCppType,
                         final int type, final int exiEventCode)
  {
    this.elementName = elementName;
    this.elementEXIType = elementEXIType;
    this.elementCppType = elementCppType;
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
    this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());

    // Set element C++ type (bool, int, float etc.)
    this.elementCppType = this.getCppTypeName(elementEXIType);

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

  public String getElementEXIType()
  {
    return this.elementEXIType;
  }

  public void setElementEXIType(final String elementEXIType)
  {
    this.elementEXIType = elementEXIType;
  }


  public String getElementCppType()
  {
    return this.elementCppType;
  }

  public void setElementCppType(final String elementCppType) {
      this.elementCppType = elementCppType;
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
    return new ElementMetadata(this.elementName, this.elementEXIType, this.elementCppType, this.type, this.exiEventCode);
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
    if (typesFabricToEXI.containsKey(schemaTypeName))
    {
      LOGGER.debug(String.format("Mapped Fabric data type '%s' to EXI built-in type '%s'.", schemaTypeName, typesFabricToEXI.get(schemaTypeName)));

      return typesFabricToEXI.get(schemaTypeName);
    }

    throw new IllegalArgumentException(String.format("No mapping found for XML datatype '%s'.", schemaTypeName));
  }

    /**
     * TODO: Add comment
     *
     * @param exiTypeName
     * @return
     * @throws IllegalArgumentException No matching mapping found
     */
    private String getCppTypeName(final String exiTypeName) throws IllegalArgumentException
    {
        if (typesEXIToCpp.containsKey(exiTypeName))
        {
            return typesEXIToCpp.get(exiTypeName);
        }

        throw new IllegalArgumentException(String.format("No mapping found for EXI datatype '%s'.", exiTypeName));
    }

    /**
     * Private helper method to populate the mapping of Fabric's
     * XML Schema type names to EXI built-in type names.
     */
  private static void createMappingFabricToEXI()
  {
    typesFabricToEXI.put("FBoolean", "Boolean");
    typesFabricToEXI.put("FFloat", "Float");
    typesFabricToEXI.put("FDouble", "Float");
    typesFabricToEXI.put("FByte", "NBitUnsignedInteger");
    typesFabricToEXI.put("FUnsignedByte", "NBitUnsignedInteger");
    typesFabricToEXI.put("FShort", "Integer");
    typesFabricToEXI.put("FUnsignedShort", "Integer");
    typesFabricToEXI.put("FInt", "Integer");
    typesFabricToEXI.put("FInteger", "Integer");
    typesFabricToEXI.put("FPositiveInteger", "UnsignedInteger");
    typesFabricToEXI.put("FUnsignedInt", "UnsignedInteger");
    typesFabricToEXI.put("FLong", "Integer");
    typesFabricToEXI.put("FUnsignedLong", "UnsignedInteger");
    typesFabricToEXI.put("FDecimal", "Decimal");
    typesFabricToEXI.put("FString", "String");
    typesFabricToEXI.put("FHexBinary", "Binary");
    typesFabricToEXI.put("FBase64Binary", "Binary");
    typesFabricToEXI.put("FDateTime", "DateTime");
    typesFabricToEXI.put("FTime", "DateTime");
    typesFabricToEXI.put("FDate", "DateTime");
    typesFabricToEXI.put("FDay", "DateTime");
    typesFabricToEXI.put("FMonth", "DateTime");
    typesFabricToEXI.put("FMonthDay", "DateTime");
    typesFabricToEXI.put("FYear", "DateTime");
    typesFabricToEXI.put("FYearMonth", "DateTime");
    typesFabricToEXI.put("FDuration", "String");
    typesFabricToEXI.put("FNOTATION", "String");
    typesFabricToEXI.put("FQName", "String");
    typesFabricToEXI.put("FName", "String");
    typesFabricToEXI.put("FNCName", "String");
    typesFabricToEXI.put("FNegativeInteger", "Integer");
    typesFabricToEXI.put("FNMTOKEN", "String");
    typesFabricToEXI.put("FNonNegativeInteger", "UnsignedInteger");
    typesFabricToEXI.put("FNonPositiveInteger", "Integer");
    typesFabricToEXI.put("FNormalizedString", "String");
    typesFabricToEXI.put("FToken", "String");
    typesFabricToEXI.put("FAnyURI", "String");
    typesFabricToEXI.put("FAny", "String");
  }

    // TODO: Add comment
    private static void createMappingEXIToCpp()
    {
        typesEXIToCpp.put("Boolean", "bool");
        typesEXIToCpp.put("Float", "float");
        typesEXIToCpp.put("String", "const char*");
        typesEXIToCpp.put("Decimal", "char*");
        typesEXIToCpp.put("Integer", "int32");
        typesEXIToCpp.put("UnsignedInteger", "uint32");
        typesEXIToCpp.put("NBitUnsignedInteger", "unsigned int");
    }
}
