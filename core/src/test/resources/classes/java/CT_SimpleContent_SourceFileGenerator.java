package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

/**
 * SourceFileGenerator for complexType_simpleContent.xsd
 */
public class CT_SimpleContent_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_SimpleContent_SourceFileGenerator(JavaClassGenerationStrategy strategy) {
        super(strategy);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {

        JClass shoeType = ((JClass) AttributeContainer.newBuilder()
            .setName("ShoeType")
            .addElement("int", "this_name_should_be_removed")   // TODO remove name
            .addAttribute("String", "Country")
            .build()
            .asClassObject(strategy));


        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(ROOT)
            .addElement("ShoeType", "Shoe")
            .build()
            .asClassObject(strategy));

        types.add(root);
    }
}
