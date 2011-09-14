package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
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
    	    	
    	JClass digitType = ((JClass) AttributeContainer.newBuilder()
            .setName("DigitType")
            .addAttribute("int", "minInclusive", "0")  //TODO inclusives correctly implmented?
            .addAttribute("int", "maxInclusive", "9")
            .build()
            .asClassObject(strategy));
        types.add(digitType); 
    	
    	
    	JClass positiveDigitType = ((JClass) AttributeContainer.newBuilder()
            .setName("PositiveDigitType")
            .addAttribute("int", "minExclusive", "0")  //TODO exclusives correctly implmented?
            .addAttribute("int", "maxExclusive", "9")
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
