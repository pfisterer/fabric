package fabric.wsdlschemaparser.schema;

import org.apache.log4j.*;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.junit.Test;
import java.io.File;

import static org.junit.Assert.*;

public class FSchemaTypeFactoryTest {
    static{
        PatternLayout patternLayout = new PatternLayout(
                "%-13d{HH:mm:ss,SSS} | %-20.20C{3} | %-5p | %m%n");
        final Appender appender = new ConsoleAppender(patternLayout);
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @Test
    public void testDefaultAndFixedValue() throws Exception {
        File file = new File("src/test/resources/schemas/defaultFixedValues.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FElement otherColor         = objectList.getTopLevelElement("OtherColor");
        FElement foregroundColor    = objectList.getTopLevelElement("ForegroundColor");
        FElement backgroundColor    = objectList.getTopLevelElement("BackgroundColor");

        /*
        Tests
         */
        assertNull("Default value of OtherColor must be null.",
                otherColor.getDefaultValue());
        assertNull("Fixed value of OtherColor must be null.",
                otherColor.getFixedValue());
        assertEquals("Default value of ForegroundColor must be \"red\".",
                "red", foregroundColor.getDefaultValue());
        assertNull("Fixed value of ForegroundColor must be null.",
                foregroundColor.getFixedValue());
        assertNull("Default value of BackgroundColor must be null.",
                backgroundColor.getDefaultValue());
        assertEquals("Fixed value of ForegroundColor must be \"white\".",
                "white", backgroundColor.getFixedValue());
    }

    @Test
    public void testList() throws Exception {
        File file = new File("src/test/resources/schemas/list.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FSchemaType intList    = objectList.getTopLevelElement("IntList").getSchemaType();
        FSchemaType intValue   = objectList.getTopLevelElement("IntValue").getSchemaType();
        FSchemaType intList2   = objectList.getTopLevelElement("AnotherIntList").getSchemaType();
        FSchemaType restrictedList
                = objectList.getTopLevelElement("IntListWithRestriction").getSchemaType();

        /*
        Tests
         */
        assertTrue("IntList has to be a xs:list of type xs:integer.",
                intList.isSimple()
                && ((FSimpleType) intList).isList()
                && ((FList)intList).getItemType() instanceof FInteger);
        assertTrue("IntValue has to be a single value of type xs:integer.",
                intValue.isSimple()
                && !((FSimpleType) intValue).isList()
                && intValue instanceof FInteger);
        assertTrue("AnotherIntList has to be a xs:list of type xs:integer.",
                intList2.isSimple()
                && ((FSimpleType) intList2).isList()
                && ((FList)intList2).getItemType() instanceof FInteger);
        assertTrue("IntListWithRestriction has to be a xs:list of type IntListType.",
                restrictedList.isSimple()
                && ((FSimpleType) restrictedList).isList()
                && ((FList)restrictedList).getItemType() instanceof FInteger);
        assertTrue("Length of IntListWithRestriction has to be restricted to 6.",
                ((FSimpleType) restrictedList).getRestrictions()
                        .getIntegerValue(SchemaType.FACET_LENGTH) == 6);
    }

    @Test
    public void testWhiteSpace() throws Exception {
        File file = new File("src/test/resources/schemas/whiteSpace.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FSchemaRestrictions address    = objectList.getTopLevelElement("Address")
                .getSchemaType().getRestrictions();
        FSchemaRestrictions address2   = objectList.getTopLevelElement("Address2")
                .getSchemaType().getRestrictions();
        FSchemaRestrictions address3   = objectList.getTopLevelElement("Address3")
                .getSchemaType().getRestrictions();

        /*
        Tests
         */
        assertTrue("Address has to be xs:whiteSpace restricted.",
                address.hasRestriction(SchemaType.FACET_WHITE_SPACE));
        assertTrue("Address2 has to be xs:whiteSpace restricted.",
                address2.hasRestriction(SchemaType.FACET_WHITE_SPACE));
        assertTrue("Address3 has to be xs:whiteSpace restricted.",
                address3.hasRestriction(SchemaType.FACET_WHITE_SPACE));
        assertEquals("Value of xs:whiteSpace in Address has to be 'preserve'.",
                "preserve",
                address.getStringValue(SchemaType.FACET_WHITE_SPACE));
        assertEquals("Value of xs:whiteSpace in Address2 has to be 'replace'.",
                "replace",
                address2.getStringValue(SchemaType.FACET_WHITE_SPACE));
        assertEquals("Value of xs:whiteSpace in Address3 has to be 'collapse'.",
                "collapse",
                address3.getStringValue(SchemaType.FACET_WHITE_SPACE));
    }

    @Test
    public void testPattern() throws Exception {
        File file = new File("src/test/resources/schemas/pattern.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FSchemaRestrictions initials = objectList.getTopLevelElement("Initials")
                .getSchemaType().getRestrictions();

        /*
        Tests
         */
        assertTrue("Initials has to be xs:pattern restricted.",
                initials.hasRestriction(SchemaType.FACET_PATTERN));
        assertEquals("Value of xs:pattern in Initials has to be '[A-Z][A-Z][A-Z]'.",
                "[A-Z][A-Z][A-Z]",
                initials.getStringValue(SchemaType.FACET_PATTERN));
    }

    @Test
    public void testDigit() throws Exception {
        File file = new File("src/test/resources/schemas/digit.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FSchemaRestrictions temperature = objectList.getTopLevelElement("Temperature")
                .getSchemaType().getRestrictions();

        /*
        Tests
         */
        assertTrue("Temperature has to be xs:totalDigits restricted.",
                temperature.hasRestriction(SchemaType.FACET_TOTAL_DIGITS));
        assertTrue("Temperature has to be xs:fractionDigits restricted.",
                temperature.hasRestriction(SchemaType.FACET_FRACTION_DIGITS));
        assertEquals("Value of xs:totalDigits in Temperature has to be 3.",
                3,
                temperature.getIntegerValue(SchemaType.FACET_TOTAL_DIGITS));
        assertEquals("Value of xs:fractionDigits in Temperature has to be 1.",
                1,
                temperature.getIntegerValue(SchemaType.FACET_FRACTION_DIGITS));
    }
}
