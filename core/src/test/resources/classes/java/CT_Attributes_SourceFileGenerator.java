package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_attributes.xsd
 */
public class CT_Attributes_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_Attributes_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {
        /*
         * CarType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addAttribute("long",   "Length")
                .addAttribute("String", "LicenseNumber")    // use="required"
                .addAttribute("String", "Name")             // use="prohibited"
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
