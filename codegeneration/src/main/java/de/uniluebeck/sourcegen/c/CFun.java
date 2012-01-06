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

import java.util.List;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;

public interface CFun extends CLangElem {

	class CFunctionFactory {

		private static CFunctionFactory instance;

		private CFunctionFactory() {
      /* not to be invoked */
		}

		static CFunctionFactory getInstance() {
			if (instance == null)
				instance = new CFunctionFactory();
			return instance;
		}

		public CFun create(String name, String returnType,
				CFunSignature signature)
				throws CDuplicateException {
/* TODO
		    if(signature == null) {
                try {
                    CParam param = CParam.factory.create(returnType, name);
                    signature = CFunSignature.factory.create(param);
                } catch (CConflictingModifierException e) {}
		    }
*/
			return new CFunImpl(name, returnType, signature);
		}

		public CFun create(String name, String returnType,
				CFunSignature signature, CFunBody body)
				throws CCodeValidationException, CDuplicateException {

			return new CFunImpl(name, returnType, signature, body);
		}

		public CFun create(String name, String returnType,
				String declarations, String code, CParam... parameters)
				throws CCodeValidationException, CDuplicateException {

			return new CFunImpl(name, returnType, declarations, code, parameters);
		}

	}

	public static final CFunctionFactory factory = CFunctionFactory.getInstance();

	/**
	 * Adds a C preprocessor directive which will be printed out after the
	 * function declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive
	 * @return
	 */
	public CFun addDirectiveAfterFunction(CPreProcessorDirective... directive);

	/**
	 * Adds a C preprocessor directive which will be printed out after the
	 * function declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive to add
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 */
	public CFun addDirectiveAfterFunction(String... directive) throws ValidationException;

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
	public CFun addDirectiveAfterFunction(boolean hash, String... directive) throws CPreProcessorValidationException;


	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * function declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive
	 * @return
	 */
	public CFun addDirectiveBeforeFunction(CPreProcessorDirective... directive);

	/**
	 * Adds a C preprocessor directive which will be printed out before the
	 * function declaration/implementation.
	 *
	 * @param directive
	 *            the preprocessor directive
	 * @throws ValidationException
	 *             if the directive is syntactically invalid
	 * @return
	 */
	public CFun addDirectiveBeforeFunction(String... directive) throws ValidationException;

	/**
	 * TODO: javadoc
	 *
	 * Test: {@link CFunctionTest#testAddDirectiveBeforeFunctionStringBoolean()}
	 * @param hash
	 * @param directive
	 *
	 * @throws CPreProcessorValidationException
	 */
	public CFun addDirectiveBeforeFunction(boolean hash, String... directive) throws CPreProcessorValidationException;

	/**
	 * Adds a parameter to this funs signature.
	 *
	 * @param param
	 *            the parameter to add
	 * @throws CDuplicateException
	 *             if the signature already contains a parameter of that name
	 */
	public CFun add(CParam... param) throws CDuplicateException;

	/**
	 * Checks if the signature of this function contains the parameter
	 * <code>param</code>.
	 *
	 * @param param
	 *            the parameter to check for
	 * @return <code>true</code> if the signature contains <code>param</code>,
	 *         <code>false</code> otherwise
	 */
	public boolean contains(CParamImpl param);

	/**
	 * Tests the two funs for equality.
	 *
	 * @param other
	 *            the function to compare with
	 * @return <code>true</code> if the names of the two funs are equal
	 */
	public boolean equals(CFun other);

	public CFun appendCode(String... code);

	public String getName();

	public CFun	setComment(CComment comment);

	public String getSignature();

	public CFun addParents(List<String> parents, String cFun);

}