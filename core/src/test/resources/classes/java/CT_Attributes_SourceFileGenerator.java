package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_attributes.xsd
 */
public class CT_Attributes_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_Attributes_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {
        /*
               * CarType
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addAttribute("long",   "Length")
                .addAttribute("String", "LicenseNumber")    // use="required"
                .addAttribute("String", "Name")             // use="prohibited"
                .build()
                .asClassObject(strategy));
        /*
               * Root
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy));
    }
}
