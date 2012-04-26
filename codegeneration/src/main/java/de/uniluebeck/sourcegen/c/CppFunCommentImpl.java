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
/**
 *
 */
package de.uniluebeck.sourcegen.c;



/**
 * The actual implementation for a Java method comment.
 *
 * @author Marco Wegner
 */
public class CppFunCommentImpl extends CppConstructorCommentImpl implements CppFunComment {

	/**
	 * String containing the description for the return type or
	 * <code>null</code> if the method does not return a value at all.
	 */
	private String returnType = null;

	/**
	 * Generate a Javadoc method comment with specified description.
	 *
	 * @param description The actual method comment description.
	 */
	public CppFunCommentImpl(String description) {
		super(description);
	}

	/**
	 * <p>
	 * Sets the description for the return type and value.
	 * </p>
	 *
	 * <p>
	 * Please note that setting this description for methods with the return
	 * type <code>void</code> will result in an incorrect Javadoc tag. It
	 * will not be determined whether the method actually returns any value at
	 * all.
	 * </p>
	 *
	 * @param description The description for the method's return type and
	 *        value.
	 */
	public void setReturnTypeDescription(String description) {
		this.returnType = description;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnTypeDescription() {
		return returnType;
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(java.lang.StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		buffer.append("/**" + Cpp.newline);

		addDescriptionComment(buffer, tabCount);

		if(!getParameters().isEmpty() && getDescription() != null) {
			buffer.append(" * " + Cpp.newline);
			addParameterComments(buffer, tabCount);
		}

		if(returnType != null) {
			buffer.append(" * " + Cpp.newline);
			addReturnTypeComment(buffer, tabCount);
		}

		buffer.append(" */" + Cpp.newline);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		this.toString(buffer, 0);
		return buffer.toString();
	}

	/**
	 * @param buffer
	 * @param tabCount
	 */
	protected void addDescriptionComment(StringBuffer buffer, int tabCount) {
		if(getDescription() != null) {
			buffer.append(" * ").append(getDescription()).append(Cpp.newline);
		}
	}

	/**
	 * @param buffer
	 * @param tabCount
	 */
	private void addReturnTypeComment(StringBuffer buffer, int tabCount) {
		if (returnType != null) {
			buffer.append(" * @return ").append(returnType).append(Cpp.newline);
		}
	}
}
