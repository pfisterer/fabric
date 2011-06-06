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
        COMPLEX, LONG, STRING
    }

    private ReturnType returnType;
    private CComplexType returnTypeComplex;
    private long returnTypeLong;
    private String returnTypeString;

    private String name;
    private CppSignature signature;
    private StringBuffer body = new StringBuffer();
    private CppClass clazz;

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

    public CppFun appendCode(String str) {
        body.append(str);
        return this;
    }

    public String getReturnType() {
        switch (returnType) {
        case COMPLEX:
            return returnTypeComplex.getTypeName();
        case LONG:
            return Cpp.toString(returnTypeLong);
        case STRING:
            return returnTypeString;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        String type = null;

        switch (returnType) {
        case COMPLEX:
            type = returnTypeComplex.getTypeName();
            break;
        case LONG:
            type = Cpp.toString(returnTypeLong);
            break;
        case STRING:
            type = returnTypeString;
            break;
        }

        return type + " " + signature.toString();
    }

    public String getBody() {
        return body.toString();
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        // indent(buffer, tabCount);
        switch (returnType) {
        case COMPLEX:
            buffer.append(returnTypeComplex.getTypeName());
            break;
        case LONG:
            buffer.append(Cpp.toString(returnTypeLong));
            break;
        case STRING:
            buffer.append(returnTypeString);
            break;
        }
        buffer.append(" " + this.clazz.getTypeName() + "::\n");
        signature.toString(buffer, 0);
        buffer.append(" {\n");
        appendBody(buffer, body, tabCount + 1);
        // indent(buffer, tabCount);
        buffer.append("\n");
    }

}
