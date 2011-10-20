package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_complexContent.xsd
 */
public class CT_ComplexContent_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_ComplexContent_SourceFileGenerator(Properties properties) {
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
            .build()
            .asClassObject(strategy));
        types.put(carType, strategy.getRequiredDependencies());

        /**
         * CarExtendedType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass extendedCarType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarExtendedType")
            .addElement("java.math.BigDecimal", "Milage")
            .addElement("int", "Gears")
            .build()
            .asClassObject(strategy));
        extendedCarType.setExtends(carType);
        types.put(extendedCarType, strategy.getRequiredDependencies());

        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("CarExtendedType", "ExtendedCar")
            .build()
            .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
