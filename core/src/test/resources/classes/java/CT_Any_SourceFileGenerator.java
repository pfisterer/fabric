package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_any.xsd
 */
public class CT_Any_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_Any_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    @Override
    void generateClasses() throws Exception {
        /*
               * CarType
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("CarType")
                .addElement("int", "HorsePower")
                .addElement("String", "LicenseNumber")
                .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
                .addElement("String", "anyElement")
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
