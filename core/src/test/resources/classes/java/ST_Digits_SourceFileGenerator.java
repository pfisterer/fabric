package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_digits.xsd
 */
public class ST_Digits_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public ST_Digits_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {

        /**
         * TotalType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
    	Restriction totalTypeRestriction = new Restriction();
    	totalTypeRestriction.totalDigits = "6";
    	JClass totalType = ((JClass) AttributeContainer.newBuilder()
            .setName("TotalType")
            .addElement("java.math.BigDecimal", "value", totalTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(totalType, strategy.getRequiredDependencies());

        /**
         * FractionType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction fractionTypeRestriction = new Restriction();
        fractionTypeRestriction.fractionDigits = "2";
        JClass fractionType = ((JClass) AttributeContainer.newBuilder()
            .setName("FractionType")
            .addElement("java.math.BigDecimal", "value", fractionTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(fractionType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("TotalType", "Total")
            .addElement("FractionType", "Fraction")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
