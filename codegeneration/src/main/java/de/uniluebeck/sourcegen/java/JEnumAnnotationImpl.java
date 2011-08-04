package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of fields in Java.
 */
public class JEnumAnnotationImpl extends JElemImpl implements JEnumAnnotation {

	/**
	 * The actual annotation description.
	 */
	private final String description;

	/**
	 * Generate an enum annotation with specified description.
	 *
	 * @param description The actual annotation description.
	 */
	public JEnumAnnotationImpl(String description) {
		this.description = description;
	}

	/**
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(java.lang.StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append("@").append(this.description).append("\n");
	}
}
