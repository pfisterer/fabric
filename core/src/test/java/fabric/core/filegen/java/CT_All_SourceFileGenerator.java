package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_all.xsd
 */
public class CT_All_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_All_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    @Override
    void generateClasses() throws Exception {

        /*
         * PersonType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName("PersonType")
                .addElement("String", "FirstName")
                .addElement("String", "LastName")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());

        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("PersonType", "Person")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
