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
package de.uniluebeck.sourcegen.c;

import de.uniluebeck.sourcegen.exceptions.CConflictingModifierException;



/**
 * A class representing a C parameter containing a name and a data type.
 * 
 * @author Daniel Bimschas
 */
class CParamImpl extends CElemImpl implements CParam {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the data type
	 */
	private String type;

	/**
	 * Constructs a new <code>CDataType</code> instance of type
	 * <code>type</code> and the name <code>name</code>.
	 * 
	 * @param type
	 *            the type
	 * @param modifiers
	 *            a list of type modifiers or an empty array or
	 *            <code>null</code> if there should be no modifiers
	 * @param name
	 *            the name
	 * @throws CConflictingModifierException
	 *             if there are conflicting modifiers (see {@link CTypeModifier}
	 *             JavaDoc for information about allowed combinations
	 */
	public CParamImpl(String type, String name)
			throws CConflictingModifierException {

		this.type = type;
		this.name = name;

		validate();
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.c.CParam#equals(de.uniluebeck.sourcegen.c.CParamImpl)
	 */
	public boolean equals(CParam other) {
		return name.equals(((CParamImpl)other).name);
	}

	/**
	 * Returns the name of this parameter.
	 * 
	 * @return the name of this parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the type of this parameter.
	 * 
	 * @return the type of this parameter
	 */
	public String getType() {
		return type;
	}

	private void validate() throws CConflictingModifierException {
		validateVoid();
		validateLonglong();
		validateInt();
		validateFloat();
		validateDouble();
		validateChar();
	}

	private void validateChar() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	private void validateDouble() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	private void validateFloat() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	private void validateInt() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	private void validateLonglong() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	private void validateVoid() throws CConflictingModifierException {
		// TODO: implement validation
		// remove SuppressWarnings (!)
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append(type);
		buffer.append(" ");
		buffer.append(name);
	}
	
	public static void main(String[] args) throws Exception {
		CParamImpl p = new CParamImpl("int", "theInt");
		System.out.print(p.toString(1));
		System.out.println("----------");
	}
}
