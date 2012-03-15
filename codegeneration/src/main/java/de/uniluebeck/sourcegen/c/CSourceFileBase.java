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

import de.uniluebeck.sourcegen.ElemImpl;
import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;


public class CSourceFileBase extends ElemImpl {

	/**
	 * a list of preprocessor directives printed after the source file main
	 * content
	 */
	public LinkedList<CPreProcessorDirectiveImpl> afterDirectives;

	/**
	 * a list of preprocessor directives printed before the source file main
	 * content
	 */
	public LinkedList<CPreProcessorDirectiveImpl> beforeDirectives;

	/**
	 * a list of enums contained in this source file
	 */
	public LinkedList<CEnumImpl> enums;

	/**
	 * the name of this source file
	 */
	public String fileName;

	/**
	 * a list of forward declarations
	 */
	public LinkedList<CFunImpl> forwardDeclarations;

	/**
	 * a list of funs contained in this source file
	 */
	public LinkedList<CFunImpl> funs;

	/**
	 * a list of global declarations for this source file
	 */
	public LinkedList<String> globalDeclarations;

	/**
	 * a list of header files
	 */
	public LinkedList<CHeaderFileImpl> includes;

	/**
	 * a list of header library files to be included
	 */
	public LinkedList<CppInclude> libIncludes;

	/**
	 * a list of structs and unions contained in this source file, which are
	 * kept together in one list in order to retain their order of declaration
	 * in the generated source file
	 */
	public LinkedList<CStructBaseImpl> structsUnions;

	public LinkedList<CTypeDef> typeDefs;

	/**
	 * Copy constructor;
	 *
	 * @param sourceFile
	 */
	public CSourceFileBase(CSourceFileBase other, boolean copyFuns) {
		afterDirectives 	= new LinkedList<CPreProcessorDirectiveImpl>(other.afterDirectives);
		beforeDirectives 	= new LinkedList<CPreProcessorDirectiveImpl>(other.beforeDirectives);
		enums 				= new LinkedList<CEnumImpl>(other.enums);
		fileName 			= new String(other.fileName);
		forwardDeclarations = new LinkedList<CFunImpl>(other.forwardDeclarations);
		globalDeclarations 	= new LinkedList<String>(globalDeclarations);
		includes 			= new LinkedList<CHeaderFileImpl>(other.includes);
		libIncludes			= new LinkedList<CppInclude>(other.libIncludes);
		structsUnions		= new LinkedList<CStructBaseImpl>(other.structsUnions);
		typeDefs			= new LinkedList<CTypeDef>(other.typeDefs);

		if (copyFuns) funs = new LinkedList<CFunImpl>(other.funs);
		else funs = new LinkedList<CFunImpl>();
	}

	/**
	 * Standard constructor
	 *
	 * @param newFileName
	 */
	public CSourceFileBase() {
		afterDirectives 	= new LinkedList<CPreProcessorDirectiveImpl>();
		beforeDirectives 	= new LinkedList<CPreProcessorDirectiveImpl>();
		enums 				= new LinkedList<CEnumImpl>();
		forwardDeclarations = new LinkedList<CFunImpl>();
		funs				= new LinkedList<CFunImpl>();
		globalDeclarations 	= new LinkedList<String>();
		includes 			= new LinkedList<CHeaderFileImpl>();
		libIncludes			= new LinkedList<CppInclude>();
		structsUnions		= new LinkedList<CStructBaseImpl>();
		typeDefs			= new LinkedList<CTypeDef>();
	}

	public boolean equals(CSourceFileBase other) {
		return fileName.equals(other.fileName);
	}

	public LinkedList<CPreProcessorDirectiveImpl> getAfterDirectives() {
		return afterDirectives;
	}

	public LinkedList<CPreProcessorDirectiveImpl> getBeforeDirectives() {
		return beforeDirectives;
	}

	public LinkedList<CEnumImpl> getEnums() {
		return enums;
	}

	public String getFileName() {
		return fileName;
	}

	public LinkedList<CFunImpl> getForwardDeclarations() {
		return forwardDeclarations;
	}

	public LinkedList<CFunImpl> getFuns() {
		return funs;
	}

	public LinkedList<CTypeDef> getTypeDefs() {
		return typeDefs;
	}

	public LinkedList<String> getGlobalDeclarations() {
		return globalDeclarations;
	}

	public LinkedList<CHeaderFileImpl> getIncludes() {
		return includes;
	}

	public LinkedList<CppInclude> getLibIncludes() {
		return libIncludes;
	}

	public LinkedList<CStructBaseImpl> getStructsUnions() {
		return structsUnions;
	}

	public void internalAddAfterDirective(boolean hash, String... directives) throws CPreProcessorValidationException {
		for (String directive : directives)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(hash, directive));
	}

	public void internalAddAfterDirective(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.afterDirectives.add((CPreProcessorDirectiveImpl)d);
	}

	public void internalAddAfterDirective(String... directive) throws CPreProcessorValidationException {
		for (String d : directive)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(d));
	}

	public void internalAddBeforeDirective(boolean hash, String... directives) throws CPreProcessorValidationException {
		for (String directive : directives)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(hash, directive));
	}

	public void internalAddBeforeDirective(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.beforeDirectives.add((CPreProcessorDirectiveImpl)d);
	}

	public void internalAddBeforeDirective(String... directives)
			throws CPreProcessorValidationException {
		for (String d : directives)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(d));
	}

	public void internalAddEnum(CEnum... enums) throws CDuplicateException {

		for (CEnum e : enums) {
			if (internalContainsEnum(e))
				throw new CDuplicateException("Duplicate enum " + ((CEnumImpl)e).getName());

			this.enums.add((CEnumImpl)e);
		}

	}

	public void internalAddForwardDeclaration(CFun... functions) throws CDuplicateException {

		for (CFun f : functions) {
			if (internalContainsForwardDeclaration(f))
				throw new CDuplicateException("Duplicate forward declaration of function " + ((CFunImpl)f).getName());
			this.forwardDeclarations.add((CFunImpl)f);
		}

	}

	public void internalAddFun(CFun... functions) throws CDuplicateException {

		for (CFun f : functions) {
			if (internalContainsFun(f))
				throw new CDuplicateException("Duplicate function " + ((CFunImpl)f).getName());

			this.funs.add((CFunImpl)f);
		}

	}

	public void internalAddGlobalDeclaration(String... declarations) throws CCodeValidationException {

		for (String d : declarations) {
			// TODO: implement validation
			// remove SuppressWarnings(!)
			globalDeclarations.add(d);
		}

	}

	public void internalAddInclude(CHeaderFile... headerFile) throws CDuplicateException {

		for (CHeaderFile hf : headerFile) {
			if (internalContainsInclude(hf))
				throw new CDuplicateException("Duplicate header file include of " + fileName);

			includes.add((CHeaderFileImpl)hf);
		}

	}

	public void internalAddLibInclude(CppInclude... fileNames) throws CDuplicateException {
	    // Check for error
		for (CppInclude inc : fileNames) {
		    String[] includes = inc.include;
		    for (String fn : includes) {
		        if (internalContainsLibInclude(fn))
		            throw new CDuplicateException("Duplicate header file include of " + fn);
            }
		    libIncludes.add(inc);
		}

	}

	public void internalAddStruct(CStruct... structs) throws CDuplicateException {

		for (CStruct s : structs) {
			if (internalContainsStruct(s))
				throw new CDuplicateException("Duplicate struct " + ((CStructImpl)s).getName());

			structsUnions.add((CStructImpl)s);
		}

	}

	public void internalAddUnion(CUnion... unions) throws CDuplicateException {

		for (CUnion u : unions) {
			if (internalContainsUnion(u))
				throw new CDuplicateException("Duplicate structUnion " + ((CUnionImpl)u).getName());

			structsUnions.add((CUnionImpl)u);
		}

	}

	public void internalTypedef(CTypeDef... typedefs) throws CDuplicateException {

		for (CTypeDef t : typedefs) {
			if (internalContainsTypeDef(t)) {
				throw new CDuplicateException("Duplicate Typedef " + ((CTypeDef)t).getAlias());
			}
			this.typeDefs.add(t);
		}

	}

	public boolean internalContainsAfterDirective(CPreProcessorDirective directive) {
		return internalContainsAfterDirective(directive.toString());
	}

	public boolean internalContainsAfterDirective(String directive) {

		for (CPreProcessorDirective d : afterDirectives)
			if (d.toString().equals(directive))
				return true;

		return false;

	}

	public boolean internalContainsBeforeDirective(CPreProcessorDirective directive) {
		return internalContainsBeforeDirective(directive.toString());
	}

	public boolean internalContainsBeforeDirective(String directive) {

		for (CPreProcessorDirective d : beforeDirectives)
			if (d.toString().equals(directive))
				return true;

		return false;

	}

	public boolean internalContainsEnum(CEnum enumObj) {
		return internalContainsEnum(((CEnumImpl)enumObj).getName());
	}

	public boolean internalContainsEnum(String name) {
		return internalGetEnumByName(name) != null;
	}

	public boolean internalContainsForwardDeclaration(CFun function) {
		if (internalGetForwardDeclarationByName(((CFunImpl)function).getName()) != null)
			return true;
		return false;
	}

	public boolean internalContainsFun(CFun function) {
		return internalContainsFunction(((CFunImpl)function).getName());
	}

	public boolean internalContainsFunction(String name) {
		return internalGetFunctionByName(name) != null;
	}

	public boolean internalContainsGlobalDeclaration(String declaration) {

		for (String s : globalDeclarations)
			if (s.equals(declaration))
				return true;

		return false;

	}

	public boolean internalContainsInclude(CHeaderFile headerFile) {

		for (CHeaderFile f : includes)
			if (f.equals(headerFile))
				return true;

		return false;

	}

	public boolean internalContainsLibInclude(String fileName) {

		for (CppInclude inc : libIncludes) {
		    String[] includes = inc.include;
		    for (String s : includes) {
		        if (s.equals(fileName))
		            return true;
            }
		}

		return false;
	}

	public boolean internalContainsStruct(CStruct struct) {
		return internalContainsStruct(((CStructImpl)struct).getName());
	}

	public boolean internalContainsStruct(String name) {
		return internalGetStructByName(name) != null;
	}

	public boolean internalContainsUnion(CUnion union) {
		return internalContainsUnion(((CUnionImpl)union).getName());
	}

	public boolean internalContainsUnion(String name) {
		return internalGetUnionByName(name) != null;
	}

	public boolean internalContainsTypeDef(CTypeDef type) {
		return internalContainsTypeDef(((CTypeDef)type).getAlias());
	}

	public boolean internalContainsTypeDef(String name) {
		return internalGetTypeDefByName(name) != null;
	}

	/**
	 * Returns the list of directives after the function.
	 *
	 * @return the list of directives after the function
	 */
	public CPreProcessorDirective[] internalGetAfterDirectives() {
		return afterDirectives.toArray(new CPreProcessorDirectiveImpl[afterDirectives.size()]);
	}

	/**
	 * Returns the preprocessor beforeDirectives.
	 *
	 * @return the preprocessor beforeDirectives
	 */
	public CPreProcessorDirective[] internalGetBeforeDirectives() {
		return beforeDirectives.toArray(new CPreProcessorDirectiveImpl[beforeDirectives.size()]);
	}

	/**
	 * Returns the enum with the name <code>name</code>.
	 *
	 * @param name
	 *            the name of the enum to search for
	 * @return a <code>CEnum</code> instance or <code>null</code> if an enum
	 *         with the name <code>name</code> was not found
	 */
	public CEnum internalGetEnumByName(String name) {

		for (CEnum e : enums)
			if (((CEnumImpl)e).getName().equals(name))
				return e;

		return null;

	}

	/**
	 * Returns the list of enums contained in this source file.
	 *
	 * @return the list of enums contained in this source file
	 */
	public CEnum[] internalGetEnums() {
		return enums.toArray(new CEnum[enums.size()]);
	}

	/**
	 * Returns the name of this C source file.
	 *
	 * @return the name of this C source file
	 */
	public String internalGetFileName() {
		return fileName;
	}

	/**
	 * Returns the forward declaration of the function named
	 * <code>functionName</code>.
	 *
	 * @param functionName
	 *            the name of the function
	 * @return a <code>CFunImpl</code> instance or <code>null</code> if no
	 *         function with the name <code>functionName</code> was found
	 */
	public CFun internalGetForwardDeclarationByName(String functionName) {
		for (CFun f : forwardDeclarations)
			if (((CFunImpl)f).getName().equals(functionName))
				return f;
		return null;
	}

	/**
	 * Returns the list of forward declarations.
	 *
	 * @return the list of forward declarations
	 */
	public CFun[] internalGetForwardDeclarations() {
		return forwardDeclarations.toArray(new CFunImpl[forwardDeclarations.size()]);
	}

	/**
	 * Returns the function with the name <code>name</code>.
	 *
	 * @param name
	 *            the funs' name
	 * @return a <code>CFunImpl</code> instance or <code>null</code> if no
	 *         function of the name <code>name</code> was found
	 */
	public CFun internalGetFunctionByName(String name) {

		for (CFun f : funs)
			if (((CFunImpl)f).getName().equals(name))
				return f;

		return null;

	}

	/**
	 * Returns the funs of this C source file.
	 *
	 * @return the funs of this C source file
	 */
	public CFun[] internalGetFunctions() {
		return funs.toArray(new CFunImpl[funs.size()]);
	}

	/**
	 * Returns the list of global variable declarations.
	 *
	 * @return the list of global variable declarations
	 */
	public String[] internalGetGlobalDeclarations() {

		return globalDeclarations.toArray(new String[globalDeclarations.size()]);

	}

	/**
	 * Returns the included header files.
	 *
	 * @return the included header files
	 */
	public CHeaderFile[] internalGetIncludes() {
		return includes.toArray(new CHeaderFileImpl[includes.size()]);
	}

	/**
	 * Returns the included header files from libs.
	 *
	 * @return the included header files from libs
	 */
	public String[] internalGetLibIncludes() {
		return libIncludes.toArray(new String[libIncludes.size()]);
	}

	/**
	 * Returns the struct with the name <code>name</code>.
	 *
	 * @param name
	 *            the name of the struct to search for
	 * @return a <code>CStructImpl</code> instance or <code>null</code> if no
	 *         struct of the name <code>name</code> was found
	 */
	public CStruct internalGetStructByName(String name) {

		for (CStructBase s : structsUnions)
			if (s instanceof CStruct && ((CStructImpl) s).getName().equals(name))
				return (CStruct) s;

		return null;

	}

	/**
	 * Returns the structsUnions of this C source file.
	 *
	 * @return the structsUnions of this C source file
	 */
	public CStructBase[] internalGetStructsUnions() {
		return structsUnions.toArray(new CStructBaseImpl[structsUnions.size()]);
	}

	/**
	 * Returns the structUnion with the name <code>name</code>.
	 *
	 * @param name
	 *            the name to check for
	 * @return a <code>CUnionImpl</code> instance or <code>null</code> if no
	 *         structUnion with the name <code>name</code> was found
	 */

	public CUnion internalGetUnionByName(String name) {

		for (CStructBase u : structsUnions)
			if (u instanceof CUnion && ((CUnionImpl) u).getName().equals(name))
				return (CUnion) u;

		return null;

	}

	public CTypeDef internalGetTypeDefByName(String name) {
		for (CTypeDef u : typeDefs)
			if (u instanceof CTypeDef && ((CTypeDef) u).getAlias().equals(name))
				return (CTypeDef) u;

		return null;
	}


	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		if(beforeDirectives.size() > 0) {
			toString(buffer, tabCount, beforeDirectives, "", "\n");
			buffer.append("\n\n");
		}

		if (includes.size() > 0) {
			for (CHeaderFileImpl header : includes) {
				indent(buffer, tabCount);
				buffer.append("#include \"");
				buffer.append(header.getFileName());
				buffer.append(".h\"\n");
			}
			buffer.append("\n");
		}

		if (libIncludes.size() > 0) {
			for (CppInclude i : libIncludes) {

			    String[] includes = i.include;

			    // TODO: before directive
			    for (String s : includes) {
			        indent(buffer, tabCount);
			        buffer.append("#include <");
			        buffer.append(s);
			        buffer.append(">\n");
                }
			    // TODO: after directive
			}
			buffer.append("\n");
		}

		if (globalDeclarations.size() > 0) {
			for (String d : globalDeclarations) {
				indent(buffer, tabCount);
				buffer.append(d);
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		if (enums.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, enums);
			buffer.append("\n");
		}
		if (structsUnions.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, structsUnions);
			buffer.append("\n");
		}
		if (forwardDeclarations.size() > 0) {
			buffer.append("\n");
			for (CFunImpl f : forwardDeclarations) {
				f.toStringForwardDecl(buffer, tabCount);
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		if (funs.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, funs);
			buffer.append("\n");
		}
		if (afterDirectives.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, afterDirectives, "", "\n");
			buffer.append("\n");
		}

	}

}
