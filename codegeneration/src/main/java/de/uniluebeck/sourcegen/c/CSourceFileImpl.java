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

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;



/**
 * Class representing a C source file.
 * 
 * @author Daniel Bimschas
 */
//TODO make package private. it's public because of Workspace::getCSourceFile. (new CSourceFileImpl(fileName);)
public class CSourceFileImpl extends CElemImpl implements CSourceFile {

	//private final org.slf4j.Logger log = LoggerFactory.getLogger(CSourceFileImpl.class);

	private CSourceFileBase base;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	/**
	 * Constructs a new <code>CSourceFileImpl</code> instance with the filename
	 * <code>fileName</code>.
	 * 
	 * @param fileName
	 *            the filename
	 */
	public CSourceFileImpl(String fileName) {
		this.fileName = fileName;
		base = new CSourceFileBase();
	}

	public CSourceFile addAfterDirective(boolean hash, String... directive) throws CPreProcessorValidationException {
		base.internalAddAfterDirective(hash, directive);
		return this;
	}
	
	public CSourceFile addAfterDirective(CPreProcessorDirective... directives) {
		base.internalAddAfterDirective(directives);
		return this;
	}

	public CSourceFile addAfterDirective(String... directive) throws CPreProcessorValidationException {
		base.internalAddAfterDirective(directive);
		return this;
	}

	public CSourceFile addBeforeDirective(boolean hash, String... directives) throws CPreProcessorValidationException {
		base.internalAddBeforeDirective(hash, directives);
		return this;
	}

	public CSourceFile addBeforeDirective(CPreProcessorDirective... directives) {
		base.internalAddBeforeDirective(directives);
		return this;
	}

	public CSourceFile addBeforeDirective(String... directive) throws CPreProcessorValidationException {
		base.internalAddBeforeDirective(directive);
		return this;
	}

	public CSourceFile addEnum(CEnum... enums) throws CDuplicateException {
		base.internalAddEnum(enums);
		return this;
	}

	public CSourceFile addForwardDeclaration(CFun... functions) throws CDuplicateException {
		base.internalAddForwardDeclaration(functions);
		return this;
	}

	public CSourceFile addFun(CFun... fun) throws CDuplicateException {
		base.internalAddFun(fun);
		return this;
	}

	public CSourceFile addGlobalDeclaration(String... declarations) throws CCodeValidationException {
		base.internalAddGlobalDeclaration(declarations);
		return this;
	}

	public CSourceFile addInclude(CHeaderFile... headerFile) throws CDuplicateException {
		base.internalAddInclude(headerFile);
		return this;
	}

	public CSourceFile addLibInclude(String... fileNames) throws CDuplicateException {
		base.internalAddLibInclude(fileNames);
		return this;
	}

	public CSourceFile addStruct(CStruct... structs) throws CDuplicateException {
		base.internalAddStruct(structs);
		return this;
	}

	public CSourceFile addUnion(CUnion... unions) throws CDuplicateException {
		base.internalAddUnion(unions);
		return this;
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

	public boolean containsEnum(CEnum enumObj) {
		return base.internalContainsEnum(enumObj);
	}

	public boolean containsEnum(String name) {
		return base.internalContainsEnum(name);
	}

	public boolean containsForwardDeclaration(CFun function) {
		return base.internalContainsForwardDeclaration(function);
	}

	public boolean containsFun(CFun fun) {
		return base.internalContainsFun(fun);
	}

	public boolean containsFun(String name) {
		return base.internalContainsFunction(name);
	}

	public boolean containsGlobalDeclaration(String declaration) {
		return base.internalContainsGlobalDeclaration(declaration);
	}

	public boolean containsInclude(CHeaderFile headerFile) {
		return base.internalContainsInclude(headerFile);
	}

	public boolean containsLibInclude(String fileName) {
		return base.internalContainsLibInclude(fileName);
	}

	public boolean containsStruct(CStruct struct) {
		return base.internalContainsStruct(struct);
	}

	public boolean containsStruct(String name) {
		return base.internalContainsStruct(name);
	}

	public boolean containsUnion(CUnion union) {
		return base.internalContainsUnion(union);
	}

	public boolean containsUnion(String name) {
		return base.internalContainsUnion(name);
	}

	public boolean equals(CSourceFile other) {
		return fileName.equals(((CSourceFileImpl)other).fileName);
	}
	
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		base.toString(buffer, tabCount);
	}

	public CSourceFileBase getBase() {
		return base;
	}


}
