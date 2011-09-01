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
    // TODO: Add missing datatype mappings for C++

    types.put("FBoolean", "bool");
    types.put("FFloat", "float");
    types.put("FDouble", "double");
    types.put("FByte", "byte");
    types.put("FUnsignedByte", "short");
    types.put("FShort", "short");
    types.put("FUnsignedShort", "int");
    types.put("FInt", "int");
    // types.put("FPositiveInteger, "java.math.BigInteger");
    types.put("FUnsignedInt", "long");
    types.put("FLong", "long");
    // types.put("FUnsignedLong, "java.math.BigDecimal");
    // types.put("FDecimal, "java.math.BigDecimal");
    /*
    types.put("FString", "String");
    types.put("FHexBinary", "byte[]");
    types.put("FBase64Binary", "byte[]");
    types.put("FDateTime", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FTime", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDate", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDay", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FMonth", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FMonthDay", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FYear", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FYearMonth", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDuration", "javax.xml.datatype.Duration");
    types.put("FNOTATION", "javax.xml.namespace.QName");
    types.put("FQName", "javax.xml.namespace.QName");
    types.put("FAnyURI", "String");
     */
  }
}
