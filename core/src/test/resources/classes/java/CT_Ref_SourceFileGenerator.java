package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_ref.xsd
 */
public class CT_Ref_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_Ref_SourceFileGenerator(Properties properties) {
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
            .addElement("String","Name")
            .addElement("PersonType","Person")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());

        /**
         * PersonType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass personType = ((JClass) AttributeContainer.newBuilder()
            .setName("PersonType")
            .addElement("String","Name")
            .build()
            .asClassObject(strategy));
        types.put(personType, strategy.getRequiredDependencies());
    }
}
