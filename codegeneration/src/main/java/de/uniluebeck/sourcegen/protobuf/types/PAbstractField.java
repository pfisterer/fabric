package de.uniluebeck.sourcegen.protobuf.types;

public abstract class PAbstractField extends PAbstractElem {
	private String name;

	private boolean optional;

	private boolean required;

	private boolean repeated;

	private int uniqueNumberTag;

	public PAbstractField(String name, boolean optional, boolean required, boolean repeated, int uniqueNumberTag) {
		super();
		
		this.name = name;
		this.optional = optional;
		this.required = required;
		this.repeated = repeated;
		this.uniqueNumberTag = uniqueNumberTag;

		{//Ensure that only one of the flags is set
			int count = 0;

			if (isOptional())
				count++;

			if (isRepeated())
				count++;

			if (isRequired())
				count++;

			if (count > 1)
				throw new RuntimeException("More than one of optional" + isOptional() + ", repeated" + isRepeated()
						+ ", and required" + isRequired() + " is true.");
		}
		
	}

	public String getName() {
		return name;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isRepeated() {
		return repeated;
	}

	public int getUniqueNumberTag() {
		return uniqueNumberTag;
	}

}
