package classes.java;

import org.apache.xmlbeans.impl.xb.xsdschema.impl.RestrictionDocumentImpl;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
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

    	Restriction totalTypeRestriction = new Restriction();
    	totalTypeRestriction.totalDigits = "6";
    	JClass totalType = ((JClass) AttributeContainer.newBuilder()
            .setName("TotalType")
            .addElement("java.math.BigDecimal", "value", totalTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.add(totalType);
        
        Restriction fractionTypeRestriction = new Restriction();
        fractionTypeRestriction.fractionDigits = "2";
        JClass fractionType = ((JClass) AttributeContainer.newBuilder()
            .setName("FractionType")
            .addElement("java.math.BigDecimal", "value", fractionTypeRestriction)
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
