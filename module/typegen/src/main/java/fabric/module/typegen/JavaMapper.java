package fabric.module.typegen;

import fabric.wsdlschemaparser.schema.*;

public class JavaMapper extends LanguageMapper {
	
	public void createMapping() {
		types.put(new FBoolean(),			"boolean");
		types.put(new FFloat(),				"float");
		types.put(new FDouble(),			"double");
		types.put(new FByte(),				"byte");
		types.put(new FUnsignedByte(),		"short");
		types.put(new FShort(),				"short");
		types.put(new FUnsignedShort(),		"int");
		types.put(new FInt(),				"int");
		types.put(new FPositiveInteger(),	"java.math.BigInteger");
		types.put(new FUnsignedInt(),		"long");
		types.put(new FLong(),				"long");
		types.put(new FUnsignedLong(),		"java.math.BigDecimal");
		types.put(new FDecimal(),			"java.math.BigDecimal");
		types.put(new FString(),			"String");
		types.put(new FHexBinary(),			"byte[]");
		types.put(new FBase64Binary(),		"byte[]");
		types.put(new FDateTime(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FTime(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDate(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDay(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FMonth(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FMonthDay(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FYear(),				"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FYearMonth(),			"javax.xml.datatype.XMLGregorianCalendar");
		types.put(new FDuration(),			"javax.xml.datatype.Duration");
		//TODO: NOTATION! types.put(new FNOTATION(), "");
		types.put(new FQName(),				"javax.xml.namespace.QName");
		types.put(new FAnyURI(),			"java.net.URI" );
	}

}
