package de.uniluebeck.sourcegen.helloworld;

import java.util.List;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;

/**
 * Workspace helper class for Hello World! program generation.
 *
 * @author seidel
 */
public class HelloWorldWorkspace {
    /**
     * Property key to retrieve the Hello World! program file name.
     */
    private static final String KEY_HELLOWORLD_OUTFILE = "HelloWorld.java";

    /**
     * Property key to retrieve the Hello World! package name.
     */
    private static final String KEY_HELLOWORLD_PACKAGE = "helloworld";

    /**
     * The actual Hello World! program file name.
     */
    private String fileName;

    /**
     * The actual Hello World! program package name.
     */
    private String packageName;

    /**
     * The default file used for Hello World! program creation.
     */
    private JSourceFileImpl defaultSourceFile;

    {
        defaultSourceFile = null;
    }

    private final List<SourceFile> sourceFiles;

    /**
     * Constructs a new echo workspace helper.
     *
     * @param properties
     */
    public HelloWorldWorkspace(Workspace w) {
        this.sourceFiles = w.getSourceFiles();
        fileName = w.getProperties().getProperty(KEY_HELLOWORLD_OUTFILE);
        packageName = w.getProperties().getProperty(KEY_HELLOWORLD_PACKAGE);
    }

    /**
     * Returns the default source file. If no such file exists exist,
     * then it is created and added to the workspace's list of source
     * files.
     *
     * @return
     */
    public JSourceFileImpl getDefaultSourceFile() {
        if (defaultSourceFile == null) {
            defaultSourceFile = new JSourceFileImpl(packageName, fileName);
            sourceFiles.add(defaultSourceFile);
        }
        return this.defaultSourceFile;
    }
}
