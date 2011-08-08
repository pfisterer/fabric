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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;


class JClassImpl extends JComplexTypeImpl implements JClass {

	private static final ResourceBundle res = ResourceBundle.getBundle(JClassImpl.class.getCanonicalName());

	protected String className;

	protected LinkedList<JConstructorImpl> constructors = new LinkedList<JConstructorImpl>();

	protected LinkedList<JEnumImpl> enums = new LinkedList<JEnumImpl>();

	protected LinkedList<JFieldImpl> fields = new LinkedList<JFieldImpl>();

	protected LinkedList<JInterfaceImpl> implementedInterfaces = new LinkedList<JInterfaceImpl>();

	protected LinkedList<String> implementedInterfacesStrings = new LinkedList<String>();

	protected LinkedList<JMethodImpl> methods = new LinkedList<JMethodImpl>();

	protected int modifiers;

	protected LinkedList<JClassImpl> classes = new LinkedList<JClassImpl>();

	protected LinkedList<JInterfaceImpl> interfaces = new LinkedList<JInterfaceImpl>();

	protected JClassImpl extendedClass;

	protected StringBuffer staticCode = new StringBuffer();

	/**
	 * This class's comment.
	 */
	private JClassComment comment = null;

	/**
	 * This class's list of Java annotations.
	 */
	private List<JClassAnnotation> annotations = new ArrayList<JClassAnnotation>( );

	protected JClassImpl(int modifiers, String className) throws JInvalidModifierException, JConflictingModifierException {
		this.className = className;
		this.modifiers = modifiers;
		validateModifiers();
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

	@Override
	JComplexType getParent() {
		return parentClass == null ? (parentInterface == null ? null : parentInterface) : parentClass;
	}

	public JClassImpl(String className) {
		this.className = className;
	}

	public JClass add(JConstructor... constructors) throws JDuplicateException, JCodeValidationException {
		for (JConstructor c : constructors)
			addInternal(c);
		return this;
	}

	public JClass add(JEnum... jEnum) throws JDuplicateException {
		for(JEnum e : jEnum) {
			if(contains(e))
				throw new JDuplicateException(e);
			((JEnumImpl)e).setParent(this);
			enums.add((JEnumImpl)e);
		}
		return this;
	}

	public JClass add(JField... field) throws JDuplicateException {
		for (JField f : field)
			addInternal(f);
		return this;
	}

	public JClass addImplements(JInterface... iface) throws JDuplicateException {
		for (JInterface i : iface)
			addInternalImplements(i);
		return this;
	}

	public JClass add(JMethod... methods) throws JDuplicateException, JCodeValidationException {
		for (JMethod m : methods)
			addInternal(m);
		return this;
	}

	public JClass add(JClass... classes) throws JDuplicateException {
		for (JClass c : classes) {
			if(contains(c) || ((JClassImpl)c).getParent() != null)
				throw new JDuplicateException(c);
			((JClassImpl)c).setParent(this);
			this.classes.add((JClassImpl)c);
		}
		return this;
	}

	public JClass add(JInterface... ifaces) throws JDuplicateException {
		for (JInterface iface : ifaces) {
			if(contains(iface) || ((JInterfaceImpl)iface).getParent() != null)
				throw new JDuplicateException(iface);
			((JInterfaceImpl)iface).setParent(this);
			interfaces.add((JInterfaceImpl)iface);
		}
		return this;
	}

	private void addInternal(JConstructor constructor) throws JCodeValidationException, JDuplicateException {
		if (((JConstructorImpl)constructor).getParent() != this)
			throw new JCodeValidationException(
					res.getString("exception.wrong_constructor_class")); //$NON-NLS-1$

		if(contains(constructor))
			throw new JDuplicateException(constructor);

		constructors.add((JConstructorImpl)constructor);
	}

	private void addInternal(JField field) throws JDuplicateException {
		if(contains(field))
			throw new JDuplicateException(field);
		fields.add((JFieldImpl)field);
	}

	private void addInternal(JMethod method) throws JDuplicateException, JCodeValidationException {

		boolean invalid =
			JModifier.isAbstract(((JMethodImpl)method).modifiers) &&
			!JModifier.isAbstract(modifiers);

		if (invalid)
			throw new JCodeValidationException(res.getString("exception.abstract_methods")); //$NON-NLS-1$

		if(contains(method))
			throw new JDuplicateException(method);

		methods.add((JMethodImpl)method);
		
		// Set backreference to this class
		method.setClazz(this);
	}

	private void addInternalImplements(JInterface iface) throws JDuplicateException {
		if (contains(iface))
			throw new JDuplicateException("Duplicate implemented interface: " + ((JInterfaceImpl)iface).getName());
		implementedInterfaces.add((JInterfaceImpl)iface);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		// write comment if necessary
		if (comment != null) {
			comment.toString(buffer, tabCount);
		}

		// write annotations if there are any
                for (JClassAnnotation ann : this.annotations) {
                    ann.toString(buffer, tabCount);
                }

		if (toStringModifiers(buffer, tabCount, modifiers))
			buffer.append(" ");
		buffer.append("class ");
		buffer.append(className);
		if (extendedClass != null) {
			buffer.append(" extends ");
			buffer.append(extendedClass.getName());
		}
		if (implementedInterfaces.size() > 0 || implementedInterfacesStrings.size() > 0) {
			buffer.append(" implements ");
			for (JInterfaceImpl iface : implementedInterfaces) {
				buffer.append(iface.getName());
				if (iface != implementedInterfaces.getLast() || implementedInterfacesStrings.size() > 0)
					buffer.append(", ");
			}
			for (String iface : implementedInterfacesStrings) {
				buffer.append(iface);
				if (!iface.equals(implementedInterfacesStrings.getLast()))
					buffer.append(", ");
			}
		}
		buffer.append(" {\n\n");

		toString(buffer, tabCount+1, enums, true);
		toString(buffer, tabCount+1, interfaces, true);
		toString(buffer, tabCount+1, classes, true);
		toString(buffer, tabCount+1, fields, true);
		toString(buffer, tabCount+1, constructors, true);
		toString(buffer, tabCount+1, methods, true);

		if (staticCode.length() > 0) {

			indent(buffer, tabCount+1);
			buffer.append("static {\n");

			int begin = 0;
			int end = 0;
			int staticCodeLength = staticCode.length();

			while(begin < staticCodeLength) {
				end = staticCode.indexOf("\n", begin);
				end = (end == -1) ? staticCodeLength : end;
				indent(buffer, tabCount+2);
				buffer.append(staticCode, begin, end);
				buffer.append("\n");
				begin = end+1;
			}

			indent(buffer, tabCount+1);
			buffer.append("}");

		}

		buffer.append("\n");
		indent(buffer, tabCount);
		buffer.append("}");
	}

	public boolean contains(JConstructor constructor) {

		for (JConstructor con : constructors)
			if (con.equals(constructor))
				return true;

		return false;

	}

	public boolean contains(JEnum jEnum) {

		for(JEnum e : enums)
			if(e.equals(jEnum))
				return true;

		return false;

	}

	public boolean contains(JField field) {

		for (JField f : fields)
			if (f.equals(field))
				return true;

		return false;

	}

	public boolean containsImplements(JInterface iface) {

		for (JInterface i : implementedInterfaces)
			if (i.equals(iface))
				return true;

		return false;

	}

	public boolean contains(JMethod method) {

		for (JMethod m : methods)
			if (m.equals(method))
				return true;

		return false;

	}

	public boolean contains(JClass nestedClass) {

		for (JClass jnc : classes)
			if (jnc.equals(nestedClass))
				return true;

		return false;

	}

	public boolean contains(JInterface iface) {

		for (JInterface jni : interfaces)
			if (jni.equals(iface))
				return true;

		return false;

	}

	@Override
	public String getName() {
		return className;
	}

	public JClass setExtends(JClass extendedClass) {
		this.extendedClass = (JClassImpl) extendedClass;
		return this;
	}

	public JClass setExtends(String extendedClass) {
		this.extendedClass = new JClassImpl(extendedClass);
		return this;
	}

	private void validateModifiers() throws JInvalidModifierException, JConflictingModifierException {

		// allowed:
		// 		public, protected, private,
		// 		static, abstract, final,
		// 		strict
		// unallowed:
		// 		interface, native, synchronized,
		// 		transient, volatile

		boolean invalid =
			JModifier.isInterface(modifiers) ||
			JModifier.isNative(modifiers) ||
			JModifier.isSynchronized(modifiers) ||
			JModifier.isTransient(modifiers) ||
			JModifier.isVolatile(modifiers);

		boolean conflictAbstractFinal =
			JModifier.isAbstract(modifiers) &&
			JModifier.isFinal(modifiers);

		if(invalid)
			throw new JInvalidModifierException(res.getString("exception.modifier.invalid") + JModifier.toString(modifiers)); //$NON-NLS-1$

		if(conflictAbstractFinal)
			throw new JConflictingModifierException(res.getString("exception.modifier.abstract_final")); //$NON-NLS-1$

		if(JModifier.isConflict(modifiers))
			throw new JConflictingModifierException(res.getString("exception.modifier.conflict")); //$NON-NLS-1$

	}

	public JClass appendStaticCode(String... code) {
		for (String c : code)
			staticCode.append(c);
		return this;
	}

	public JInterface getImplementedJInterfaceByName(String name) {
		for (JInterface i : implementedInterfaces)
			if (i.getName().equals(name))
				return i;
		return null;
	}

	public JClass getJClassByName(String name) {
		for (JClass c : classes)
			if (c.getName().equals(name))
				return c;
		return null;
	}

	public JConstructor getJConstructorByName(String name) {
		for (JConstructor i : constructors)
			if (i.equals(name))
				return i;
		return null;
	}

	public JEnum getJEnumByName(String name) {
		for (JEnum i : enums)
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

	public JInterface getJInterfaceByName(String name) {
		for (JInterface i : interfaces)
			if (i.equals(name))
				return i;
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.JClass#setComment(de.uniluebeck.sourcegen.JClassComment)
	 */
	public JClass setComment(JClassComment comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * @see de.uniluebeck.sourcegen.java.JClass#addAnnotation(de.uniluebeck.sourcegen.java.JClassAnnotation[])
	 */
	public JClass addAnnotation(JClassAnnotation... annotation) {
	    for (JClassAnnotation ann : annotation) {
	        this.annotations.add(ann);
	    }
	    return this;
	}

	public List<JMethod> getMethods()
	{
		return new LinkedList<JMethod>(this.methods);
	}
}
