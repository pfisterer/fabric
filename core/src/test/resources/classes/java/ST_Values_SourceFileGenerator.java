package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_values.xsd
 */
public class ST_Values_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Values_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	
    	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("String", "ForegroundColor", "red")  //default value already implemented
        //TODO fixed value   .addConstantElement("String", "BackgroundColor", "white")  
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}