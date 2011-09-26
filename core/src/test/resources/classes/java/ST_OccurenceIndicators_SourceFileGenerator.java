package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_occurenceIndicators.xsd
 */
public class ST_OccurenceIndicators_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_OccurenceIndicators_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	JClass intValuesType = ((JClass) AttributeContainer.newBuilder()
            .setName("IntValuesType")
         //TODO minOccurs not supported  .addElementArray("int", "IntValue1", 0)
         //TODO minOccurs not supported  .addElementArray("int", "IntValue2", 2)
            .addElementArray("int", "IntValue3", 2)
            .addElementArray("int", "IntValue4", Integer.MAX_VALUE)
            .addElementArray("int", "IntValue5", 0)
            .addElementArray("int", "IntValue6", Integer.MAX_VALUE)
            .build()
            .asClassObject(strategy));
        types.add(intValuesType); 
	    		
		JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("IntValuesType", "IntValues")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
