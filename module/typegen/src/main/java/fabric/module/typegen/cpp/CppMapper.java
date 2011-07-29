package fabric.module.typegen.cpp;

import fabric.module.typegen.base.Mapper;
import fabric.wsdlschemaparser.schema.FBoolean;
import fabric.wsdlschemaparser.schema.FByte;
import fabric.wsdlschemaparser.schema.FDouble;
import fabric.wsdlschemaparser.schema.FFloat;
import fabric.wsdlschemaparser.schema.FInt;
import fabric.wsdlschemaparser.schema.FLong;
import fabric.wsdlschemaparser.schema.FShort;
import fabric.wsdlschemaparser.schema.FUnsignedByte;
import fabric.wsdlschemaparser.schema.FUnsignedInt;
import fabric.wsdlschemaparser.schema.FUnsignedShort;

/**
 * LanguageMapper implementation for C++. This class will map
 * the XML Schema built-in datatypes to its C++ equivalents.
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
    // TODO Add missing datatype mappings for C++

    types.put(new FBoolean(), "bool");
    types.put(new FFloat(), "float");
    types.put(new FDouble(), "double");
    types.put(new FByte(), "byte");
    types.put(new FUnsignedByte(), "short");
    types.put(new FShort(), "short");
    types.put(new FUnsignedShort(), "int");
    types.put(new FInt(), "int");
    // types.put(new FPositiveInteger(),	"java.math.BigInteger");
    types.put(new FUnsignedInt(), "long");
    types.put(new FLong(), "long");
    // types.put(new FUnsignedLong(),		"java.math.BigDecimal");
    // types.put(new FDecimal(),			"java.math.BigDecimal");
    /*
    types.put(new FString(), "String");
    types.put(new FHexBinary(), "byte[]");
    types.put(new FBase64Binary(), "byte[]");
    types.put(new FDateTime(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FTime(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FDate(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FDay(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FMonth(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FMonthDay(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FYear(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FYearMonth(), "javax.xml.datatype.XMLGregorianCalendar");
    types.put(new FDuration(), "javax.xml.datatype.Duration");
    types.put(new FNOTATION(), "javax.xml.namespace.QName");
    types.put(new FQName(), "javax.xml.namespace.QName");
    types.put(new FAnyURI(), "String");
     */
  }
}
