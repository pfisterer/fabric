package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_all.xsd
 */
public class CT_All_SourceFileGenerator extends JSourceFileGenerator {

    /**
        * Constructor
        */
    public CT_All_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    @Override
    void generateClasses() throws Exception {
        /*
               * PersonType
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("PersonType")
                .addElement("String", "FirstName")
                .addElement("String", "LastName")
                .build()
                .asClassObject(strategy));
        /*
               * Root
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("PersonType", "Person")
                .build()
                .asClassObject(strategy));
    }
}
