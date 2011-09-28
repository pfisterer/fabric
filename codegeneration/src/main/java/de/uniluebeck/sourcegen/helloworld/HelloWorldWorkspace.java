package de.uniluebeck.sourcegen.helloworld;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger object for debugging.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(HelloWorldWorkspace.class);
	
    /**
     * Property key to retrieve the Hello World! program file name.
     */
    private static final String KEY_HELLOWORLD_OUTFILE = "helloworld.outfile";

    /**
     * Property key to retrieve the Hello World! package name.
     */
    private static final String KEY_HELLOWORLD_PACKAGE = "helloworld.package";

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
     * Constructs a new Hello World workspace helper.
     *
     * @param properties
     */
    public HelloWorldWorkspace(Workspace w) {
    	LOGGER.debug("Constructor of 'HelloWorldWorkspace' was called.");
    	
        this.sourceFiles = w.getSourceFiles();
        fileName = w.getProperties().getProperty(KEY_HELLOWORLD_OUTFILE);
        packageName = w.getProperties().getProperty(KEY_HELLOWORLD_PACKAGE);
        
        LOGGER.debug("FileName is now: " + fileName);
        LOGGER.debug("PackageName is now: " + packageName);
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
    		LOGGER.debug("DefaultSourceFile was null.");
    		
            defaultSourceFile = new JSourceFileImpl(packageName, fileName);
            sourceFiles.add(defaultSourceFile);
        }
    	
    	LOGGER.info("DefaultSourceFile is now: " + defaultSourceFile.getFileName());
    	
        return this.defaultSourceFile;
    }
}
