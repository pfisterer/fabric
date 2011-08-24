import de.uniluebeck.sourcegen.java.JSourceFileImpl;

/**
 * Created by IntelliJ IDEA.
 * User: reichart
 * Date: 22.08.11
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class Waehrung3JSF {
    /**
     * JSourceFile
     */
    private JSourceFileImpl waehrung3Java;

    /**
     * Name of XSD root element
     */
    private static final String ROOT        = "Waehrung3";

    /**
     * Java file ending
     */
    private static final String ENDING_JAVA = ".java";

    /**
     * Constructor
     */
    public Waehrung3JSF() {
        buildWaehrung3Java();
    }

    /**
     * Generates the expected JSourceFile for Waehrung3.java
     */
    private void buildWaehrung3Java() {
        AttributeContainer waehrung3Container = AttributeContainer.newBuilder()
                                           .setName(ROOT)
                                           .addElement("String", "Name")
                                           .addAttribute("String", "Waehrungscode")
                                           .build();
        waehrung3Java = new JSourceFileImpl(ROOT.toLowerCase(), ROOT + ENDING_JAVA);
        waehrung3Java.add(waehrung3Container.asJClass());
    }

    /**
     * @return JSourceFile of Waehrung3.java
     */
    public JSourceFileImpl getWaehrung3Java() {
        return waehrung3Java;
    }

}
