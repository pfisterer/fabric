package de.uniluebeck.sourcegen.echo;

import java.util.List;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

/**
 * Workspace helper class for echo file generation.
 */
public class EchoWorkspace {
    /**
     * Property key to retrieve the echo file name.
     */
    private static final String KEY_ECHO_OUTFILE = "echo.outfile";

    /**
     * The actual echo file name.
     */
    private String fileName;

    /**
     * The default file used for echo creation.
     */
    private EchoFile defaultSourceFile;

    {
        defaultSourceFile = null;
    }

    private final List<SourceFile> sourceFiles;

    /**
     * Constructs a new echo workspace helper.
     *
     * @param properties
     */
    public EchoWorkspace(Workspace w) {
        this.sourceFiles = w.getSourceFiles();
        fileName = w.getProperties().getProperty(KEY_ECHO_OUTFILE);
    }

    /**
     * Returns the default source file. If no such file exists exist,
     * then it is created and added to the workspace's list of source
     * files.
     *
     * @return
     */
    public EchoFile getDefaultSourceFile() {
        if (defaultSourceFile == null) {
            defaultSourceFile = new EchoFile(fileName);
            sourceFiles.add(defaultSourceFile);
        }
        return this.defaultSourceFile;
    }
}