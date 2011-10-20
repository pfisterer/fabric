package fabric.core.filegen.base;

import java.util.List;
import java.util.Properties;

import de.uniluebeck.sourcegen.SourceFile;

import fabric.module.typegen.FabricTypeGenModule;

/**
 * Interface for files generating the expected SourceFile objects of a test case.
 */
public abstract class SourceFileGenerator {
    /**
     * Name of the root class
     */
    public static String rootName;

    /**
     * Constructor
     */
    public SourceFileGenerator(Properties properties) {
        try {
            rootName = properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY);
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
