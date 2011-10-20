package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_innerComplexType.xsd
 */
public class CT_InnerComplexType_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_InnerComplexType_SourceFileGenerator(Properties properties) {
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
        JClass carType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarType")
            .addElement("int", "HorsePower")
            .addElement("String", "LicenseNumber")
            .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
            .addElement("notype", "Tank")       // TODO Anonymous type doesn't have an identifier
            .build()
            .asClassObject(strategy));

        /**
         * Tank
         */
        JClass tank = ((JClass) AttributeContainer.newBuilder()
            .setName("Tank")
            .addElement("long", "Capacity")
            .addElement("String", "Material")
            .build()
            .asClassObject(strategy));
        carType.add(tank);
        types.put(carType, strategy.getRequiredDependencies());


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
