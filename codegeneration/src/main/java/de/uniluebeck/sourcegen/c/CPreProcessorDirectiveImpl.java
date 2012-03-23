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

import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;


/**
 * A class representing C preprocessor directives.
 * 
 * @author Daniel Bimschas
 */
class CPreProcessorDirectiveImpl extends CElemImpl implements CPreProcessorDirective {

	/**
	 * the preprocessor directive
	 */
	private String directive;
	private boolean hash;

	/**
	 * Constructs a new <code>CPreProcessorDirectiveImpl</code> instance.
	 * 
	 * @param directive
	 *            the directive code
	 * @throws CPreProcessorValidationException
	 *             if syntax validation of <code>directive</code> fails
	 */
	public CPreProcessorDirectiveImpl(String directive)
			throws CPreProcessorValidationException {
		this(true, directive);
	}
	
	public CPreProcessorDirectiveImpl(boolean hash, String directive) throws CPreProcessorValidationException {
		this.directive = directive;
		this.hash = hash;
		// TODO: implement validation
	}
	
	public boolean equals(CPreProcessorDirective other) {
		return directive.equals(((CPreProcessorDirectiveImpl)other).directive);
	}

	/**
	 * Returns the directive.
	 * 
	 * @return the directive
	 */
	public String getDirective() {
		return directive;
	}

	public boolean isHash() {
		return hash;
	}

	public void setHash(boolean hash) {
		this.hash = hash;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append(hash ? "#" : "");
		buffer.append(directive);
	}

}
