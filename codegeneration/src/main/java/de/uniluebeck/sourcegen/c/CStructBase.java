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

import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;



public interface CStructBase extends CComplexType {

	/**
	 * Adds a C preprocessor directive which will be printed out after the
	 * struct/structUnion declaration/implementation.
	 * 
	 * @param directive
	 *            the preprocessor directive
	 */
	public void addAfterDirective(CPreProcessorDirective directive);

	/**
	 * Adds an array of C preprocessor directives which will be printed out
	 * after the struct/structUnion declaration/implementation.
	 * 
	 * @param directives
	 * 			the preprocessore directives to add
	 */
	public void addAfterDirective(CPreProcessorDirective[] directives);

	/**
	 * Adds a C preprocessor directive which will be printed out after the
	 * struct/structUnion declaration/implementation.
	 * 
	 * @param directive
	 *            the preprocessor directive to add
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 */
	public void addAfterDirective(String directive)
			throws ValidationException;

	/**
	 * TODO: javadoc
	 * 
	 * Test: {@link CFunctionTest#testAddDirectiveAfterFunctionStringBoolean()}
	 * 
	 * @param directive
	 * @param hash
	 * @throws CPreProcessorValidationException
	 */
	public void addAfterDirective(String directive, boolean hash)
			throws CPreProcessorValidationException;

	/**
	 * Adds an array of C preprocessor directives which will be printed
	 * out after the struct/structUnion declaration/implementation.
	 * 
	 * @param directives
	 *            the preprocessor directives to add
	 * @throws CPreProcessorValidationException
	 *             if one of the directives is syntactically invalid
	 */
	public void addAfterDirective(String[] directives)
			throws CPreProcessorValidationException;

	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * struct/structUnion declaration/implementation.
	 * 
	 * @param directive
	 *            the preprocessor directive
	 */
	public void addBeforeDirective(CPreProcessorDirective directive);

	/**
	 * Adds an array of C preprocessor directives which will be printed out
	 * before the struct/structUnion declaration/implementation.
	 * 
	 * @param directives
	 *            the preprocessor directives
	 */
	public void addBeforeDirective(CPreProcessorDirective[] directives);

	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * struct/structUnion declaration/implementation.
	 * 
	 * @param directive
	 *            the preprocessor directive
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 */
	public void addBeforeDirective(String directive)
			throws ValidationException;

	/**
	 * TODO: javadoc
	 * 
	 * Test: {@link CFunctionTest#testAddDirectiveBeforeFunctionStringBoolean()}
	 * 
	 * @param directive
	 * @param hash
	 * @throws CPreProcessorValidationException
	 */
	public void addBeforeDirective(String directive, boolean hash)
			throws CPreProcessorValidationException;

	/**
	 * Adds an array of C preprocessor directives which will be printed out
	 * before the struct/structUnion declaration/implementation.
	 * 
	 * @param directives
	 * 				the preprocessor directive
	 * @throws ValidationException
	 * 				if the directive is syntactically invalid
	 */
	public void addBeforeDirective(String[] directives)
			throws ValidationException;

	/**
	 * Adds the enum <code>cEnum</code> to this struct or structUnion.
	 * 
	 * @param cEnum the enum to add
	 * @throws CDuplicateException if an enum with the same
	 * name is already contained in this struct or structUnion
	 */
	public void add(CEnum... cEnum) throws CDuplicateException;

	/**
	 * Adds a struct to this source file.
	 * 
	 * @param struct
	 *            the struct to add
	 * @throws CDuplicateException
	 *             if a struct of that name is already contained in this source
	 *             file
	 */
	public void add(CStruct... struct)
			throws CDuplicateException;

	/**
	 * Adds a structUnion to this source file.
	 * 
	 * @param structUnion
	 *            the structUnion to add
	 * @throws CDuplicateException
	 *             if a structUnion of that name is already contained in this source
	 *             file
	 */
	public void add(CUnion... union) throws CDuplicateException;

	/**
	 * Adds a variable to the struct/structUnion/enum.
	 * 
	 * @param var
	 *            the variable to add
	 * @throws CDuplicateException
	 *             if this struct/structUnion/enum already contains an equal variable
	 */
	public void add(CParam... var) throws CDuplicateException;

	/**
	 * Checks wether the enum <code>cEnum</code> is already
	 * contained in this struct or structUnion.
	 * 
	 * @param cEnum the enum to check for
	 * @return <code>true</code> if <code>cEnum</code> is already
	 * contained in this struct or structUnion, <code>false</code> otherwise
	 */
	public boolean contains(CEnum cEnum);

	/**
	 * Returns <code>true</code> if a struct of name
	 * <code>struct.getName()</code> is contained in this source file.
	 * 
	 * @param struct
	 *            the struct to check for
	 * @return <code>true</code> if a struct of name <code>name</code> is
	 *         contained in this source file
	 */
	public boolean contains(CStruct struct);

	/**
	 * Checks if a structUnion of the name <code>structUnion.getName()</code> is contained
	 * in this source file.
	 * 
	 * @param structUnion
	 *            the structUnion to check for
	 * @return <code>true</code> if a structUnion named <code>name</code> is
	 *         contained in this source file, <code>false</code> otherwise
	 */
	public boolean contains(CUnion union);
	
	public boolean contains(CParam variable);

	/**
	 * Checks for equality.
	 * 
	 * @param other
	 *            the struct base to compare with
	 * @return <code>true</code> if the names equal, <code>false</code>
	 *         otherwise
	 */
	public boolean equals(CStructBase other);
	
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getTypeName();

}