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
package de.uniluebeck.sourcegen.c;



//TODO change to package private. it is public because of Workspace::getCppHeaderFile (new CppHeaderFileImpl(fileName);)
public class CppHeaderFileImpl extends CppSourceFileImpl implements CppHeaderFile {

	public CppHeaderFileImpl(String fileName) {
		super(fileName);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		// Write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		// LibIncludes: System header files
		if (null != base.getLibIncludes() && base.getLibIncludes().size() > 0) {
      for (String include : base.getLibIncludes()) {
        buffer.append("#include <" + include + ">" + Cpp.newline);
      }
      buffer.append(Cpp.newline);
		}

		// Before Preprocessordiretives
		if (null != base.beforeDirectives && base.beforeDirectives.size() > 0) {
			for (CPreProcessorDirectiveImpl ppd : base.beforeDirectives) {
				ppd.toString(buffer, tabCount);
				buffer.append(Cpp.newline);
			}
			buffer.append(Cpp.newline);
		}

	    // Namespaces
	    if (null != this.cppNamespaces && this.cppNamespaces.size() > 0) {
	    	// Include the namespaces
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

		// TODO: Namespace
		// buffer.append("namespace isense {\n\n");

		// Classes
    if (null != this.cppClasses && this.cppClasses.size() > 0) {
      for (CppClass c : this.cppClasses) {
        buffer.append(c.toString());
      }
    }

		// TODO: Namespaces
		// buffer.append("}\n");

		// After Preprocessordiretives
		if (null != this.base && null != this.base.afterDirectives && this.base.afterDirectives.size() > 0) {
			for (CPreProcessorDirectiveImpl ppd : this.base.afterDirectives) {
				ppd.toString(buffer, tabCount);
				buffer.append(Cpp.newline);
			}
			buffer.append(Cpp.newline);
		}
	}
}
