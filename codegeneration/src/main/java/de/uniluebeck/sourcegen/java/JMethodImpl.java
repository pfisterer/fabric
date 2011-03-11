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

import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;



class JMethodImpl extends JInterfaceMethodImpl implements JMethod {

	private static final ResourceBundle res = ResourceBundle.getBundle(JMethodImpl.class.getCanonicalName());
	
	private JMethodBodyImpl body;
	
	/** The java class this method is contained in */
	private JClass parentClass = null;

	public JMethodImpl(int modifiers, String returnType, String name,
			JMethodSignature signature, String[] exceptions, String... body)
			throws JCodeValidationException, JDuplicateException,
			JInvalidModifierException {

		super(modifiers, returnType, name, signature, exceptions);

		this.body = (body == null) ?
				new JMethodBodyImpl() : new JMethodBodyImpl(body);
		
		validateModifiers();
		validateMoreModifiers();

	}
	
	public boolean equals(JMethod other) {
		return name.equals(((JMethodImpl)other).name) && signature.equals(((JMethodImpl)other).signature);
	}

	public JMethodBody getBody() {
		return body;
	}

	@Override
	protected void validateModifiers() throws JInvalidModifierException {

		// allowed:
		// 		public protected private
		// 		abstract static final
		// 		synchronized native strictfp
		// unallowed:
		// 		interface, transient, volatile

		boolean invalid =
				JModifier.isInterface(modifiers) ||
				JModifier.isTransient(modifiers) ||
				JModifier.isVolatile(modifiers);

		if (invalid)
			throw new JInvalidModifierException(res
					.getString("exception.modifier.invalid") + //$NON-NLS-1$
					JModifier.toString(modifiers));

	}

	protected void validateMoreModifiers() throws JCodeValidationException {
		
		boolean abstractOrNative =
			JModifier.isAbstract(modifiers) ||
			JModifier.isNative(modifiers);
		
		if (abstractOrNative && !"".equals(body.getSource())) //$NON-NLS-1$
			throw new JCodeValidationException(
					res.getString("exception.abstract_body") //$NON-NLS-1$
			);
		
	}

	public JClass getClazz()
	{
		return this.parentClass;
	}

	public void setClazz(JClass clazz)
	{
		this.parentClass = clazz;
		
	}

}
