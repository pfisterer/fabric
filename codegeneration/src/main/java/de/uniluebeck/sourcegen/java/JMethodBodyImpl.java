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
package de.uniluebeck.sourcegen.java;



class JMethodBodyImpl extends JElemImpl implements JMethodBody {

	private StringBuffer source = new StringBuffer();
	private StringBuffer declarationsSrc = new StringBuffer();

	JMethodBodyImpl() {
		setSource("");
	}
	
	public JMethodBodyImpl(String... source) {
		for (String s : source)
			appendSource(s);
	}

	public JMethodBody appendSource(String... source) {
		for (String s : source)
			this.source.append(s);
		return this;
	}
	
	public JMethodBody appendSourceLine(String sourceLine) {
		this.source.append(sourceLine + "\n");
		return this;
	}

	private void emptySource() {
		if (source.length() > 0)
			this.source.delete(0, source.length());
	}

	public String getSource() {
		return source.toString();
	}

	public JMethodBody setSource(String source) {
		emptySource();
		appendSource(source);
		return this;
	}

	public JMethodBody addDeclarationAtBeginning(String... declSrc) {
		for (String src : declSrc)
			declarationsSrc .append(src + "\n");
		return this;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		int begin = 0;
		int end = 0;
		int declSrcLength = declarationsSrc.length();
		while(begin < declSrcLength) {
			end = declarationsSrc.indexOf("\n", begin);
			end = (end == -1) ? declSrcLength : end;
			indent(buffer, tabCount);
			buffer.append(declarationsSrc, begin, end);
			buffer.append("\n");
			begin = end+1;
		}
		
		begin = 0;
		end = 0;
		int sourceLength = source.length();
		
		while(begin < sourceLength) {
			end = source.indexOf("\n", begin);
			end = (end == -1) ? sourceLength : end;
			indent(buffer, tabCount);
			buffer.append(source, begin, end);
			buffer.append(end != sourceLength ? "\n" : "");
			begin = end+1;
		}
	}

}
