package de.uniluebeck.sourcegen.protobuf.types;


public class PEnumField extends PAbstractField {
	private PEnum enumType;
	
	private String defaultValue;
	
	public PEnumField(PEnum enumType, String name, boolean optional, boolean required, int uniqueNumberTag) {
		super(name, optional, required, required, uniqueNumberTag);
		this.enumType = enumType;
	}

	public PEnum getEnumType() {
		return enumType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		StringBuffer b = new StringBuffer();

		if (isOptional())
			b.append("optional ");

		if (isRepeated())
			b.append("repeated ");

		if (isRequired())
			b.append("required ");

		b.append(enumType.getName());
		b.append(" ");
		b.append(getName());
		b.append(" = ");
		b.append(getUniqueNumberTag());
		b.append(";");
		
		addLine(buffer, tabCount, b.toString());
	}
}
