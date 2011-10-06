package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_occurenceIndicators.xsd
 */
public class ST_OccurenceIndicators_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_OccurenceIndicators_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /**
         * IntValuesType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
    	JClass intValuesType = ((JClass) AttributeContainer.newBuilder()
            .setName("IntValuesType")
            .addElement("int", "IntValue1")
            .addElementArray("int", "IntValue2", 2, 3)
            .addElementArray("int", "IntValue3", 2)
            .addElementArray("int", "IntValue4", Integer.MAX_VALUE)
            .addElementArray("int", "IntValue5", 0, 0)
            .addElementArray("int", "IntValue6", 0, Integer.MAX_VALUE)
            .build()
            .asClassObject(strategy));
        types.put(intValuesType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
	JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("IntValuesType", "IntValues")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
