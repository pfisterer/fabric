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

import java.util.LinkedList;

import de.uniluebeck.sourcegen.exceptions.CDuplicateException;



/**
 * A class representing the signature of a C function declaration.
 * 
 * @author Daniel Bimschas
 */
class CFunSignatureImpl extends CElemImpl implements CFunSignature {

	/**
	 * the list of parameters
	 */
	private LinkedList<CParamImpl> parameters = new LinkedList<CParamImpl>();

	/**
	 * Constructs a new <code>CFunSignatureImpl</code> instance.
	 * 
	 * @param parameters
	 *            the parameter list
	 * @throws CDuplicateException
	 *             if the parameter list contains duplicates
	 */
	public CFunSignatureImpl(CParam... parameters)
			throws CDuplicateException {

		if (parameters != null)
			for (CParam p : parameters)
				add(p);

	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.c.CFunSignature#add(de.uniluebeck.sourcegen.c.CParamImpl)
	 */
	public CFunSignature add(CParam... params) throws CDuplicateException {
		for (CParam p : params)
			addInternal(p);
		return this;
	}

	private void addInternal(CParam param) throws CDuplicateException {

		for (CParam p : parameters)
			if (p.equals(param))
				throw new CDuplicateException("Duplicate parameter "
						+ ((CParamImpl)param).getName() + " in signature.");

		parameters.add((CParamImpl)param);

	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.c.CFunSignature#containsParameter(de.uniluebeck.sourcegen.c.CParamImpl)
	 */
	public boolean contains(CParamImpl param) {

		for (CParam p : parameters)
			if (p.equals(param))
				return true;

		return false;

	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.c.CFunSignature#equals(de.uniluebeck.sourcegen.c.CFunSignatureImpl)
	 */
	public boolean equals(CFunSignatureImpl other) {

		if (parameters.size() != other.parameters.size())
			return false;

		for (int i = 0; i < parameters.size(); i++)
			if (!parameters.get(i).getType().equals(
					other.parameters.get(i).getType()))
				return false;

		return true;

	}

	/**
	 * Returns the list of parameters.
	 * 
	 * @return the list of parameters
	 */
	public CParam[] getParameters() {
		return parameters.toArray(new CParamImpl[parameters.size()]);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append("(");
		for (CParam p : parameters) {
			p.toString(buffer, 0);
			if (p != parameters.getLast())
				buffer.append(", ");
		}
		buffer.append(")");
	}
	
	public static void main(String[] args) throws Exception {
		CFunSignatureImpl sig = new CFunSignatureImpl(
				
		);
		System.out.print(sig.toString(1));
		System.out.println("-------------");
		sig = new CFunSignatureImpl(
				CParam.factory.create("int", "theInt")
		);
		System.out.print(sig.toString(1));
		System.out.println("-------------");
		sig = new CFunSignatureImpl(
				CParam.factory.create("int", "theInt"),
				CParam.factory.create("long", "theLong")
		);
		System.out.print(sig.toString(1));
		System.out.println("-------------");
	}

}
