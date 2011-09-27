package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_enumeration_local.xsd
 */
public class ST_Enumeration_Local_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Enumeration_Local_SourceFileGenerator(Properties properties) {
        super(properties);
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
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = (JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy);

        types.put(root.add(carType), strategy.getRequiredDependencies());
    }
}
