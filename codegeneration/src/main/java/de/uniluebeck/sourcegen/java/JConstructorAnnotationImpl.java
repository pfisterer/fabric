package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of constructors in Java.
 */
public class JConstructorAnnotationImpl extends JElemImpl implements JConstructorAnnotation {

	/**
	 * The actual annotation description.
	 */
	private final String description;

	/**
	 * Generate a constructor annotation with specified description.
	 *
	 * @param description The actual constructor annotation description.
	 */
	public JConstructorAnnotationImpl(String description) {
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
