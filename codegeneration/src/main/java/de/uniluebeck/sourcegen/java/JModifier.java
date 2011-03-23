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

/**
 * Wrapper class around <code>java.lang.reflect.Modifier</code>.
 * 
 * http://java.sun.com/docs/books/jls/second_edition/html/classes.doc.html
 * 
 * @author Daniel Bimschas
 *
 */
public class JModifier extends Modifier {
	
	/**
     * The <code>int</code> value representing no modifier.
     */
	public static final int NONE = 0x00000000;
	
	/**
	 * Tests if <code>modifiers</code> contains two out of public,
	 * protected and private at the same time. 
	 * 
	 * Test: {@link JavaModifierTest#testIsConflict()}
	 * 
	 * @param modifiers the modifiers
	 * @return <code>true</code> if two out of public, protected
	 * and private is contained, <code>false</code> otherwise
	 */
	public static boolean isConflict(int modifiers) {
		return
			(isProtected(modifiers) && isPrivate(modifiers)) ||
			(isPublic(modifiers) 	&& isPrivate(modifiers)) ||
			(isPublic(modifiers) 	&& isProtected(modifiers));
	}
	
	/**
	 * Returns <code>true</code> if the modifiers is none.
	 * 
	 * Test: {@link JavaModifierTest#testIsNone()}
	 * 
	 * @param mod the modifier to test
	 * @return <code>true</code> if the modifier is none,
	 * <code>false</code> otherwise.
	 */
	public static boolean isNone(int mod) {
		return mod == 0x00000000;
	}
	
	/**
	 * Disallow class instantiation.
	 */
	private JModifier() {
		// no invocation allowed
	}
	
}
