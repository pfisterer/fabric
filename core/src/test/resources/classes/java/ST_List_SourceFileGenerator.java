package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for simpleType_list.xsd
 */
public class ST_List_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_List_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
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
            .setName(ROOT)
            .addElement("IntListType", "IntList")
            .build()
            .asClassObject(strategy));
        types.add(root);    	
    }
}
