package classes.base;

import de.uniluebeck.sourcegen.SourceFile;
import java.util.List;

/**
 * Interface for classes generating the expected SourceFile objects of a test case.
 */
public interface SourceFileGenerator {
    /**
     * Name of the root class
     */
    public static final String ROOT = "Main";

    /**
     * Returns the SourceFile objects representing the generated containers.
     *
     * @return SourceFile objects in a list
     */
    public List<SourceFile> getSourceFiles();
}
