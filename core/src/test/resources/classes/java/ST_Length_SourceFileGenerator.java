package classes.java;

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
    		
    		JClass passwordType = ((JClass) AttributeContainer.newBuilder()
                    .setName("PasswordType")
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