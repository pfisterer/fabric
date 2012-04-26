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
package fabric.core.filegen.cpp;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppSourceFileImpl;
import fabric.core.filegen.base.SourceFileGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Abstract class for generating the expected CppSourceFile objects of a test case.
 */
public abstract class CppSourceFileGenerator extends SourceFileGenerator {
    /**
     * CppClass objects generated for the test XSD
     */
    List<CppClass> types;

    /**
     * Constructor
     */
    public CppSourceFileGenerator(Properties properties) {
        super(properties);
        types = new LinkedList<CppClass>();
        generateClasses();
    }

    /**
     * Returns the CppSourceFile objects representing the generated containers.
     *
     * @return CppSourceFile objects in a list
     */
    @Override
    public List<SourceFile> getSourceFiles() {
        List<SourceFile> files = new LinkedList<SourceFile>();
        CppSourceFileImpl file;
        try {
            for (CppClass type : types) {
                file = new CppSourceFileImpl(type.getName());
                files.add(file.add(type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Generates the AttributeContainer objects corresponding to the test XSD.
     */
    abstract void generateClasses();
}
