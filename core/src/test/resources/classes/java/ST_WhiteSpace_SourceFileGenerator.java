package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_whiteSpace.xsd
 */
public class ST_WhiteSpace_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_WhiteSpace_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
		JClass address1Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address1Type")
            .addAttribute("String", "whiteSpace", "preserve")
            .build()
            .asClassObject(strategy));
        types.add(address1Type);  
        
        JClass address2Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address2Type")
            .addAttribute("String", "whiteSpace", "replace")
            .build()
            .asClassObject(strategy));
        types.add(address2Type); 
        
        JClass address3Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address3Type")
            .addAttribute("String", "whiteSpace", "collapse")
            .build()
            .asClassObject(strategy));
        types.add(address3Type); 
	    		
		JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("Address1Type", "Address1")
            .addElement("Address2Type", "Address2")
            .addElement("Address3Type", "Address3")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
