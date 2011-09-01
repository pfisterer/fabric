package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_attributes.xsd
 */
public class CT_Attributes_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Attributes_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /*
                 PersonType
             */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addAttribute("long",   "Length")
                .addAttribute("String", "LicenseNumber")
                .addAttribute("String", "Name")
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
