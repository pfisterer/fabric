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
import java.util.Iterator;

import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;


class CppSignature extends CElemImpl {
	
	//
	private String name = "";
	private ArrayList<CppVar> vars = new ArrayList<CppVar>();
	
	public CppSignature(CppVar... vars) throws CppDuplicateException {
		add(vars);
	}
	
	/*
	 * Added by O. Kleine: A method signature contains the variables as well as the
	 * methods name. Thus the following new Constructor is necessary. 
	 */ 
	public CppSignature(String pName, CppVar... pVars) throws CppDuplicateException {
		this.name = pName;
		add(pVars);
	}
	
	void add(CppVar... vars) throws CppDuplicateException {
		for (CppVar var : vars) {
			for (CppVar v : this.vars) {
				if (v.equals(var)) {
					throw new CppDuplicateException("Duplicate variable " + var);
				}
			}
			this.vars.add(var);
		}
			
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append(name);
		
		if (vars.size() == 0) {
			//indent(buffer, tabCount);
			buffer.append("()");
			return;
		}
		
		//indent(buffer, tabCount);
		Iterator<CppVar> it = vars.iterator();
		buffer.append("(");
		
		while(it.hasNext()) {
			buffer.append(((CppVarImpl)it.next()).toString());
			if (it.hasNext())
				buffer.append(", ");
			
		}
		buffer.append(")");
	}
	
}
