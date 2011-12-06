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
package de.uniluebeck.sourcegen;

import java.util.LinkedList;

import de.uniluebeck.sourcegen.c.Cpp;

public abstract class ElemImpl implements WorkspaceElement {

	public abstract void toString(StringBuffer buffer, int tabCount);

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		toString(buff, 0);
		return buff.toString();
	}

	public void toString(StringBuffer buffer) {
		toString(buffer, 0);
	}

	public String toString(int tabCount) {
		StringBuffer buff = new StringBuffer();
		toString(buff, tabCount);
		return buff.toString();
	}

	protected void addLine(StringBuffer buffer, int tabCount, String line) {
		indent(buffer, tabCount);
		buffer.append(line);
		buffer.append("\n");
	}

	protected void indent(StringBuffer buffer, int tabCount) {
		for (int i = 0; i < tabCount; i++)
			buffer.append("\t");
	}

	protected void toString(StringBuffer buffer, int tabCount, LinkedList<? extends WorkspaceElement> elemList,
			boolean lastSeparator) {
		toString(buffer, tabCount, elemList, "", "\n\n", lastSeparator);
	}

	protected void toString(StringBuffer buffer, int tabCount, LinkedList<? extends WorkspaceElement> elemList) {
		toString(buffer, tabCount, elemList, "", "\n\n");
	}

	void toString(StringBuffer buffer, int tabCount, LinkedList<? extends WorkspaceElement> elemList,
			String elemSuffix, String separator, boolean lastSeparator) {
		if (elemList.size() > 0) {
			for (WorkspaceElement elem : elemList) {
				elem.toString(buffer, tabCount);
				buffer.append(elemSuffix);
				if (lastSeparator || elem != elemList.getLast())
					buffer.append(separator);
			}
		}
	}

	protected void toString(StringBuffer buffer, int tabCount, LinkedList<? extends WorkspaceElement> elemList,
			String elemSuffix, String separator) {
		toString(buffer, tabCount, elemList, elemSuffix, separator, false);
	}

	protected void appendBody(StringBuffer buffer, StringBuffer body, int tabCount) {
		int begin = 0, end = 0;
		if (body.length() > 0) {
			begin = end = 0;
			int codeLength = body.length();

			while (begin < codeLength) {
				end = body.indexOf("\n", begin);
				end = (end == -1) ? codeLength : end;
				indent(buffer, tabCount);
				buffer.append(body, begin, end);
				buffer.append(end < codeLength - 1 ? "\n" : "");
				begin = end + 1;
			}
		}
	}
}
