/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
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
package fabric.core.filegen.java;

import java.util.*;
import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import fabric.core.filegen.base.SourceFileGenerator;
import fabric.module.typegen.FabricTypeGenModule;

/**
 * Abstract class for generating the expected JSourceFile objects of a test case.
 */
public abstract class JSourceFileGenerator extends SourceFileGenerator {

    /**
     * JComplexType objects (JClass, JEnum, ...) generated for the test XSD with its required imports as a string list
     */
    HashMap<JComplexType, ArrayList<String>> types;

    /**
     * Package name
     */
    String packageName;

    /**
     * XML Framework
     */
    String xmlFramework;

    /**
     * Constructor
     */
    public JSourceFileGenerator(Properties properties) {
        super(properties);
        try {
            packageName     = properties.getProperty(FabricTypeGenModule.PACKAGE_NAME_KEY);
            xmlFramework    = properties.getProperty(FabricTypeGenModule.XML_FRAMEWORK_KEY);
            types           = new HashMap<JComplexType, ArrayList<String>>();
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
            for (JComplexType type : types.keySet()) {
                // Generate new JSourceFile object
                file = new JSourceFileImpl(packageName, type.getName());
                // Add imports to source file
                for (String requiredImport: types.get(type)) {
                    file.addImport(requiredImport);
                }
                // Add source file to list
                files.add(file.add(type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Generates the source files representing the generated types in the given workspace.
     *
     * @return True, if source files have been generated successfully; false, otherwise.
     */
    public boolean generateSourceFilesInWorkspace(Workspace workspace) {
        List<SourceFile> files = new LinkedList<SourceFile>();
        JSourceFile file;
        try {
            for (JComplexType type : types.keySet()) {
                // Get new JSourceFile object from workspace
                file = workspace.getJava().getJSourceFile(packageName, type.getName());
                // Add imports to source file
                for (String requiredImport: types.get(type)) {
                    file.addImport(requiredImport);
                }
                // Add source file to list
                files.add(file.add(type));
            }
            workspace.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    abstract void generateClasses() throws Exception;
}
