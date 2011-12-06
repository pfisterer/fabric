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




public interface CppVar extends CppLangElem {

	class CppVarFactory {

		private static CppVarFactory instance;

		private CppVarFactory() { /* not to be invoked */ }

		static CppVarFactory getInstance() {
			if (instance == null)
				instance = new CppVarFactory();
			return instance;
		}

		public CppVar create(CppTypeGenerator type, String varName) {

			return new CppVarImpl(type, varName);
		}

		public CppVar create(long qualifier, String varName) {
			return new CppVarImpl(new CppTypeGenerator(qualifier), varName);
		}

		public CppVar create(String qualifiedType, String varName) {
			return new CppVarImpl(new CppTypeGenerator(qualifiedType), varName);
		}

		public CppVar create(CStruct struct, String varName) {
			return new CppVarImpl(new CppTypeGenerator(struct.getTypeName()), varName);
		}

		public CppVar create(CEnum enumm, String varName) {
			return new CppVarImpl(new CppTypeGenerator(enumm.getTypeName()), varName);
		}

		public CppVar create(long visibility, String type, String varName) {
			return new CppVarImpl(visibility, new CppTypeGenerator(type), varName);
		}

		public CppVar create(long visibility, String type, String varName, String initCode) {
			return new CppVarImpl(visibility, new CppTypeGenerator(type), varName, initCode);
		}

/*
		// qualifiedType
        public CppVar create(long qualifiedType) {
        	return create(qualifiedType, "", "");
        }

		public CppVar create(long qualifiedType, String varName) {
			return create(qualifiedType, varName, "");
		}

		public CppVar create(long qualifiedType, String varName, String initCode) {
			return new CppVarImpl(qualifiedType, varName, initCode);
		}

		public CppVar create(long qualifiedType, String varName,
				CppTemplateHelper template) {
			return new CppVarImpl(qualifiedType, varName, template);
		}

		// CppTypeGenerator

		//public CppVar create(long qualifiedType, String type, String varName) {

		}
		///

		public CppVar create(long qualifiedType, String type, String varName, String initCode) {
			CppTypeGenerator t = new CppTypeGenerator(qualifiedType, type);
			return new CppVarImpl(varName, t, initCode);
		}

		public CppVar create(String type, String varName,
				CppTemplateName... types) {
			return new CppVarImpl(new CppTypeGenerator(type), varName, null,
					types);
		}

		public CppVar create(CppTypeGenerator type, String varName,
				CppTemplateName... types) {
			return new CppVarImpl(type, varName, null, types);
		}

		public CppVar create(CppTypeGenerator type, String varName,
				CppTemplateHelper template, CppTemplateName... types) {
			return new CppVarImpl(type, varName, template, types);
		}

		// CppClass
		public CppVar create(CppClass clazz, String varName,
				CppTemplateHelper template) {
			return new CppVarImpl(new CppTypeGenerator(clazz, template),
					varName, template);
		}

        // String
		public CppVar create(String varDeclString) {
			return new CppVarImpl(varDeclString);
		}

		// CStruct
		public CppVar create(CStruct type, String varName) {
			return create(type.getTypeName(), varName);
		}

		// Enum
		public CppVar create(CEnum type, String varName) {
			return create(type.getTypeName(), varName);
		}

		// CComplexType
		public CppVar create(CComplexType type, String varName) {
			return create(Cpp.NONE, type, varName);
		}

		public CppVar create(CppComplexType type, String varName) {
			return create(Cpp.NONE, type, varName);
		}

        // qualifier + ComplexType
        public CppVar create(long qualifier, CComplexType type, String varName) {
            return new CppVarImpl(qualifier, type, varName, "");
        }

        public CppVar create(long qualifier, CppComplexType type, String varName) {
                return new CppVarImpl(qualifier, type, varName, "");
        }

        public CppVar create(long qualifier, CComplexType type, String varName, String initCode) {
                return new CppVarImpl(qualifier, type, varName, initCode);
        }
*/

	}

	public static final CppVarFactory factory = CppVarFactory.getInstance();
//	public static final CppVar VOID = factory.create(Cpp.VOID);
	public CppVar 	setComment(CComment comment);
	public String getVarName();

    public String getInitCode();
    public Long getVisability();
    //public CppVar setClass(CppClass clazz);
    public String getInit();

}
