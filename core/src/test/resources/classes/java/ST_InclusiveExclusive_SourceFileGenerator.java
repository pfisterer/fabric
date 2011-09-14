package classes.java;

import org.apache.xmlbeans.impl.xb.xsdschema.impl.RestrictionDocumentImpl;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_inclusiveExclusive.xsd
 */
public class ST_InclusiveExclusive_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_InclusiveExclusive_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	    
    	Restriction digitTypeRestriction = new Restriction();
    	digitTypeRestriction.minInclusive = "0";
    	digitTypeRestriction.maxInclusive = "9";
    	JClass digitType = ((JClass) AttributeContainer.newBuilder()
            .setName("DigitType")
            .addAttribute("int", "value", digitTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.add(digitType); 
    	
        Restriction positiveDigitTypeRestriction = new Restriction();
        positiveDigitTypeRestriction.minExclusive = "0";
        positiveDigitTypeRestriction.maxExclusive = "9";
    	JClass positiveDigitType = ((JClass) AttributeContainer.newBuilder()
            .setName("PositiveDigitType")
            .addAttribute("int", "value", positiveDigitTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.add(positiveDigitType); 
	    		
		JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("DigitType", "Digit")
            .addElement("PositiveDigitType", "PositiveDigit")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
