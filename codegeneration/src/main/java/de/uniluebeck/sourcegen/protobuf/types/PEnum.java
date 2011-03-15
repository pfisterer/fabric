package de.uniluebeck.sourcegen.protobuf.types;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PEnum extends PAbstractElem {

	private String name;

	private List<String> values = new LinkedList<String>();

	public PEnum(String name, List<String> values) {
		this.name = name;
		this.values.addAll(values);
	}

	public PEnum(String name, String... values) {
		this(name, Arrays.asList(values));
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		int count = 0;

		addLine(buffer, tabCount, "enum " + name + "{");

		for (String value : values)
			addLine(buffer, tabCount, value + " = " + (count++));

		addLine(buffer, tabCount, "};");
	}

}
