package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_enumeration_global.xsd
 */
public class ST_Enumeration_Global_SourceFileGenerator extends JSourceFileGenerator {
    /**
     * Constructor
     */
    public ST_Enumeration_Global_SourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
    }

    @Override void generateClasses() throws Exception {
        /*
        CarType
         */
        types.add(JEnum.factory.create(JModifier.PUBLIC, "CarType", "Audi", "Golf", "BMW"));

        /*
        Root
         */
        types.add((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy));
    }
}
