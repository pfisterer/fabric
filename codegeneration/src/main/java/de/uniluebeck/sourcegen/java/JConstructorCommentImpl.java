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

/**
 *
 */
package de.uniluebeck.sourcegen.java;

import java.util.Map;
import java.util.TreeMap;


/**
 * @author wegner
 *
 */
public class JConstructorCommentImpl extends JElemImpl implements JConstructorComment {

	/**
	 * The actual comment description.
	 */
	private final String description;

	/**
	 * Parameters mapped to their descriptions.
	 */
	private Map<String,String> params = new TreeMap<String, String>( );

	/**
	 * Generate an empty Javadoc constructor comment.
	 */
	public JConstructorCommentImpl() {
		this("");
	}

	/**
	 * Generate a Javadoc constructor comment with specified description.
	 *
	 * @param description The actual constructor comment description.
	 */
	public JConstructorCommentImpl(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Adds a parameter to the list of method parameters.
	 *
	 * @param name The parameter's name.
	 * @param description The parameter's description.
	 */
	public void addParameter(String name, String description) {
		this.params.put(name, description);
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParameters() {
		return params;
	}

	/* (non-Javadoc)
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(java.lang.StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append("/**\n");

		addDescriptionComment(buffer, tabCount);

		addParameterComments(buffer, tabCount);

		indent(buffer, tabCount);
		buffer.append(" */\n");
	}

	/**
	 * @param buffer
	 * @param tabCount
	 */
	protected void addDescriptionComment(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append(" * ").append(this.description).append("\n");
		if (!params.isEmpty()) {
			indent(buffer, tabCount);
			buffer.append(" *\n");
		}
	}

	/**
	 * @param buffer
	 * @param tabCount
	 */
	protected void addParameterComments(StringBuffer buffer, int tabCount) {
		for (String key : params.keySet()) {
			indent(buffer, tabCount);
			buffer.append(" * @param ").append(key).append(" ").append(params.get(key)).append("\n");
		}
	}
}
