//package classes.java;
//
//import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
//import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
//import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;
//import de.uniluebeck.sourcegen.java.JModifier;
//import de.uniluebeck.sourcegen.java.JSourceFileImpl;
//import de.uniluebeck.sourcegen.java.JEnum;
//import fabric.module.typegen.AttributeContainer;
//
///**
// * Created by IntelliJ IDEA.
// * User: reichart
// * Date: 22.08.11
// * Time: 12:07
// * To change this template use File | Settings | File Templates.
// */
//public class Waehrung4JSF {
//
//    /**
//     * JSourceFiles
//     */
//    private JSourceFileImpl waehrung4Java;
//    private JSourceFileImpl codesJava;
//
//    /**
//     * Name of XSD root element
//     */
//    private static final String ROOT        = "Waehrung4";
//
//    /**
//     * Java file ending
//     */
//    private static final String ENDING_JAVA = ".java";
//
//    /**
//     * Constructor
//     */
//    public Waehrung4JSF() {
//        buildWaehrung4Java();
//        buildCodesJava();
//    }
//
//    /**
//     * Generates the expected JSourceFile for Waehrung3.java
//     */
//    private void buildWaehrung4Java() {
//        AttributeContainer waehrung4Container = AttributeContainer.newBuilder()
//                                           .setName(ROOT)
//                                           .addElement("String", "Name")
//                                           .addAttribute("Waehrungscodes", "Waehrungscode")
//                                           .build();
//        waehrung4Java = new JSourceFileImpl(ROOT.toLowerCase(), ROOT + ENDING_JAVA);
//        waehrung4Java.add(waehrung4Container.asJClass());
//    }
//
//    /**
//     * Generates the expected JSourceFile for Waehrungscodes.java
//     */
//    private void buildCodesJava() {
//        try {
//            JEnum codes = JEnum.factory.create( JModifier.PUBLIC, "Waehrungscodes",
//                                                "AUD", "BRL", "CAD", "CNY", "EUR", "GBP",
//                                                "INR", "JPY", "RUR", "USD");
//            codesJava = new JSourceFileImpl(ROOT.toLowerCase(), ROOT + ENDING_JAVA);
//            codesJava.add(codes);
//        } catch (JDuplicateException e) {
//            e.printStackTrace();
//        } catch (JInvalidModifierException e) {
//            e.printStackTrace();
//        } catch (JConflictingModifierException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @return JSourceFile of Waehrung4.java
//     */
//    public JSourceFileImpl getWaehrung4Java() {
//        return waehrung4Java;
//    }
//
//    /**
//     * @return JSourceFile of Waehrungscodes.java
//     */
//    public JSourceFileImpl getCodesJava() {
//        return codesJava;
//    }
//
//}
