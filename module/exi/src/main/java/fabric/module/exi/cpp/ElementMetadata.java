/** 27.03.2012 15:01 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.SchemaHelper;

// TODO: Remove unused imports
import exi.events.ExiEventCode;
import exi.events.ExiMalformedEventCodeException;

/**
 * This class is a container for XML element metadata. While
 * the treewalker processes an XML Schema file, information
 * about XML elements is collected in a Queue of ElementMetadata
 * objects. The data is later used to generate the serialization
 * and deserialization methods in EXIConverter class dynamically.
 * 
 * @author seidel, reichart
 */
public class ElementMetadata implements Comparable<ElementMetadata>
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(ElementMetadata.class);
  
  /** Mapping from Fabric type names (FInt, FString etc.) to EXI built-in type names */
  private static HashMap<String, String> typesFabricToEXI = new HashMap<String, String>();
  
  /** Mapping from EXI built-in type names to C++ types */
  private static HashMap<String, String> typesEXIToCpp = new HashMap<String, String>();
  
  static
  {
    // Initialize type mappings
    ElementMetadata.createMappingFabricToEXI();
    ElementMetadata.createMappingEXIToCpp();
  }
  
  /** XML element is a single value */
  public static final int XML_ATOMIC_VALUE = 0;
  
  /** XML element is a list that may contain multiple values */
  public static final int XML_LIST = 1;
  
  /** XML element is an array that may contain multiple values */
  public static final int XML_ARRAY = 2;

  // TODO: Constant added
  public static final int CUSTOM_TYPED = 3;
  
  /** Name of the XML element */
  private String elementName;
  
  /** Name of the parent XML element */
  private String parentName;

  /** EXI type of XML element content (e.g. Boolean, Integer or String) */
  private String elementEXIType;
  
  /** Type of the XML element (e.g. atomic value, list or array) */
  private int type;
  
  /** EXI event code within the XML Schema document structure */
  private ExiEventCode exiEventCode; // TODO: EXIficient grammar builder will return event code as int
  
  /**
   * Parameterized constructor.
   * 
   * @param elementName XML element name
   * @param elementEXIType EXI type of element content (e.g. Boolean,
   * Integer or String)
   * @param type XML element type (atomic value, list or array)
   * @param exiEventCode EXI event code
   */
  public ElementMetadata(final String elementName, final String elementEXIType, final int type, final ExiEventCode exiEventCode)
  {
    this.elementName = elementName;
    this.elementEXIType = elementEXIType;
    this.type = type;
    this.exiEventCode = exiEventCode;

    // Validate support for EXI type
    ElementMetadata.checkEXITypeSupport(this.elementEXIType);
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

    // TODO: Line added
//    boolean isCustomTyped =  !SchemaHelper.isBuiltinTypedElement(element);
//    LOGGER.debug(">>> " + element.getName() + " is top-level: " + element.isTopLevel()); // TODO: Remove

    // Element is an XML list
    if (FSchemaTypeHelper.isList(element))
    {
      FList listType = (FList)element.getSchemaType();

      this.elementEXIType = this.getEXITypeName(listType.getItemType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_LIST;
    }
    // Element is an XML array
    else if (FSchemaTypeHelper.isArray(element))
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_ARRAY;
    }
    // TODO: Block added
    // Element is custom typed
    else if (!element.isTopLevel())
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.CUSTOM_TYPED;
    }
    // Element is an atomic value
    else
    {
      this.elementEXIType = this.getEXITypeName(element.getSchemaType().getClass().getSimpleName());
      this.type = ElementMetadata.XML_ATOMIC_VALUE;
    }

    // Set EXI event code
    try {
        this.exiEventCode = new ExiEventCode(0);
    } catch (ExiMalformedEventCodeException e) {
        e.printStackTrace(); // TODO: Properly handle exception
    }

    // Validate support for EXI type
    ElementMetadata.checkEXITypeSupport(this.elementEXIType);
  }

  /**
   * Parameterized constructor creates ElementMetadata object
   * from an FElement object and stores the name of the parent
   * XML element.
   *
   * @param element FElement object passed through from treewalker
   * @param parentName Name of the parent XML element
   */
  public ElementMetadata(final FElement element, final String parentName)
  {
    // Set element metadata
    this(element);
    
    // Set name of parent XML element
    this.parentName = parentName;
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
   * Setter for parent XML element name.
   *
   * @param parentName Parent XML element name
   */
  public void setParentName(final String parentName)
  {
    this.parentName = parentName;
  }

  /**
   * Getter for parent XML element name.
   *
   * @return Parent XML element name
   */
  public String getParentName()
  {
    return this.parentName;
  }

  /**
   * Setter for EXI element content type.
   * 
   * @param elementEXIType EXI element content type
   */
  public void setElementEXIType(final String elementEXIType)
  {
    this.elementEXIType = elementEXIType;
  }

  /**
   * Getter for EXI element content type.
   * 
   * @return EXI element content type
   */
  public String getElementEXIType()
  {
    return this.elementEXIType;
  }

  /**
   * Getter for C++ element content type.
   * 
   * @return C++ element content type
   */
  public String getElementCppType()
  {
    return this.getCppTypeName(this.elementEXIType);
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
  public void setEXIEventCode(final ExiEventCode exiEventCode)
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
    // TODO: This is probably not correct yet. Check which part of the
    // event code we need or how all parts must be written to EXI stream.
    return Integer.valueOf(this.exiEventCode.getPart(0));
  }

  /**
   * Clone ElementMetadata object and return a deep copy.
   * 
   * @return Cloned ElementMetadata object
   */
  @Override
  public ElementMetadata clone()
  {
      ElementMetadata result = null;

      try {
          ExiEventCode c = new ExiEventCode(exiEventCode.getPart(0), exiEventCode.getPart(1), exiEventCode.getPart(2));
          result = new ElementMetadata(this.elementName, this.elementEXIType, this.type, c);
      } catch (ExiMalformedEventCodeException e) {
          e.printStackTrace(); // TODO: Properly handle exception
      }

    result.setParentName(this.parentName);

    return result;
  }

  /**
   * Compare two ElementMetadata objects with each other. The
   * element name is used for the comparison here.
   * 
   * @param elementMetadata ElementMetadata object to compare with
   * 
   * @return Integer value to represent the order of two objects
   */
  @Override
  public int compareTo(final ElementMetadata elementMetadata)
  {
    return this.elementName.compareTo(elementMetadata.elementName);
  }

  /**
   * Private helper method to get the EXI built-in type name
   * (e.g. Boolean, Integer or String) for one of Fabric's
   * XML Schema type names (e.g. FBoolean, FInt or FString).
   * 
   * @param schemaTypeName Fabric type name
   * 
   * @return Corresponding EXI built-in type name
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
   * Private helper method to get the C++ type name (e.g.
   * bool, int or char) for one of the EXI built-in type
   * names (e.g. Boolean, Integer or String).
   *
   * @param exiTypeName EXI built-in type name
   * 
   * @return Corresponding C++ type name
   * 
   * @throws IllegalArgumentException No matching mapping found
   */
  private String getCppTypeName(final String exiTypeName) throws IllegalArgumentException
  {
    // Return mapping if available
    if (typesEXIToCpp.containsKey(exiTypeName))
    {
      LOGGER.debug(String.format("Mapped EXI built-in type '%s' to C++ type '%s'.", exiTypeName, typesEXIToCpp.get(exiTypeName)));

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

  /**
   * Private helper method to populate the mapping of EXI
   * built-in type names to C++ type names.
   */
  private static void createMappingEXIToCpp()
  {
    typesEXIToCpp.put("Boolean", "bool");
    typesEXIToCpp.put("Float", "xsd_float_t");
    typesEXIToCpp.put("String", "const char*");
    typesEXIToCpp.put("Decimal", "char*");
    typesEXIToCpp.put("Integer", "int32");
    typesEXIToCpp.put("UnsignedInteger", "uint32");
    typesEXIToCpp.put("NBitUnsignedInteger", "unsigned int");
  }

  /**
   * Private helper method to check whether a desired EXI type
   * is supported by our C++ EXI implementation or not. We do
   * currently not support all EXI types, e.g. there is no
   * implementation for EXI string tables yet.
   * 
   * In case of an unsupported EXI type an exception is raised.
   * 
   * @param exiTypeName EXI type name
   * 
   * @throws UnsupportedOperationException EXI type not supported
   */
  private static void checkEXITypeSupport(final String exiTypeName)
  {
    // Create a list of supported EXI types
    List<String> supportedEXITypes = new ArrayList<String>();
    supportedEXITypes.add("Boolean");
    supportedEXITypes.add("Float");
    // supportedEXITypes.add("String"); // TODO: Add support for String
    // supportedEXITypes.add("Decimal"); // TODO: Add support for Decimal
    supportedEXITypes.add("Integer");
    supportedEXITypes.add("UnsignedInteger");
    supportedEXITypes.add("NBitUnsignedInteger");

    // Validate desired EXI type
    if (!supportedEXITypes.contains(exiTypeName))
    {
      throw new UnsupportedOperationException(String.format("EXI data type '%s' is not supported yet.", exiTypeName));
    }
  }
}
