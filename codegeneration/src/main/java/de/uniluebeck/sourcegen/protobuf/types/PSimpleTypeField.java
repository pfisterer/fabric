package de.uniluebeck.sourcegen.protobuf.types;

public class PSimpleTypeField extends PAbstractField {

	private SimpleType type;

	public PSimpleTypeField(SimpleType type, String name, boolean optional, boolean required, boolean repeated, int uniqueNumberTag) {
		super(name, optional, required, repeated, uniqueNumberTag);
		this.type = type;
	}

	public SimpleType getType() {
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

		b.append(type.toTypeString());
		b.append(" ");
		b.append(getName());
		b.append(" = ");
		b.append(getUniqueNumberTag());
		b.append(";");
		
		addLine(buffer, tabCount, b.toString());
	}
	
	
	public enum SimpleType {
		DOUBLE, FLOAT, INT32, INT64, UINT32, UINT64, SINT32, SINT64, FIXED32, FIXED64, SFIXED32, SFIXED64, BOOL, STRING, BYTES;

		public String toTypeString() {

			switch (this) {
			case DOUBLE:
				return "double";
			case FLOAT:
				return "double";
			case INT32:
				return "int32";
			case INT64:
				return "int64";
			case UINT32:
				return "uint32";
			case UINT64:
				return "uint64";
			case SINT32:
				return "sint32";
			case SINT64:
				return "sint64";
			case FIXED32:
				return "fixed32";
			case FIXED64:
				return "fixed64";
			case SFIXED32:
				return "sfixed32";
			case SFIXED64:
				return "sfixed64";
			case BOOL:
				return "bool";
			case STRING:
				return "string";
			case BYTES:
				return "bytes";
			}

			throw new RuntimeException("Unimplemented type: " + this);
		}
	}

}
