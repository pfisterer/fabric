package classes.java.archive;

import de.uniluebeck.sourcegen.java.*;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.base.ClassGenerationStrategy;

/**
 * Class that generates the expected JSourceFiles for car.xsd manually
 */
public class CarJSF {
    /**
     * JSourceFile
     */
    private JSourceFileImpl carJava;

    /**
     * Name of XSD root element
     */
    private static final String ROOT        = "Car";

    /**
     * Java file ending
     */
    private static final String ENDING_JAVA = ".java";

    /**
     * Class generation strategy
     */
    private ClassGenerationStrategy strategy;

    /**
     * Constructor
     */
    public CarJSF(ClassGenerationStrategy strategy) {
        this.strategy = strategy;
        buildCarClass();
        buildSpeedClass();
        buildWeightClass();
    }

    /**
     * Generates the expected JSourceFile for Car.java
     */
    private void buildCarClass() {
        AttributeContainer carContainer = AttributeContainer.newBuilder()
                                           .setName(ROOT)
                                           .addAttribute("String", "Name")
                                           .addElement("CurrentSpeedClass", "CurrentSpeed")
                                           .addElement("int", "HorsePower")
                                           .addElement("java.math.BigDecimal", "Milage")
                                           .addElementArray("PassengerWeightClass", "PassengerWeight")
                                           .build();
        carJava = new JSourceFileImpl(ROOT.toLowerCase(), ROOT + ENDING_JAVA);
        try {
            carJava.add((JClass)carContainer.asClassObject(strategy));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the expected JSourceFile for SpeedGermany.java
     */
    private void buildSpeedClass() {
        // TODO: Change to static class!
        AttributeContainer speedContainer = AttributeContainer.newBuilder()
                                           .setName("CurrentSpeedClass")
                                           .addElement("int", "SpeedGermany")
                                           .build();
        try {
            carJava.add((JClass)speedContainer.asClassObject(strategy));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the expected JSourceFile for PassengerWeight.java
     */
    private void buildWeightClass() {
        // TODO: Support of ElementArray in AttributeContainer
        // TODO: Change to static class!
        AttributeContainer weightContainer = AttributeContainer.newBuilder()
                                            .setName("PassengerWeightClass")
                                            .addElement("ArrayList<BigDecimal>", "Float")
                                            .addAttribute("int", "Length")
                                            .build();
        try {
            carJava.add((JClass)weightContainer.asClassObject(strategy));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return JSourceFile of Car.java
     */
    public JSourceFileImpl getCarJava() {
        return carJava;
    }

}
