package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_ref.xsd
 */
public class CT_Ref_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_Ref_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override void generateClasses() throws Exception {
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("String","Name")
            .addElement("PersonType","Person")
            .build()
            .asClassObject(strategy));
        types.add(root);

        JClass personType = ((JClass) AttributeContainer.newBuilder()
            .setName("PersonType")
            .addElement("String","Name")
            .build()
            .asClassObject(strategy));
        types.add(personType);
    }
}
