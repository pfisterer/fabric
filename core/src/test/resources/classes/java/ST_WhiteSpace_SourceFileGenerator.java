package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
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
		Restriction address1TypeRestriction = new Restriction();
		address1TypeRestriction.whiteSpace = "preserve";
    	JClass address1Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address1Type")
            .addAttribute("String", "value", address1TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.add(address1Type);  
        
        Restriction address2TypeRestriction = new Restriction();
        address2TypeRestriction.whiteSpace = "replace";
        JClass address2Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address2Type")
            .addAttribute("String", "value", address2TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.add(address2Type); 
        
        Restriction address3TypeRestriction = new Restriction();
        address3TypeRestriction.whiteSpace = "collapse";
        JClass address3Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address3Type")
            .addAttribute("String", "value", address3TypeRestriction)
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
