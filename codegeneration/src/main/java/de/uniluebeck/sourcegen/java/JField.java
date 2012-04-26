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

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

public interface JField extends JLangElem {

	class JavaFieldFactory {

		private static JavaFieldFactory instance;

		private JavaFieldFactory() { /* not to be invoked */ }

		static JavaFieldFactory getInstance() {
			if (instance == null)
				instance = new JavaFieldFactory();
			return instance;
		}

		public JField create(String type, String name) {

			return new JFieldImpl(type, name);
		}

		public JField create(int modifiers, String type, String name)
				throws JConflictingModifierException, JInvalidModifierException {

			return new JFieldImpl(modifiers, type, name);
		}

		public JField create(int modifiers, String type, String name, String initCode)
				throws JConflictingModifierException, JInvalidModifierException {

			return new JFieldImpl(modifiers, type, name, initCode);
		}

		public JField create(String type, String name, String initCode) {

			return new JFieldImpl(type, name, initCode);
		}

	}

	public static final JavaFieldFactory factory = JavaFieldFactory.getInstance();

	public boolean equals(JField other);

	/**
	 * Set the Javadoc comment for the current field.
	 *
	 * @param comment The Java field comment.
	 * @return This object.
	 */
	public JField setComment(JFieldComment comment);

  /**
	 * Adds an annotation to this field.
	 *
	 * @param annotations The annotation's name.
	 * @return This object.
	 */
	public JField addAnnotation       (JFieldAnnotation... annotations);
}
