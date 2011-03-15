package de.uniluebeck.sourcegen.protobuf.types;

public class PComplexTypeField extends PAbstractField {
	private PMessage type;

	public PComplexTypeField(PMessage type, String name, boolean optional, boolean required, boolean repeated, int uniqueNumberTag) {
		super(name, optional, required, repeated, uniqueNumberTag);
		this.type = type;
	}

	public PMessage getType() {
		return type;
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

		b.append(type.getName());
		b.append(" ");
		b.append(getName());
		b.append(" = ");
		b.append(getUniqueNumberTag());
		b.append(";");
		
		addLine(buffer, tabCount, b.toString());
	}

}
