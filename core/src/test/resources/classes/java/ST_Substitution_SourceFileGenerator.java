package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_substitution.xsd
 */
public class ST_Substitution_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Substitution_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	
    	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("String", "Name")
         //TODO substitutionGroup   .addElement("Name", "Alias")
         //TODO block attribute with value substitution .addElement("String", "FirstName", "substitution")
         //TODO substitutionGroup   .addElement("FirstName", "Nickname")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
