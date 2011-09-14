package classes.java;

import org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocResults;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_length.xsd
 */
public class ST_Length_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Length_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    		
        	JClass usernameType = ((JClass) AttributeContainer.newBuilder()
                .setName("UsernameType")
                .addAttribute("String", "length", "8")
                .build()
                .asClassObject(strategy));
            types.add(usernameType);   
    	
    		JClass passwordType = ((JClass) AttributeContainer.newBuilder()
                .setName("PasswordType")
                .addAttribute("String", "minLength", "5")
                .addAttribute("String", "maxLength", "8")
                .build()
                .asClassObject(strategy));
            types.add(passwordType);
                         
    		JClass root = ((JClass) AttributeContainer.newBuilder()
                .setName(ROOT)
                .addElement("UsernameType", "UsernameType")
                .addElement("Password", "PasswordType")
                .build()
                .asClassObject(strategy));
            types.add(root);    	
    }
}