package fabric.testsuite.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_anyAttribute.xsd
 */
public class CT_AnyAttribute_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_AnyAttribute_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
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
                .setName("PersonType")
                .addElement("String", "FirstName")
                .addElement("String", "LastName")
                .addAttribute("String", "anyAttribute")
                .build()
                .asClassObject(strategy));
        /*
            Root
             */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(ROOT)
                .addElement("PersonType", "Person")
                .build()
                .asClassObject(strategy));
    }
}
