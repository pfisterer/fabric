package fabric.wsdlschemaparser.schema;

import org.apache.log4j.*;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

        /*
        Tests
         */
        assertTrue("IntList has to be a xs:list of type xs:integer.",
                intList.isSimple() && ((FSimpleType) intList).isList());
        assertTrue("IntValue has to be a single value of type xs:integer.",
                intValue.isSimple() && !((FSimpleType) intValue).isList());
    }
}
