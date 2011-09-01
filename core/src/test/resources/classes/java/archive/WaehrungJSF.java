//package classes.java;
//
//import de.uniluebeck.sourcegen.java.JSourceFileImpl;
//import fabric.module.typegen.AttributeContainer;
//
///**
// * Created by IntelliJ IDEA.
// * User: reichart
// * Date: 22.08.11
// * Time: 12:07
// * To change this template use File | Settings | File Templates.
// */
//public class WaehrungJSF {
//    /**
//     * JSourceFile
//     */
//    private JSourceFileImpl waehrungJava;
//
//    /**
//     * Name of XSD root element
//     */
//    private static final String NAME        = "Waehrung";
//
//    /**
//     * Java file ending
//     */
//    private static final String ENDING_JAVA = ".java";
//
//    /**
//     * Constructor
//     */
//    public WaehrungJSF() {
//        buildWaehrungJava();
//    }
//
//    /**
//     * Generates the expected JSourceFile for Waehrung.java
//     */
//    private void buildWaehrungJava() {
//        AttributeContainer waehrungContainer = AttributeContainer.newBuilder()
//                                           .setName(NAME)
//                                           .addElement("String", "Name")
//                                           .addElement("String", "Waehrungscode")
//                                           .build();
//        waehrungJava = new JSourceFileImpl(NAME.toLowerCase(), NAME + ENDING_JAVA);
//        waehrungJava.add(waehrungContainer.asJClass());
//    }
//
//    /**
//     * @return JSourceFile of Waehrung.java
//     */
//    public JSourceFileImpl getWaehrungJava() {
//        return waehrungJava;
//    }
//
//}
