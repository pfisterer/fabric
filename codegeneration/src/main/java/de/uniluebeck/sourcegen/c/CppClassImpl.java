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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;


class CppClassImpl extends CElemImpl implements CppClass {

	class VisElem {
		public WorkspaceElement elem;
		public long vis;
		public VisElem(WorkspaceElement elem, long vis) {
			this.vis = vis;
			this.elem = elem;
		}
	}

	private String className;

	private LinkedList<CPreProcessorDirectiveImpl> afterDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	private LinkedList<CPreProcessorDirectiveImpl> beforeDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

	private LinkedList<VisElem> constructors = new LinkedList<VisElem>();

	private LinkedList<VisElem> destructors = new LinkedList<VisElem>();

	private LinkedList<VisElem> enums = new LinkedList<VisElem>();

	private LinkedList<VisElem> extendeds = new LinkedList<VisElem>();

	private LinkedList<VisElem> funs = new LinkedList<VisElem>();

	private LinkedList<VisElem> nested = new LinkedList<VisElem>();

	private LinkedList<String> globalDeclarations = new LinkedList<String>();

	private LinkedList<VisElem> structsUnions = new LinkedList<VisElem>();

	private LinkedList<VisElem> vars = new LinkedList<VisElem>();

	private LinkedList<VisElem> cfuns = new LinkedList<VisElem>();

	private CComment comment = null;

	// Needed for nested class
	private List<CppClass> parents = new LinkedList<CppClass>();

	public CppClassImpl(String className) {
		this.className = className;
	}

	public CppClass add(long vis, CEnum... enums) throws CppDuplicateException {
		for (CEnum e : enums)
			addInternal(vis, e);
		return this;
	}

	public CppClass add(long vis, CFun... functions) throws CppDuplicateException {
		for (CFun f : functions)
			addInternal(vis, f);
		return this;
	}

	public CppClass add(long vis, CppConstructor... constructors) throws CppDuplicateException {
		for (CppConstructor cc : constructors)
			addInternal(vis, cc);
		return this;
	}

	public CppClass add(long vis, CppDestructor... destructors) throws CppDuplicateException {
		for (CppDestructor d : destructors)
			addInternal(vis, d);
		return this;
	}

	public CppClass add(long vis, CppFun... funs) throws CppDuplicateException {
		for (CppFun f : funs)
			addInternal(vis, f);
		return this;
	}

	public CppClass add(long vis, CppVar... vars) throws CppDuplicateException {
		for (CppVar v : vars)
			addInternal(vis, v);
		return this;
	}

	public CppClass add(long vis, CStruct... structs) throws CppDuplicateException {
		for (CStruct s : structs)
			addInternal(vis, s);
		return this;
	}

	public CppClass add(long vis, CUnion... unions) throws CppDuplicateException {
		for (CUnion u : unions)
			addInternal(vis, u);
		return this;
	}

	@Override
    public CppClass add(long vis, CppClass... cppClass) throws CppDuplicateException {

		for (CppClass c : cppClass) {
			addInternal(vis, c);
			// Set the parent
			c.addParents(this.parents, this);
		}

		/*
    	for (CppClass c : cppClass) {

    		for(CppClass n : nested) {
    			String name = c.getTemplateName();
    			if(name != null && n.getTemplateName().equals(c.getTemplateName())) {
    				throw new CppDuplicateException("Nested class " + c.getTemplateName() + " exists.");
    			}
    		}

    		// No exception happens, just add it
    		this.nested.add(c);
		}
		*/

    	return this;
    }

	public CppClass addAfterDirective(CPreProcessorDirective... afterDirectivesArr) {
		for (CPreProcessorDirective d : afterDirectivesArr)
			afterDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}

	public CppClass addAfterDirective(String... directives) throws CPreProcessorValidationException {
		for (String d : directives)
			afterDirectives.add(new CPreProcessorDirectiveImpl(d));
		return this;
	}

	public CppClass addBeforeDirective(CPreProcessorDirective... beforeDirectivesArr) {
		for (CPreProcessorDirective d : beforeDirectivesArr)
			beforeDirectives.add((CPreProcessorDirectiveImpl)d);
		return this;
	}

	public CppClass addBeforeDirective(String... directives) throws CPreProcessorValidationException {
		for (String d : directives)
			beforeDirectives.add(new CPreProcessorDirectiveImpl(d));
		return this;
	}

	public CppClass addExtended(long vis, CppClass... extendeds) throws CppDuplicateException {
		for (CppClass e : extendeds)
			addExtendedInternal(vis, e);
		return this;
	}

	private void addExtendedInternal(long vis, CppClass extended) throws CppDuplicateException {
		if (containsExtended(extended))
			throw new CppDuplicateException("Extended class already contained");
		extendeds.add(new VisElem(extended, vis));
	}

	private void addInternal(long vis, CppClass classObj) throws CppDuplicateException {
		if (contains(classObj))
			throw new CppDuplicateException("Enum already contained");
		nested.add(new VisElem(classObj, vis));
	}


	private void addInternal(long vis, CEnum enumObj) throws CppDuplicateException {
		if (contains(enumObj))
			throw new CppDuplicateException("Enum already contained");
		enums.add(new VisElem(enumObj, vis));
	}

	private void addInternal(long vis, CFun fun) throws CppDuplicateException {
		if (contains(fun))
			throw new CppDuplicateException("Function already contained");
		cfuns.add(new VisElem(fun, vis));
	}

	private void addInternal(long vis, CppConstructor constructor) throws CppDuplicateException {
		if (contains(constructor))
			throw new CppDuplicateException("Constructor already contained");
		if (constructor == null)
			throw new NullPointerException();
		// Set the class automatically
		constructor.setClass(this);
		constructors.add(new VisElem(constructor, vis));
	}

	private void addInternal(long vis, CppDestructor destructor) throws CppDuplicateException {
		if (contains(destructor))
			throw new CppDuplicateException("Destructor already contained");
		destructor.setClass(this);
		destructors.add(new VisElem(destructor, vis));
	}

	private void addInternal(long vis, CppFun fun) throws CppDuplicateException {
		if (contains(fun))
			throw new CppDuplicateException("Function already contained");
		// Set the class automatically
		fun.setClass(this);
		funs.add(new VisElem(fun, vis));
	}

	private void addInternal(long vis, CppVar var) throws CppDuplicateException {
		if (contains(var))
			throw new CppDuplicateException("Var already contained");
		vars.add(new VisElem(var, vis));
	}

	private void addInternal(long vis, CStruct struct) throws CppDuplicateException {
		if (contains(struct))
			throw new CppDuplicateException("Struct already contained");
		structsUnions.add(new VisElem(struct, vis));
	}

	private void addInternal(long vis, CUnion union) throws CppDuplicateException {
		if (contains(union))
			throw new CppDuplicateException("Union already contained");
		structsUnions.add(new VisElem(union, vis));
	}

	public boolean contains(CEnum enumObj) {
		for (VisElem ev : enums)
			if (((CEnumImpl)ev.elem).equals(enumObj))
				return true;
		return false;
	}

	public boolean contains(CppClass classObj) {
		for (VisElem ev : nested)
			if (((CppClass)ev.elem).equals(classObj))
				return true;
		return false;
	}

	public boolean contains(CFun fun) {
		for (VisElem cfv : cfuns)
			if (((CFunImpl)cfv.elem).equals(fun))
				return true;
		return false;
	}

	public boolean contains(CppConstructor constructor) {
		for (VisElem cv : constructors)
			if (((CppConstructorImpl)cv.elem).equals(constructor))
				return true;
		return false;
	}

	public boolean contains(CppDestructor destructor) {
		for (VisElem dv : destructors)
			if (((CppDestructorImpl)dv.elem).equals(destructor))
				return true;
		return false;
	}

	public boolean contains(CppFun fun) {
		for (VisElem fv : funs)
			if (((CppFunImpl)fv.elem).equals(fun))
				return true;
		return false;
	}

	public boolean contains(CppVar var) {
		for (VisElem vv: vars)
			if (((CppVarImpl)vv.elem).equals(var))
				return true;
		return false;
	}

	public boolean contains(CStruct struct) {
		for (VisElem suv: structsUnions)
			if (suv.elem instanceof CStruct && ((CStruct)suv.elem).equals(struct))
				return true;
		return false;
	}

	public boolean contains(CUnion union) {
		for (VisElem suv: structsUnions)
			if (suv.elem instanceof CUnion && ((CUnion)suv.elem).equals(union))
				return true;
		return false;
	}

	public boolean containsAfterDirective(CPreProcessorDirective directive) {
		for (CPreProcessorDirective d : afterDirectives)
			if (d.equals(directive))
				return true;
		return false;
	}

	public boolean containsBeforeDirective(CPreProcessorDirective directive) {
		for (CPreProcessorDirective d : beforeDirectives)
			if (d.equals(directive))
				return true;
		return false;
	}

	public boolean containsExtended(CppClass extended) {
		for (VisElem ccv : extendeds)
			if (((CppClass)ccv.elem).equals(extended))
				return true;
		return false;
	}

	public CPreProcessorDirectiveImpl[] getAfterDirectives() {
		return afterDirectives.toArray(new CPreProcessorDirectiveImpl[afterDirectives.size()]);
	}

	public CPreProcessorDirectiveImpl[] getBeforeDirectives() {
		return beforeDirectives.toArray(new CPreProcessorDirectiveImpl[beforeDirectives.size()]);
	}

	private List<CFun> getCFuns(long vis) {
		ArrayList<CFun> ret = new ArrayList<CFun>();
		for (VisElem fv : cfuns)
			if (fv.vis == vis)// || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
				ret.add((CFun)fv.elem);
		return ret;
	}

	public List<CppConstructor> getConstructors(long vis) {
		ArrayList<CppConstructor> ret = new ArrayList<CppConstructor>();
		for (VisElem cv : constructors)
			if (cv.vis == vis)// || (vis == Cpp.PRIVATE && cv.vis == Cpp.NONE))
				ret.add((CppConstructor)cv.elem);
		return ret;
	}

	public List<CppDestructor> getDestructors(long vis) {
		ArrayList<CppDestructor> ret = new ArrayList<CppDestructor>();
		for (VisElem cv : destructors)
			if (cv.vis == vis)// || (vis == Cpp.PRIVATE && cv.vis == Cpp.NONE))
				ret.add((CppDestructor)cv.elem);
		return ret;
	}

	public CEnum getEnumByName(String name) {
		for (VisElem ev : enums)
			if (((CEnumImpl)ev.elem).getTypeName().equals(name))
				return (CEnum)ev.elem;
		return null;
	}

	private List<CEnum> getEnums(long vis) {
		ArrayList<CEnum> pe = new ArrayList<CEnum>();
		for (VisElem ev : enums)
			if (ev.vis == vis)// || (vis == Cpp.PRIVATE && ev.vis == Cpp.NONE))
				pe.add((CEnum)ev.elem);
		return pe;
	}

	public String[] getExtendeds() {
		String[] ext = new String[extendeds.size()];
		for (int i=0; i<extendeds.size(); i++)
			ext[i] = ((CppClassImpl)extendeds.get(i).elem).className;
		return ext;
	}

	public List<CppFun> getFuns(long vis) {
		ArrayList<CppFun> ret = new ArrayList<CppFun>();
		for (VisElem fv : funs)
			if (fv.vis == vis)// || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
				ret.add((CppFun)fv.elem);
		return ret;
	}

	public List<CppClass> getNested(long vis) {
		ArrayList<CppClass> ret = new ArrayList<CppClass>();
		for (VisElem fv : nested)
			if (fv.vis == vis)// || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
				ret.add((CppClass)fv.elem);
		return ret;
	}

	public String[] getGlobalDeclarations() {
		return globalDeclarations.toArray(new String[globalDeclarations.size()]);
	}

	public List<CFun> getPrivateCFuns() {
		return getCFuns(Cpp.PRIVATE);
	}

	public List<CppConstructor> getPrivateConstructors() {
		return getConstructors(Cpp.PRIVATE);
	}

	public List<CppDestructor> getPrivateDestructors() {
		return getDestructors(Cpp.PRIVATE);
	}

	public List<CEnum> getPrivateEnums() {
		return getEnums(Cpp.PRIVATE);
	}

	public List<CppFun> getPrivateFuns() {
		return getFuns(Cpp.PRIVATE);
	}

	public List<CStructBase> getPrivateStructsUnions() {
		return getStructsUnions(Cpp.PRIVATE);
	}

	public List<CppVar> getPrivateVars() {
		return getVars(Cpp.PRIVATE);
	}

	public List<CFun> getProtectedCFuns() {
		return getCFuns(Cpp.PROTECTED);
	}

	public List<CppConstructor> getProtectedConstructors() {
		return getConstructors(Cpp.PROTECTED);
	}

	public List<CppDestructor> getProtectedDestructors() {
		return getDestructors(Cpp.PROTECTED);
	}

	public List<CEnum> getProtectedEnums() {
		return getEnums(Cpp.PROTECTED);
	}

	public List<CppFun> getProtectedFuns() {
		return getFuns(Cpp.PROTECTED);
	}

	public List<CStructBase> getProtectedStructsUnions() {
		return getStructsUnions(Cpp.PROTECTED);
	}

	public List<CppVar> getProtectedVars() {
		return getVars(Cpp.PROTECTED);
	}

	public List<CFun> getPublicCFuns() {
		return getCFuns(Cpp.PUBLIC);
	}

	public List<CppConstructor> getPublicConstructors() {
		return getConstructors(Cpp.PUBLIC);
	}

	public List<CppDestructor> getPublicDestructors() {
		return getDestructors(Cpp.PUBLIC);
	}

	public List<CEnum> getPublicEnums() {
		return getEnums(Cpp.PUBLIC);
	}

	public List<CppFun> getPublicFuns() {
		return getFuns(Cpp.PUBLIC);
	}

	public List<CStructBase> getPublicStructsUnions() {
		return getStructsUnions(Cpp.PUBLIC);
	}

	public List<CppVar> getPublicVars() {
		return getVars(Cpp.PUBLIC);
	}

	private List<CStructBase> getStructsUnions(long vis) {
		ArrayList<CStructBase> su = new ArrayList<CStructBase>();
		for (VisElem suv : structsUnions)
			if (suv.vis == vis || (vis == Cpp.PRIVATE && suv.vis == Cpp.NONE))
				su.add((CStructBase)suv.elem);
		return su;
	}

	private List<CppVar> getVars(long vis) {
		ArrayList<CppVar> ret = new ArrayList<CppVar>();
		for (VisElem vv : vars)
			if (vv.vis == vis || (vis == Cpp.PRIVATE && vv.vis == Cpp.NONE))
				ret.add((CppVar)vv.elem);
		return ret;
	}

	public long getVis(CEnum enumObj) {
		for (VisElem ev : enums)
			if (ev.elem == enumObj)
				return ev.vis;
		return Cpp.NONE;
	}

	public long getVis(CFun fun) {
		for (VisElem fv : funs)
			if (((CFun)fv.elem).equals(fun))
				return fv.vis;
		return Cpp.NONE;
	}

	public long getVis(CppConstructor constructor) {
		for (VisElem cv : constructors)
			if (cv.elem == constructor)
				return cv.vis;
		return Cpp.NONE;
	}

	public long getVis(CppDestructor destructor) {
		for (VisElem dv : destructors)
			if (dv.elem == destructor)
				return dv.vis;
		return Cpp.NONE;
	}

	public long getVis(CppFun fun) {
		for (VisElem fv : funs)
			if (fv.elem == fun)
				return fv.vis;
		return Cpp.NONE;
	}

	public long getVis(CppVar var) {
		for (VisElem vv : vars)
			if (vv.elem == var)
				return vv.vis;
		return Cpp.NONE;
	}

	public long getVis(CStruct struct) {
		for (VisElem suv : structsUnions)
			if (suv.elem == struct)
				return suv.vis;
		return Cpp.NONE;
	}

	public long getVis(CUnion union) {
		for (VisElem suv : structsUnions)
			if (suv.elem == union)
				return suv.vis;
		return Cpp.NONE;
	}

	public long getVisExtended(CppClass extended) {
		for (VisElem cv : extendeds)
			if (cv.elem == extended)
				return cv.vis;
		return Cpp.NONE;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		/*
		 * Generates the *.hpp files
		 */

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		buffer.append("class " + this.className);

		//inheritance
		int counter = 0;
		for(String c : this.getExtendeds()){
			counter++;
			buffer.append("\t" + c);
			if(counter != this.extendeds.size()){
				buffer.append("," + Cpp.newline);
			}
		}
		buffer.append(Cpp.newline + "{" + Cpp.newline);

		// public stuff
		buffer.append("public:" + Cpp.newline);

		// public stuff will be one tab count deeper
		StringBuffer tmp_public = new StringBuffer();

		// constructors
		if(this.getConstructors(Cpp.PUBLIC).size() > 0) {
			//tmp_public.append("/* Constructors of " + this.getName() + " */" + Cpp.newline);
			for(CppConstructor c : this.getConstructors(Cpp.PUBLIC)){
				tmp_public.append("" + c.getSignature() + ";" + Cpp.newline);
			}
			tmp_public.append(Cpp.newline);
		}

		// destructors
		if(this.getDestructors(Cpp.PUBLIC).size() > 0) {
			//tmp_public.append("/* Destructors of " + this.getName() + " */" + Cpp.newline);
			for(CppDestructor d : this.getDestructors(Cpp.PUBLIC)){
				tmp_public.append("virtual " + d.getSignature() + ";" + Cpp.newline);
			}
			tmp_public.append(Cpp.newline);
		}


		// public functions
		if(this.getFuns(Cpp.PUBLIC).size() > 0) {
			//tmp_public.append("/* Public functions of " + this.getName() + " */" + Cpp.newline);
			for(CppFun f : this.getFuns(Cpp.PUBLIC)){
				tmp_public.append(f.getSignature() + ";" + Cpp.newline);
			}
			tmp_public.append(Cpp.newline);
		}
		// public nested classes
		if(this.getNested(Cpp.PUBLIC).size() > 0) {
			//tmp_public.append("/* Public nested classed of " + this.getName() + " */" + Cpp.newline);
			for(CppClass f : this.getNested(Cpp.PUBLIC)){
				// Add the classes recursive
				tmp_public.append(f + ";" + Cpp.newline);
			}
			tmp_public.append(Cpp.newline);
		}

		// public variables
		if(this.getVars(Cpp.PUBLIC).size() > 0) {
			//tmp_public.append("/* Public variables of " + this.getName() + " */" + Cpp.newline);
			for(CppVar v : this.getVars(Cpp.PUBLIC)){
				tmp_public.append(v.toString() + ";" + Cpp.newline);
			}
			tmp_public.append(Cpp.newline);
		}

		// Append the public stuff
		appendBody(buffer, tmp_public, tabCount + 1);

		// Private stuff will be one tab count deeper
		StringBuffer tmp_private = new StringBuffer();

		buffer.append(Cpp.newline);

		// private stuff
		buffer.append("private:" + Cpp.newline);

		// private functions
		if(this.getFuns(Cpp.PRIVATE).size() > 0) {
			//tmp_private.append("/* Private functions of " + this.getName() + " */" + Cpp.newline);
			for(CppFun f : this.getFuns(Cpp.PRIVATE)){
				tmp_private.append(f.getSignature() + ";" + Cpp.newline);
			}
			tmp_private.append(Cpp.newline);
		}

		// private nested classes
		if(this.getNested(Cpp.PRIVATE).size() > 0) {
			//tmp_private.append("/* Private nested classes of " + this.getName() + " */" + Cpp.newline);
			for(CppClass f : this.getNested(Cpp.PRIVATE)){
				// Add the classes recursive
				f.toString(tmp_private, tabCount);
			}
			tmp_private.append(Cpp.newline);
		}

		// private variables
		if(this.getVars(Cpp.PRIVATE).size() > 0) {
			//tmp_private.append("/* Private variables of " + this.getName() + " */" + Cpp.newline);
			for(CppVar v : this.getVars(Cpp.PRIVATE)){
				tmp_private.append(v.toString() + ";" + Cpp.newline);
			}
			tmp_private.append(Cpp.newline);
		}

		// Append the private stuff
		appendBody(buffer, tmp_private, tabCount + 1);

		// Close the class
		buffer.append(Cpp.newline + "};" + Cpp.newline + Cpp.newline);


		/*
		toString(buffer, tabCount, beforeDirectives, "", "\n", true);
		indent(buffer, tabCount);
		buffer.append("class " + className);
		buffer.append("{\n");
		if (extendeds.size() > 0) {
			buffer.append(" : ");
			for (VisElem ve : extendeds) {
				buffer.append(Cpp.toString(ve.vis));
				buffer.append(" ");
				buffer.append(((CppClassImpl)ve.elem).getTypeName());
			}
		}
		if (globalDeclarations.size() > 0) {
			for (String s : globalDeclarations) {
				indent(buffer, tabCount);
				buffer.append(s);
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		//appendVisElem(buffer, tabCount, enums);
		//appendVisElem(buffer, tabCount, structsUnions);
		//appendVisElem(buffer, tabCount, vars);
		appendVisElem(buffer, tabCount, constructors);
		//appendVisElem(buffer, tabCount, destructors);
		//appendVisElem(buffer, tabCount, funs);
		buffer.append("\n}");
		//toString(buffer, tabCount, afterDirectives, "", "\n", true);
		 * */
	}

    @Override
    public String getName() {
        return this.className;
    }

	@Override
	public CppClass addParents(List<CppClass> cppClass, CppClass clazz) {
		this.parents.addAll(cppClass);
		this.parents.add(clazz);
		return this;
	}

	@Override
	public List<CppClass> getParents() {
		return parents;
	}

	@Override
	@Deprecated
	public String getTypeName() {
		System.err.print("getTypeName() is depricated. Use getName() instead.");
		return this.getName();
	}

	@Override
	public CppClass setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

}