package fabric.testsuite.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_any.xsd
 */
public class CT_Any_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Any_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    @Override void generateClasses() throws Exception {
        /*
        PersonType
         */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addElement("int", "HorsePower")
                .addElement("String", "LicenseNumber")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
                .build()
                .asClassObject(strategy));
        /*
        Root
         */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(ROOT)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy));
    }
}
