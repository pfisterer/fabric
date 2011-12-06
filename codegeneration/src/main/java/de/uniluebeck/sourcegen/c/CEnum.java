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

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;


public interface CEnum extends CComplexType {

	class CEnumFactory {

		private static CEnumFactory instance;

		private CEnumFactory() { /* not to be invoked */ }

		static CEnumFactory getInstance() {
			if (instance == null)
				instance = new CEnumFactory();
			return instance;
		}

		public CEnum create(String name, String varname, boolean typedef, String... constants) throws CDuplicateException, CCodeValidationException {
			return new CEnumImpl(name, varname, typedef, constants);
		}

	}

	public static final CEnumFactory factory = CEnumFactory.getInstance();

	/**
	 * Adds a C preprocessor directive which will be printed out after the
	 * enum declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive(s)
	 * @return
	 */
	public CEnum addAfterDirective(CPreProcessorDirective... directive);

	/**
	 * Adds (a) C preprocessor directive(s) which will be printed out after the
	 * enum declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive(s) to add
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 * @return
	 */
	public CEnum addAfterDirective(String... directive) throws ValidationException;

	/**
	 * TODO: javadoc
	 *
	 * Test: {@link CFunctionTest#testAddDirectiveAfterFunctionStringBoolean()}
	 * @param hash
	 * @param directive
	 *
	 * @throws CPreProcessorValidationException
	 * @return
	 */
	public CEnum addAfterDirective(boolean hash, String... directive) throws CPreProcessorValidationException;

	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * enum declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive
	 * @return
	 */
	public CEnum addBeforeDirective(CPreProcessorDirective... directive);

	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * enum declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 * @return
	 */
	public CEnum addBeforeDirective(String... directive) throws ValidationException;

	/**
	 * TODO: javadoc
	 *
	 * Test: {@link CFunctionTest#testAddDirectiveBeforeFunctionStringBoolean()}
	 * @param hash
	 * @param directive
	 *
	 * @throws CPreProcessorValidationException
	 * @return
	 */
	public CEnum addBeforeDirective(boolean hash, String... directive) throws CPreProcessorValidationException;

	/**
	 * Adds a constant to this enum.
	 *
	 * @param constant the constant String to add
	 * @throws CDuplicateException if there's already a constant with
	 * the same String
	 * @return
	 */
	public CEnum addConstant(String... constant) throws CDuplicateException;

	/**
	 * Checks whether the constant is already contained in this enum.
	 *
	 * @param constant the constant to check for
	 * @return <code>true</code> if this constant is already contained
	 * in the enum, <code>false</code> otherwise.
	 */
	public boolean containsConstant(String constant);

	/**
	 * Set a comment
	 *
	 * @param comment The comment
	 * @return
	 */
	public CEnum setComment(CComment comment);

}