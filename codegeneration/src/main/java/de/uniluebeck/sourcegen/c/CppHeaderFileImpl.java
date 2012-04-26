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

//TODO change to package private. it is public because of Workspace::getCppHeaderFile (new CppHeaderFileImpl(fileName);)
public class CppHeaderFileImpl extends CppSourceFileImpl implements CppHeaderFile {

    public CppHeaderFileImpl(String fileName) {
        super(fileName);
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {

        prepare();

        // Write comment if necessary
        if (null != this.comment) {
            this.comment.toString(buffer, tabCount);
            buffer.append(Cpp.newline);
        }

        // LibIncludes: System header files
        if (null != this.base && null != this.base.getLibIncludes() && this.base.getLibIncludes().size() > 0) {
            for (CppInclude include: this.base.getLibIncludes()) {
                if (null != include.beforeDirective) {
                    buffer.append("#" + include.beforeDirective + Cpp.newline);
                }

                for (String file: include.include) {
                    buffer.append("#include <" + file + ">" + Cpp.newline);
                }

                if (null != include.afterDirective) {
                    buffer.append("#" + include.afterDirective + Cpp.newline);
                }
            }
            buffer.append(Cpp.newline);
        }

        // Includes: User header files
        if ((null != this.cppUserHeaderFiles && this.cppUserHeaderFiles.size() > 0) || (this.cppUserHeaderFilesStrings.size() > 0)) {
            for (CppSourceFile file: this.cppUserHeaderFiles) {
                // TODO: before directive
                buffer.append("#include \"" + file.getFileName() + ".hpp\"" + Cpp.newline);
                // TODO: after directive
            }

            for (CppInclude include: this.cppUserHeaderFilesStrings) {
                if (null != include.beforeDirective) {
                    buffer.append("#" + include.beforeDirective + Cpp.newline);
                }

                for (String file: include.include) {
                    buffer.append("#include \"" + file + "\"" + Cpp.newline);
                }

                if (null != include.afterDirective) {
                    buffer.append("#" + include.afterDirective + Cpp.newline);
                }
            }

            buffer.append(Cpp.newline);
        }

        // Before pre-processor directives
        if (null != base.beforeDirectives && base.beforeDirectives.size() > 0) {
            for (CPreProcessorDirectiveImpl ppd : base.beforeDirectives) {
                ppd.toString(buffer, tabCount);
                buffer.append(Cpp.newline);
            }
            buffer.append(Cpp.newline);
        }

        // Namespaces
        if (null != this.cppNamespaces && this.cppNamespaces.size() > 0) {
            for (String ns : this.cppNamespaces) {
                buffer.append("using namespace " + ns + ";" + Cpp.newline);
            }
            buffer.append(Cpp.newline);
        }

        // Enums
        if (null != this.base && null != this.base.getEnums() && base.getEnums().size() > 0) {
            for (CEnum e : this.base.getEnums()) {
                buffer.append(e.toString() + Cpp.newline);
            }
        }

        // Typedefs
        if (null != this.base && null != this.base.getTypeDefs() && this.base.getTypeDefs().size() > 0) {
            for (CTypeDef t : this.base.getTypeDefs()) {
                buffer.append(t.toString());
            }
            buffer.append(Cpp.newline);
        }

        // Structs
        if (null != this.base && null != this.base.structsUnions && this.base.structsUnions.size() > 0) {
            for (CStructBaseImpl struct : this.base.structsUnions) {
                buffer.append(struct.toString());
                buffer.append(Cpp.newline + Cpp.newline);
            }
        }

        // Namespace definitions
        if (null != this.cppNamespace && this.cppNamespace.size() > 0) {
            for (CppNamespace ns : this.cppNamespace) {
                buffer.append(ns.toString());
            }
            buffer.append(Cpp.newline);
        }

        // Classes
        if (null != this.cppClasses && this.cppClasses.size() > 0) {
            for (CppClass c : this.cppClasses) {
                buffer.append(c.toString());
            }
        }

        // After pre-processor directives
        if (null != this.base && null != this.base.afterDirectives && this.base.afterDirectives.size() > 0) {
            for (CPreProcessorDirectiveImpl ppd : this.base.afterDirectives) {
                ppd.toString(buffer, tabCount);
                buffer.append(Cpp.newline);
            }
        }
    }
}
