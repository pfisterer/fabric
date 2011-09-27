package classes.java;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JEnumAnnotationImpl;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.ArrayList;
import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_enumeration_global.xsd
 */
public class ST_Enumeration_Global_SourceFileGenerator extends JSourceFileGenerator {
    /**
     * Constructor
     */
    public ST_Enumeration_Global_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    @Override void generateClasses() throws Exception {
        /**
         * CarType
         */
        AnnotationMapper mapper = new AnnotationMapper(xmlFramework);
        JEnum carType = JEnum.factory.create(JModifier.PUBLIC, "CarType", "Audi", "Golf", "BMW");
        carType.addAnnotation(new JEnumAnnotationImpl(mapper.getAnnotation("enum", carType.getName())));
        types.put(JEnum.factory.create(JModifier.PUBLIC, "CarType", "Audi", "Golf", "BMW"), mapper.getUsedImports());

        /**
         * Root
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
                .setName(rootName)
                .addElement("CarType", "Car")
                .build()
                .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
