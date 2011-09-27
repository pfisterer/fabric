package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_choice.xsd
 */
public class CT_Choice_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Choice_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {
        /*
         * MonthType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName("MonthType")
                .addElement("int", "MonthInt")
                .addElement("String", "MonthName")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());

        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("MonthType", "Month")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
