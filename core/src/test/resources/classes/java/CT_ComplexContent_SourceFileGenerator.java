package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_complexContent.xsd
 */
public class CT_ComplexContent_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_ComplexContent_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {

        /*
               * Create types
               */
        JClass carType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarType")
            .addElement("int", "HorsePower")
            .addElement("String", "LicenseNumber")
            .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
            .build()
            .asClassObject(strategy));
        types.add(carType);

        JClass extendedCarType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarExtendedType")
            .addElement("java.math.BigDecimal", "Milage")
            .addElement("int", "Gears")
            .build()
            .asClassObject(strategy));
        extendedCarType.setExtends(carType);
        types.add(extendedCarType);

        /*
               * Root
               */
        types.add((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("CarExtendedType", "ExtendedCar")
            .build()
            .asClassObject(strategy));
    }
}
