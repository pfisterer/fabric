/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleTypes.xsd
 */
public class ST_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    @Override void generateClasses() throws Exception {
        /*
        Root
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
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
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
