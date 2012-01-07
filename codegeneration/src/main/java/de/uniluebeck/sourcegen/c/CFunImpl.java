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

import java.util.LinkedList;
import java.util.List;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;



/**
 * @author Daniel Bimschas
 */
class CFunImpl extends CElemImpl implements CFun {

	private LinkedList<CPreProcessorDirectiveImpl> afterDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	private LinkedList<CPreProcessorDirectiveImpl> beforeDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	private CFunBody body;

	private String name;

	private String returnType;

	private CFunSignature signature;

	private CComment comment = null;

	private List<String> parents = new LinkedList<String>();

	public CFunImpl(String name, String returnType,
			CFunSignature signature, CFunBody body)
			throws CCodeValidationException, CDuplicateException {

		this.name = name;
		this.returnType = returnType;
		this.body = body;
		this.signature = signature;

		if (this.signature == null)
			this.signature = new CFunSignatureImpl();

		if (this.body == null)
			this.body = new CFunBody("", ""); //$NON-NLS-1$ //$NON-NLS-2$

	}

	public CFunImpl(String name, String returnType,
			String declarations, String code, CParam... parameters)
			throws CCodeValidationException, CDuplicateException {

		this(name, returnType, new CFunSignatureImpl(parameters),
				new CFunBody(declarations, code));

	}

	public CFunImpl(String name, String returnType, CFunSignature signature) throws CDuplicateException {
		this.name = name;
		this.returnType = returnType;
		this.signature = signature;

		if (this.signature == null)
			this.signature = new CFunSignatureImpl();

		try {
			this.body = new CFunBody("", "");
		} catch (CCodeValidationException e) {
			System.err.println("This should never occur");
			System.exit(1);
		}
	}

	public CFun addDirectiveAfterFunction(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.afterDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}

	public CFun addDirectiveAfterFunction(boolean hash, String... directive) throws CPreProcessorValidationException {
		for (String d : directive)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(hash, d));
		return this;
	}

	public CFun addDirectiveAfterFunction(String... directives) throws CPreProcessorValidationException {
		for(String d : directives)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(d));
		return this;
	}

	public CFun addDirectiveBeforeFunction(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.beforeDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.c.CFun#addDirectiveBeforeFunction(java.lang.String, boolean)
	 */
	public CFun addDirectiveBeforeFunction(boolean hash, String... directive) throws CPreProcessorValidationException {
		for (String d : directive)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(hash, d));
		return this;
	}

	public CFun addDirectiveBeforeFunction(String... directives) throws ValidationException {
		for(String d : directives)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(d));
		return this;
	}

	public CFun add(CParam... param) throws CDuplicateException {
		for (CParam p : param)
			signature.add(p);
		return this;
	}

	public boolean contains(CParamImpl param) {
		return signature.contains(param);
	}

	public boolean equals(CFun other) {
		return name.equals(((CFunImpl)other).name);
	}

	/**
	 * Returns the list of directives that will be printed out after function
	 * declaration/implementation.
	 *
	 * @return a list of preprocessor directives
	 */
	public LinkedList<CPreProcessorDirectiveImpl> getAfterDirectives() {
		return afterDirectives;
	}

	/**
	 * Returns the list of directives that will be printed out before function
	 * declaration/implementation.
	 *
	 * @return a list of preprocessor directives
	 */
	public LinkedList<CPreProcessorDirectiveImpl> getBeforeDirectives() {
		return beforeDirectives;
	}

	/**
	 * Returns the funs body code.
	 *
	 * @return the funs body code
	 */
	public CFunBody getBody() {
		return body;
	}

	/**
	 * Returns the name of the function.
	 *
	 * @return the name of the function
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the return type of the function.
	 *
	 * @return the return type of the function
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * Returns the signature of the function.
	 *
	 * @return the signature of the function
	 */
	public String getSignature() {

	    StringBuffer buffer = new StringBuffer();
	    buffer.append(returnType);
        buffer.append(" ");
        buffer.append(name);
        signature.toString(buffer, 0);
		return buffer.toString();
	}

	public CFun appendCode(String... code) {
		for (String c : code)
			body.append(c + Cpp.newline);
		return this;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		if (beforeDirectives.size() > 0) {
			toString(buffer, tabCount, beforeDirectives, "", "\n");
			buffer.append("\n");
		}
		indent(buffer, tabCount);

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		buffer.append(returnType);
		buffer.append(" ");
		buffer.append(getParents() + name);
		signature.toString(buffer, 0);
		buffer.append(" {\n");
		body.toString(buffer, tabCount+1);
		buffer.append("\n");
		indent(buffer, tabCount);
		buffer.append("}");
		if(afterDirectives.size() > 0)
			buffer.append("\n");
		toString(buffer, tabCount, afterDirectives, "", "\n");
	}

	public void toStringForwardDecl(StringBuffer buffer, int tabCount) {
		toString(buffer, tabCount, beforeDirectives);
		indent(buffer, tabCount);
		buffer.append(returnType);
		buffer.append(" ");
		buffer.append(name);
		signature.toString(buffer, 0);
		buffer.append(";\n");
		toString(buffer, tabCount, afterDirectives);
	}

	public static void main(String[] args) throws Exception {
		CFunImpl fun = new CFunImpl("theFunction", "theReturnType", null, null);
		fun.appendCode("int helloWorld = theWorld;\n", "helloWorld++;\n");
		fun.add(CParam.factory.create("int", "theWorld"));
		fun.addDirectiveBeforeFunction("IFNDEF _THE_ENUM_", "DEFINE _THE_ENUM_");
		fun.addDirectiveAfterFunction("ENDIF");
		System.out.print(fun.toString(1));
		System.out.println("-----------");
	}

	public CFun setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

    @Override
    public CFun addParents(List<String> cppClass, String clazz) {
        this.parents.addAll(cppClass);
        this.parents.add(clazz);
        return this;
    }

    private String getParents(){
        StringBuffer myParents = new StringBuffer();
        for (String s : this.parents) {
            myParents.append(s + "::");
        }
        return myParents.toString();
    }

    @Override
    public CComment getComment() {
        return this.comment;
    }

}
