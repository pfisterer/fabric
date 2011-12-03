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

class CppFunImpl extends CElemImpl implements CppFun {

    private enum ReturnType {
        COMPLEX, LONG, STRING, GENERATOR
    }

    private ReturnType returnType;
    private CComplexType returnTypeComplex;
    private long returnTypeLong;
    private String returnTypeString;
    private CppTypeGenerator returnTypeGenerator;

    private String name;
    private CppSignature signature;
    private StringBuffer body = new StringBuffer();
    private CppClass clazz;

	private CComment comment = null;

    public CppFunImpl(CppClass clazz, CComplexType returnType, String name, CppVar... signatureVar)
            throws CppDuplicateException {

        this(returnType, name, signatureVar);
        this.clazz = clazz;
    }

    private CppFunImpl(CComplexType returnType, String name, CppVar... signatureVar) throws CppDuplicateException {

        this(name, signatureVar);

        this.returnType = ReturnType.COMPLEX;
        this.returnTypeComplex = returnType;
    }

    private CppFunImpl(String name, CppVar... signatureVar) throws CppDuplicateException {
        this.name = name;
        this.signature = new CppSignature(name, signatureVar);
    }

    public CppFunImpl(CppClass clazz, long returnType, String name, CppVar... signatureVar)
            throws CppDuplicateException {

        this(name, signatureVar);

        this.returnType = ReturnType.LONG;
        this.returnTypeLong = returnType;
        this.clazz = clazz;

    }

    public CppFunImpl(CppClass clazz, String returnType, String name, CppVar[] signatureVars)
            throws CppDuplicateException {

        this(name, signatureVars);

        this.returnType = ReturnType.STRING;
        this.returnTypeString = returnType;
        this.clazz = clazz;
    }

    public CppFunImpl(CppClass clazz, CppTypeGenerator returnType, String name, CppVar[] signatureVars)
            throws CppDuplicateException {

        this(name, signatureVars);

        this.returnType = ReturnType.GENERATOR;
        this.returnTypeGenerator = returnType;
        this.clazz = clazz;
    }

    public CppFun appendCode(String str) {
        body.append(str + Cpp.newline);
        return this;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {

    	StringBuffer buffer = new StringBuffer();
    	if (comment != null) {
    		buffer.append(Cpp.newline);
    		buffer.append(comment.toString());
    	}

        return buffer.toString() + getType() + " " + signature.toString();
    }

    public String getBody() {
        return body.toString();
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        // indent(buffer, tabCount);

    	// write comment if necessary
    	if (comment != null) {
    		comment.toString(buffer, tabCount);
    	}

    	// Get the parents to get OUTER::NESTED1::NESTED2::...::NESTEDN
    	StringBuffer myParents = new StringBuffer();
    	for (CppClass p : this.clazz.getParents()) {
    		myParents.append(p.getName()+ "::");
		}

        buffer.append(getType() + " " + myParents + this.clazz.getName() + "::");
        signature.toString(buffer, 0);
        buffer.append(" {" + Cpp.newline);
        appendBody(buffer, body, tabCount + 1);
		buffer.append(Cpp.newline + "}" + Cpp.newline);

        // indent(buffer, tabCount);
        buffer.append(Cpp.newline);
    }

    private String getType() {

        switch (returnType) {
	        case GENERATOR:
	            return returnTypeGenerator.toString();
	        case COMPLEX:
	        	return returnTypeComplex.getTypeName();
	        case LONG:
	        	return Cpp.toString(returnTypeLong);
	        case STRING:
	        	return returnTypeString;
        }

        return "{UNKNOWN_TYPE}";
    }

	@Override
	public CppFun setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

}
