package fabric.module.typegen.cpp;

import fabric.module.typegen.base.Mapper;

/**
 * LanguageMapper implementation for C++. This class will map
 * Fabric's XSD built-in datatypes to its C++ equivalents.
 *
 * @author seidel
 */
public class CppMapper extends Mapper
{
  /**
   * This method populates the map for the datatype mapping
   * with the C++ type names.
   */
  @Override
  public void createMapping()
  {
    types.put("FBoolean", "bool");
    types.put("FFloat", "float");
    types.put("FDouble", "double");
    types.put("FByte", "int8");
    types.put("FUnsignedByte", "uint8");
    types.put("FShort", "int32");
    types.put("FUnsignedShort", "uint16");
    types.put("FInt", "int32");
    types.put("FInteger", "char*");
    types.put("FPositiveInteger", "char*");
    types.put("FUnsignedInt", "uint32");
    types.put("FLong", "int64");
    types.put("FUnsignedLong", "uint64");
    types.put("FDecimal", "char*");
    types.put("FString", "char*");
    types.put("FHexBinary", "hexBinary_t");
    types.put("FBase64Binary", "base64Binary_t");
    types.put("FDateTime", "dateTime_t");
    types.put("FTime", "time_t");
    types.put("FDate", "date_t");
    types.put("FDay", "gDay_t");
    types.put("FMonth", "gMonth_t");
    types.put("FMonthDay", "gMonthDay_t");
    types.put("FYear", "gYear_t");
    types.put("FYearMonth", "gYearMonth_t");
    types.put("FDuration", "duration_t");
    types.put("FNOTATION", "notation_t");
    types.put("FQName", "qName_t");
    types.put("FName", "char*");
    types.put("FNCName", "char*");
    types.put("FNegativeInteger", "char*");
    types.put("FNMTOKEN", "char*");
    types.put("FNonNegativeInteger", "char*");
    types.put("FNonPositiveInteger", "char*");
    types.put("FNormalizedString", "char*");
    types.put("FToken", "char*");
    types.put("FAnyURI", "char*");
    types.put("FAny", "char*");
  }
}
