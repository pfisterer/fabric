package fabric.module.typegen.java;

import fabric.module.typegen.base.Mapper;

/**
 * LanguageMapper implementation for Java. This class will map
 * Fabric's XSD built-in datatypes to its Java equivalents.
 *
 * @author seidel
 */
public class JavaMapper extends Mapper
{
  /**
   * This method populates the map for the datatype mapping with the
   * Java type names. Non-standard datatypes must be fully qualified
   * (e.g. javax.xml.namespace.QName instead of QName).
   */
  @Override
  public void createMapping()
  {
    types.put("FBoolean", "boolean");
    types.put("FFloat", "float");
    types.put("FDouble", "double");
    types.put("FByte", "byte");
    types.put("FUnsignedByte", "short");
    types.put("FShort", "short");
    types.put("FUnsignedShort", "int");
    types.put("FInt", "int");
    types.put("FPositiveInteger", "java.math.BigInteger");
    types.put("FUnsignedInt", "long");
    types.put("FLong", "long");
    types.put("FUnsignedLong", "java.math.BigDecimal");
    types.put("FDecimal", "java.math.BigDecimal");
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
  }
}
