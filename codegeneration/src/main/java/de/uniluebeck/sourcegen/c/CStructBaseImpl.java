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

import java.util.LinkedList;

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.ValidationException;



/**
 * The abstract base class for <code>CStructImpl</code>, <code>CUnionImpl</code>
 * and <code>CEnum</code>.
 *
 * @author Daniel Bimschas
 */
abstract class CStructBaseImpl extends CElemImpl implements CStructBase {

	/**
	 * the structs' name
	 */
	protected String name;

	/**
	 * to save a string if this will be a struct or a structUnion.
	 * needed since StringTemplate seems not to be able to
	 * perform a &lt;if(it instanceof CStructImpl)&gt; operation
	 */
	protected String structOrUnion;

	/**
	 * a list of nested structs and unions contained in this source file, which
	 * are kept together in one list in order to retain their order of declaration
	 * in the generated source file
	 */
	private LinkedList<CStructBaseImpl> structsUnions = new LinkedList<CStructBaseImpl>();

	/**
	 * The comment
	 */
	private CComment comment = null;

	/**
	 * true if the struct should be a typedef
	 */
	protected boolean typedef;

	/**
	 * the variable name of the struct
	 */
	protected String varname;

	/**
	 * the structs' enums
	 */
	protected LinkedList<CEnumImpl> enums = new LinkedList<CEnumImpl>();

	/**
	 * the structs' variables
	 */
	protected LinkedList<CParamImpl> vars = new LinkedList<CParamImpl>();

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
	 * Constructs a new <code>CStructBaseImpl</code> instance.
	 *
	 * @param name
	 *            the name
	 * @param vars
	 *            the structs' variables or <code>null</code> if they should
	 *            be empty
	 * @param varname
	 *            the variable name or <code>null</code> if no variable
	 *            assigned
	 * @param typedef
	 *            <code>true</code> if the struct should be a typedef,
	 *            <code>false</code> else
	 * @throws CCodeValidationException
	 *             if validation of the method body fails or either
	 *             <code>name</code> or <code>varname</code> are invalid
	 *             identifiers.
	 * @throws CDuplicateException
	 */
	public CStructBaseImpl(String structOrUnion, String name, CParam[] vars, String varname,
			boolean typedef) throws CCodeValidationException,
			CDuplicateException {

		this.structOrUnion = structOrUnion;
		this.name = name;
		this.varname = varname;
		this.typedef = typedef;

		if (vars != null)
			for (CParam v : vars)
				add(v);

		validateFields();
	}

	public void addAfterDirective(CPreProcessorDirective directive) {
		this.afterDirectives.add((CPreProcessorDirectiveImpl)directive);
	}

	public void addAfterDirective(CPreProcessorDirective[] directives) {
		for (CPreProcessorDirective d : directives)
			this.afterDirectives.add((CPreProcessorDirectiveImpl)d);
	}

	public void addAfterDirective(String directive)
			throws ValidationException {
		addAfterDirective(directive, true);
	}

	public void addAfterDirective(String directive, boolean hash) throws CPreProcessorValidationException {
		this.afterDirectives.add(new CPreProcessorDirectiveImpl(hash, directive));
	}

	public void addAfterDirective(String[] directives)
			throws CPreProcessorValidationException {
		for(String d : directives)
			this.afterDirectives.add(new CPreProcessorDirectiveImpl(d));
	}

	public void addBeforeDirective(CPreProcessorDirective directive) {
		this.beforeDirectives.add((CPreProcessorDirectiveImpl)directive);
	}

	public void addBeforeDirective(CPreProcessorDirective[] directives) {
		for (CPreProcessorDirective d : directives)
			this.beforeDirectives.add((CPreProcessorDirectiveImpl)d);
	}

	public void addBeforeDirective(String directive)
			throws ValidationException {
		addBeforeDirective(directive, true);
	}

	public void addBeforeDirective(String directive, boolean hash) throws CPreProcessorValidationException {
		this.beforeDirectives.add(new CPreProcessorDirectiveImpl(hash, directive));
	}


	public void addBeforeDirective(String[] directives)
			throws ValidationException {
		for(String d : directives)
			this.beforeDirectives.add(new CPreProcessorDirectiveImpl(d));
	}

	public void add(CEnum... cEnum) throws CDuplicateException {
		for (CEnum e : cEnum) {
			if (contains(e))
				throw new CDuplicateException("Duplicate enum " + cEnum);
			enums.add((CEnumImpl)e);
		}
	}

	public boolean contains(CEnum cEnum) {
		for (CEnum e : enums)
			if (((CEnumImpl)e).getName().equals(((CEnumImpl)cEnum).getName()))
				return true;
		return false;
	}

	public void add(CStruct... struct) throws CDuplicateException {
		for (CStruct s : struct) {
			if (contains(s))
				throw new CDuplicateException("Duplicate struct " + s.getName());
			structsUnions.add((CStructImpl)s);
		}
	}

	public void add(CUnion... union) throws CDuplicateException {
		for (CUnion u : union) {
			if (contains(u))
				throw new CDuplicateException("Duplicate structUnion " + u.getName());
			structsUnions.add((CUnionImpl)u);
		}
	}

	public boolean contains(CUnion union) {
		for (CStructBaseImpl u : structsUnions)
			if (u instanceof CUnion && u.equals(union))
				return true;
		return false;
	}

	/**
	 * Returns the list of directives that will be printed out after struct/structUnion
	 * declaration/implementation.
	 *
	 * @return a list of preprocessor directives
	 */
	public CPreProcessorDirective[] getAfterDirectives() {
		return afterDirectives
				.toArray(new CPreProcessorDirectiveImpl[afterDirectives.size()]);
	}

	/**
	 * Returns the list of directives that will be printed out before struct/structUnion
	 * declaration/implementation.
	 *
	 * @return a list of preprocessor directives
	 */
	public CPreProcessorDirective[] getBeforeDirectives() {
		return beforeDirectives
				.toArray(new CPreProcessorDirectiveImpl[beforeDirectives.size()]);
	}

	public void add(CParam... var) throws CDuplicateException {
		for (CParam v : var) {
			if (contains(v))
				throw new CDuplicateException("Duplicate variable "
						+ ((CParamImpl)v).getName() + " in struct/structUnion/enum " + name);
			vars.add((CParamImpl)v);
		}
	}

	public boolean contains(CStruct struct) {
		for (CStructBaseImpl elem : structsUnions)
			if (elem instanceof CStruct && elem.equals(struct))
				return true;
		return false;
	}

	public boolean contains(CParam variable) {
		for (CParam elem : vars)
			if (elem.equals(variable))
				return true;
		return false;
	}

	public CUnion getUnionByVarName(String varName) {

		for (CStructBase s : structsUnions)
			if (s instanceof CUnion && ((CUnionImpl) s).getVarname().equals(varName))
				return (CUnion) s;

		return null;

	}

	public CStruct getStructByVarName(String varName) {

		for (CStructBase s : structsUnions)
			if (s instanceof CStruct && ((CStructImpl) s).getVarname().equals(varName))
				return (CStruct) s;

		return null;

	}

	public boolean equals(CStructBase other) {

		CStructBaseImpl o = (CStructBaseImpl) other;

		// Check for a duplicate varname
		if(this.varname != "" && o.varname != "") {
			 if(varname.equals(o.varname)) {
				 return true;
			 }
		}

		// Check for same struct name
		if(this.name.equals(o.name)) {
			return true;
		}

		// check, if varname don't collide with name
		if(this.name.equals(o.varname)) {
			return true;
		}

		// check, if varname don't collide with name
		if(this.varname.equals(o.name)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the name of this struct.
	 *
	 * @return the name of this struct
	 */
	@Override
	public String getName() {
    if (typedef && varname != null) {
      return varname;
    }

    return name != null ? name : varname;
  }

  /**
	 * Returns the struct with the name <code>name</code>.
	 *
	 * @param name
	 *            the name of the struct to search for
	 * @return a <code>CStructImpl</code> instance or <code>null</code> if no
	 *         struct of the name <code>name</code> was found
	 */
	public CStruct getStructByName(String name) {

		for (CStructBase s : structsUnions)
			if (s instanceof CStruct && ((CStructImpl) s).getName() != null && ((CStructImpl) s).getName().equals(name))
				return (CStruct) s;

		return null;

	}

	public String getStructOrUnion() {
		return structOrUnion;
	}

	/**
	 * Returns the structsUnions of this C source file.
	 *
	 * @return the structsUnions of this C source file
	 */
	public CStructBase[] getStructsUnions() {
		return structsUnions.toArray(new CStructBaseImpl[structsUnions.size()]);
	}

	/**
	 * Returns the structUnion with the name <code>name</code>.
	 *
	 * @param name
	 *            the name to check for
	 * @return a <code>CUnionImpl</code> instance or <code>null</code> if no
	 *         structUnion with the name <code>name</code> was found
	 */

	public CUnion getUnionByName(String name) {

		for (CStructBase u : structsUnions)
			if (u instanceof CUnion && ((CUnionImpl) u).getName().equals(name))
				return (CUnion) u;

		return null;

	}

	/**
	 * Returns the variables of this struct.
	 *
	 * @return the variables of this struct
	 */
	public CParam[] getVariables() {
		return vars.toArray(new CParamImpl[vars.size()]);
	}

	/**
	 * Returns the variable name of this struct.
	 *
	 * @return the variable name of this struct
	 */
	public String getVarname() {
		return varname;
	}

	/**
	 * Returns if this struct is a typedef.
	 *
	 * @return <code>true</code> if this struct is a typedef, otherwise
	 *         <code>false</code>
	 */
	public boolean isTypedef() {
		return typedef;
	}

	/**
	 * Returns the enums of this struct.
	 *
	 * @return the enums of this struct.
	 */
	public CEnum[] getEnums() {
		return enums.toArray(new CEnum[enums.size()]);
	}

	/**
	 * Validates the fields <code>name</code>, <code>varname</code> and
	 * <code>body</code> for syntactic correctness.
	 *
	 * @throws CCodeValidationException
	 *             if validation of identifiers and/or code fails
	 */
	public abstract void validateFields() throws CCodeValidationException;

	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		if (beforeDirectives.size() > 0) {
			toString(buffer, tabCount, beforeDirectives, "", "\n");
			buffer.append("\n");
		}
		indent(buffer, tabCount);
		buffer.append(typedef ? "typedef " : "");
		buffer.append(structOrUnion);
		buffer.append("".equals(name) ? "" : " "+name);
		buffer.append(" {\n");
		if (enums.size() > 0) {
			toString(buffer, tabCount+1, enums);
			buffer.append("\n");
		}
		if (structsUnions.size() > 0) {
			toString(buffer, tabCount+1, structsUnions);
			buffer.append("\n");
		}
		if (vars.size() > 0) {
			toString(buffer, tabCount+1, vars, ";", "\n");
			buffer.append("\n");
		}
		indent(buffer, tabCount);
		buffer.append("}");
		buffer.append("".equals(varname) ? "" : " "+varname);
		buffer.append(";");
		if (afterDirectives.size() > 0) {
			buffer.append("\n");
			toString(buffer, tabCount, afterDirectives);
		}
	}

	public static void main(String[] args) throws Exception {
		CStruct struct = CStruct.factory.create("TheStruct", "theStructVarName", false);
		struct.add(
				CParam.factory.create("int", "theInt"),
				CParam.factory.create("long", "theLong")
		);
		struct.add(CStruct.factory.create("TheStruct", "theStructVarName", false, CParam.factory.create("int", "theNestedInt")));
		struct.add(CEnum.factory.create("TheEnum", null, true, "theConstantOne", "theConstantTwo"));
		struct.addBeforeDirective("IFNDEF _THE_ENUM_");
		struct.addBeforeDirective("DEFINE _THE_ENUM_");
		struct.addAfterDirective("ENDIF");
		System.out.print(struct.toString(1));
		System.out.println("----------");
	}

	public CStructBase setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

}
