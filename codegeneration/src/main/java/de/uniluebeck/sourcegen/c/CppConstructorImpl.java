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

class CppConstructorImpl extends CElemImpl implements CppConstructor {

	private CppSignature signature;
	private StringBuffer body = new StringBuffer();
	private CppClass clazz;
	private List<String> extendeds = new LinkedList<String>();
	private List<CppConstructor> extendeds_con = new LinkedList<CppConstructor>();

	private CComment comment = null;

	private List<CppVar> inititializedVars = new LinkedList<CppVar>();

	public CppConstructorImpl(CppVar... cppVars) throws CppDuplicateException {
		this.signature = new CppSignature(cppVars);
	}

	public CppConstructor add(CppVar... var) throws CppDuplicateException {
		this.signature.add(var);
		return this;
	}

	public CppConstructor appendCode(String str) {
		this.body.append(str + Cpp.newline);
		return this;
	}

	public CppConstructor add(String... pExtendeds)
			throws CppDuplicateException {
		for (String str : pExtendeds) {
			for (String s : this.extendeds) {
				if (s.equals(str))
					throw new CppDuplicateException("Duplicate extend: " + s);
			}
			this.extendeds.add(str);
		}
		return this;
	}

	public String getSignature() {

		StringBuffer buffer = new StringBuffer();
		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, 0);
		}

		return buffer.toString() + signature.toString();
	}

	public String getBody() {
		return body.toString();
	}

	public List<String> getExtendeds(){
		List<String> e = new LinkedList<String>();
		e.addAll(this.extendeds);

		for (CppConstructor ex : this.extendeds_con) {
			e.add(ex.getSignature());
		}

		return e;
	}


	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		buffer.append(getParents() + this.clazz.getName() + "::");
		signature.toString(buffer, 0);

		// Add initial values
		for (int i = 0; i < inititializedVars.size(); i++) {
			CppVar v = inititializedVars.get(i);
			if(i == 0) {
				buffer.append(" : ");
			} else {
				buffer.append(" , ");
			}
			buffer.append(v.getInit());
		}

		// add extendeds (special constructors of superclasses)
		// inheritance
		if(this.getExtendeds().size() > 0) {
			for (int i = 0; i < this.getExtendeds().size(); i++) {
				if(i == 0){
					buffer.append(" : ");
				} else {
					buffer.append(", ");
				}
				buffer.append(this.getExtendeds().get(i));
			}
		}

		buffer.append(" {" + Cpp.newline);
		appendBody(buffer, body, tabCount + 1);
		buffer.append(Cpp.newline);
		indent(buffer, tabCount);
		buffer.append("}" + Cpp.newline + Cpp.newline);
	}

	@Override
	public CppConstructor setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * returns OUTER::NESTED1::NESTED2::...::NESTEDN
	 *
	 * @return
	 */
	private String getParents() {
		StringBuffer myParents = new StringBuffer();
		if (this.clazz != null) {
			for (CppClass p : this.clazz.getParents()) {
				myParents.append(p.getName() + "::");
			}
		}
		return myParents.toString();
	}

	@Override
	public CppConstructor setClass(CppClass clazz) {
		this.clazz = clazz;
		this.signature.setName(clazz.getName());
		return this;
	}

	@Override
	public CppConstructor setInititalVars(List<CppVar> init) {
		// Ignore static vars
		for (CppVar cppVar : init) {
			if(!Cpp.isStatic(cppVar.getVisability())) {
				this.inititializedVars.add(cppVar);
			}
		}

		return this;
	}

	@Override
	public CppConstructor add(CppConstructor... cons) throws CppDuplicateException {

		for (CppConstructor con : cons) {
			if(extendeds_con.contains(con)) {
				throw new CppDuplicateException("The constructor " + con.getSignature() + " is already a super constructor.");
			}
			extendeds_con.add(con);
		}
		return this;
	}
}
