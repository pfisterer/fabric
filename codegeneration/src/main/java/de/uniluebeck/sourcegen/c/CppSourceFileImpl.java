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

import java.util.LinkedList;
import java.util.List;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

//TODO make package private. it's public because of Workspace::getCppSourceFile (new CppSourceFileImpl(fileName);)
public class CppSourceFileImpl extends CElemImpl implements CppSourceFile {

	protected LinkedList<CppVar> 			cppVars;
	protected LinkedList<CppFun> 			cppFuns;
	protected LinkedList<CppClass> 		 	cppClasses;
	protected LinkedList<CppSourceFileImpl> cppUserHeaderFiles;
	protected LinkedList<String> 			cppNamespaces;

	protected CSourceFileBase base;
	protected String fileName;

	protected CComment comment = null;

	//private final org.slf4j.Logger log = LoggerFactory.getLogger(CppSourceFileImpl.class);

	public String getFileName() {
		return fileName;
	}

	public CppSourceFileImpl(String fileName) {
		this(fileName, new CSourceFileBase());
	}

	private CppSourceFileImpl(String newFileName, CSourceFileBase newBase) {
		fileName	= newFileName;
		base 		= newBase;
		cppVars 	= new LinkedList<CppVar>();
		cppFuns 	= new LinkedList<CppFun>();
		cppClasses 	= new LinkedList<CppClass>();
		cppUserHeaderFiles = new LinkedList<CppSourceFileImpl>();
		cppNamespaces = new LinkedList<String>();
	}

	public CppSourceFile add(CEnum... enums) throws CDuplicateException {
		base.internalAddEnum(enums);
		return this;
	}

	public CppSourceFile add(CFun... functions) throws CDuplicateException {
		base.internalAddFun(functions);
		return this;
	}

	public CppSourceFile add(CppClass... classes) throws CppDuplicateException {
		for (CppClass c : classes) {
			if (contains(c))
				throw new CppDuplicateException("Duplicate class " + c);
			this.cppClasses.add(c);
		}
		return this;
	}

	public CppSourceFile add(CppFun... funs) throws CppDuplicateException {
		for (CppFun fun : funs) {
			if (contains(fun))
				throw new CppDuplicateException("Duplicate function " + fun);
			this.cppFuns.add(fun);
		}
		return this;
	}

	public CppSourceFile add(CppVar... vars) throws CppDuplicateException {
		for (CppVar var : vars) {
			if (contains(var))
				throw new CppDuplicateException("Duplicate var " + var);
			this.cppVars.add(var);
		}
		return this;
	}

	public CppSourceFile add(CStruct... structs) throws CDuplicateException {
		base.internalAddStruct(structs);
		return this;
	}

	public CppSourceFile add(CUnion... unions) throws CDuplicateException {
		base.internalAddUnion(unions);
		return this;
	}

	public CppSourceFile addAfterDirective(boolean hash, String... directives) throws CPreProcessorValidationException {
		base.internalAddAfterDirective(hash, directives);
		return this;
	}

	public CppSourceFile addAfterDirective(CPreProcessorDirective... directives) {
		base.internalAddAfterDirective(directives);
		return this;
	}


	public CppSourceFile addAfterDirective(String... directive) throws CPreProcessorValidationException {
		base.internalAddAfterDirective(directive);
		return this;
	}

	public CppSourceFile addBeforeDirective(boolean hash, String... directives) throws CPreProcessorValidationException {
		base.internalAddBeforeDirective(hash, directives);
		return this;
	}

	public CppSourceFile addBeforeDirective(CPreProcessorDirective... directives) {
		base.internalAddBeforeDirective(directives);
		return this;
	}

	public CppSourceFile addBeforeDirective(String... directives) throws CPreProcessorValidationException {
		base.internalAddBeforeDirective(directives);
		return this;
	}

	public CppSourceFile addForwardDeclaration(CFun... functions) throws CDuplicateException {
		base.internalAddForwardDeclaration(functions);
		return this;
	}

	public CppSourceFile addGlobalDeclaration(String... declarations) throws CCodeValidationException {
		base.internalAddGlobalDeclaration(declarations);
		return this;
	}

	public CppSourceFile addInclude(CHeaderFile... includes) throws CppDuplicateException {
		try {
			base.internalAddInclude(includes);
		} catch (CDuplicateException e) {
			throw new CppDuplicateException(e);
		}
		return null;
	}

	/**
	 * Add user header files. Will be #include "..."
	 */
	public CppSourceFile addInclude(CppSourceFile... sourceFiles) throws CppDuplicateException {
		for (CppSourceFile csf : sourceFiles) {
			if (containsInclude(csf))
				throw new CppDuplicateException("Duplicate source file included " + csf);
			cppUserHeaderFiles.add((CppSourceFileImpl)csf);
			// We cannot use base here, beauce the type is cpp
		}
		return this;
	}

	public CppSourceFile addLibInclude(String... libIncludes) throws CppDuplicateException {
		try {
			base.internalAddLibInclude(libIncludes);
		} catch (CDuplicateException e) {
			throw new CppDuplicateException(e);
		}
		return this;
	}

	public CppSourceFile addUsingNameSpace(String... namespaces) throws CppDuplicateException {

		for (String n : namespaces) {
			if(this.cppNamespaces.contains(n)){
				throw new CppDuplicateException("Duplicate namespace " + n);
			}
			this.cppNamespaces.add(n);
		}
		return this;
	}


	public boolean contains(CEnum enumObj) {
		return base.internalContainsEnum(enumObj);
	}

	public boolean contains(CFun fun) {
		return base.internalContainsFun(fun);
	}

	public boolean contains(CppClass clazz) {
		for (CppClass c : cppClasses)
			if (c.equals(clazz))
				return true;
		return false;
	}

	public boolean contains(CppFun fun) {
		for (CppFun f : cppFuns)
			if (f.equals(fun))
				return true;
		return false;
	}

	public boolean contains(CppVar var) {
		for (CppVar v : cppVars)
			if (v.equals(var))
				return true;
		return false;
	}

	public boolean contains(CStruct struct) {
		return base.internalContainsStruct(struct);
	}

	public boolean contains(CUnion union) {
		return base.internalContainsUnion(union);
	}

	public boolean containsAfterDirective(CPreProcessorDirective directive) {
		return base.internalContainsAfterDirective(directive);
	}

	public boolean containsAfterDirective(String directive) {
		return base.internalContainsAfterDirective(directive);
	}

	public boolean containsBeforeDirective(CPreProcessorDirective directive) {
		return base.internalContainsBeforeDirective(directive);
	}

	public boolean containsBeforeDirective(String directive) {
		return base.internalContainsBeforeDirective(directive);
	}

	public boolean containsForwardDeclaration(CFun function) {
		return base.internalContainsForwardDeclaration(function);
	}

	public boolean containsGlobalDeclaration(String declaration) {
		return base.internalContainsGlobalDeclaration(declaration);
	}

	public boolean containsInclude(CHeaderFile headerFile) {
		return base.internalContainsInclude(headerFile);
	}

	public boolean containsInclude(CppSourceFile includeFile) {
		for (CppSourceFileImpl csf : cppUserHeaderFiles)
			if (csf.equals(includeFile))
				return true;
		return false;
	}

	public boolean containsLibInclude(String libInclude) {
		for (String l : base.libIncludes)
			if (l.equals(libInclude))
				return true;
		return false;
	}

	public boolean equals(CppSourceFile other) {
		return super.equals(other);
	}

	public CppClass[] getCppClasses() {
		return cppClasses.toArray(new CppClass[cppClasses.size()]);
	}

	public CppFun[] getCppFuns() {
		return cppFuns.toArray(new CppFun[cppFuns.size()]);
	}

	public CppSourceFileImpl[] getCppIncludes() {
		return cppUserHeaderFiles.toArray(new CppSourceFileImpl[cppUserHeaderFiles.size()]);
	}

	public CppVar[] getCppVars() {
		return cppVars.toArray(new CppVar[cppVars.size()]);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		/**
		 * Generates the cpp-file
		 * Note, that the header file is created in the CppClassImpl
		 */

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		// LibIncludes: System header files
	    for(String include : base.getLibIncludes()) {
	    	buffer.append("#include <" + include + ">" + Cpp.newline);
	    }

		// Before Preprocessordiretives
		if(base.beforeDirectives.size() > 0) {
			for(CPreProcessorDirectiveImpl ppd : base.beforeDirectives){
				ppd.toString(buffer, tabCount);
				buffer.append(Cpp.newline);
			}
			buffer.append(Cpp.newline);
		}

		// Includes: User header files
		for(CppSourceFile file : this.cppUserHeaderFiles){
            buffer.append("#include \"" + file.getFileName() + ".hpp\"" + Cpp.newline);
		}

		if(this.cppUserHeaderFiles.size() > 0 || this.cppNamespaces.size() > 0) {
			buffer.append(Cpp.newline);
	    }

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

		// Structs
		for(CStructBaseImpl struct : base.structsUnions){
			buffer.append(struct.toString());
			buffer.append(Cpp.newline + Cpp.newline);
		}

		//namespace
		// TODO: dynamically with a program-parameter
//		buffer.append("namespace isense {\n\n");

		for(CppSourceFileImpl file : this.cppUserHeaderFiles){
			for(CppClass clazz : file.getCppClasses()){
				toStringHelper(buffer, clazz, tabCount);
			}
		}

		// Add functions, such main is possible
		if(this.base.getFuns().size() > 0) {
			//buffer.append("/* Other non class functions */" + Cpp.newline);
			for (CFun fun : this.base.getFuns()) {
				fun.toString(buffer, tabCount);
			}
		}

		// Before Preprocessordiretives
		if(base.afterDirectives.size() > 0) {
			buffer.append(Cpp.newline);
			for(CPreProcessorDirectiveImpl ppd : base.afterDirectives){
				buffer.append(Cpp.newline);
				ppd.toString(buffer, tabCount);
			}
			buffer.append(Cpp.newline);
		}

/*
 *
 * @see: CSourceFileBase
 *
		// beforeDirectives

		if (globalDeclarations.size() > 0) {
			for (String d : globalDeclarations) {
				indent(buffer, tabCount);
				buffer.append(d);
				buffer.append("\n");
			}
			buffer.append("\n");
		}

		// ENUMS
		// STRUCT_UNIONS

		if (forwardDeclarations.size() > 0) {
			buffer.append("\n");
			for (CFunImpl f : forwardDeclarations) {
				f.toStringForwardDecl(buffer, tabCount);
				buffer.append("\n");
			}
			buffer.append("\n");
		}

		// FUNCTIONS
		// afterDirectives
*/

	}

	private void toStringHelper(StringBuffer buffer, CppClass clazz, int tabCount) {

		List<CppVar> vars = clazz.getVars(Cpp.PRIVATE);
		vars.addAll(clazz.getVars(Cpp.PUBLIC));
		vars.addAll(clazz.getVars(Cpp.PROTECTED));

		// get variables, which are initialized
		List<CppVar> initializedVars = new LinkedList<CppVar>();

		for (CppVar cppVar : vars) {
			String init = cppVar.getInit();
			if(init != null && init != "") {
				initializedVars.add(cppVar);
			}
		}

		// constructors
		if(clazz.getConstructors(Cpp.PUBLIC).size() > 0) {
			//buffer.append("/* Constructors of " + clazz.getName() + " */" + Cpp.newline);
			for(CppConstructor c : clazz.getConstructors(Cpp.PUBLIC)){
				c.setInititalVars(initializedVars);
				c.toString(buffer, tabCount);
			}
		}


		// constructors
		if(clazz.getConstructors(Cpp.PRIVATE).size() > 0) {
			//buffer.append("/* Constructors of " + clazz.getName() + " */" + Cpp.newline);
			for(CppConstructor c : clazz.getConstructors(Cpp.PRIVATE)){
				c.toString(buffer, tabCount);
			}
		}

		// destructors
		if(clazz.getDestructors(Cpp.PRIVATE).size() > 0){
			//buffer.append("/* Destrictors of " + clazz.getName() + " */" + Cpp.newline);
			for(CppDestructor d : clazz.getDestructors(Cpp.PRIVATE)){
				d.toString(buffer, tabCount);
			}
		}

		// destructors
		if(clazz.getDestructors(Cpp.PUBLIC).size() > 0){
			//buffer.append("/* Destrictors of " + clazz.getName() + " */" + Cpp.newline);
			for(CppDestructor d : clazz.getDestructors(Cpp.PUBLIC)){
				d.toString(buffer, tabCount);
			}
		}

		// public functions
		if(clazz.getFuns(Cpp.PUBLIC).size() > 0) {
			//buffer.append("/* Public functions of " + clazz.getName() + " */" + Cpp.newline);
			for(CppFun f : clazz.getFuns(Cpp.PUBLIC)){
				f.toString(buffer, tabCount);
			}
		}

		// private functions
		if(clazz.getFuns(Cpp.PRIVATE).size() > 0) {
			//buffer.append("/* Private functions of " + clazz.getName() + " */" + Cpp.newline);
			for(CppFun f : clazz.getFuns(Cpp.PRIVATE)){
				f.toString(buffer, tabCount);
			}
		}

		// public nested classes
		if(clazz.getNested(Cpp.PUBLIC).size() > 0) {
			//buffer.append(Cpp.newline + "/* Public nested classes of " + clazz.getTemplateName() + " */" + Cpp.newline);
			for(CppClass f : clazz.getNested(Cpp.PUBLIC)){
				toStringHelper(buffer, f, tabCount);
			}
		}

		// private nested classes
		if(clazz.getNested(Cpp.PRIVATE).size() > 0) {
			//buffer.append(Cpp.newline + "/* Private nested classes of " + clazz.getTemplateName() + " */" + Cpp.newline);
			for(CppClass f : clazz.getNested(Cpp.PRIVATE)){
				toStringHelper(buffer, f, tabCount);
			}
		}

	}

	@Override
	public CppSourceFile setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

}
