package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_sequence_global.xsd
 */
public class CT_Sequence_Global_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Sequence_Global_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override
    void generateClasses() throws Exception {
        /*
               * CarType
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addElement("int", "HorsePower")
                .addElement("String", "LicenseNumber")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
                .build()
                .asClassObject(strategy));
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
