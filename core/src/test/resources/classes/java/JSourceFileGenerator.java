package classes.java;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import fabric.module.typegen.java.JavaClassGenerationStrategy;
import classes.base.SourceFileGenerator;
import fabric.module.typegen.FabricTypeGenModule;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Abstract class for generating the expected JSourceFile objects of a test case.
 */
public abstract class JSourceFileGenerator extends SourceFileGenerator {

    /**
     * JComplexType objects (JClass, JEnum, ...) generated for the test XSD
     */
    List<JComplexType> types;

    /**
     * Package name
     */
    String packageName;

    /**
     * Constructor
     */
    public JSourceFileGenerator(JavaClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
        try {
            packageName = properties.getProperty(FabricTypeGenModule.PACKAGE_NAME_KEY);
            types = new LinkedList<JComplexType>();
            generateClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the JSourceFile objects representing the generated types.
     *
     * @return JSourceFile objects in a list
     */
    @Override
    public List<SourceFile> getSourceFiles() {
        List<SourceFile> files = new LinkedList<SourceFile>();
        JSourceFile file;
        try {
            for (JComplexType type : types) {
                file = new JSourceFileImpl(packageName, type.getName());
                files.add(file.add(type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    abstract void generateClasses() throws Exception;
}
