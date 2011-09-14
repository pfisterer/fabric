package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_pattern.xsd
 */
public class ST_Pattern_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Pattern_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	    	
    	JClass address1Type = ((JClass) AttributeContainer.newBuilder()
            .setName("InitialsType")
            .addAttribute("String", "pattern", "[A-Z][A-Z][A-Z]")
            .build()
            .asClassObject(strategy));
        types.add(address1Type); 
        
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("InitialsType", "Initials")
            .build()
            .asClassObject(strategy));
        types.add(root); 
    }
}
