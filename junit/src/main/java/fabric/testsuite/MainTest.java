//package fabric.testsuite;
//
//import de.uniluebeck.sourcegen.Workspace;
//import de.uniluebeck.sourcegen.SourceFile;
//import de.uniluebeck.sourcegen.java.JSourceFile;
//import de.uniluebeck.sourcegen.java.JSourceFileImpl;
//import junit.framework.TestCase;
//import org.junit.Before;
//import org.junit.Test;
//import fabric.Main;
//import java.util.Properties;
//
//public class MainTest extends TestCase {
//
//    /**
//     * Parameters for using the Java Type-Generator in the test cases.
//     *
//     * TODO: Anpassen!
//     */
//    public static final String USE_TYPE_GEN = "typegen java";
//
//    /**
//     * File format endings of the test files.
//     */
//    public static final String ENDING_XSD   = ".xsd";
//    public static final String ENDING_XML   = ".xml";
//
//    /**
//     * Names of the predefined test files without file format endings.
//     *
//     * TODO: Weitere Tests!
//     */
//    public static final String CAR       = "Car";
//    public static final String WAEHRUNG  = "Waehrung";
//    public static final String WAEHRUNG2 = "Waehrung2";
//    public static final String WAEHRUNG3 = "Waehrung3";
//    public static final String WAEHRUNG4 = "Waehrung4";
//
//    /**
//     * Workspace
//     */
//    private Workspace workspace;
//
//    /**
//     * Calls the main method with the given XSD and the correct parameters
//     * for usage of the type generator.
//     *
//     * @param xsd Name of the XSD file (without ending ".xsd")
//     */
//    private void generateSourceFileAutomatically(String xsd) {
//        String[] params = {"-x", xsd + ENDING_XSD, "-m", USE_TYPE_GEN};
//        Main.main(params);
//    }
//
//    @Before
//    public void setUpWorkspace() throws Exception {
//        workspace = new Workspace(new Properties());
//    }
//
//    @Test
//    public void car() throws Exception {
//        /*
//         Generate JSourceFile automatically.
//         */
//        generateSourceFileAutomatically(CAR);
//        JSourceFile carAuto = workspace.getJava().getJSourceFile(CAR.toLowerCase(), CAR);
//
//        /*
//         Generate JSourceFile manually.
//         */
//        CarJSF car = new CarJSF();
//        JSourceFileImpl carMan = car.getCarJava();
//
//        /**
//         Test
//         */
//        assertEquals(carMan, carAuto);
//    }
//
//    @Test
//    public void waehrung() throws Exception {
//        /*
//         Generate JSourceFile automatically.
//         */
//        generateSourceFileAutomatically(WAEHRUNG);
//        JSourceFile waehrungAuto = workspace.getJava().getJSourceFile(WAEHRUNG.toLowerCase(), WAEHRUNG);
//
//        /*
//         Generate JSourceFile manually.
//         */
//        WaehrungJSF waehrung = new WaehrungJSF();
//        JSourceFileImpl waehrungMan = waehrung.getWaehrungJava();
//
//        /**
//         Test
//         */
//        assertEquals(waehrungMan, waehrungAuto);
//    }
//
//    @Test
//    public void waehrung2() throws Exception {
//        /*
//         Generate JSourceFile automatically.
//         */
//        generateSourceFileAutomatically(WAEHRUNG2);
//        JSourceFile waehrungAuto = workspace.getJava().getJSourceFile(WAEHRUNG2.toLowerCase(), WAEHRUNG2);
//
//        /*
//         Generate JSourceFile manually.
//         */
//        WaehrungJSF waehrung2 = new WaehrungJSF();
//        JSourceFileImpl waehrung2Man = waehrung2.getWaehrungJava();
//
//        /**
//         Test
//         */
//        assertEquals(waehrung2Man, waehrungAuto);
//    }
//
//    @Test
//    public void waehrung3() throws Exception {
//        /*
//         Generate JSourceFile automatically.
//         */
//        generateSourceFileAutomatically(WAEHRUNG3);
//        JSourceFile waehrung3Auto = workspace.getJava().getJSourceFile(WAEHRUNG3.toLowerCase(), WAEHRUNG3);
//
//        /*
//         Generate JSourceFile manually.
//         */
//        Waehrung3JSF waehrung3 = new Waehrung3JSF();
//        JSourceFileImpl waehrung3Man = waehrung3.getWaehrung3Java();
//
//        /**
//         Test
//         */
//        assertEquals(waehrung3Man, waehrung3Auto);
//    }
//
//    @Test
//    public void waehrung4() throws Exception {
//        /*
//         Generate JSourceFile automatically.
//         */
//        generateSourceFileAutomatically(WAEHRUNG4);
//        JSourceFile waehrung4Auto = workspace.getJava().getJSourceFile(WAEHRUNG4.toLowerCase(), WAEHRUNG4);
//        JSourceFile codesAuto = workspace.getJava().getJSourceFile(WAEHRUNG4.toLowerCase(), "Waehrungscodes");
//
//        /*
//         Generate JSourceFile manually.
//         */
//        Waehrung4JSF waehrung4 = new Waehrung4JSF();
//        JSourceFileImpl waehrung4Man = waehrung4.getWaehrung4Java();
//        JSourceFileImpl codesMan = waehrung4.getCodesJava();
//
//        /**
//         Tests
//         */
//        assertEquals(waehrung4Man, waehrung4Auto);
//        assertEquals(codesMan, codesAuto);
//    }
//}
