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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;



/**
 * 
 * 
 * @author Daniel Bimschas
 */
class CEnumImpl extends CElemImpl implements CEnum {

	private boolean typedef;

	private String varname;

	private ArrayList<String> constants = new ArrayList<String>();

	private String name;
	
	/**
	 * the directives which will be printed after struct/structUnion
	 * declaration/implementation
	 */
	protected LinkedList<CPreProcessorDirectiveImpl> afterDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	/**
	 * the directives which will be printed after struct/structUnion
	 * declaration/implementation
	 */
	protected LinkedList<CPreProcessorDirectiveImpl> beforeDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	/**
	 * Constructs a new <code>CEnum</code> instance.
	 * 
	 * @param name
	 *            the name
	 * @param vars
	 *            the enums variables
	 * @param varname
	 *            the variable name or <code>null</code> if no variable
	 *            assigned
	 * @param typedef
	 *            <code>true</code> if the enum should be a typedef,
	 *            <code>false</code> else
	 * @throws CCodeValidationException
	 *             if validation of the method body fails or either
	 *             <code>name</code> or <code>varname</code> are invalid
	 *             identifiers.
	 * @throws CDuplicateException
	 *             if the variable list contains duplicates
	 */
	public CEnumImpl(String name, String varname, boolean typedef, String... constants)
			throws CCodeValidationException, CDuplicateException {

		this.name = name;
		this.varname = varname;
		this.typedef = typedef;

		addConstant(constants);
		
		validateFields();

	}
	
	public CEnum addAfterDirective(boolean hash, String... directive) throws CPreProcessorValidationException {
		for (String d : directive)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(hash, d));
		return this;
	}
	
	public CEnum addAfterDirective(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.afterDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}
	
	public CEnum addAfterDirective(String... directive) throws ValidationException {
		addAfterDirective(true, directive);
		return this;
	}

	public CEnum addBeforeDirective(boolean hash, String... directive) throws CPreProcessorValidationException {
		for (String d : directive)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(hash, d));
		return this;
	}

	public CEnum addBeforeDirective(CPreProcessorDirective... directives) {
		for (CPreProcessorDirective d : directives)
			this.beforeDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}
	
	public CEnum addBeforeDirective(String... directives) throws ValidationException {
		for(String d : directives)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(d));
		return this;
	}
	
	private CEnum addConstantInternal(String constant) throws CDuplicateException {
		if (constants.contains(constant))
			throw new CDuplicateException("Duplicate enum constant " + constant);
		constants.add(constant);
		return this;
	}
	
	public CEnum addConstant(String... constants) throws CDuplicateException {
		for (String c : constants)
			addConstantInternal(c);
		return this;
	}
	
	public boolean containsConstant(String constant) {
		return constants.contains(constant);
	}

	/**
	 * Returns the list of directives that will be printed out after enum
	 * declaration/implementation.
	 * 
	 * @return a list of preprocessor directives
	 */
	public CPreProcessorDirective[] getAfterDirectives() {
		return afterDirectives
				.toArray(new CPreProcessorDirectiveImpl[afterDirectives.size()]);
	}

	/**
	 * Returns the list of directives that will be printed out before enum
	 * declaration/implementation.
	 * 
	 * @return a list of preprocessor directives
	 */
	public CPreProcessorDirective[] getBeforeDirectives() {
		return beforeDirectives
				.toArray(new CPreProcessorDirectiveImpl[beforeDirectives.size()]);
	}

	public ArrayList<String> getConstants() {
		return constants;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getTypeName() {
		return name != null ? name : varname;
	}

	/**
	 * Returns the variable name of this enum.
	 * 
	 * @return the variable name of this enum
	 */
	public String getVarname() {
		return varname;
	}

	/**
	 * Returns if this enum is a <code>typedef</code>.
	 * 
	 * @return <code>true</code> if this enum is a <code>typedef</code>,
	 *         <code>false</code> otherwise
	 */
	public boolean isTypedef() {
		return typedef;
	}

	/**
	 * Validates the fields of this enum for syntactic correctness.
	 * 
	 * @throws CCodeValidationException
	 *             if validation fails
	 */
	public void validateFields() throws CCodeValidationException {
		// TODO: implement validation of identifiers and code
		// remove SuppressWarnings(!) here and in constructor(!)
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		if (beforeDirectives.size() > 0) {
			toString(buffer, tabCount, beforeDirectives, "", "\n");
			buffer.append("\n");
		}
		indent(buffer, tabCount);
		buffer.append(typedef ? "typedef " : "");
		buffer.append("enum ");
		buffer.append(name);
		buffer.append(" {\n");
		Iterator<String> iterator = constants.iterator();
		while (iterator.hasNext()) {
			String c = iterator.next();
			indent(buffer, tabCount+1);
			buffer.append(c);
			if (iterator.hasNext())
				buffer.append(",");
			buffer.append("\n");
		}
		indent(buffer, tabCount);
		buffer.append("}");
		buffer.append(varname == null || "".equals(varname) ? "" : " " + varname);
		buffer.append(";");
		if (afterDirectives.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, afterDirectives);
		}
	}
	
	public static void main(String[] args) throws Exception {
		CEnumImpl e = new CEnumImpl("TheEnum", "theVarName", true);
		e.addBeforeDirective("IFNDEF _THE_ENUM_");
		e.addBeforeDirective("DEFINE _THE_ENUM_");
		e.addAfterDirective("ENDIF");
		e.addConstant("theFirstEnumElem");
		e.addConstant("theSecondEnumElem");
		e.addConstant("theThirdEnumElem");
		System.out.print(e.toString(1));
		System.out.println("--------");
	}

}
