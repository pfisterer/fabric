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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;



class JFieldImpl extends JElemImpl implements JField {

	private static final ResourceBundle res =
		ResourceBundle.getBundle(JFieldImpl.class.getCanonicalName());

	private String initCode;

	private int modifiers;

	private String name;

	private String type;

	private JFieldComment comment = null;

	/**
	 * This field's list of Java annotations.
	 */
	private List<JFieldAnnotation> annotations = new ArrayList<JFieldAnnotation>( );

	public JFieldImpl(int modifiers, String type, String name)
			throws JConflictingModifierException,
			JInvalidModifierException {

		this.modifiers = modifiers;
		this.type = type;
		this.name = name;

		validateModifiers();

	}

	public JFieldImpl(int modifiers, String type, String name, String initCode)
			throws JConflictingModifierException,
			JInvalidModifierException {

		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
		this.initCode = initCode;

		validateModifiers();

	}

	public JFieldImpl(String type, String name) {
		this.name = name;
		this.type = type;
	}

	public JFieldImpl(String type, String name, String initCode) {
		this.name = name;
		this.type = type;
		this.initCode = initCode;
	}

	/**
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(java.lang.StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

    // write annotations if there are any
    for (JFieldAnnotation ann: this.annotations) {
      ann.toString(buffer, tabCount);
    }

		if (toStringModifiers(buffer, tabCount, modifiers))
			buffer.append(" ");
		else
			indent(buffer, tabCount);
		buffer.append(type);
		buffer.append(" ");
		buffer.append(name);
		if (initCode != null) {
			buffer.append(" = ");
			buffer.append(initCode);
		}
		buffer.append(";");
	}

	public boolean equals(JField other) {
		return this.name.equals(((JFieldImpl)other).name);
	}

	public String getName() {
		return name;
	}

	public JField setComment(JFieldComment comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * @see de.uniluebeck.sourcegen.java.JField#addAnnotation(de.uniluebeck.sourcegen.java.JFieldAnnotation[])
	 */
	public JField addAnnotation(JFieldAnnotation... annotations) {
	    for (JFieldAnnotation ann : annotations) {
	        this.annotations.add(ann);
	    }
	    return this;
	}

	protected void validateModifiers() throws JInvalidModifierException, JConflictingModifierException {

		// allowed:
		// 		public, protected, private,
		// 		static, final, transient,
		//		volatile
		// unallowed:
		// 		abstract, interface, native,
		// 		strict, synchronized

		boolean invalid =
			JModifier.isAbstract(modifiers) ||
			JModifier.isInterface(modifiers) ||
			JModifier.isNative(modifiers) ||
			JModifier.isStrict(modifiers) ||
			JModifier.isSynchronized(modifiers);

		boolean conflictFinalVolatile =
			JModifier.isFinal(modifiers) &&
			JModifier.isVolatile(modifiers);

		if(invalid)
			throw new JInvalidModifierException(
					res.getString("exception.modifier.invalid") + //$NON-NLS-1$
					JModifier.toString(modifiers)
			);

		if(conflictFinalVolatile)
			throw new JConflictingModifierException(
					res.getString("exception.modifier.final_volatile") //$NON-NLS-1$
			);

		if(JModifier.isConflict(modifiers))
			throw new JConflictingModifierException(
					res.getString("exception.modifier.conflict") //$NON-NLS-1$
			);

	}

}
