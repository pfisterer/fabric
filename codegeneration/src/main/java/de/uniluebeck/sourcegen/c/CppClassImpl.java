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
import java.util.LinkedList;

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

	private LinkedList<String> globalDeclarations = new LinkedList<String>();

	private LinkedList<VisElem> structsUnions = new LinkedList<VisElem>();

	private LinkedList<VisElem> vars = new LinkedList<VisElem>();
	
	private LinkedList<VisElem> cfuns = new LinkedList<VisElem>();
	
	private CppSourceFileImpl sourceFile;

	public CppClassImpl(String className, CppSourceFile sourceFile) {
		this.className = className;
		this.sourceFile = (CppSourceFileImpl) sourceFile;
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
		constructors.add(new VisElem(constructor, vis));
	}

	private void addInternal(long vis, CppDestructor destructor) throws CppDuplicateException {
		if (contains(destructor))
			throw new CppDuplicateException("Destructor already contained");
		destructors.add(new VisElem(destructor, vis));
	}
	
	private void addInternal(long vis, CppFun fun) throws CppDuplicateException {
		if (contains(fun))
			throw new CppDuplicateException("Function already contained");
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

	private CFun[] getCFuns(long vis) {
		ArrayList<CFun> ret = new ArrayList<CFun>();
		for (VisElem fv : cfuns)
			if (fv.vis == vis || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
				ret.add((CFun)fv.elem);
		return ret.toArray(new CFun[ret.size()]);
	}

	public String getTypeName() {
		return className;
	}

	public CppConstructor[] getConstructors(long vis) {
		ArrayList<CppConstructor> ret = new ArrayList<CppConstructor>();
		for (VisElem cv : constructors)
			if (cv.vis == vis || (vis == Cpp.PRIVATE && cv.vis == Cpp.NONE))
				ret.add((CppConstructor)cv.elem);
		return ret.toArray(new CppConstructor[ret.size()]);
	}

	public CppDestructor[] getDestructors(long vis) {
		ArrayList<CppDestructor> ret = new ArrayList<CppDestructor>();
		for (VisElem cv : destructors)
			if (cv.vis == vis || (vis == Cpp.PRIVATE && cv.vis == Cpp.NONE))
				ret.add((CppDestructor)cv.elem);
		return ret.toArray(new CppDestructor[ret.size()]);
	}

	public CEnum getEnumByName(String name) {
		for (VisElem ev : enums)
			if (((CEnumImpl)ev.elem).getTypeName().equals(name))
				return (CEnum)ev.elem;
		return null;
	}

	private CEnum[] getEnums(long vis) {
		ArrayList<CEnum> pe = new ArrayList<CEnum>();
		for (VisElem ev : enums)
			if (ev.vis == vis || (vis == Cpp.PRIVATE && ev.vis == Cpp.NONE))
				pe.add((CEnum)ev.elem);
		return pe.toArray(new CEnum[pe.size()]);
	}

	public String[] getExtendeds() {
		String[] ext = new String[extendeds.size()];
		for (int i=0; i<extendeds.size(); i++)
			ext[i] = ((CppClassImpl)extendeds.get(i).elem).className;
		return ext;
	}

	public CppFun[] getFuns(long vis) {
		ArrayList<CppFun> ret = new ArrayList<CppFun>();
		for (VisElem fv : funs)
			if (fv.vis == vis || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
				ret.add((CppFun)fv.elem);
		return ret.toArray(new CppFun[ret.size()]);
	}
	
	public String[] getGlobalDeclarations() {
		return globalDeclarations.toArray(new String[globalDeclarations.size()]);
	}
	
	public CFun[] getPrivateCFuns() {
		return getCFuns(Cpp.PRIVATE);
	}
	
	public CppConstructor[] getPrivateConstructors() {
		return getConstructors(Cpp.PRIVATE);
	}
	
	public CppDestructor[] getPrivateDestructors() {
		return getDestructors(Cpp.PRIVATE);
	}
	
	public CEnum[] getPrivateEnums() {
		return getEnums(Cpp.PRIVATE);
	}
	
	public CppFun[] getPrivateFuns() {
		return getFuns(Cpp.PRIVATE);
	}
	
	public CStructBase[] getPrivateStructsUnions() {
		return getStructsUnions(Cpp.PRIVATE);
	}
	
	public CppVar[] getPrivateVars() {
		return getVars(Cpp.PRIVATE);
	}
	
	public CFun[] getProtectedCFuns() {
		return getCFuns(Cpp.PROTECTED);
	}

	public CppConstructor[] getProtectedConstructors() {
		return getConstructors(Cpp.PROTECTED);
	}
	
	public CppDestructor[] getProtectedDestructors() {
		return getDestructors(Cpp.PROTECTED);
	}
	
	public CEnum[] getProtectedEnums() {
		return getEnums(Cpp.PROTECTED);
	}

	public CppFun[] getProtectedFuns() {
		return getFuns(Cpp.PROTECTED);
	}
	
	public CStructBase[] getProtectedStructsUnions() {
		return getStructsUnions(Cpp.PROTECTED);
	}
	
	public CppVar[] getProtectedVars() {
		return getVars(Cpp.PROTECTED);
	}
	
	public CFun[] getPublicCFuns() {
		return getCFuns(Cpp.PUBLIC);
	}
	
	public CppConstructor[] getPublicConstructors() {
		return getConstructors(Cpp.PUBLIC);
	}
	
	public CppDestructor[] getPublicDestructors() {
		return getDestructors(Cpp.PUBLIC);
	}
	
	public CEnum[] getPublicEnums() {
		return getEnums(Cpp.PUBLIC);
	}
	
	public CppFun[] getPublicFuns() {
		return getFuns(Cpp.PUBLIC);
	}
	
	public CStructBase[] getPublicStructsUnions() {
		return getStructsUnions(Cpp.PUBLIC);
	}

	public CppVar[] getPublicVars() {
		return getVars(Cpp.PUBLIC);
	}
	
	private CStructBase[] getStructsUnions(long vis) {
		ArrayList<CStructBase> su = new ArrayList<CStructBase>();
		for (VisElem suv : structsUnions)
			if (suv.vis == vis || (vis == Cpp.PRIVATE && suv.vis == Cpp.NONE))
				su.add((CStructBase)suv.elem);
		return su.toArray(new CStructBase[su.size()]);
	}
	
	private CppVar[] getVars(long vis) {
		ArrayList<CppVar> ret = new ArrayList<CppVar>();
		for (VisElem vv : vars)
			if (vv.vis == vis || (vis == Cpp.PRIVATE && vv.vis == Cpp.NONE))
				ret.add((CppVar)vv.elem);
		return ret.toArray(new CppVar[ret.size()]);
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

	public CppSourceFileImpl getSourceFile() {
		return sourceFile;
	}
	
	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		buffer.append("class " + this.className + ":\n");
		
		//inheritance
		int counter = 0;
		for(String c : this.getExtendeds()){
			counter++;
			buffer.append("\t" + c);
			if(counter != this.extendeds.size()){
				buffer.append(",\n");
			}
		}
		buffer.append("\n{\n");
		
		//public constructors, methods and values
		buffer.append("public:\n");
	
		//constructors
		for(CppConstructor c : this.getConstructors(Cpp.PUBLIC)){
			buffer.append("\t" + c.getSignature() + ";\n");
		}
				
		//destructors
		for(CppDestructor d : this.getDestructors(Cpp.PUBLIC)){
			buffer.append("\tvirtual " + d.getSignature() + ";\n");
		}
		
		//public functions
		for(CppFun f : this.getFuns(Cpp.PUBLIC)){
			buffer.append("\t" + f.getSignature() + ";\n");
		}
				
		buffer.append("\n");
		
		//private stuff
		buffer.append("private:\n");
		
		//private variables 
		for(CppVar v : this.getVars(Cpp.PRIVATE)){
			buffer.append("\t" + v.toString() + ";\n");
		}
		
		//private functions
		for(CppFun f : this.getFuns(Cpp.PRIVATE)){
			buffer.append("\t" + f.getSignature() + ";\n");
		}
		
		buffer.append("};\n");
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

}