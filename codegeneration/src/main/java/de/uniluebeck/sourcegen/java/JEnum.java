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
package de.uniluebeck.sourcegen.java;

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

public interface JEnum extends JComplexType {

	class JavaEnumFactory {

		private static JavaEnumFactory instance;

		static JavaEnumFactory getInstance() {
			if (instance == null)
				instance = new JavaEnumFactory();
			return instance;
		}

		private JavaEnumFactory() { /* not to be invoked */ }

		public JEnum create(int modifiers, String name, String... constants)
				throws JDuplicateException, JInvalidModifierException,
				JConflictingModifierException {

			return new JEnumImpl(modifiers, name, constants);
		}

	}

	public static final JavaEnumFactory factory = JavaEnumFactory.getInstance();

	public JEnum 	add				(JConstructor... create)		throws JDuplicateException;
	public JEnum	add				(JMethod... method)				throws JDuplicateException;
	public JEnum	add				(JField... field)				throws JDuplicateException;
	public JEnum 	addConstant		(String... constant) 			throws JDuplicateException;

	public boolean 	contains		(JConstructor create)			;
	public boolean 	contains		(JMethod method)				;
	public boolean 	contains		(JField field)					;
	public boolean 	containsConstant(String constant)				;

	public JConstructor getJConstructorByName	(String name);
	public JField 		getJFieldByName			(String name);

	/**
	 * Set the Javadoc comment for the current enum.
	 *
	 * @param comment The Java enum comment.
	 * @return This object.
	 */
	public JEnum setComment(JEnumComment comment);

  /**
	 * Set the annotation for the current enum.
	 *
	 * @param annotation The Java enum annotation.
	 * @return This object.
	 */
	public JEnum setAnnotation(JEnumAnnotation annotation);
}
