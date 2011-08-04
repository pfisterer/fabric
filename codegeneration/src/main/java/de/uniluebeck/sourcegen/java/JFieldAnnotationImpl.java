package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of fields in Java.
 */
public class JFieldAnnotationImpl extends JElemImpl implements JFieldAnnotation {

	/**
	 * The actual annotation description.
	 */
	private final String description;

	/**
	 * Generate a Javadoc field annotation with specified description.
	 *
	 * @param description The actual annotation description.
	 */
	public JFieldAnnotationImpl(String description) {
		this.description = description;
	}

	/**
	 * @see de.uniluebeck.sourcegen.ElemImpl#toString(StringBuffer, int)
	 */
	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		indent(buffer, tabCount);
		buffer.append("@").append(this.description).append("\n");
	}
}
