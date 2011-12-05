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

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		// LibIncludes: System header files
		if(base.getLibIncludes().size() > 0) {
			for(String include : base.getLibIncludes()) {
				buffer.append("#include <" + include + ">" + Cpp.newline);
			}
	    	buffer.append(Cpp.newline);
		}

		// Before Preprocessordiretives
		if(base.beforeDirectives.size() > 0) {
			for(CPreProcessorDirectiveImpl ppd : base.beforeDirectives){
				ppd.toString(buffer, tabCount);
				buffer.append(Cpp.newline);
			}
			buffer.append(Cpp.newline);
		}

	    // Namespaces
	    if(this.cppNamespaces.size() > 0) {
	    	// Include the namespaces
	    	for(String ns : this.cppNamespaces){
	    		buffer.append("using namespace " + ns + ";" + Cpp.newline);
	    	}
	    	buffer.append(Cpp.newline);
	    }

		// Enums
		if(base.getEnums().size() > 0) {
			for(CEnum e : base.getEnums()) {
				buffer.append(e.toString() + Cpp.newline + Cpp.newline);
			}
		}

		//structs
		for(CStructBaseImpl struct : base.structsUnions){
			buffer.append(struct.toString());
			buffer.append(Cpp.newline + Cpp.newline);
		}

		//namespace TODO: ordentlich machen!
		// buffer.append("namespace isense {\n\n");

		//classes
		for(CppClass c : this.cppClasses){
			buffer.append(c.toString());
		}
/*
		// buffer.append("}\n");

		//After Preprocessordirectives
		for(CPreProcessorDirectiveImpl ppd : base.afterDirectives){
			buffer.append("\n");
			ppd.toString(buffer, tabCount);
		}
*/
	}
}
