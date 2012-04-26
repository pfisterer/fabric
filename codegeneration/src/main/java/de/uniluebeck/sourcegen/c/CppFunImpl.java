/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
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
import java.util.ArrayList;

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

	private CppFunImpl(String name, CppVar... signatureVar) throws CppDuplicateException {
		this.name = name;
		this.signature = new CppSignature(name, signatureVar);
	}

	public CppFunImpl(CComplexType returnType, String name, CppVar... signatureVar) throws CppDuplicateException {
        this(name, signatureVar);
        this.returnType = ReturnType.COMPLEX;
        this.returnTypeComplex = returnType;
    }

    public CppFunImpl(long returnType, String name, CppVar... signatureVar) throws CppDuplicateException {

        this(name, signatureVar);

        this.returnType = ReturnType.LONG;
        this.returnTypeLong = returnType;
    }

    public CppFunImpl(String returnType, String name, CppVar[] signatureVars) throws CppDuplicateException {

        this(name, signatureVars);

        this.returnType = ReturnType.STRING;
        this.returnTypeString = returnType;
    }

    public CppFunImpl(CppTypeGenerator returnType, String name, CppVar[] signatureVars) throws CppDuplicateException {

        this(name, signatureVars);

        this.returnType = ReturnType.GENERATOR;
        this.returnTypeGenerator = returnType;
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
        this.toString(buffer, tabCount, false);
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount, boolean isLast) {
        // Write comment if necessary
        if (comment != null) {
            comment.toString(buffer, tabCount);
        }

        buffer.append(getType() + " " + getParents(this.clazz) + this.clazz.getName() + "::");
        signature.toString(buffer, 0);
        buffer.append(" {" + Cpp.newline);
        appendBody(buffer, body, tabCount + 1);
        buffer.append(Cpp.newline + "}");

        if (!isLast) {
            buffer.append(Cpp.newline + Cpp.newline);
        }
    }

    private String getType() {

        switch (returnType) {
	        case GENERATOR:
            // TODO: Ignore static also in class generator
            return returnTypeGenerator.toString();
          case COMPLEX:
            return returnTypeComplex.getName();
	        case LONG:
            long vis = new Long(returnTypeLong).longValue();
            if (Cpp.isStatic(vis)) {
              vis = vis ^ Cpp.STATIC;
            }
            return Cpp.toString(vis);
	        case STRING:

	            // TODO: Just working for one layer until now
	            // TODO: clazz.getNested(Cpp.PRIVATE | Cpp.PUBLIC | Cpp.PROTECTED)

              // Check nested classes
	            List<CppClass> inner = new ArrayList<CppClass>();
              inner.addAll(clazz.getNested(Cpp.PRIVATE));
              inner.addAll(clazz.getNested(Cpp.PUBLIC));
              inner.addAll(clazz.getNested(Cpp.PROTECTED));

              for (CppClass c : inner) {
                  // Return type may also be a pointer
                  if (returnTypeString.equals(c.getName()) || returnTypeString.equals(c.getName() + "*")) {
                      return getParents(clazz) + clazz.getName() + "::" + returnTypeString;
                  }
              }

              List<CEnum> enums = new ArrayList<CEnum>();
              enums.addAll(clazz.getEnums(Cpp.PRIVATE));
              enums.addAll(clazz.getEnums(Cpp.PUBLIC));
              enums.addAll(clazz.getEnums(Cpp.PROTECTED));

              // Check local enumerations
              for (CEnum e : enums) {
                  // Return type may also be a pointer
                  if (returnTypeString.equals(e.getName()) || returnTypeString.equals(e.getName() + "*")) {
                      return getParents(clazz) + clazz.getName() + "::" + returnTypeString;
                  }
              }

	        	  return returnTypeString;
        }

        return "{UNKNOWN_TYPE}";
    }

    @Override
    public CppFun setComment(CComment comment) {
    	this.comment = comment;
    	return this;
    }

    /**
     * Returns OUTER::NESTED1::NESTED2::...::NESTEDN
     */
    private String getParents(CppClass clazz){
        StringBuffer myParents = new StringBuffer();
        if(clazz != null) {
            for (String s : clazz.getParents()) {
                myParents.append(s + "::");
            }
        }
        return myParents.toString();
    }

    @Override
    public CppFun setClass(CppClass clazz) {
        this.clazz = clazz;
        return this;
    }

}
