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

package de.uniluebeck.sourcegen.java;

import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

public interface JParameter extends JLangElem {

	class JavaParameterFactory {

		private static JavaParameterFactory instance;

		private JavaParameterFactory() { /* not to be invoked */ }

		static JavaParameterFactory getInstance() {
			if (instance == null)
				instance = new JavaParameterFactory();
			return instance;
		}

		public JParameter create(String type, String name) throws JInvalidModifierException {
			return new JParameterImpl(JModifier.NONE, type, name);
		}

		public JParameter create(JComplexType type, String name) throws JInvalidModifierException {
			return new JParameterImpl(JModifier.NONE, type.getName(), name);
		}
		
		public JParameter create(int modifiers, String type, String name) throws JInvalidModifierException {
			return new JParameterImpl(modifiers, type, name);
		}
		
		public JParameter create(int modifiers, JComplexType type, String name) throws JInvalidModifierException {
			return new JParameterImpl(modifiers, type.getName(), name);
		}

	}

	public static final JavaParameterFactory factory = JavaParameterFactory.getInstance();
	
	/**
	 * Please use typeEquals() or nameEquals() instead.
	 * 
	 * @param other
	 * @return
	 * @deprecated use typeEquals() or nameEquals() instead
	 */
	@Deprecated
	public boolean equals(JParameter other);
	public boolean nameEquals(JParameter other);
	public boolean typeEquals(JParameter other);

}