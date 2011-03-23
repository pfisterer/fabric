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

import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;


class CppDestructorImpl extends CElemImpl implements CppDestructor {

	private CppSignature signature;
	private StringBuffer body = new StringBuffer();
	private CppClass clazz;

	public CppDestructorImpl(CppClass clazz, CppVar... vars ) throws CppDuplicateException {
		this.signature = new CppSignature(clazz.getTypeName(), vars);
		this.clazz = clazz;
	}
	
	public CppDestructorImpl(CppVar... vars) throws CppDuplicateException {
		this.signature = new CppSignature(vars);
	}
	
	public CppDestructor add(CppVar... vars) throws CppDuplicateException {
		this.signature.add(vars);
		return this;
	}

	public CppDestructor appendCode(String str) {
		this.body.append(str);
		return this;
	}
	
	public String getSignature() {
		return "~" + signature.toString();
	}

	public String getBody() {
		return body.toString();
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		buffer.append(this.clazz.getTypeName() + "::~" + this.signature.toString());
		buffer.append(" {\n");
		appendBody(buffer, body, tabCount+1);
		buffer.append("\n");
		indent(buffer, tabCount);
		buffer.append("}\n\n");
	}

}
