package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_whiteSpace.xsd
 */
public class ST_WhiteSpace_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_WhiteSpace_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /**
         * Address1Type
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
	Restriction address1TypeRestriction = new Restriction();
	address1TypeRestriction.whiteSpace = "preserve";
    	JClass address1Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address1Type")
            .addElement("String", "value", address1TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address1Type, strategy.getRequiredDependencies());

        /**
         * Address2Type
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction address2TypeRestriction = new Restriction();
        address2TypeRestriction.whiteSpace = "replace";
        JClass address2Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address2Type")
            .addElement("String", "value", address2TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address2Type, strategy.getRequiredDependencies());

        /**
         * Address3Type
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction address3TypeRestriction = new Restriction();
        address3TypeRestriction.whiteSpace = "collapse";
        JClass address3Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address3Type")
            .addElement("String", "value", address3TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address3Type, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("Address1Type", "Address1")
            .addElement("Address2Type", "Address2")
            .addElement("Address3Type", "Address3")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
