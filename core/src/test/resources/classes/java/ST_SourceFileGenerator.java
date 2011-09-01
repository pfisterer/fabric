package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleTypes.xsd
 */
public class ST_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    @Override void generateClasses() throws Exception {
        /*
        Root
         */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(ROOT)
                .addElement("boolean", "BooleanValue")
                .addElement("float", "FloatValue")
                .addElement("double", "DoubleValue")
                .addElement("byte", "ByteValue")
                .addElement("short", "UnsignedByteValue")
                .addElement("short", "ShortValue")
                .addElement("int", "UnsignedShortValue")
                .addElement("int", "IntValue")
                .addElement("java.math.BigInteger", "PositiveIntValue")
                .addElement("long", "UnsignedIntValue")
                .addElement("long", "LongValue")
                .addElement("java.math.BigDecimal", "UnsignedLongValue")
                .addElement("java.math.BigDecimal", "DecimalValue")
                .addElement("String", "StringValue")
                .addElement("byte[]", "HexBinaryValue")
                .addElement("byte[]", "Base64BinaryValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "DateTimeValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "TimeValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "DateValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "DayValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "MonthValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "MonthDayValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "YearValue")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "YearMonthValue")
                .addElement("javax.xml.datatype.Duration", "DurationValue")
                .addElement("javax.xml.datatype.QName", "NOTATIONValue")
                .addElement("javax.xml.namespace.QName", "QNameValue")
                .addElement("String", "AnyURIValue")
                .addElement("java.math.BigDecimal", "IntegerValue")
                .addElement("String", "NameValue")
                .addElement("String", "NCNameValue")
                .addElement("java.math.BigDecimal", "NegativeIntegerValue")
                .addElement("String", "NMTOKENValue")
                .addElement("java.math.BigDecimal", "NonNegativeIntegerValue")
                .addElement("java.math.BigDecimal", "NonPositiveIntegerValue")
                .addElement("String", "NormalizedStringValue")
                .addElement("String", "TokenValue")
                .build()
                .asClassObject(strategy));
    }
}
