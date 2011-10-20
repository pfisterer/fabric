package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_values.xsd
 */
public class ST_Values_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Values_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /**
         * Root
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
    	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("String", "ForegroundColor", "red")  //default value already implemented
            .addConstantElement("String", "BackgroundColor", "white")  
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}