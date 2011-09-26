package classes.base;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.java.JComplexType;
import fabric.module.typegen.base.ClassGenerationStrategy;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Interface for classes generating the expected SourceFile objects of a test case.
 */
public abstract class SourceFileGenerator {
    /**
     * Name of the root class
     */
    public static String rootName;

    /**
     * ClassGenerationStrategy
     */
    protected ClassGenerationStrategy strategy;

    /**
     * Constructor
     */
    public SourceFileGenerator(ClassGenerationStrategy strategy, Properties properties) {
        try {
            this.strategy = strategy;
            rootName = properties.getProperty("typegen.main_class_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the SourceFile objects representing the generated containers.
     *
     * @return SourceFile objects in a list
     */
    public abstract List<SourceFile> getSourceFiles();
}
