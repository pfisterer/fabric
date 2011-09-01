package fabric.testsuite;

import de.uniluebeck.sourcegen.SourceFile;
import fabric.module.typegen.java.JavaClassGenerationStrategy;
import fabric.testsuite.java.*;
import junit.framework.TestCase;
import de.uniluebeck.sourcegen.Workspace;
//import fabric.Main;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import java.util.Properties;

public class MainTest extends TestCase {
    /**
     * Parameters for using the Java Type-Generator in the test cases.
     *
     * TODO: Parameter anpassen!
     */
    public static final String USE_TYPE_GEN = "typegen java";

    /**
     * File format endings of the test files.
     */
    public static final String ENDING_XSD   = ".xsd";
    public static final String ENDING_XML   = ".xml";

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
    private Workspace workspace;

    /**
     * JavaClassGenerationStrategy
     */
    private JavaClassGenerationStrategy javaStrategy;

    @BeforeClass
    public void setUpStrategy() {
        try {
            javaStrategy = new JavaClassGenerationStrategy();   // TODO: Annotation Mapper?

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUpWorkspace() throws Exception {
        workspace = new Workspace(new Properties());
    }

    @Test
    public void test_CT_all() throws Exception {
        assertTrue(
                testFile(CT_ALL, new CT_All_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_any() throws Exception {
        assertTrue(
                testFile(CT_ANY, new CT_Any_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_anyAttribute() throws Exception {
        assertTrue(
                testFile(CT_ANYATTRIBUTE, new CT_AnyAttribute_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_attributes() throws Exception {
        assertTrue(
                testFile(CT_ATTRIBUTES, new CT_Attributes_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_choice() throws Exception {
        assertTrue(
                testFile(CT_CHOICE, new CT_Choice_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_complexContent() throws Exception {
        assertTrue(
                testFile(CT_COMPLEXCONTENT, new CT_ComplexContent_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_innerComplexType() throws Exception {
        assertTrue(
                testFile(CT_INNERCOMPLEXTYPE, new CT_InnerComplexType_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_ref() throws Exception {
        assertTrue(
                testFile(CT_REF, new CT_Ref_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_sequence_global() throws Exception {
        assertTrue(
                testFile(CT_SEQUENCE_GLOBAL, new CT_Sequence_Global_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_sequence_local() throws Exception {
        assertTrue(
                testFile(CT_SEQUENCE_LOCAL, new CT_Sequence_Local_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_CT_simpleContent() throws Exception {
        assertTrue(
                testFile(CT_SIMPLECONTENT, new CT_SimpleContent_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_digits() throws Exception {
        assertTrue(
                testFile(ST_DIGITS, new ST_Digits_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_enumeration_global() throws Exception {
        assertTrue(
                testFile(ST_ENUMERATION_GLOBAL, new ST_Enumeration_Global_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_enumeration_local() throws Exception {
        assertTrue(
                testFile(ST_ENUMERATION_LOCAL, new ST_Enumeration_Local_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_inclusiveExclusive() throws Exception {
        assertTrue(
                testFile(ST_INCLUSIVEEXCLUSIVE, new ST_InclusiveExclusive_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_length() throws Exception {
        assertTrue(
                testFile(ST_LENGTH, new ST_Length_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_list() throws Exception {
        assertTrue(
                testFile(ST_LIST, new ST_List_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_occurenceIndicators() throws Exception {
        assertTrue(
                testFile(ST_OCCURENCEINDICATORS, new ST_OccurenceIndicators_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_pattern() throws Exception {
        assertTrue(
                testFile(ST_PATTERN, new ST_Pattern_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST() throws Exception {
        assertTrue(
                testFile(ST, new ST_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_substitution() throws Exception {
        assertTrue(
                testFile(ST_SUBSTITUTION, new ST_Substitution_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_values() throws Exception {
        assertTrue(
                testFile(ST_VALUES, new ST_Values_SourceFileGenerator(javaStrategy))
        );
    }

    @Test
    public void test_ST_whiteSpace() throws Exception {
        assertTrue(
                testFile(ST_WHITESPACE, new ST_WhiteSpace_SourceFileGenerator(javaStrategy))
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
        String[] params = {"-x", xsd + ENDING_XSD, "-m", USE_TYPE_GEN};
        Main.main(params);
        return workspace.getSourceFiles();
    }
}
