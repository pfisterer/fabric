package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_digits.xsd
 */
public class ST_Digits_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public ST_Digits_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {
        JClass totalType = ((JClass) AttributeContainer.newBuilder()
            .setName("TotalType")
            .addElement("java.math.BigDecimal", "no_name")   // TODO doesn't have a name
                                                             // TODO include restriction 6 digits
            .build()
            .asClassObject(strategy));
        types.add(totalType);

        JClass fractionType = ((JClass) AttributeContainer.newBuilder()
            .setName("FractionType")
            .addElement("java.math.BigDecimal", "no_name")   // TODO doesn't have a name
                                                             // TODO include restriction 2 digits
            .build()
            .asClassObject(strategy));
        types.add(totalType);


        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("TotalType", "Total")
            .addElement("FractionType", "Fraction")
            .build()
            .asClassObject(strategy));
        types.add(root);
    }
}
