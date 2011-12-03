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

import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * @author Daniel Bimschas
 */
public interface CppClass extends CppComplexType, CppTemplateName {

    class CppClassFactory {

        private static CppClassFactory instance;

        private CppClassFactory() { /* not to be invoked */
        }

        static CppClassFactory getInstance() {
            if (instance == null)
                instance = new CppClassFactory();
            return instance;
        }

        public CppClass create(String className, CppSourceFile sourceFile) {
            return new CppClassImpl(className, sourceFile);
        }

        public CppClass create(String className) {
            return new CppClassImpl(className, null);
        }

    }

	public CppClass setComment(CComment comment);

    public static final CppClassFactory factory = CppClassFactory.getInstance();

    public CppClass add(long vis, CEnum... enumObj) throws CppDuplicateException;

    public CppClass add(long vis, CFun... function) throws CppDuplicateException;

    public CppClass add(long vis, CppConstructor... constructor) throws CppDuplicateException;

    public CppClass add(long vis, CppDestructor... destructor) throws CppDuplicateException;

    public CppClass add(long vis, CppFun... fun) throws CppDuplicateException;

    public CppClass add(long vis, CppVar... var) throws CppDuplicateException;

    public CppClass add(long vis, CStruct... struct) throws CppDuplicateException;

    public CppClass add(long vis, CUnion... unions) throws CppDuplicateException;

    public CppClass add(long vis, CppClass... cppClass) throws CppDuplicateException;

    public CppClass addParents(List<CppClass> cppClass, CppClass cppClazz);

    public List<CppClass> getParents();

	public void setSourceFile(CppSourceFile sourceFile);

    public CppClass addAfterDirective(CPreProcessorDirective... directive);

    public CppClass addAfterDirective(String... directives) throws CPreProcessorValidationException;

    public CppClass addBeforeDirective(CPreProcessorDirective... directive);

    public CppClass addBeforeDirective(String... directive) throws CPreProcessorValidationException;

    public CppClass addExtended(long vis, CppClass... extended) throws CppDuplicateException;

    public boolean contains(CEnum enumObj);

    public boolean contains(CFun fun);

    public boolean contains(CppConstructor constructor);

    public boolean contains(CppDestructor destructor);

    public boolean contains(CppFun fun);

    public boolean contains(CppVar var);

    public boolean contains(CStruct struct);

    public boolean contains(CUnion enumObj);

    public boolean containsAfterDirective(CPreProcessorDirective directive);

    public boolean containsBeforeDirective(CPreProcessorDirective directive);

    public boolean containsExtended(CppClass extended);

    public CEnum getEnumByName(String name);

    public long getVis(CEnum enumObj);

    public long getVis(CFun fun);

    public long getVis(CppConstructor constructor);

    public long getVis(CppDestructor destructor);

    public long getVis(CppFun fun);

    public long getVis(CppVar var);

    public long getVis(CStruct struct);

    public long getVis(CUnion union);

    public long getVisExtended(CppClass extended);

    // Needed for CppSourceFileImp
    public List<CppConstructor> getConstructors(long vis);
    public List<CppDestructor> getDestructors(long vis);
    public List<CppFun> getFuns(long vis);
    public List<CppClass> getNested(long vis);


}