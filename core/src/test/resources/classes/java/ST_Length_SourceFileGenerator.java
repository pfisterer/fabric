package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_length.xsd
 */
public class ST_Length_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Length_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    		
    		Restriction usernameTypeRestriction = new Restriction();
    		usernameTypeRestriction.length = "8";
        	JClass usernameType = ((JClass) AttributeContainer.newBuilder()
                .setName("UsernameType")
                .addAttribute("String", "value", usernameTypeRestriction)
                .build()
                .asClassObject(strategy));
            types.add(usernameType);   
    	
            Restriction passwordTypeRestriction = new Restriction();
            passwordTypeRestriction.minLength = "5";
            passwordTypeRestriction.maxLength = "8";
    		JClass passwordType = ((JClass) AttributeContainer.newBuilder()
                .setName("PasswordType")
                .addAttribute("String", "value", passwordTypeRestriction)
                .build()
                .asClassObject(strategy));
            types.add(passwordType);
                         
    		JClass root = ((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("UsernameType", "UsernameType")
                .addElement("Password", "PasswordType")
                .build()
                .asClassObject(strategy));
            types.add(root);    	
    }
}