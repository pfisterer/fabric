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
package de.uniluebeck.sourcegen.java;

import java.util.LinkedList;
import java.util.List;

import de.uniluebeck.sourcegen.exceptions.JDuplicateException;



class JMethodSignatureImpl extends JElemImpl implements JMethodSignature {
	
	private LinkedList<JParameterImpl> parameters = new LinkedList<JParameterImpl>();
	
	JMethodSignatureImpl() {
		// nothing to do
	}
	
	public JMethodSignatureImpl(JParameter... params) throws JDuplicateException {
		if(params != null)
			add(params);
	}
	
	private void addInternal(JParameter param) throws JDuplicateException {
		if(contains(param))
			throw new JDuplicateException(param);
		parameters.add((JParameterImpl)param);
	}
	
	public JMethodSignature add(JParameter... params) throws JDuplicateException {
		for(JParameter p : params)
			addInternal(p);
		return this;
	}
	
	public boolean contains(JParameter param) {
		
		for(JParameter o: parameters)
			if(o.nameEquals(param))
				return true;
		return false;
		
	}

	public boolean equals(JMethodSignature other) {
		
		// test length of signature
		if(parameters.size() != ((JMethodSignatureImpl)other).parameters.size())
			return false;
		
		// test for same order of parameter types
		for(int i=0; i<parameters.size(); i++)
			if(!parameters.get(i).typeEquals(((JMethodSignatureImpl)other).parameters.get(i)))
				return false;
		
		return true;
		
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append("(");
		for (JParameterImpl param : parameters) {
			param.toString(buffer, 0);
			if (param != parameters.getLast())
				buffer.append(", ");
		}
		buffer.append(")");
	}

	public List<JParameter> getParameters()
	{
		return new LinkedList<JParameter>(this.parameters);
	}
	
}
