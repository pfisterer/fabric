package de.uniluebeck.sourcegen.dot;

import java.util.List;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

/**
 * Workspace helper class for Graphviz dot file generation.
 *
 * @author Marco Wegner
 */
public class DotGraphWorkspace {
    /**
     * Property key to retrieve the dot file name.
     */
    private static final String KEY_DOT_OUTFILE = "dot.outfile";

    /**
     * The actual dot graph file name.
     */
    private String fileName;

    /**
     * The default file used for dot graph creation.
     */
    private DGraphFile defaultSourceFile;

    {
        defaultSourceFile = null;
    }

    private final List<SourceFile> sourceFiles;

    /**
     * Constructs a new dot graph workspace helper.
     *
     * @param properties
     */
    public DotGraphWorkspace(Workspace w) {
        this.sourceFiles = w.getSourceFiles();
        fileName = w.getProperties().getProperty(KEY_DOT_OUTFILE);
    }

    /**
     * Returns the default source file. If no such file exists exist, then it is created and added to the
     * workspace's list of source files.
     *
     * @return
     */
    public DGraphFile getDefaultSourceFile() {
        if (defaultSourceFile == null) {
            defaultSourceFile = new DGraphFile(fileName);
            sourceFiles.add(defaultSourceFile);
        }
        return this.defaultSourceFile;
    }
}
