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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;



class JInterfaceMethodImpl extends JElemImpl implements JInterfaceMethod {

	private static final ResourceBundle res =
		ResourceBundle.getBundle(JInterfaceMethodImpl.class.getCanonicalName());

	protected int modifiers;

	protected String name;

	protected String returnType;

	protected JMethodSignatureImpl signature;

	private ArrayList<String> exceptions = new ArrayList<String>();

	/**
	 * This method's list of Java annotations (e.g. Override).
	 */
	private List<String> annotations = new ArrayList<String>( );

	/**
	 * This method's Javadoc comment.
	 */
	private JMethodComment comment = null;

    	/**
	 * This method's annotation.
	 */
	private JMethodAnnotation annotation = null;

	public JInterfaceMethodImpl(int modifiers, String returnType, String name,
			JMethodSignature signature, String[] exceptions) throws JDuplicateException,
			JInvalidModifierException {

		this.modifiers = modifiers;
		this.returnType = returnType;
		this.name = name;
		this.signature = signature == null ? new JMethodSignatureImpl() : (JMethodSignatureImpl) signature;

		if (exceptions != null)
			for (String e : exceptions)
				addException(e);

		validateModifiers();

	}

	public JInterfaceMethod add(JParameter... params) throws JDuplicateException {
		for (JParameter param : params)
			this.signature.add(param);
		return this;
	}

	public JInterfaceMethod addException(String... exception) throws JDuplicateException {
		for (String e : exception)
			addExceptionInternal(e);
		return this;
	}

	private void addExceptionInternal(String exception) throws JDuplicateException {
		if (containsException(exception))
			throw new JDuplicateException(res.getString("exception.exceptions.duplicate"));
		this.exceptions.add(exception);
	}

	public boolean containsException(String exception) {

		for (String e : exceptions)
			if (e.equals(exception))
				return true;

		return false;

	}

	/**
	 * @see de.uniluebeck.sourcegen.java.JInterfaceMethod#addAnnotation(java.lang.String[])
	 */
	public JInterfaceMethod addAnnotation(String... annotation) {
	    for (String ann : annotation) {
	        this.annotations.add(ann);
	    }
	    return this;
	}

	/**
	 * @see de.uniluebeck.sourcegen.java.JInterfaceMethod#setComment(de.uniluebeck.sourcegen.java.JMethodComment)
	 */
	public JInterfaceMethod setComment(JMethodComment comment) {
		this.comment = comment;
		return this;
	}

    	/**
	 * @see de.uniluebeck.sourcegen.java.JInterfaceMethod#setAnnotation(JMethodAnnotation)(de.uniluebeck.sourcegen.java.JMethodAnnotation)
	 */
	public JInterfaceMethod setAnnotation(JMethodAnnotation annotation) {
		this.annotation = annotation;
		return this;
	}

	public boolean equals(JInterfaceMethod other) {
		return
			name.equals(((JInterfaceMethodImpl)other).name) &&
			signature.equals(((JInterfaceMethodImpl)other).signature);
	}

	public String getName() {
		return name;
	}

	protected void validateModifiers() throws JInvalidModifierException {

		// allowed:
		// 		public, abstract
		// unallowed:
		// 		final, interface, native,
		// 		private, protected, static,
		// 		strict, synchronized, transient,
		// 		volatile

		boolean invalid = JModifier.isFinal(modifiers)
				|| JModifier.isInterface(modifiers)
				|| JModifier.isNative(modifiers)
				|| JModifier.isPrivate(modifiers)
				|| JModifier.isProtected(modifiers)
				|| JModifier.isStatic(modifiers)
				|| JModifier.isStrict(modifiers)
				|| JModifier.isSynchronized(modifiers)
				|| JModifier.isTransient(modifiers)
				|| JModifier.isVolatile(modifiers);

		if (invalid)
			throw new JInvalidModifierException(res
					.getString("exception.modifier.invalid") + //$NON-NLS-1$
					JModifier.toString(modifiers));

	}

	/**
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(java.lang.StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {

                // write annotation if necessary
		if (annotation != null) {
			annotation.toString(buffer, tabCount);
		}

		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		// write annotations if there are any
        for (String a : this.annotations) {
            indent(buffer, tabCount);
            buffer.append("@").append(a).append("\n");
        }

		indent(buffer, tabCount);
		buffer.append(Modifier.toString(modifiers));
		buffer.append(" ");
		buffer.append(returnType);
		buffer.append(" ");
		buffer.append(name);
		signature.toString(buffer, 0);
		if (exceptions.size() > 0) {
			buffer.append(" throws ");
			for (String exception : exceptions) {
				buffer.append(exception);
				if (!exception.equals(exceptions.get(exceptions.size()-1)))
					buffer.append(", ");
			}
		}
		if (this instanceof JMethodImpl && !Modifier.isAbstract(modifiers)) {
			buffer.append(" {\n");
			((JMethodImpl)this).getBody().toString(buffer, tabCount+1);
			buffer.append("\n");
			indent(buffer, tabCount);
			buffer.append("}");
		}
		else
			buffer.append(";");
	}

	public static void main(String[] args) throws Exception {
		JMethodImpl meth = new JMethodImpl(
				Modifier.PUBLIC | Modifier.STATIC,
				"String",
				"toString",
				JMethodSignature.factory.create(
						JParameter.factory.create(JModifier.NONE, "String", "testString"),
						JParameter.factory.create(Modifier.FINAL, "Exception", "e")
				),
				new String[] { "TestException", "Test2Exception" },
				"hello\n",
				"world"
		);
		System.out.print(meth.toString(1));
	}

	public JMethodSignature getSignature()
	{
		return this.signature;
	}
}
