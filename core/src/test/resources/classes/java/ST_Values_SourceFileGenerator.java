package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_values.xsd
 */
public class ST_Values_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Values_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("String", "ForegroundColor", "red")  //default value already implemented
            .addConstantElement("String", "BackgroundColor", "white")  
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}