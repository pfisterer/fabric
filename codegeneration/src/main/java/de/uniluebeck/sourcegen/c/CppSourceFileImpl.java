/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck
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

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

//TODO make package private. it's public because of Workspace::getCppSourceFile (new CppSourceFileImpl(fileName);)
public class CppSourceFileImpl extends CElemImpl implements CppSourceFile {
	
	protected LinkedList<CppVar> 			cppVars;
	protected LinkedList<CppFun> 			cppFuns;
	protected LinkedList<CppClass> 		 	cppClasses;
	protected LinkedList<CppSourceFileImpl> cppIncludes;

	protected CSourceFileBase base;
	protected String fileName;

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
		cppIncludes = new LinkedList<CppSourceFileImpl>();
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

	public CppSourceFile addInclude(CppSourceFile... sourceFiles) throws CppDuplicateException {
		for (CppSourceFile csf : sourceFiles) {
			if (containsInclude(csf))
				throw new CppDuplicateException("Duplicate source file included " + csf);
			cppIncludes.add((CppSourceFileImpl)csf);
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
		for (CppSourceFileImpl csf : cppIncludes)
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
		return cppIncludes.toArray(new CppSourceFileImpl[cppIncludes.size()]);
	}

	public CppVar[] getCppVars() {
		return cppVars.toArray(new CppVar[cppVars.size()]);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		
		//includes
		for(CppSourceFileImpl file : this.cppIncludes){
			buffer.append("#include <" + file.getFileName().substring(0,17) + ".hpp>\n");
		}
		buffer.append("\n");
			
		//namespace
		buffer.append("namespace isense {\n\n");
		
		for(CppSourceFileImpl file : this.cppIncludes){
			for(CppClass clazz : file.cppClasses){
				//constructors
				for(CppConstructor c : clazz.getConstructors(Cpp.PUBLIC)){
					c.toString(buffer, tabCount);
				}
							
				//destructors
				for(CppDestructor d : clazz.getDestructors(Cpp.PUBLIC)){
					d.toString(buffer, tabCount);
				}
				
				//public functions
				for(CppFun f : clazz.getFuns(Cpp.PUBLIC)){
					f.toString(buffer, tabCount);
					buffer.append("}\n\n");
				}
				
				//private functions
				for(CppFun f : clazz.getFuns(Cpp.PRIVATE)){
					f.toString(buffer, tabCount);
					buffer.append("}\n\n");
				}
					
			}
		}
		buffer.append("}");
	}
}
