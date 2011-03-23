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





class CppVarImpl extends CElemImpl implements CppVar {
	private enum Type {
		DECL_STRING,
		LONG_STRING,
		LONG_CCOMPLEX_STRING,
		LONG_CPPCOMPLEX_STRING
	}

	private String varDeclString;
	private Type type;
	private String initCode;
	private String varName;
	private CComplexType cComplexType;
	private CppComplexType cppComplexType;
	private long qualifier;
	private long qualifiedType;
	
	public CppVarImpl(String varDeclString) {
		this.type = Type.DECL_STRING;
		this.varDeclString = varDeclString;
	}

	public CppVarImpl(long qualifiedType, String varName, String initCode) {
		this.type = Type.LONG_STRING;
		this.qualifiedType = qualifiedType;
		this.varName = varName;
		this.initCode = initCode;
	}

	public CppVarImpl(long qualifier, CComplexType type, String varName, String initCode) {
		this.type = Type.LONG_CCOMPLEX_STRING;
		this.qualifier = qualifier;
		this.cComplexType = type;
		this.varName = varName;
		this.initCode = initCode;
	}

	public CppVarImpl(long qualifier, CppComplexType type, String varName, String initCode) {
		this.type = Type.LONG_CPPCOMPLEX_STRING;
		this.qualifier = qualifier;
		this.cppComplexType = type;
		this.varName = varName;
		this.initCode = initCode;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		switch(type) {
		case DECL_STRING:
			buffer.append(varDeclString);
			break;
		case LONG_CCOMPLEX_STRING: case LONG_CPPCOMPLEX_STRING:
			String modString = Cpp.toString(this.qualifier);
			buffer.append(modString.length() > 0 ? modString + " " : "");
			buffer.append(type == Type.LONG_CPPCOMPLEX_STRING ?
				this.cppComplexType.getTypeName() :
				this.cComplexType.getTypeName()
			);
			buffer.append(" " + varName);
			buffer.append(initCode);
			break;
		case LONG_STRING:
			buffer.append(Cpp.toString(this.qualifiedType));
			buffer.append(" ");
			buffer.append(varName);
			buffer.append(initCode);
			break;
		}
	}

}
