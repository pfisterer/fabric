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

import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;


public interface CppFun extends CppLangElem {

	class CppFunFactory {

		private static CppFunFactory instance;

		private CppFunFactory() { /* not to be invoked */
		}

		static CppFunFactory getInstance() {
			if (instance == null)
				instance = new CppFunFactory();
			return instance;
		}

		public CppFun create(CppClass clazz, CComplexType returnType, String name, CppVar... signatureVar) throws CppDuplicateException {
			return new CppFunImpl(clazz, returnType, name, signatureVar);
		}

		public CppFun create(CppClass clazz, long returnType, String name, CppVar... signatureVar) throws CppDuplicateException {
			return new CppFunImpl(clazz, returnType, name, signatureVar);
		}

		public CppFun create(CppClass clazz, String returnType, String name, CppVar... signatureVars) throws CppDuplicateException {
			return new CppFunImpl(clazz, returnType, name, signatureVars);
		}

		public CppFun create(CppClass clazz, CppTypeGenerator returnType, String name, CppVar... signatureVars) throws CppDuplicateException {
			return new CppFunImpl(clazz, returnType, name, signatureVars);
		}

	}

	public static final CppFunFactory factory = CppFunFactory.getInstance();

	public CppFun setComment(CppFunComment comment);

	public CppFun appendCode(String string);
	public String getSignature();

}
