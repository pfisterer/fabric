package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_innerComplexType.xsd
 */
public class CT_InnerComplexType_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_InnerComplexType_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /*
               * PersonType
               */
        JClass carType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarType")
            .addElement("int", "HorsePower")
            .addElement("String", "LicenseNumber")
            .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
            .addElement("Tank", "anyElement")
            .build()
            .asClassObject(strategy));

        JClass tank = ((JClass) AttributeContainer.newBuilder()
            .setName("Tank")
            .addElement("long", "Capacity")
            .addElement("String", "Material")
            .build()
            .asClassObject(strategy));

        carType.add(tank);
        types.add(carType);


        /*
               * Root
               */
        types.add((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("CarType", "Car")
            .build()
            .asClassObject(strategy));
    }
}
