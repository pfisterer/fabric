package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_pattern.xsd
 */
public class ST_Pattern_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Pattern_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /**
         * InitialsType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
    	Restriction initialsTypeRestriction = new Restriction();
    	initialsTypeRestriction.pattern = "[A-Z][A-Z][A-Z]";
    	JClass initialsType = ((JClass) AttributeContainer.newBuilder()
            .setName("InitialsType")
            .addElement("String", "value", initialsTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(initialsType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("InitialsType", "Initials")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
