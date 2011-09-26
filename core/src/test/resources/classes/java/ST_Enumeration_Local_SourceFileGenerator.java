package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_enumeration_local.xsd
 */
public class ST_Enumeration_Local_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Enumeration_Local_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /*
        CarType
         */
        JEnum carType = JEnum.factory.create(JModifier.PRIVATE, "CarType", "Audi", "Golf", "BMW");

        /*
        Root
         */
        JClass root = (JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy);

        types.add(root.add(carType));
    }
}
