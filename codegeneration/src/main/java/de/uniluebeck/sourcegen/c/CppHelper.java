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

public class CppHelper {

    public static void toStringHelper(StringBuffer buffer, CppClass clazz, int tabCount, boolean isLast) {

        // Public constructors
        if (null != clazz.getConstructors(Cpp.PUBLIC) && clazz.getConstructors(Cpp.PUBLIC).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PUBLIC)) {
                buffer.append(Cpp.newline + Cpp.newline);
                c.toString(buffer, tabCount);
            }
        }

        // Private constructors
        if (null != clazz.getConstructors(Cpp.PRIVATE) && clazz.getConstructors(Cpp.PRIVATE).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PRIVATE)) {
                buffer.append(Cpp.newline + Cpp.newline);
                c.toString(buffer, tabCount);
            }
        }

        // Public destructors
        if (null != clazz.getDestructors(Cpp.PUBLIC) && clazz.getDestructors(Cpp.PUBLIC).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PUBLIC)) {
                d.toString(buffer, tabCount);
            }
        }

        // Private destructors
        if (null != clazz.getDestructors(Cpp.PRIVATE) && clazz.getDestructors(Cpp.PRIVATE).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PRIVATE)) {
                d.toString(buffer, tabCount);
            }
        }

        // Public functions
        if (null != clazz.getFuns(Cpp.PUBLIC) && clazz.getFuns(Cpp.PUBLIC).size() > 0) {
            // Does not add new lines after last function
            for (int i = 0; i < clazz.getFuns(Cpp.PUBLIC).size(); ++i) {
                CppFun f = clazz.getFuns(Cpp.PUBLIC).get(i);

                if (i < clazz.getFuns(Cpp.PUBLIC).size() - 1) {
                    f.toString(buffer, tabCount, false);
                } else {
                    f.toString(buffer, tabCount, true);
                }
            }
            if (!isLast) {
                buffer.append(Cpp.newline + Cpp.newline);
            }
        }

        // Private functions
        if (null != clazz.getFuns(Cpp.PRIVATE) && clazz.getFuns(Cpp.PRIVATE).size() > 0) {
            // Does not add new lines after last function
            for (int i = 0; i < clazz.getFuns(Cpp.PRIVATE).size(); ++i) {
                CppFun f = clazz.getFuns(Cpp.PRIVATE).get(i);

                if (i < clazz.getFuns(Cpp.PRIVATE).size() - 1) {
                    f.toString(buffer, tabCount, false);
                } else {
                    f.toString(buffer, tabCount, true);
                }
            }
            if (!isLast) {
                buffer.append(Cpp.newline + Cpp.newline);
            }
        }

        // Public nested classes
        if (null != clazz.getNested(Cpp.PUBLIC) && clazz.getNested(Cpp.PUBLIC).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PUBLIC).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PUBLIC).size() - 1) &&
                        (clazz.getNested(Cpp.PROTECTED).size() <= 0) &&
                        (clazz.getNested(Cpp.PRIVATE).size() <= 0);
                toStringHelper(buffer, clazz.getNested(Cpp.PUBLIC).get(i), tabCount, last);
            }
        }

        // Protected nested classes
        if (null != clazz.getNested(Cpp.PROTECTED) && clazz.getNested(Cpp.PROTECTED).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PROTECTED).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PROTECTED).size() - 1) &&
                        (clazz.getNested(Cpp.PRIVATE).size() <= 0);
                toStringHelper(buffer, clazz.getNested(Cpp.PROTECTED).get(i), tabCount, last);
            }
        }

        // Private nested classes
        if (null != clazz.getNested(Cpp.PRIVATE) && clazz.getNested(Cpp.PRIVATE).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PRIVATE).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PRIVATE).size() - 1);
                toStringHelper(buffer, clazz.getNested(Cpp.PRIVATE).get(i), tabCount, last);
            }
        }

    }

}
