package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_choice.xsd
 */
public class CT_Choice_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_Choice_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {
        /*
               * MonthType
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName("MonthType")
                .addElement("int", "MonthInt")
                .addElement("String", "MonthName")
                .build()
                .asClassObject(strategy));

        /*
               * Root
               */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("MonthType", "Month")
                .build()
                .asClassObject(strategy));
    }
}
