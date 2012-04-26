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
package de.uniluebeck.sourcegen.c;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.Workspace;

public class CWorkspace {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CWorkspace.class);

    // TODO Make sure that CFun has a working Comparator
    private Set<CFun> globalMethodStoreC = new HashSet<CFun>();

    private final List<SourceFile> sourceFiles;

    public CWorkspace(Workspace w) {
        this.sourceFiles = w.getSourceFiles();
    }

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
