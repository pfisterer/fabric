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
import de.uniluebeck.sourcegen.exceptions.ValidationException;

public interface CSourceFile extends SourceFile, CElem {
	
	/**
	 * TODO: javadoc
	 * 
	 * @param directive
	 * @throws CPreProcessorValidationException
	 */
	public CSourceFile addAfterDirective(String... directive)
		throws CPreProcessorValidationException;
	
	/**
	 * Adds an array of C preprocessor directives which will be printed out at
	 * the very end of the resulting C source file.
	 * 
	 * @param directives
	 * 				the directives
	 */
	public CSourceFile addAfterDirective(CPreProcessorDirective... directives);

	/**
	 * TODO: javadoc
	 * 
	 * Test: {@link CSourceFileTest#testAddAfterDirectiveStringBoolean()}
	 * @param hash
	 * @param directive
	 * 
	 * @throws CPreProcessorValidationException
	 */
	public CSourceFile addAfterDirective(boolean hash, String... directive)
			throws CPreProcessorValidationException;

	/**
	 * Adds an array of C preprocessor directives which will be printed out at
	 * the very top of the resulting C source file.
	 * 
	 * @param directives
	 * 				the directives
	 */
	public CSourceFile addBeforeDirective(CPreProcessorDirective... directives);

	/**
	 * Adds a C preprocessor directive which will be printed out at the very top
	 * of the resulting C source file.
	 * 
	 * @param directive
	 *            the directive
	 * @throws ValidationException
	 *             if syntax validation fails
	 */
	public CSourceFile addBeforeDirective(String... directive)
			throws CPreProcessorValidationException;

	/**
	 * TODO: javadoc
	 * 
	 * Test: {@link CSourceFileTest#testAddBeforeDirectiveStringBoolean()}
	 * @param hash
	 * @param directive
	 * 
	 * @throws CPreProcessorValidationException
	 */
	public CSourceFile addBeforeDirective(boolean hash, String... directive)
			throws CPreProcessorValidationException;

	/**
	 * Adds an array of enums to this source file.
	 * 
	 * @param enums
	 * 			   the enums to add
	 * @throws CDuplicateException
	 *             if an enum of that name is already contained in this source
	 *             file
	 */
	public CSourceFile addEnum(CEnum... enums) throws CDuplicateException;

	/**
	 * Adds a forward declaration for the function <code>function</code>.
	 * 
	 * @param function
	 *            the function to declare forward
	 * @throws CDuplicateException
	 *             if the function is already declared forward
	 */
	public CSourceFile addForwardDeclaration(CFun... function)
			throws CDuplicateException;

	/**
	 * Adds a function to this source file.
	 * 
	 * @param function
	 *            the <code>CFunImpl</code> instance to add to this workspace
	 * @throws CDuplicateException
	 *             if a function of the same name is already contained in this
	 *             workspace
	 */
	public CSourceFile addFun(CFun... function)
			throws CDuplicateException;

	/**
	 * Adds a global variable declaration to this source file.
	 * 
	 * @param declaration
	 *            the declaration to add
	 * @throws CCodeValidationException
	 * 				if syntax validation of the declaration fails
	 */
	public CSourceFile addGlobalDeclaration(String... declaration)
			throws CCodeValidationException;

	/**
	 * Adds an array of header files to be included in the resulting source code.s
	 * 
	 * @param headerFile
	 *            the header files to be included
	 * @throws CDuplicateException
	 *             if one of the header files is already included in this
	 *             source file
	 */
	public CSourceFile addInclude(CHeaderFile... headerFile)
			throws CDuplicateException;

	/**
	 * Adds an array of header files from the library to be included in the
	 * resulting source code.
	 * 
	 * @param fileNames
	 *            the header files to be included
	 * @throws CDuplicateException
	 *            if one of the header files is already included in this
	 *            source file
	 */
	public CSourceFile addLibInclude(String... fileNames)
			throws CDuplicateException;

	/**
	 * Adds a struct to this source file.
	 * 
	 * @param structs
	 *            the structs to add
	 * @throws CDuplicateException
	 *            if one of the structs of that name is already contained
	 *            in this source file
	 */
	public CSourceFile addStruct(CStruct... structs)
			throws CDuplicateException;

	/**
	 * Adds an array of unions to this source file.
	 * 
	 * @param unions
	 * 			the unions to add
	 * @throws CDuplicateException
	 * 			if one of the unions of that name is already contained
	 * 			in this source file
	 */
	public CSourceFile addUnion(CUnion... unions) throws CDuplicateException;

	/**
	 * Tests if the before directive is contained in this source file.
	 * 
	 * @param directive
	 *            the directive to search for
	 * @return <code>true</code> if a String-equal before directive is already
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsAfterDirective(CPreProcessorDirective directive);

	/**
	 * Tests if the before directive is contained in this source file.
	 * 
	 * @param directive
	 *            the directive to search for
	 * @return <code>true</code> if a String-equal before directive is already
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsAfterDirective(String directive);

	/**
	 * Tests if the before directive is contained in this source file.
	 * 
	 * @param directive
	 *            the directive to search for
	 * @return <code>true</code> if a String-equal before directive is already
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsBeforeDirective(CPreProcessorDirective directive);

	/**
	 * Tests if the before directive is contained in this source file.
	 * 
	 * @param directive
	 *            the directive to search for
	 * @return <code>true</code> if a String-equal before directive is already
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsBeforeDirective(String directive);

	/**
	 * Checks if this source file contains an enum named <code>name</code>.
	 * 
	 * @param enumObj
	 *            the enum to check for
	 * @return <code>true</code> if this source file contains an enum named
	 *         <code>name</code>, <code>false</code> otherwise
	 */
	public boolean containsEnum(CEnum enumObj);

	/**
	 * Checks if this source file contains an enum named <code>name</code>.
	 * 
	 * @param name
	 *            the name to check for
	 * @return <code>true</code> if this source file contains an enum named
	 *         <code>name</code>, <code>false</code> otherwise
	 */
	public boolean containsEnum(String name);

	/**
	 * Checks if the function <code>function</code> is declared foward.
	 * 
	 * @param function
	 *            the function to check for
	 * @return <code>true</code> if <code>function</code> is declared
	 *         forward, <code>false</code> otherwise
	 */
	public boolean containsForwardDeclaration(CFun function);

	/**
	 * Checks if this source file already contains a function named
	 * <code>function.getName()</code>.
	 * 
	 * @param function
	 *            the function to check for
	 * @return <code>true</code> if this source file contains a function named
	 *         <code>name</code>, <code>false</code> otherwise
	 */
	public boolean containsFun(CFun function);

	/**
	 * Checks if this source file contains a function named <code>name</code>.
	 * 
	 * @param name
	 *            the name to check for
	 * @return <code>true</code> if this source file contains a function named
	 *         <code>name</code>, <code>false</code> otherwise
	 */
	public boolean containsFun(String name);

	/**
	 * Checks if a String-equal global variable declaration is contained in this
	 * source file.
	 * 
	 * @param declaration
	 *            the global variable declaration to check for
	 * @return <code>true</code> if a String-equal declaration is contained in
	 *         this source file, <code>false</code> otherwise
	 */
	public boolean containsGlobalDeclaration(String declaration);

	/**
	 * Checks if this source file contains the header file
	 * <code>headerFile</code>.
	 * 
	 * @param headerFile
	 *            the header file to check for
	 * @return <code>true</code> if this source file contains the header file,
	 *         <code>false</code> otherwise
	 */
	public boolean containsInclude(CHeaderFile headerFile);

	/**
	 * Checks if this source file includes the header file <code>fileName</code>.
	 * 
	 * @param fileName
	 *            the filename to check for
	 * @return <code>true</code> if this source file is includes
	 *         <code>fileName</code>, <code>false</code> otherwise
	 */
	public boolean containsLibInclude(String fileName);

	/**
	 * Returns <code>true</code> if a struct of name
	 * <code>struct.getName()</code> is contained in this source file.
	 * 
	 * @param struct
	 *            the struct to check for
	 * @return <code>true</code> if a struct of name <code>name</code> is
	 *         contained in this source file
	 */
	public boolean containsStruct(CStruct struct);

	/**
	 * Returns <code>true</code> if a struct of name <code>name</code> is
	 * contained in this source file.
	 * 
	 * @param name
	 *            the name to check for
	 * @return <code>true</code> if a struct of name <code>name</code> is
	 *         contained in this source file
	 */
	public boolean containsStruct(String name);

	/**
	 * Checks if a structUnion of the name <code>structUnion.getName()</code> is contained
	 * in this source file.
	 * 
	 * @param structUnion
	 *            the structUnion to check for
	 * @return <code>true</code> if a structUnion named <code>name</code> is
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsUnion(CUnion union);

	/**
	 * Checks if a structUnion of the name <code>name</code> is contained in this
	 * source file.
	 * 
	 * @param name
	 *            the name to check for
	 * @return <code>true</code> if a structUnion named <code>name</code> is
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean containsUnion(String name);
	
	/**
	 * Return the base class
	 * XXX: refactor this, so that this internal class is not exposed.
	 * 
	 * @return
	 */
	public CSourceFileBase getBase();

	/**
	 * Checks for equality of the two source files.
	 * 
	 * @param other
	 *            the source file to compare with
	 * @return <code>true</code> if the files have the same filename,
	 *         <code>false</code> otherwise
	 */
	public boolean equals(CSourceFile other);

}