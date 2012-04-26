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
package de.uniluebeck.sourcegen.protobuf.types;

import java.util.Collection;
import java.util.LinkedList;

public class PMessage extends PAbstractElem {
	private String name;
	
	private Collection<PMessage> messages = new LinkedList<PMessage>();
	
	private Collection<PAbstractField> fields = new LinkedList<PAbstractField>();
	
	private Collection<POption> options = new LinkedList<POption>();

	public PMessage(String name) {
		this.name = name;
	}
	
	public void add(PMessage message){
		getMessages().add(message);
	}
	
	public void add(PAbstractField field){
		getFields().add(field);
	}
	
	public void add(POption option){
		getOptions().add(option);
	}
	
	
	
	public Collection<PMessage> getMessages() {
		return messages;
	}

	public void setMessages(Collection<PMessage> messages) {
		this.messages = messages;
	}

	public Collection<PAbstractField> getFields() {
		return fields;
	}

	public void setFields(Collection<PAbstractField> fields) {
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public Collection<POption> getOptions() {
		return options;
	}

	public void setOptions(Collection<POption> options) {
		this.options = options;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		addLine(buffer, tabCount, "message " + name + "{");

		for(POption option : options )
			option.toString(buffer, tabCount+1);
		
		for(PMessage message : messages)
			message.toString(buffer, tabCount+1);
		
		for(PAbstractField field : fields)
			field.toString(buffer, tabCount+1);
		
		addLine(buffer, tabCount, "};");
	}

}
