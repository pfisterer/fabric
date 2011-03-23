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

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JDuplicateException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;

public interface JInterface extends JComplexType {

	class JavaInterfaceFactory {
		
		private static JavaInterfaceFactory instance;
		
		static JavaInterfaceFactory getInstance() {
			if (instance == null)
				instance = new JavaInterfaceFactory();
			return instance;
		}
		
		private JavaInterfaceFactory() { /* not to be invoked */ }
		
		public JInterface create(int modifiers, String name)
				throws JInvalidModifierException, JConflictingModifierException {
			
			return new JInterfaceImpl(modifiers, name);
		}
		
	}
	
	public static final JavaInterfaceFactory factory = JavaInterfaceFactory.getInstance();
	
	public JInterface 	addExtends		(String... iface) 		throws JDuplicateException;
	public JInterface 	addExtends		(JInterface... iface) 	throws JDuplicateException;
	public JInterface 	add				(JInterfaceMethod... method) throws JDuplicateException;
	public JInterface	add				(JClass... classes)		throws JDuplicateException;
	public JInterface	add				(JInterface... ifaces) 	throws JDuplicateException;
	public JInterface	add				(JEnum... enums)		throws JDuplicateException;
	
	public boolean 		containsExtends	(String iface)			;
	public boolean		containsExtends	(JInterface iface)		;
	public boolean 		contains		(JInterfaceMethod method);
	public boolean		contains		(JInterface iface)		;
	public boolean 		contains		(JClass clazz)			;
	public boolean		contains		(JEnum jEnum)			;
	
	public JInterface 		getExtendedJInterfaceByName	(String name);
	public JClass 			getJClassByName				(String name);
	public JInterface 		getJInterfaceByName			(String name);
	public JEnum			getJEnumByName				(String name);

	public String 		toString		();

}