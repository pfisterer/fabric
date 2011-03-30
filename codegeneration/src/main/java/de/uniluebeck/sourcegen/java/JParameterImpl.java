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
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

class JParameterImpl extends JElemImpl implements JParameter {

	private static final ResourceBundle res = ResourceBundle.getBundle(JParameterImpl.class.getCanonicalName());

	private final org.slf4j.Logger log = LoggerFactory.getLogger(JParameterImpl.class);

	private int modifiers;

	private String name;

	private String type;

	/**
	 * Test: {@link JavaParameterTest#testJavaParameter()}
	 * 
	 * @param modifiers
	 * @param type
	 * @param name
	 * @throws JInvalidModifierException
	 */
	public JParameterImpl(int modifiers, String type, String name) throws JInvalidModifierException {
		this.type = type;
		this.name = name;
		this.modifiers = modifiers;

		validateModifiers();
	}

	@Deprecated
	public boolean equals(JParameter other) {
		try {
			throw new Exception(res.getString("exception.equals")); //$NON-NLS-1$
		} catch (Exception e) {
			log.error("" + e, e);
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean nameEquals(JParameter other) {
		return name.equals(((JParameterImpl) other).name);
	}

	public boolean typeEquals(JParameter other) {
		return type.equals(((JParameterImpl) other).type);
	}

	private void validateModifiers() throws JInvalidModifierException {

		// allowed:
		// final
		// unallowed:
		// all others

		boolean invalid = !JModifier.isNone(modifiers) && !JModifier.isFinal(modifiers);

		if (invalid)
			throw new JInvalidModifierException(res.getString("exception.modifier.invalid") + //$NON-NLS-1$
					JModifier.toString(modifiers));

	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		if (modifiers != JModifier.NONE) {
			buffer.append(Modifier.toString(modifiers));
			buffer.append(" ");
		}
		buffer.append(type);
		buffer.append(" ");
		buffer.append(name);
	}

}
