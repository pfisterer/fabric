package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_inclusiveExclusive.xsd
 */
public class ST_InclusiveExclusive_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_InclusiveExclusive_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {

        /**
         * DigitType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
    	Restriction digitTypeRestriction = new Restriction();
    	digitTypeRestriction.minInclusive = "0";
    	digitTypeRestriction.maxInclusive = "9";
    	JClass digitType = ((JClass) AttributeContainer.newBuilder()
            .setName("DigitType")
            .addElement("int", "value", digitTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(digitType, strategy.getRequiredDependencies());

        /**
         * PositiveDigitType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction positiveDigitTypeRestriction = new Restriction();
        positiveDigitTypeRestriction.minExclusive = "0";
        positiveDigitTypeRestriction.maxExclusive = "9";
    	JClass positiveDigitType = ((JClass) AttributeContainer.newBuilder()
            .setName("PositiveDigitType")
            .addElement("int", "value", positiveDigitTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(positiveDigitType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("DigitType", "Digit")
            .addElement("PositiveDigitType", "PositiveDigit")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
