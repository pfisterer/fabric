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
  private HashMap<String, String> typesFabricToEXI = new HashMap<String, String>();

  /** Mapping from EXI built-in type names to C++ type names */
  private HashMap<String, String> typesEXIToCpp = new HashMap<String, String>();
  
  public static final int XML_ATOMIC_VALUE = 0;
  public static final int XML_LIST = 1;
  public static final int XML_ARRAY = 2;
  
  private String elementName;
  private String elementEXIType;
  private String elementCppType;
  private int type;
  private int exiEventCode;

  private ElementMetadata()
  {
  }

  public ElementMetadata(final FElement element)
  {
    // Initialize mapping of type names
    this.createMappingFabricToEXI(); // TODO: Better use static initializer?
    this.createMappingEXIToCpp();

    // Set element name
    this.elementName = element.getName();

    // Set element EXI type (Boolean, Integer, Float etc.)
    this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());

    // Set element C++ type (bool, int, float etc.)
    this.elementCppType = this.getCppTypeName(elementEXIType);

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

  public void setElementCppType(final String elementCppType)
  {
    this.elementCppType = elementCppType;
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
    element.elementEXIType = this.elementEXIType;
    element.elementCppType = this.elementCppType;
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

    if (typesFabricToEXI.containsKey(schemaTypeName))
    {
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

  // TODO: Add comment
  private void createMappingFabricToEXI()
  {
    this.typesFabricToEXI.put("FBoolean", "Boolean");
    this.typesFabricToEXI.put("FFloat", "Float");
    this.typesFabricToEXI.put("FDouble", "Float");
    this.typesFabricToEXI.put("FByte", "NBitUnsignedInteger");
    this.typesFabricToEXI.put("FUnsignedByte", "NBitUnsignedInteger");
    this.typesFabricToEXI.put("FShort", "Integer");
    this.typesFabricToEXI.put("FUnsignedShort", "Integer");
    this.typesFabricToEXI.put("FInt", "Integer");
    this.typesFabricToEXI.put("FInteger", "Integer");
    this.typesFabricToEXI.put("FPositiveInteger", "UnsignedInteger");
    this.typesFabricToEXI.put("FUnsignedInt", "UnsignedInteger");
    this.typesFabricToEXI.put("FLong", "Integer");
    this.typesFabricToEXI.put("FUnsignedLong", "UnsignedInteger");
    this.typesFabricToEXI.put("FDecimal", "Decimal");
    this.typesFabricToEXI.put("FString", "String");
    this.typesFabricToEXI.put("FHexBinary", "Binary");
    this.typesFabricToEXI.put("FBase64Binary", "Binary");
    this.typesFabricToEXI.put("FDateTime", "DateTime");
    this.typesFabricToEXI.put("FTime", "DateTime");
    this.typesFabricToEXI.put("FDate", "DateTime");
    this.typesFabricToEXI.put("FDay", "DateTime");
    this.typesFabricToEXI.put("FMonth", "DateTime");
    this.typesFabricToEXI.put("FMonthDay", "DateTime");
    this.typesFabricToEXI.put("FYear", "DateTime");
    this.typesFabricToEXI.put("FYearMonth", "DateTime");
    this.typesFabricToEXI.put("FDuration", "String");
    this.typesFabricToEXI.put("FNOTATION", "String");
    this.typesFabricToEXI.put("FQName", "String");
    this.typesFabricToEXI.put("FName", "String");
    this.typesFabricToEXI.put("FNCName", "String");
    this.typesFabricToEXI.put("FNegativeInteger", "Integer");
    this.typesFabricToEXI.put("FNMTOKEN", "String");
    this.typesFabricToEXI.put("FNonNegativeInteger", "UnsignedInteger");
    this.typesFabricToEXI.put("FNonPositiveInteger", "Integer");
    this.typesFabricToEXI.put("FNormalizedString", "String");
    this.typesFabricToEXI.put("FToken", "String");
    this.typesFabricToEXI.put("FAnyURI", "String");
      this.typesFabricToEXI.put("FAny", "String");
  }

    // TODO: Add comment
    private void createMappingEXIToCpp()
    {
        this.typesEXIToCpp.put("Boolean", "bool");
        this.typesEXIToCpp.put("Float", "float");
        this.typesEXIToCpp.put("String", "const char*");
        this.typesEXIToCpp.put("Decimal", "char*");
        this.typesEXIToCpp.put("Integer", "int32");
        this.typesEXIToCpp.put("UnsignedInteger", "uint32");
        this.typesEXIToCpp.put("NBitUnsignedInteger", "unsigned int");
    }
}
