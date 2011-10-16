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

import java.util.List;

import de.uniluebeck.sourcegen.exceptions.JCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;



public interface JClass extends JComplexType {

	class JavaClassFactory {

		private static JavaClassFactory instance = null;

		static JavaClassFactory getInstance() {
			if (instance == null)
				instance = new JavaClassFactory();
			return instance;
		}

		private JavaClassFactory() { /* not to be invoked */ }

		public JClass create(int modifiers, String className) throws JConflictingModifierException, JInvalidModifierException {
			return new JClassImpl(modifiers, className);
		}

		public JClass create(String className) {
			return new JClassImpl(className);
		}

	}

	public static final JavaClassFactory factory = JavaClassFactory.getInstance();

	public JClass 	add				(JConstructor... constructor) 	throws JDuplicateException, JCodeValidationException;
	public JClass 	add				(JEnum... jEnum) 				throws JDuplicateException;
	public JClass 	add				(JField... field) 				throws JDuplicateException;
	public JClass 	add				(JInterface... iface) 			throws JDuplicateException;
	public JClass 	add				(JMethod... method) 				throws JDuplicateException, JCodeValidationException;
	public JClass 	add				(JClass... classes) 			throws JDuplicateException;
	public JClass	addImplements	(JInterface... iface) 			throws JDuplicateException;

	public boolean 	contains		(JConstructor constructor);
	public boolean 	contains		(JEnum jEnum);
	public boolean 	contains		(JField field);
	public boolean 	contains		(JInterface iface);
	public boolean 	contains		(JMethod method);
	public boolean 	contains		(JClass nestedClass);
	public boolean 	containsImplements(JInterface iface);

	public List<JMethod> 		getMethods						();

	public JConstructor   getJConstructorByName           (String name);
	public JEnum          getJEnumByName                	(String name);
	public JField         getJFieldByName       					(String name);
	public JInterface     getJInterfaceByName           	(String name);
	public JClass         getJClassByName             		(String name);
	public JInterface     getImplementedJInterfaceByName  (String name);
  public List<JMethod>  getJMethodsByName               (String name);

	public JClass 	setExtends		(JClass extendedClass);
	public JClass 	setExtends		(String extendedClass);

	public String 	toString		();
	public JClass 	appendStaticCode(String... code);

	/**
	 * Set the Javadoc comment for the current class.
	 *
	 * @param comment The Java class comment.
	 * @return This object.
	 */
	public JClass	setComment(JClassComment comment);

  /**
	 * Adds an annotation to this class.
	 *
	 * @param annotations The Java class annotation.
	 * @return This object.
	 */
	public JClass addAnnotation       (JClassAnnotation... annotations);
}
