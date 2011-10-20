package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_simpleContent.xsd
 */
public class CT_SimpleContent_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_SimpleContent_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {

        /**
         * ShoeType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass shoeType = ((JClass) AttributeContainer.newBuilder()
            .setName("ShoeType")
            .addElement("int", "this_name_should_be_removed")   // TODO remove name
            .addAttribute("String", "Country")
            .build()
            .asClassObject(strategy));
        types.put(shoeType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("ShoeType", "Shoe")
            .build()
            .asClassObject(new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework))));

        types.put(root, strategy.getRequiredDependencies());
    }
}
