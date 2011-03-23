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
