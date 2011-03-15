package de.uniluebeck.sourcegen.protobuf.types;

public class POption extends PAbstractElem {
	private String name;
	
	private String value;

	public POption(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		addLine(buffer, tabCount, "option " + name + " = " + value);
	}

}
