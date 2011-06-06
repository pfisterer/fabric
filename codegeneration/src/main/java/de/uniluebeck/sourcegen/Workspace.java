/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.sourcegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CHeaderFile;
import de.uniluebeck.sourcegen.c.CHeaderFileImpl;
import de.uniluebeck.sourcegen.c.CSourceFile;
import de.uniluebeck.sourcegen.c.CSourceFileImpl;
import de.uniluebeck.sourcegen.c.CppHeaderFile;
import de.uniluebeck.sourcegen.c.CppHeaderFileImpl;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppSourceFileImpl;
import de.uniluebeck.sourcegen.dot.DGraphFile;
import de.uniluebeck.sourcegen.echo.EchoFile;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import de.uniluebeck.sourcegen.protobuf.PSourceFile;

public class Workspace {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(Workspace.class);

	public static final String KEY_C_FILENAME = "c.filename";

	public static final String KEY_JAVA_PKG_PREFIX = "java.package";

	public static final String KEY_PROJECTDIR = "project.dir";

	public static final String KEY_PROJECTSUBDIR = "project.subdir";

	private Properties properties;

	private LinkedList<SourceFile> sourceFiles = new LinkedList<SourceFile>();

	private String jPackagePrefix;

	// TODO Make sure that JMethod has a working Comparator
	private Set<JMethod> globalMethodStoreJava = new HashSet<JMethod>();

	// TODO Make sure that CFun has a working Comparator
	private Set<CFun> globalMethodStoreC = new HashSet<CFun>();

	private JavaWorkspaceHelper javaHelper;

	private CWorkspaceHelper cHelper;

	private ProtobufWorkspaceHelper protobufHelper;
	
	private final EchoWorkspaceHelper echoHelper;
	
	private final HelloWorldWorkspaceHelper helloWorldHelper;
	
    /**
     * Helper for dot graph creation in this workspace.
     */
    private final DotGraphWorkspaceHelper dotHelper;

	public Workspace() {
		this(new Properties());
	}

	public Workspace(Properties properties) {
		this.properties = properties;
		
		javaHelper = new JavaWorkspaceHelper();
		cHelper = new CWorkspaceHelper();
		protobufHelper = new ProtobufWorkspaceHelper(properties);
        this.dotHelper = new DotGraphWorkspaceHelper(properties);
        echoHelper = new EchoWorkspaceHelper(properties);
        helloWorldHelper = new HelloWorldWorkspaceHelper(properties);
	}

	public Properties getProperties() {
		return properties;
	}

	public JavaWorkspaceHelper getJava() {
		return javaHelper;
	}

	public CWorkspaceHelper getC() {
		return cHelper;
	}

	public ProtobufWorkspaceHelper getProtobuf() {
		return protobufHelper;
	}
	
	public EchoWorkspaceHelper getEchoHelper() {
	        log.info("Dies ist ein Test für das Echo-Modul.");
	        return echoHelper;
	}
	
	public HelloWorldWorkspaceHelper getHelloWorldHelper() {
	    log.info("Dies ist ein Test für das Hello World-Modul.");
	    return helloWorldHelper;
	}

    /**
     * Returns the helper for dot graph generation in this workspace.
     * 
     * @return The dot graph helper instance.
     */
    public DotGraphWorkspaceHelper getDotHelper( ) {
        return this.dotHelper;
    }

	public void generate() throws Exception {
		log.info("Generating " + sourceFiles.size() + " source files.");

		jPackagePrefix = properties.getProperty(KEY_JAVA_PKG_PREFIX, "");

		for (SourceFile sF : sourceFiles) {

			String dirString = getDirString(sF);
			String fileString = getFileString(sF);

			File dir = new File(dirString);
			File file = new File(dirString + fileString);

			assureDirExists(dir);
			assureFileExists(file);

			log.info("Generating file " + file.getAbsolutePath() + ".");

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(sF.toString() + "\n");
			log.debug("Sourcecode of " + sF.getFileName() + ":\n");
			log.debug(sF.toString());
			writer.close();
		}
	}

	private void assureDirExists(File dir) throws Exception {
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new Exception("File output directory couldn't be created.");
	}

	private void assureFileExists(File file) throws Exception {
		if (!file.exists())
			if (!file.createNewFile())
				throw new Exception("File couldn't be created.");
	}

	private String getFileString(SourceFile sourceFile) {
		if (sourceFile instanceof JSourceFile)
			return sourceFile.getFileName() + ".java";
		if (sourceFile instanceof CHeaderFile)
			return sourceFile.getFileName() + ".h";
		if (sourceFile instanceof CSourceFile)
			return sourceFile.getFileName() + ".c";
		if (sourceFile instanceof CppHeaderFile)
			return sourceFile.getFileName() + ".hpp";
        if (sourceFile instanceof CppSourceFile)
            return sourceFile.getFileName( ) + ".cpp";
        return sourceFile.getFileName( );
	}

	private String getDirString(SourceFile sourceFile) {
		String projectDirString = properties.getProperty(KEY_PROJECTDIR, System.getProperty("user.dir"));
		String subDir = properties.getProperty(KEY_PROJECTSUBDIR, "");

		projectDirString = assureTrailingSeparator(projectDirString);
		projectDirString += subDir;

		if (sourceFile instanceof JSourceFile) {
			JSourceFile jSourceFile = (JSourceFile) sourceFile;
			projectDirString = assureTrailingSeparator(projectDirString);
			projectDirString += jPackagePrefix.replace('.', File.separatorChar);
			projectDirString = assureTrailingSeparator(projectDirString);
			projectDirString += jSourceFile.getPackageName().replace('.', File.separatorChar);
		}

		projectDirString = assureTrailingSeparator(projectDirString);
		return projectDirString;
	}

	private String assureTrailingSeparator(String s) {
		return s.endsWith(File.separator) ? s : s + File.separator;
	}

	/**
	 * 
	 *
	 */
	public class CWorkspaceHelper {
		public CppHeaderFile getCppHeaderFile(String fileName) {

			// check if file is already existing and
			// return instance if so
			for (SourceFile f : sourceFiles)
				if (f instanceof CppHeaderFile && ((CppSourceFile) f).getFileName().equals(fileName))
					return (CppHeaderFile) f;

			// create the new instance since it's not yet existing
			CppHeaderFileImpl file = new CppHeaderFileImpl(fileName);
			sourceFiles.add(file);
			return file;

		}

		public CppSourceFile getCppSourceFile(String fileName) {

			// check if file is already existing and
			// return instance if so
			for (SourceFile f : sourceFiles)
				if (f instanceof CppSourceFile && !(f instanceof CppHeaderFile)
						&& ((CppSourceFile) f).getFileName().equals(fileName))
					return (CppSourceFile) f;

			// create the new instance since it's not yet existing
			CppSourceFile file = new CppSourceFileImpl(fileName);
			sourceFiles.add(file);
			return file;
		}

		public CSourceFile getCSourceFile(String fileName) {

			// check if source file already existing and
			// return instance if so
			for (SourceFile f : sourceFiles)
				if (f instanceof CSourceFile && !(f instanceof CHeaderFile)
						&& ((CSourceFile) f).getFileName().equals(fileName))
					return (CSourceFile) f;

			// create new instance since it's not yet existing
			CSourceFile file = new CSourceFileImpl(fileName);
			sourceFiles.add(file);
			return file;

		}

		public boolean containsCHeaderFile(String fileName) {
			for (SourceFile f : sourceFiles)
				if (f instanceof CHeaderFile && f.getFileName().equals(fileName))
					return true;
			return false;

		}

		public boolean containsCSourceFile(String fileName) {
			for (SourceFile f : sourceFiles)
				if (f instanceof CSourceFile && !(f instanceof CHeaderFile) && f.getFileName().equals(fileName))
					return true;
			return false;
		}

		public CHeaderFile getCHeaderFile(String filename) {

			// check if source file already existing and
			// return instance if so
			for (SourceFile f : sourceFiles)
				if (f instanceof CHeaderFile && f.getFileName().equals(filename))
					return (CHeaderFile) f;

			// create new instance since it's not yet existing
			CHeaderFileImpl header = new CHeaderFileImpl(filename);
			sourceFiles.add(header);

			try {

				// adding header include guard (part 1)
				String guard = filename.toUpperCase() + "_H";
				header.addBeforeDirective("ifndef " + guard);
				header.addBeforeDirective("define " + guard);

				// adding the extern "C" directive
				header.addBeforeDirective("if defined __cplusplus");
				header.addBeforeDirective(false, "extern \"C\" {");
				header.addBeforeDirective("endif");
				header.addAfterDirective("if defined __cplusplus");
				header.addAfterDirective(false, "}");
				header.addAfterDirective("endif");

				// belongs to the header include guard
				header.addAfterDirective("endif");

			} catch (Exception e) {
				log.error("" + e, e);
				e.printStackTrace();
			}

			return header;

		}

		/**
		 * Stores a new method in the store
		 * 
		 * @param domain
		 * @param aspect
		 * @param type
		 * @return
		 */
		public void setGlobalMethod(CFun fun) {
			globalMethodStoreC.add(fun);

		}

	}

	/**
	 * 
	 *
	 */
	public class JavaWorkspaceHelper {
		public JSourceFile getJSourceFile(String packageName, String fileName) {
			// check if source file already exists
			for (SourceFile f : sourceFiles)
				if (f instanceof JSourceFile && ((JSourceFile) f).getPackageName().equals(packageName)
						&& ((JSourceFile) f).getFileName().equals(fileName)) {
					log.error("Sourcefile " + fileName + " gibts schon!! SCHLECHT!");
					log.info("Folgende JSourceFiles gibt es:");
					for (SourceFile file : sourceFiles)
						if (file instanceof JSourceFile) {
							log.info("  " + file.getFileName());
						}
					return (JSourceFile) f;
				}
			JSourceFile f = new JSourceFileImpl(packageName, fileName);
			sourceFiles.add(f);
			log.info("Sourcefile " + fileName + " added to workspace");
			return f;
		}

		public boolean containsJavaClass(String clazz) {
			for (SourceFile f : sourceFiles) {
				if (f instanceof JSourceFile) {
					JSourceFile file = (JSourceFile) f;
					JClass jclazz = file.getClassByName(clazz);
					if (jclazz != null) {
						return true;
					}
				}
			}
			return false;
		}

		public String getJPackagePrefix() {
			return jPackagePrefix;
		}

		/**
		 * Stores a new method in the store
		 * 
		 * @param domain
		 * @param aspect
		 * @param type
		 * @return
		 */
		public void setGlobalMethod(JMethod method) {
			globalMethodStoreJava.add(method);
		}

	}

	public class ProtobufWorkspaceHelper {
		private static final String PACKAGE = "protobuf.package";
		private static final String DEFAULT_FILENAME= "protobuf.file";
		
		private String packageName;
		private String fileName;
		private PSourceFile defaultSourceFile = null;
		
		public ProtobufWorkspaceHelper(Properties properties) {
			packageName = properties.getProperty(PACKAGE);
			fileName = properties.getProperty(DEFAULT_FILENAME, "protobuf.prot");
		}

		public String getPackageName() {
			return packageName;
		}

		public String getFileName() {
			return fileName;
		}

		public PSourceFile getDefaultSourceFile(){

			if (defaultSourceFile == null ){
				defaultSourceFile = new PSourceFile(fileName);
				sourceFiles.add(defaultSourceFile);
			}
			
			return defaultSourceFile;
		}
		
	}

    /**
     * Workspace helper class for Graphviz dot file generation.
     * 
     * @author Marco Wegner
     */
    public class DotGraphWorkspaceHelper {
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

        /**
         * Constructs a new dot graph workspace helper.
         * 
         * @param properties
         */
        public DotGraphWorkspaceHelper(Properties properties) {
            fileName = properties.getProperty(KEY_DOT_OUTFILE);
        }

        /**
         * Returns the default source file. If no such file exists exist, then
         * it is created and added to the workspace's list of source files.
         * 
         * @return
         */
        public DGraphFile getDefaultSourceFile( ) {
            if (defaultSourceFile == null) {
                defaultSourceFile = new DGraphFile(fileName);
                sourceFiles.add(defaultSourceFile);
            }
            return this.defaultSourceFile;
        }
    }
    
    
    /**
     * Workspace helper class for Graphviz dot file generation.
     * 
     * @author Marco Wegner
     */
    public class EchoWorkspaceHelper {
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

        /**
         * Constructs a new echo workspace helper.
         * 
         * @param properties
         */
        public EchoWorkspaceHelper(Properties properties) {
            fileName = properties.getProperty(KEY_ECHO_OUTFILE);
        }

        /**
         * Returns the default source file. If no such file exists exist, then
         * it is created and added to the workspace's list of source files.
         * 
         * @return
         */
        public EchoFile getDefaultSourceFile( ) {
            if (defaultSourceFile == null) {
                defaultSourceFile = new EchoFile(fileName);
                sourceFiles.add(defaultSourceFile);
            }
            return this.defaultSourceFile;
        }
    }
    
    public class HelloWorldWorkspaceHelper {
        /**
         * Property key to retrieve the file name.
         */
        private static final String KEY_HELLOWORLD_OUTFILE = "helloworld.outfile";
        
        /**
         * Property key to retrieve the package name.
         */
        private static final String KEY_HELLOWORLD_PACKAGE = "helloworld.package";

        /**
         * The actual file name.
         */
        private String fileName;
        
        /**
         * The actual package name.
         */
        private String packageName;

        /**
         * The default file used for echo creation.
         */
        private JSourceFileImpl defaultSourceFile;

        {
            defaultSourceFile = null;
        }

        /**
         * Constructs a new echo workspace helper.
         * 
         * @param properties
         */
        public HelloWorldWorkspaceHelper(Properties properties) {
            fileName = properties.getProperty(KEY_HELLOWORLD_OUTFILE);
            packageName = properties.getProperty(KEY_HELLOWORLD_PACKAGE);
        }

        /**
         * Returns the default source file. If no such file exists exist, then
         * it is created and added to the workspace's list of source files.
         * 
         * @return
         */
        public JSourceFileImpl getDefaultSourceFile( ) {
            if (defaultSourceFile == null) {
                defaultSourceFile = new JSourceFileImpl(packageName,fileName);
                sourceFiles.add(defaultSourceFile);
            }
            return this.defaultSourceFile;
        }
    }
}
