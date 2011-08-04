package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of methods in Java.
 */
public class JMethodAnnotationImpl extends JElemImpl implements JMethodAnnotation {

	/**
	 * The actual annotation description.
	 */
	private final String description;

	/**
	 * Generate a method annotation with specified description.
	 *
	 * @param description The actual annotation description.
	 */
	public JMethodAnnotationImpl(String description) {
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
