package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_length.xsd
 */
public class ST_Length_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_Length_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {

        /**
         * UsernameType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction usernameTypeRestriction = new Restriction();
            usernameTypeRestriction.length = "8";
        JClass usernameType = ((JClass) AttributeContainer.newBuilder()
            .setName("UsernameType")
            .addElement("String", "value", usernameTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(usernameType, strategy.getRequiredDependencies());

        /**
         * PasswordType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction passwordTypeRestriction = new Restriction();
        passwordTypeRestriction.minLength = "5";
        passwordTypeRestriction.maxLength = "8";
        JClass passwordType = ((JClass) AttributeContainer.newBuilder()
            .setName("PasswordType")
            .addElement("String", "value", passwordTypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(passwordType, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("UsernameType", "UsernameType")
            .addElement("Password", "PasswordType")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}