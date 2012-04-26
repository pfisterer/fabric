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
package de.uniluebeck.sourcegen.java;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

public class JavaWorkspace {

    // TODO Make sure that JMethod has a working Comparator
    private Set<JMethod> globalMethodStoreJava = new HashSet<JMethod>();

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(JavaWorkspace.class);
    private final List<SourceFile> sourceFiles;

    public JavaWorkspace(Workspace w) {
        this.sourceFiles = w.getSourceFiles();
    }

    public JSourceFile getJSourceFile(String packageName, String fileName) {
            // check if source file already exists
            for (SourceFile f : this.sourceFiles) {
                    if (f instanceof JSourceFile && ((JSourceFile) f).getPackageName().equals(packageName)
                                    && ((JSourceFile) f).getFileName().equals(fileName)) {
                            log.error("Sourcefile " + fileName + " gibts schon!! SCHLECHT!");
                            log.info("Folgende JSourceFiles gibt es:");
                            for (SourceFile file : this.sourceFiles)
                                    if (file instanceof JSourceFile) {
                                            log.info("  " + file.getFileName());
                                    }
                            return (JSourceFile) f;
                    }
            }
            JSourceFile f = new JSourceFileImpl(packageName, fileName);
            this.sourceFiles.add(f);
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

