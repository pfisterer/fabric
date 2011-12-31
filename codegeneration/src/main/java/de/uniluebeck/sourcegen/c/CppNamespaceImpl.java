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

import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * @author Dennis Boldt
 */
class CppNamespaceImpl extends CElemImpl implements CppNamespace {

    private String name;
    private CComment comment = null;

    //private List<CppClass> clazz = new LinkedList<CppClass>();
    //private List<CppNamespace> namespaces = new LinkedList<CppNamespace>();
    private List<CFun> cfuns = new LinkedList<CFun>();

    boolean isPrepared = false;

    // Needed for nested namespaces
    // private List<CppNamespace> parents = new LinkedList<CppNamespace>();

    public CppNamespaceImpl(String name) {
        this.name = name;
    }

    public CppNamespace add(long vis, CFun... functions) throws CppDuplicateException {

        for (CFun f : functions) {
            cfuns.add(f);
        }
        return this;
    }

/*
    @Override
    public CppNamespace add(CppClass... cppClass) throws CppDuplicateException {

        for (CppClass c : cppClass) {
            this.clazz.add(c);
        }
        return this;
    }
*/
    public boolean contains(CFun fun) {

        for(CFun c : this.cfuns) {
            if(c.getName().equals(fun.getName())) {
                return true;
            }
        }
        return false;
    }
/*
    public boolean contains(CppClass clazz) {

        for(CppClass c : this.clazz) {
            if(c.getName().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }
*/
    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        //prepare();

        // write comment if necessary
        if (comment != null) {
            comment.toString(buffer, tabCount);
        }

        buffer.append("namespace " + this.name);
        buffer.append(Cpp.newline + "{" + Cpp.newline);

        StringBuffer inner = new StringBuffer();

        // Add the signatures of the C functions
        for (CFun fun : cfuns) {
            // Signature does not work!
            inner.append(fun.toString());
        }

        appendBody(buffer, inner, tabCount + 1);
        buffer.append(Cpp.newline + "};");
        buffer.append(Cpp.newline);
    }

    protected void indent(StringBuffer buffer, int tabCount) {
        for (int i = 0; i < tabCount; i++)
            buffer.append("\t");
    }

/*
    @Override
    public CppNamespace addParents(List<CppNamespace> cppClass, CppNamespace clazz) {
        this.parents.addAll(cppClass);
        this.parents.add(clazz);
        return this;
    }

    public List<CppNamespace> getParents() {
        return parents;
    }
*/
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CppNamespace setComment(CComment comment) {
        this.comment = comment;
        return this;
    }

    /**
     * This method prepares the files
     */
/*   @Override
    public void prepare() {
        if (isPrepared)
            return;

        for (CppNamespace c : getNested(Cpp.PUBLIC)) {
            c.addParents(this.parents, this);
            c.prepare();
        }

        isPrepared = true;
    }
*/
}
