package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_list.xsd
 */
public class ST_List_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_List_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
    	JClass intListType = ((JClass) AttributeContainer.newBuilder()
            .setName("IntListType")
            .addElementArray("int", "IntListType")
            .build()
            .asClassObject(strategy));
        types.add(intListType); 
	    		
		JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("IntListType", "IntList")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
