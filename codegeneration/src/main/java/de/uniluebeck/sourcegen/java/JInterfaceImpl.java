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

import java.util.LinkedList;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;


class JInterfaceImpl extends JComplexTypeImpl implements JInterface {

	private static final ResourceBundle res =
		ResourceBundle.getBundle(JInterfaceImpl.class.getCanonicalName());
	
	private LinkedList<JInterfaceMethodImpl> methods = new LinkedList<JInterfaceMethodImpl>();

	private LinkedList<JInterfaceImpl> extendedInterfaces = new LinkedList<JInterfaceImpl>();

	private LinkedList<String> extendedInterfacesStrings = new LinkedList<String>();

	protected int modifiers;

	private String name;
	
	/**
	 * Test: {@link JavaInterfaceTest#testJavaInterfaceJavaPackageIntString()}
	 * 
	 * @param packageObj
	 * @param modifiers
	 * @param name
	 * @throws JInvalidModifierException
	 * @throws JConflictingModifierException
	 */
	public JInterfaceImpl(int modifiers, String name)
			throws JInvalidModifierException, JConflictingModifierException {

		this.modifiers = modifiers;
		this.name = name;
		
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

	private LinkedList<JClassImpl> classes = new LinkedList<JClassImpl>();

	private LinkedList<JInterfaceImpl> interfaces = new LinkedList<JInterfaceImpl>();

	private LinkedList<JEnumImpl> enums = new LinkedList<JEnumImpl>();
	
	@Override
	JComplexType getParent() {
		return parentClass == null ? (parentInterface == null ? null : parentInterface) : parentClass;
	}
	
	public JInterface add(JInterfaceMethod... methods) throws JDuplicateException {
		for (JInterfaceMethod m : methods)
			addInternal(m);
		return this;
	}
	
	public JInterface addExtends(JInterface... ifaces) throws JDuplicateException {
		for (JInterface i : ifaces)
			addExtendsInternal(i);
		return this;
	}
	
	public JInterface addExtends(String... ifaces) throws JDuplicateException {
		for (String i : ifaces)
			addExtendsInternal(i);
		return this;
	}

	private void addExtendsInternal(JInterface iface) throws JDuplicateException {
		if (containsExtends(iface))
			throw new JDuplicateException("Duplicate extended interface: " + ((JInterfaceImpl)iface).getName());
		extendedInterfaces.add((JInterfaceImpl)iface);
	}

	private void addExtendsInternal(String iface) throws JDuplicateException {
		if (containsExtends(iface))
			throw new JDuplicateException("Duplicate extended interface: " + iface);
		extendedInterfacesStrings.add(iface);
	}
	
	private void addInternal(JInterfaceMethod method) throws JDuplicateException {
		if(contains(method))
			throw new JDuplicateException(method);
		methods.add((JInterfaceMethodImpl)method);
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		if (toStringModifiers(buffer, tabCount, modifiers))
			buffer.append(" ");
		buffer.append("interface ");
		buffer.append(name);
		if (extendedInterfaces.size() > 0 || extendedInterfacesStrings.size() > 0) {
			buffer.append(" extends ");
			for (JInterfaceImpl iface : extendedInterfaces) {
				buffer.append(iface.getName());
				if (!iface.equals(extendedInterfaces.getLast()) || extendedInterfacesStrings.size () > 0)
					buffer.append(", ");
			}
			for (String iface : extendedInterfacesStrings) {
				buffer.append(iface);
				if (!iface.equals(extendedInterfacesStrings.getLast()))
					buffer.append(", ");
			}
		}
		buffer.append(" {\n");
		for (JInterfaceMethodImpl meth : methods) {
			meth.toString(buffer, tabCount+1);
			buffer.append("\n");
		}
		indent(buffer, tabCount);
		buffer.append("}");
	}
	
	public boolean contains(JInterfaceMethod method) {
		
		for (JInterfaceMethod meth : methods)
			if (meth.equals(method))
				return true;
		
		return false;
		
	}

	public boolean containsExtends(JInterface iface) {
		
		for (JInterface i : extendedInterfaces)
			if (i.equals(iface))
				return true;
		
		for (String i : extendedInterfacesStrings)
			if (i.equals(((JInterfaceImpl)iface).getFullyQualifiedName()))
				return true;
		
		return false;
		
	}

	public boolean containsExtends(String iface) {
		
		for (String i : extendedInterfacesStrings)
			if (i.equals(iface))
				return true;
		
		for (JInterface i : extendedInterfaces)
			if (((JInterfaceImpl)i).getFullyQualifiedName().equals(iface))
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
		// 		public, (protected, private,)
		// 		abstract, (static,) strictfp
		// unallowed:
		// 		final, interface, native,
		// 		synchronized, transient, volatile
		//
		// protected and private are only allowed
		// in nested interfaces, same goes for static
		
		boolean invalid = 
			JModifier.isFinal(modifiers) ||
			JModifier.isInterface(modifiers) ||
			JModifier.isNative(modifiers) ||
			JModifier.isSynchronized(modifiers) ||
			JModifier.isTransient(modifiers) ||
			JModifier.isVolatile(modifiers) ||
			JModifier.isProtected(modifiers) ||
			JModifier.isPrivate(modifiers) ||
			JModifier.isStatic(modifiers);
		
		if(invalid)
			throw new JInvalidModifierException(
					res.getString("exception.modifier.invalid") //$NON-NLS-1$
			);
		
		if(JModifier.isConflict(modifiers))
			throw new JConflictingModifierException(
					res.getString("exception.modifier.conflict") //$NON-NLS-1$
			);
		
	}
	
	public JInterface add(JClass... classes) throws JDuplicateException {
		for (JClass c : classes) {
			if(contains(c) || ((JClassImpl)c).getParent() != null)
				throw new JDuplicateException(c);
			((JClassImpl)c).setParent(this);
			this.classes .add((JClassImpl)c);
		}
		return this;
	}
	
	public JInterface add(JInterface... ifaces) throws JDuplicateException {
		for (JInterface iface : ifaces) {
			if(contains(iface) || ((JInterfaceImpl)iface).getParent() != null)
				throw new JDuplicateException(iface);
			((JInterfaceImpl)iface).setParent(this);
			interfaces .add((JInterfaceImpl)iface);
		}
		return this;
	}
	
	public JInterface add(JEnum... jEnum) throws JDuplicateException {
		for(JEnum e : jEnum) {
			if(contains(e) || ((JEnumImpl)e).getParent() != null)
				throw new JDuplicateException(e);
			((JEnumImpl)e).setParent(this);
			enums.add((JEnumImpl)e);
		}
		return this;
	}
	
	public boolean contains(JInterface iface) {
		for (JInterface jni : interfaces)
			if (jni.equals(iface))
				return true;
		return false;
	}

	public boolean contains(JClass clazz) {
		for (JClass jnc : classes)
			if (jnc.equals(clazz))
				return true;
		return false;
	}
	
	public boolean contains(JEnum jEnum) {
		for(JEnum e : enums)
			if(e.equals(jEnum))
				return true;
		return false;
	}

	public JInterface getExtendedJInterfaceByName(String name) {
		for (JInterface i : extendedInterfaces)
			if (i.equals(name))
				return i;
		return null;
	}

	public JClass getJClassByName(String name) {
		for (JClass i : classes)
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

	public JInterface getJInterfaceByName(String name) {
		for (JInterface i : interfaces)
			if (i.equals(name))
				return i;
		return null;
	}

}
