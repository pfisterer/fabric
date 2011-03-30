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

import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;



/**
 * A class representing the body of a C function.
 * 
 * @author Daniel Bimschas
 */
public class CFunBody extends CElemImpl {

	/**
	 * the function body
	 */
	private StringBuffer code;

	/**
	 * the funs declarations block
	 */
	private StringBuffer declarations;

	/**
	 * Constructs a new <code>CFunBody</code> instance.
	 * 
	 * @param code
	 *            the body
	 */
	public CFunBody(String declarations, String code)
			throws CCodeValidationException {

		this.declarations = new StringBuffer(declarations != null ? declarations : "");
		this.code = new StringBuffer(code);
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	/**
	 * Returns the function body code.
	 * 
	 * @return the function body code
	 */
	public String getCode() {
		return code.toString();
	}

	/**
	 * Returns the declarations block.
	 * 
	 * @return the declarations block
	 */
	public String getDeclarations() {
		return declarations.toString();
	}

	/**
	 * Check for equality of the two <code>CFunBody</code> objects.
	 * 
	 * @param other
	 *            the <code>CFunBody</code> to compare with
	 * @return <code>true</code> if declarations and code are String-equal
	 */
	public boolean equals(CFunBody other) {
		return code.equals(other.code)
				&& declarations.equals(other.declarations);
	}
	
	public void append(String code) {
		this.code.append(code);
	}
	
	public void appendDeclarations(String declStr) {
		this.declarations.append(declStr);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		int begin = 0, end = 0;
		if (declarations.length() > 0) {
			int declLength = declarations.length();
			
			while(begin < declLength) {
				end = declarations.indexOf("\n", begin);
				end = (end == -1) ? declLength : end;
				indent(buffer, tabCount);
				buffer.append(declarations, begin, end);
				buffer.append(end != declLength ? "\n" : "");
				begin = end+1;
			}
			
			if (code.length() > 0)
				buffer.append("\n\n");
		}
		if (code.length() > 0) {
			begin = end = 0;
			int codeLength = code.length();
			
			while(begin < codeLength) {
				end = code.indexOf("\n", begin);
				end = (end == -1) ? codeLength : end;
				indent(buffer, tabCount);
				buffer.append(code, begin, end);
				buffer.append(end < codeLength-1 ? "\n" : "");
				begin = end+1;
			}
		}
	}

}
