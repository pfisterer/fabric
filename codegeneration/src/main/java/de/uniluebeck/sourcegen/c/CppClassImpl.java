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

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppCodeValidationException;
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

    private List<CPreProcessorDirectiveImpl> afterDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

    private List<CPreProcessorDirectiveImpl> beforeDirectives = new LinkedList<CPreProcessorDirectiveImpl>();

    private List<VisElem> constructors = new LinkedList<VisElem>();

    private List<VisElem> destructors = new LinkedList<VisElem>();

    private List<VisElem> enums = new LinkedList<VisElem>();

    private List<VisElem> extendeds = new LinkedList<VisElem>();

    private List<String> extendeds_string = new LinkedList<String>();

    private List<VisElem> funs = new LinkedList<VisElem>();

    private List<VisElem> nested = new LinkedList<VisElem>();

    private List<String> globalDeclarations = new LinkedList<String>();

    private List<VisElem> structsUnions = new LinkedList<VisElem>();

    private List<VisElem> vars = new LinkedList<VisElem>();

    private List<VisElem> cfuns = new LinkedList<VisElem>();

    boolean isPrepared = false;

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

    public CppClass add(CppVar... vars) throws CppDuplicateException, CppCodeValidationException {
        for (CppVar v : vars) {
            // v.setClass(this);
            if (v.getVisability() == null) {
                throw new CppCodeValidationException("The variable " + v.getVarName()
                        + " cannot be added without a visability.");
            }
            addInternal(v.getVisability(), v);
        }
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

    /**
     * Nested class
     */
    @Override
    public CppClass add(long vis, CppClass... cppClass) throws CppDuplicateException {

        for (CppClass c : cppClass) {
            addInternal(vis, c);
        }
        return this;
    }

    public CppClass addAfterDirective(CPreProcessorDirective... afterDirectivesArr) {
        for (CPreProcessorDirective d : afterDirectivesArr)
            afterDirectives.add((CPreProcessorDirectiveImpl) d);
        return this;
    }

    public CppClass addAfterDirective(String... directives) throws CPreProcessorValidationException {
        for (String d : directives)
            afterDirectives.add(new CPreProcessorDirectiveImpl(d));
        return this;
    }

    public CppClass addBeforeDirective(CPreProcessorDirective... beforeDirectivesArr) {
        for (CPreProcessorDirective d : beforeDirectivesArr)
            beforeDirectives.add((CPreProcessorDirectiveImpl) d);
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

    public CppClass addExtended(long vis, String... extendeds) throws CppDuplicateException {
        for (String e : extendeds) {
            String tmp = Cpp.toString(vis) + " " + e;
            if (this.extendeds_string.contains(tmp)) {
                throw new CppDuplicateException("Extended class " + tmp + " already contained");
            }

            // Check again this.extendeds (classes)
            for (VisElem ex : this.extendeds) {
                if (ex.elem instanceof CppClass) {
                    CppClass clazz = (CppClass) ex.elem;
                    if (clazz.getName().equals(e)) {
                        throw new CppDuplicateException("Extended class " + clazz.getName() + " already contained");
                    }
                }
            }

            this.extendeds_string.add(tmp);
        }
        return this;
    }

    private void addExtendedInternal(long vis, CppClass extended) throws CppDuplicateException {

        if (containsExtended(extended)) {
            throw new CppDuplicateException("Extended class " + extended.getName() + " already contained");
        }

        // Check again extendeds_string
        if (this.extendeds_string.contains(extended.getName())) {
            throw new CppDuplicateException("Extended class " + extended.getName() + " already contained");
        }

        extendeds.add(new VisElem(extended, vis));
    }

    private void addInternal(long vis, CppClass classObj) throws CppDuplicateException {
        if (contains(classObj))
            throw new CppDuplicateException("Class already contained");
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
        // var.setClass(this);
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
            if (((CEnumImpl) ev.elem).equals(enumObj))
                return true;
        return false;
    }

    public boolean contains(CppClass classObj) {
        for (VisElem ev : nested)
            if (((CppClass) ev.elem).equals(classObj))
                return true;
        return false;
    }

    public boolean contains(CFun fun) {
        for (VisElem cfv : cfuns)
            if (((CFunImpl) cfv.elem).equals(fun))
                return true;
        return false;
    }

    public boolean contains(CppConstructor constructor) {
        for (VisElem cv : constructors)
            if (((CppConstructorImpl) cv.elem).equals(constructor))
                return true;
        return false;
    }

    public boolean contains(CppDestructor destructor) {
        for (VisElem dv : destructors)
            if (((CppDestructorImpl) dv.elem).equals(destructor))
                return true;
        return false;
    }

    public boolean contains(CppFun fun) {
        for (VisElem fv : funs)
            if (((CppFunImpl) fv.elem).equals(fun))
                return true;
        return false;
    }

    public boolean contains(CppVar var) {
        for (VisElem vv : vars)
            if (((CppVarImpl) vv.elem).equals(var))
                return true;
        return false;
    }

    public boolean contains(CStruct struct) {
        for (VisElem suv : structsUnions)
            if (suv.elem instanceof CStruct && ((CStruct) suv.elem).equals(struct))
                return true;
        return false;
    }

    public boolean contains(CUnion union) {
        for (VisElem suv : structsUnions)
            if (suv.elem instanceof CUnion && ((CUnion) suv.elem).equals(union))
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
            if (((CppClass) ccv.elem).equals(extended))
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
                ret.add((CFun) fv.elem);
        return ret;
    }

    public List<CppConstructor> getConstructors(long vis) {
        ArrayList<CppConstructor> ret = new ArrayList<CppConstructor>();
        for (VisElem cv : constructors)
            if (cv.vis == vis)// || (vis == Cpp.PRIVATE && cv.vis == Cpp.NONE))
                ret.add((CppConstructor) cv.elem);
        return ret;
    }

    public List<CppDestructor> getDestructors(long vis) {
        ArrayList<CppDestructor> ret = new ArrayList<CppDestructor>();
        for (VisElem dv : destructors)
            if (dv.vis == vis)// || (vis == Cpp.PRIVATE && dv.vis == Cpp.NONE))
                ret.add((CppDestructor) dv.elem);
        return ret;
    }

    public CEnum getEnumByName(String name) {
        for (VisElem ev : enums)
            if (((CEnumImpl) ev.elem).getName().equals(name))
                return (CEnum) ev.elem;
        return null;
    }

    public String[] getExtendeds() {
        String[] ext = new String[extendeds.size() + extendeds_string.size()];
        int i = 0;
        for (i = 0; i < extendeds.size(); i++) {
            VisElem e = extendeds.get(i);
            String name = ((CppClassImpl) e.elem).className;
            ext[i] = Cpp.toString(e.vis) + " " + name;
        }

        for (int j = i; j < extendeds_string.size(); j++) {
            ext[j] = extendeds_string.get(j);
        }

        return ext;
    }

    public List<CppFun> getFuns(long vis) {
        ArrayList<CppFun> ret = new ArrayList<CppFun>();
        for (VisElem fv : funs)
            if (fv.vis == vis)// || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
                ret.add((CppFun) fv.elem);
        return ret;
    }

    public List<CppClass> getNested(long vis) {
        ArrayList<CppClass> ret = new ArrayList<CppClass>();
        for (VisElem fv : nested)
            if (fv.vis == vis)// || (vis == Cpp.PRIVATE && fv.vis == Cpp.NONE))
                ret.add((CppClass) fv.elem);
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
                su.add((CStructBase) suv.elem);
        return su;
    }

    public List<CppVar> getVars(long vis) {
        ArrayList<CppVar> ret = new ArrayList<CppVar>();
        for (VisElem vv : vars) {
            // Four cases
            if (Cpp.isPrivate(vv.vis) && Cpp.isPrivate(vis)) {
                ret.add((CppVar) vv.elem);
            } else if (Cpp.isPublic(vv.vis) && Cpp.isPublic(vis)) {
                ret.add((CppVar) vv.elem);
            } else if (Cpp.isProtected(vv.vis) && Cpp.isProtected(vis)) {
                ret.add((CppVar) vv.elem);
            } else if (Cpp.isStatic(vv.vis) && Cpp.isStatic(vis)) {
                ret.add((CppVar) vv.elem);
            }
        }
        return ret;
    }
    
    /**
     * Get a list of all CEnums with the given visibility that
     * are stored in the current CppClass object.
     *
     * @param vis Desired element visibility
     *
     * @return List of CEnum objects with given visibility
     */
    @Override
    public List<CEnum> getEnums(long vis) {
        ArrayList<CEnum> result = new ArrayList<CEnum>();

        // Discern element visibilities
        for (VisElem ev: this.enums) {
            if (Cpp.isPrivate(ev.vis) && Cpp.isPrivate(vis)) {
                result.add((CEnum)ev.elem);
            }
            else if (Cpp.isPublic(ev.vis) && Cpp.isPublic(vis)) {
                result.add((CEnum)ev.elem);
            }
            else if (Cpp.isProtected(ev.vis) && Cpp.isProtected(vis)) {
                result.add((CEnum)ev.elem);
            }
            else if (Cpp.isStatic(ev.vis) && Cpp.isStatic(vis)) {
                result.add((CEnum)ev.elem);
            }
        }

        return result;
    }

    public long getVis(CEnum enumObj) {
        for (VisElem ev : enums)
            if (ev.elem == enumObj)
                return ev.vis;
        return Cpp.NONE;
    }

    public long getVis(CFun fun) {
        for (VisElem fv : funs)
            if (((CFun) fv.elem).equals(fun))
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

        prepare();

        // TODO: Maybe beforeDirectives, globalDeclarations

        // Write comment if necessary
        if (comment != null) {
            comment.toString(buffer, tabCount);
        }

        buffer.append("class " + this.className);

        // Inheritance
        if (this.getExtendeds().length > 0) {
            for (int i = 0; i < this.getExtendeds().length; i++) {
                if (i == 0) {
                    buffer.append(" : ");
                } else {
                    buffer.append(", ");
                }
                buffer.append(this.getExtendeds()[i]);
            }
        }

        buffer.append(Cpp.newline + "{" + Cpp.newline + Cpp.newline);

        /**
         * ##################################################################
         * Public stuff
         * ##################################################################
         */
        // Needed to get the public stuff one tab count deeper
        StringBuffer tmp_public = new StringBuffer();
        toStringHelper(tmp_public, tabCount, Cpp.PUBLIC);

        if (tmp_public.length() > 0) {
            indent(buffer, tabCount + 1);
            buffer.append("public:" + Cpp.newline);
            appendBody(buffer, tmp_public, tabCount + 2);
        }

        /**
         * ##################################################################
         * Protected stuff
         * ##################################################################
         */
        // Needed to get the protected stuff one tab count deeper
        StringBuffer tmp_protected = new StringBuffer();
        toStringHelper(tmp_protected, tabCount, Cpp.PROTECTED);

        if (tmp_protected.length() > 0) {
            buffer.append(Cpp.newline);
            indent(buffer, tabCount + 1);
            buffer.append("protected:" + Cpp.newline);
            appendBody(buffer, tmp_protected, tabCount + 2);
        }

        /**
         * ##################################################################
         * Private stuff
         * ##################################################################
         */
        // Needed to get the private stuff one tab count deeper
        StringBuffer tmp_private = new StringBuffer();
        toStringHelper(tmp_private, tabCount, Cpp.PRIVATE);

        if (tmp_private.length() > 0) {
            buffer.append(Cpp.newline);
            indent(buffer, tabCount + 1);
            buffer.append("private:" + Cpp.newline);
            appendBody(buffer, tmp_private, tabCount + 2);
        }

        /**
         * ##################################################################
         */

        // Close the class
        buffer.append(Cpp.newline + "};");

        // Final empty line
        buffer.append(Cpp.newline);

        // TODO: Maybe afterDirectives
    }

    protected void indent(StringBuffer buffer, int tabCount) {
        for (int i = 0; i < tabCount; i++)
            buffer.append("\t");
    }

    private void toStringHelper(StringBuffer tmp, int tabCount, long visability) {

        // nested classes
        if (null != this.getNested(visability) && this.getNested(visability).size() > 0) {
            for (CppClass c : this.getNested(visability)) {
                // Add the classes recursive
                c.toString(tmp, tabCount);
            }
            tmp.append(Cpp.newline);
        }

        // structs + unions
        if (null != this.getStructsUnions(visability) && this.getStructsUnions(visability).size() > 0) {
            for (CStructBase c : this.getStructsUnions(visability)) {
                tmp.append(c.toString() + Cpp.newline);
            }
        }

        // enums
        if (null != this.getEnums(visability) && this.getEnums(visability).size() > 0) {
            for (CEnum e : this.getEnums(visability)) {
                tmp.append(e.toString() + Cpp.newline);
            }
        }

        // constructors
        if (null != this.getConstructors(visability) && this.getConstructors(visability).size() > 0) {
            for (CppConstructor c : this.getConstructors(visability)) {
                tmp.append(c.getSignature() + ";" + Cpp.newline);
            }
        }

        // destructors
        if (null != this.getDestructors(visability) && this.getDestructors(visability).size() > 0) {
            for (CppDestructor d : this.getDestructors(visability)) {
                tmp.append("virtual " + d.getSignature() + ";" + Cpp.newline); // TODO: virtual?
            }
            tmp.append(Cpp.newline);
        }

        // functions
        if (null != this.getFuns(visability) && this.getFuns(visability).size() > 0) {
            for (CppFun f : this.getFuns(visability)) {
                tmp.append(f.getSignature() + ";" + Cpp.newline);
            }
            tmp.append(Cpp.newline);
        }

        // variables
        if (null != this.getVars(visability) && this.getVars(visability).size() > 0) {
            for (CppVar v : this.getVars(visability)) {
                tmp.append(v.toString() + ";" + Cpp.newline);
            }
            tmp.append(Cpp.newline);
        }
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
    public String getName() {
        return this.className;
    }

    @Override
    public CppClass setComment(CComment comment) {
        this.comment = comment;
        return this;
    }

    /**
     * This method prepares the files.
     */
    @Override
    public void prepare() {

        if (isPrepared) {
            return;
        }

        List<CppVar> vars = this.getVars(Cpp.PRIVATE);
        vars.addAll(this.getVars(Cpp.PUBLIC));
        vars.addAll(this.getVars(Cpp.PROTECTED));

        // get variables, which are initialized
        List<CppVar> initializedVars = new LinkedList<CppVar>();
        for (CppVar cppVar : vars) {
            String init = cppVar.getInit();
            if (init != null && !("").equals(init)) {
                initializedVars.add(cppVar);
            }
        }

        // if we have init values, and no constructors, than we need a
        // default constructor, such that the init values don't get lost
        if (this.getConstructors(Cpp.PUBLIC).size() == 0 && initializedVars.size() > 0) {
            // Create
            try {
                CppConstructor con = CppConstructor.factory.create();
                this.add(Cpp.PUBLIC, con);
            } catch (CppDuplicateException e) {
                e.printStackTrace();
            }
        }

        // Add the initial values to all constructors
        for (CppConstructor c : this.getConstructors(Cpp.PUBLIC)) {
            c.setInititalVars(initializedVars);
        }

        for (CppConstructor c : this.getConstructors(Cpp.PRIVATE)) {
            c.setInititalVars(initializedVars);
        }

        for (CppConstructor c : this.getConstructors(Cpp.PROTECTED)) {
            c.setInititalVars(initializedVars);
        }

        // Set all parents to subclasses
        for (CppClass c : getNested(Cpp.PUBLIC)) {
            c.addParents(this.parents, this);
            c.prepare();
        }

        for (CppClass c : getNested(Cpp.PRIVATE)) {
            c.addParents(this.parents, this);
            c.prepare();
        }

        for (CppClass c : getNested(Cpp.PROTECTED)) {
            c.addParents(this.parents, this);
            c.prepare();
        }

        isPrepared = true;
    }

}
