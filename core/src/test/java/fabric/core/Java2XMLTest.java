package fabric.core;

import de.uniluebeck.itm.tr.util.FileUtils;
import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JSourceFile;
import fabric.core.filegen.java.CT_All_SourceFileGenerator;
import fabric.core.filegen.java.JSourceFileGenerator;
import fabric.module.typegen.FabricTypeGenModule;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Test if the XML representations of the Java objects meet their expectations.
 */
public class Java2XMLTest {
    /**
     * Path to resources folder
     */
    private static final String RESOURCES = "src/test/resources/";

    /**
     * Properties file
     */
    private static final String PROPERTIES = RESOURCES + "javaTypeGen.properties";

    /**
     * Parameters for using the Type-Generator in the test cases.
     */
    private static final String USE_TYPE_GEN = "typegen, exi";

    /**
     * File format endings of the test files.
     */
    private static final String ENDING_XML   = ".xml";

    /**
     * Names of the predefined test files without file format endings.
     */
    private static final String CT_ALL                  = "complexType_all";
    private static final String CT_ANY                  = "complexType_any";
    private static final String CT_ANYATTRIBUTE         = "complexType_anyAttribute";
    private static final String CT_ATTRIBUTES           = "complexType_attributes";
    private static final String CT_CHOICE               = "complexType_choice";
    private static final String CT_COMPLEXCONTENT       = "complexType_complexContent";
    private static final String CT_INNERCOMPLEXTYPE     = "complexType_innerComplexType";
    private static final String CT_REF                  = "complexType_ref";
    private static final String CT_SEQUENCE_GLOBAL      = "complexType_sequence_global";
    private static final String CT_SEQUENCE_LOCAL       = "complexType_sequence_local";
    private static final String CT_SIMPLECONTENT        = "complexType_simpleContent";
    private static final String ST_DIGITS               = "simpleType_digits";
    private static final String ST_ENUMERATION_GLOBAL   = "simpleType_enumeration_global";
    private static final String ST_ENUMERATION_LOCAL    = "simpleType_enumeration_local";
    private static final String ST_INCLUSIVEEXCLUSIVE   = "simpleType_inclusiveExclusive";
    private static final String ST_LENGTH               = "simpleType_length";
    private static final String ST_LIST                 = "simpleType_list";
    private static final String ST_OCCURENCEINDICATORS  = "simpleType_occurenceIndicators";
    private static final String ST_PATTERN              = "simpleType_pattern";
    private static final String ST_SUBSTITUTION         = "simpleType_substitution";
    private static final String ST_VALUES               = "simpleType_values";
    private static final String ST_WHITESPACE           = "simpleType_whiteSpace";
    private static final String ST                      = "simpleTypes";

    /**
     * Workspace
     */
    private static Workspace workspace;

    /**
     * Properties
     */
    private static Properties properties;

    @BeforeClass
    public static void setUp() throws Exception {
        FileInputStream propInFile = new FileInputStream(PROPERTIES);
        properties = new Properties();
        properties.load(propInFile);
        workspace = new Workspace(properties);
    }

    @After
    public void wipeWorkspace() {
        workspace.getSourceFiles().clear();
    }

    @AfterClass
    public static void tearDown() {
        FileUtils.deleteDirectory(new File(properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY)));
    }

    @Test
    public void test_something() throws Exception {
        assertTrue(testFile(CT_ALL, new CT_All_SourceFileGenerator(properties)));
    }

    /**
     * Takes an XSD file and the corresponding JSourceFileGenerator object and tests if the manually
     * generated source files match the automatically generated ones.
     *
     * @param xml Name of the expected XML file
     * @param sourceFileGenerator JSourceFileGenerator object
     * @return  true, if the manually generated XML file matches the given XML file, false otherwise.
     */
    private boolean testFile(String xml, JSourceFileGenerator sourceFileGenerator) throws Exception {
        /*
         Generate SourceFiles manually.
         */
        if (sourceFileGenerator.generateSourceFilesInWorkspace(workspace)) {
            String className    = properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY);
            String packageName  = properties.getProperty(FabricTypeGenModule.PACKAGE_NAME_KEY);
            Class clazz = this.getClass().getClassLoader().loadClass("fabric.core." + packageName + "." + className); // TODO: Pfad falsch?
            Object instance = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("instanceToXML");
            String generatedXml = (String) method.invoke(instance);
            // TODO: XML-Datei muss ausgelesen werden

            return generatedXml.equals(xml);
        }
        return false;
    }
}
