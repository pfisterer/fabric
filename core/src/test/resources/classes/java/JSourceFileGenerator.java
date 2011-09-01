package classes.java;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import fabric.module.typegen.java.JavaClassGenerationStrategy;
import classes.base.SourceFileGenerator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for generating the expected JSourceFile objects of a test case.
 */
public abstract class JSourceFileGenerator implements SourceFileGenerator {

    /**
     * JComplexType objects (JClass, JEnum, ...) generated for the test XSD
     */
    List<JComplexType> types;

    /**
     * JavaClassGenerationStrategy
     */
    JavaClassGenerationStrategy strategy;

    /**
     * Constructor
     */
    public JSourceFileGenerator(JavaClassGenerationStrategy strategy) {
        try {
            this.strategy = strategy;
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
                file = new JSourceFileImpl(type.getPackageName(), type.getName());
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
