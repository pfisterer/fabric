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

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;



/**
 * Simple implementation of the Java 5 Enum-Datatype. Implementation is
 * currently limited to a simple Constant-Listing wrapped inside an enum
 * block.
 *
 * Further implementation of the enum features described in
 *
 *    http://java.sun.com/j2se/1.5.0/docs/guide/language/enums.html
 *
 * may follow.
 *
 *
 * @author Daniel Bimschas
 *
 */
class JEnumImpl extends JComplexTypeImpl implements JEnum {

	private static final ResourceBundle res = ResourceBundle.getBundle(JEnumImpl.class.getCanonicalName());

	public static void main(String args[]) throws Exception {
		JEnum e = JEnum.factory.create(
				Modifier.PUBLIC,
				"TheTestEnum",
				"theFirstConstant",
				"andTheSecondConstant"
		);
		System.out.print(e.toString(1));
	}

	protected LinkedList<String> constants = new LinkedList<String>();

	private int modifiers;

	private String name;

	private LinkedList<JConstructorImpl> constructors = new LinkedList<JConstructorImpl>();

	private LinkedList<JMethodImpl> methods = new LinkedList<JMethodImpl>();

	private LinkedList<JField> fields = new LinkedList<JField>();

	public JEnumImpl(int modifiers, String name, String... constants)
			throws JDuplicateException, JInvalidModifierException,
			JConflictingModifierException {

		this.modifiers = modifiers;
		this.name = name;

		validateModifiers();

		if(constants != null)
			addConstant(constants);

	}

	void setParent(JComplexType parent) throws JDuplicateException {
		if (parentClass != null || parentInterface != null)
			throw new JDuplicateException("the class " + getName() + " already is nested (has a parent)");
		if (parent instanceof JClassImpl)
			parentClass = (JClassImpl) parent;
		else if (parent instanceof JInterfaceImpl)
			parentInterface = (JInterfaceImpl) parent;
	}

	private JClassImpl parentClass = null;

	private JInterfaceImpl parentInterface = null;

	/**
	 * This enum's comment.
	 */
	private JEnumComment comment = null;

	@Override
	JComplexType getParent() {
		return parentClass == null ? (parentInterface == null ? null : parentInterface) : parentClass;
	}

	public JEnum addConstant(String... constants) throws JDuplicateException {
		for(String c : constants)
			addConstantInternal(c);
		return this;
	}

	private void addConstantInternal(String constant) throws JDuplicateException {
		if (containsConstant(constant))
			throw new JDuplicateException(constant);
		constants.add(constant);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {

		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		if (toStringModifiers(buffer, tabCount, modifiers))
			buffer.append(" ");
		buffer.append("enum ");
		buffer.append(name);
		buffer.append(" {\n");
		for (String c : constants) {
			indent(buffer, tabCount+1);
			buffer.append(c);
			if (!c.equals(constants.getLast()))
				buffer.append(",");
			else if (constructors.size() > 0 || methods .size() > 0)
				buffer.append(";");
			buffer.append("\n");
		}

		toString(buffer, tabCount+1, constructors, true);
		toString(buffer, tabCount+1, methods, true);
		toString(buffer, tabCount+1, fields, true);

		indent(buffer, tabCount);
		buffer.append("}");
	}

	public boolean containsConstant(String constant) {

		for(String c : constants)
			if(c.equals(constant))
				return true;

		return false;

	}

	@Override
	public String getName() {
		return name;
	}

	protected void validateModifiers() throws JInvalidModifierException,
			JConflictingModifierException {

		// allowed:
		// 		public, protected, private,
		// 		static, strict
		// unallowed:
		// 		abstract, final, interface
		// 		native, synchronized, transient,
		// 		volatile

		boolean invalid =
			JModifier.isAbstract(modifiers) ||
			JModifier.isFinal(modifiers) ||
			JModifier.isInterface(modifiers) ||
			JModifier.isNative(modifiers) ||
			JModifier.isSynchronized(modifiers) ||
			JModifier.isTransient(modifiers) ||
			JModifier.isVolatile(modifiers);

		if(invalid)
			throw new JInvalidModifierException(
					res.getString("exception.modifier.invalid") + //$NON-NLS-1$
					JModifier.toString(modifiers)
			);

		if(JModifier.isConflict(modifiers))
			throw new JConflictingModifierException(
					res.getString("exception.modifier.conflict") //$NON-NLS-1$
			);


	}

	public JEnum add(JConstructor... constructor) throws JDuplicateException {
		for (JConstructor constr : constructor) {
			if (contains(constr))
				throw new JDuplicateException("Duplicate Constructor " + constr);
			this.constructors.add((JConstructorImpl)constr);
		}
		return this;
	}

	public boolean contains(JConstructor constructor) {
		for (JConstructor c : constructors)
			if (c.equals(constructor))
				return true;
		return false;
	}

	public JEnum add(JMethod... method) throws JDuplicateException {
		for (JMethod meth : method) {
			if (contains(meth))
				throw new JDuplicateException("Duplicate Method " + meth);
			this.methods.add((JMethodImpl)meth);
		}
		return this;
	}

	public boolean contains(JMethod method) {
		for (JMethod m : methods)
			if (m.equals(method))
				return true;
		return false;
	}

	public JEnum add(JField... field) throws JDuplicateException {
		for (JField f : field) {
			if (contains(f))
				throw new JDuplicateException("Duplicate Field " + f);
			this.fields .add(f);
		}
		return this;
	}

	public boolean contains(JField field) {
		for (JField f : fields)
			if (f.equals(field))
				return true;
		return false;
	}

	public JConstructor getJConstructorByName(String name) {
		for (JConstructor i : constructors)
			if (i.equals(name))
				return i;
		return null;
	}

	public JField getJFieldByName(String name) {
		for (JField i : fields)
			if (i.equals(name))
				return i;
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.JEnum#setComment(de.uniluebeck.sourcegen.JEnumComment)
	 */
	public JEnum setComment(JEnumComment comment) {
		this.comment  = comment;
		return this;
	}
}
