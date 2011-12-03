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

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;



public interface CppSourceFile extends SourceFile, CppElem {

	public CppSourceFile 	add							(CEnum... enums) 						throws CDuplicateException;
	public CppSourceFile 	add							(CFun... function) 						throws CDuplicateException;
	public CppSourceFile 	add							(CppClass... classes) 					throws CppDuplicateException;
	public CppSourceFile 	add							(CppFun... funs) 						throws CppDuplicateException;
	public CppSourceFile 	add							(CppVar... vars) 						throws CppDuplicateException;
	public CppSourceFile 	add 						(CStruct... structs) 					throws CDuplicateException;
	public CppSourceFile 	add 						(CUnion... unions) 						throws CDuplicateException;
	public CppSourceFile 	addAfterDirective			(boolean hash, String... directive) 	throws CPreProcessorValidationException;
	public CppSourceFile 	addAfterDirective			(CPreProcessorDirective... directives)	;
	public CppSourceFile 	addAfterDirective			(String... directive) 					throws CPreProcessorValidationException;
	public CppSourceFile 	addBeforeDirective			(boolean hash, String... directive) 	throws CPreProcessorValidationException;
	public CppSourceFile 	addBeforeDirective			(CPreProcessorDirective... directives)	;
	public CppSourceFile 	addBeforeDirective			(String... directive) 					throws CPreProcessorValidationException;
	public CppSourceFile 	addForwardDeclaration		(CFun... function) 						throws CDuplicateException;
	public CppSourceFile 	addGlobalDeclaration		(String... declaration) 				throws CCodeValidationException;
	public CppSourceFile	addInclude					(CHeaderFile... includes)				throws CppDuplicateException;
	public CppSourceFile 	addInclude					(CppSourceFile... sourceFile) 			throws CppDuplicateException;
	public CppSourceFile 	addLibInclude				(String... libIncludes)					throws CppDuplicateException;
	public CppSourceFile 	addUsingNameSpace			(String... libIncludes)					throws CppDuplicateException;
	public CppSourceFile 	setComment(CComment comment);


	public boolean 			contains					(CEnum enumObj);
	public boolean 			contains					(CFun function);
	public boolean			contains					(CppClass clazz);
	public boolean 			contains					(CppFun fun);
	public boolean 			contains					(CppVar var);
	public boolean 			contains					(CStruct struct);
	public boolean 			contains					(CUnion union);
	public boolean 			containsAfterDirective		(CPreProcessorDirective directive);
	public boolean 			containsAfterDirective		(String directive);
	public boolean 			containsBeforeDirective		(CPreProcessorDirective directive);
	public boolean 			containsBeforeDirective		(String directive);
	public boolean 			containsForwardDeclaration	(CFun function);
	public boolean 			containsGlobalDeclaration	(String declaration);
	public boolean 			containsInclude				(CHeaderFile headerFile);
	public boolean			containsInclude				(CppSourceFile include);
	public boolean 			containsLibInclude			(String fileName);

	public boolean 			equals						(CppSourceFile other);
}
