package fabric.core;

import classes.java.*;
import de.uniluebeck.itm.tr.util.FileUtils;
import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.java.JSourceFile;
import fabric.Main;
import fabric.module.typegen.java.JavaClassGenerationStrategy;
import de.uniluebeck.sourcegen.Workspace;
import org.junit.*;
import org.w3c.dom.stylesheets.LinkStyle;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

public class MainTest {
    /**
     * Path to resources folder
     */
    private static final String RESOURCES = "src/test/resources/";

    /**
     * Path to schemas folder
     */
    private static final String SCHEMAS = RESOURCES + "schemas/";

    /**
     * Properties file
     */
    private static final String PROPERTIES = RESOURCES + "javaTypeGen.properties";

    /**
     * Parameters for using the Type-Generator in the test cases.
     */
    private static final String USE_TYPE_GEN = "typegen";

    /**
     * File format endings of the test files.
     */
    private static final String ENDING_XSD   = ".xsd";
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
     * JavaClassGenerationStrategy
     */
    private static JavaClassGenerationStrategy javaStrategy;

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
        javaStrategy = new JavaClassGenerationStrategy();  // TODO: Annotation Mapper?
    }

    @After
    public void wipeWorkspace() {
        workspace.getSourceFiles().clear();
    }

    @AfterClass
    public static void tearDown() {
        FileUtils.deleteDirectory(new File(properties.getProperty("typegen.main_class_name")));
    }

    @Test
    public void test_CT_all() throws Exception {
        assertTrue(
                testFile(CT_ALL, new CT_All_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_any() throws Exception {
        assertTrue(
                testFile(CT_ANY, new CT_Any_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_anyAttribute() throws Exception {
        assertTrue(
                testFile(CT_ANYATTRIBUTE, new CT_AnyAttribute_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_attributes() throws Exception {
        assertTrue(
                testFile(CT_ATTRIBUTES, new CT_Attributes_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_choice() throws Exception {
        assertTrue(
                testFile(CT_CHOICE, new CT_Choice_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_complexContent() throws Exception {
        assertTrue(
                testFile(CT_COMPLEXCONTENT, new CT_ComplexContent_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_innerComplexType() throws Exception {
        assertTrue(
                testFile(CT_INNERCOMPLEXTYPE, new CT_InnerComplexType_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_ref() throws Exception {
        assertTrue(
                testFile(CT_REF, new CT_Ref_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_sequence_global() throws Exception {
        assertTrue(
                testFile(CT_SEQUENCE_GLOBAL, new CT_Sequence_Global_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_sequence_local() throws Exception {
        assertTrue(
                testFile(CT_SEQUENCE_LOCAL, new CT_Sequence_Local_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_CT_simpleContent() throws Exception {
        assertTrue(
                testFile(CT_SIMPLECONTENT, new CT_SimpleContent_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_digits() throws Exception {
        assertTrue(
                testFile(ST_DIGITS, new ST_Digits_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_enumeration_global() throws Exception {
        assertTrue(
                testFile(ST_ENUMERATION_GLOBAL, new ST_Enumeration_Global_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_enumeration_local() throws Exception {
        assertTrue(
                testFile(ST_ENUMERATION_LOCAL, new ST_Enumeration_Local_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_inclusiveExclusive() throws Exception {
        assertTrue(
                testFile(ST_INCLUSIVEEXCLUSIVE, new ST_InclusiveExclusive_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_length() throws Exception {
        assertTrue(
                testFile(ST_LENGTH, new ST_Length_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_list() throws Exception {
        assertTrue(
                testFile(ST_LIST, new ST_List_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_occurenceIndicators() throws Exception {
        assertTrue(
                testFile(ST_OCCURENCEINDICATORS, new ST_OccurenceIndicators_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_pattern() throws Exception {
        assertTrue(
                testFile(ST_PATTERN, new ST_Pattern_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST() throws Exception {
        assertTrue(
                testFile(ST, new ST_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_substitution() throws Exception {
        assertTrue(
                testFile(ST_SUBSTITUTION, new ST_Substitution_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_values() throws Exception {
        assertTrue(
                testFile(ST_VALUES, new ST_Values_SourceFileGenerator(javaStrategy, properties))
        );
    }

    @Test
    public void test_ST_whiteSpace() throws Exception {
        assertTrue(
                testFile(ST_WHITESPACE, new ST_WhiteSpace_SourceFileGenerator(javaStrategy, properties))
        );
    }

    /**
     * Takes an XSD file and the corresponding JSourceFileGenerator object and tests if the manually
     * generated source files match the automatically generated ones.
     *
     * @param xsd Name of the XSD file to test
     * @param sourceFileGenerator JSourceFileGenerator object corresponding to the given XSD
     * @return  true, if the automatically and manually generated source files match, false otherwise.
     */
    private boolean testFile(String xsd, JSourceFileGenerator sourceFileGenerator) {
        /*
         Generate SourceFiles automatically.
         */
        List<SourceFile> sourceFilesAuto = generateSourceFilesAutomatically(xsd);

        /*
         Generate SourceFiles manually.
         */
        List<SourceFile> sourceFilesMan = sourceFileGenerator.getSourceFiles();
        
        
        System.out.println("**********************************************************************");
        System.out.println(" UNIT TEST " +xsd);
        System.out.println("**********************************************************************");
        System.out.println("Generated files auto/manu: " + sourceFilesAuto.size() + "/" +sourceFilesMan.size());

        for (SourceFile sourceFile : sourceFilesAuto) {
        	System.out.println("Automatically generated file: " + sourceFile.getFileName());
		}
        
        for (SourceFile sourceFile : sourceFilesMan) {
        	System.out.println("Manually generated file: " + sourceFile.getFileName());
		}
        System.out.println("**********************************************************************");
        
        
        /*
         True if all source files exist and if there are not too many source files
         */
        return sourceFilesAuto.containsAll(sourceFilesMan) && sourceFilesMan.containsAll(sourceFilesAuto);
    }

    /**
     * Calls the main method with the given XSD and the correct parameters
     * for usage of the type generator.
     *
     * @param xsd Name of the XSD file (without ending ".xsd")
     */
    private List<SourceFile> generateSourceFilesAutomatically(String xsd) {
        List<SourceFile> ret = null;
        String[] params = {"-x", SCHEMAS + xsd + ENDING_XSD, "-p", PROPERTIES, "-m", USE_TYPE_GEN, "-v"};
        Main core = new Main(params);

        Class c = core.getClass();
        Field refWorkspace = null;

        try {
            refWorkspace = c.getDeclaredField("workspace");
            refWorkspace.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Object objWorkspace = refWorkspace.get(core);
            Workspace w = (Workspace) objWorkspace;
            ret = w.getSourceFiles();
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return ret;
    }
}
