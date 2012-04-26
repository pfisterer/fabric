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


public class CppTypeGenerator {

//    private Long qualifier = null;
    private Long qualifiedType = null;
    private String typeName = null;
    private CppClass clazz = null;

    public CppTypeGenerator(String name) {
        this.typeName = name;
    }

    public CppTypeGenerator(CppClass clazz) {
        this.clazz = clazz;
    }

    public CppTypeGenerator(Long qualifier) {
    	this.qualifiedType = qualifier;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        if (this.qualifiedType != null) {
            buffer.append(Cpp.toString(this.qualifiedType));
        } else if (this.typeName != null) {
            buffer.append(this.typeName);
        } else if (this.clazz != null) {
            buffer.append(this.getParents() + this.clazz.getName());
        }

        if (buffer.length() == 0) {
            buffer = null;
            return "$UNKNOWN_TYPE_BY_CPP_TYPE_GENERATOR$";
        }

        return buffer.toString();
    }

    private String getParents() {
        StringBuffer myParents = new StringBuffer();
        if (this.clazz != null) {
            for (String s : this.clazz.getParents()) {
                myParents.append(s + "::");
            }
        }
        return myParents.toString();
    }

}
