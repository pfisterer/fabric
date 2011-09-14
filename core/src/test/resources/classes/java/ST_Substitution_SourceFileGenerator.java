package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_substitution.xsd
 */
public class ST_Substitution_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Substitution_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	
    	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("String", "Name")
         //TODO substitutionGroup   .addElement("Name", "Alias")
         //TODO block attribute with value substitution .addElement("String", "FirstName", "substitution")
         //TODO substitutionGroup   .addElement("FirstName", "Nickname")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
