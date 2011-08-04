package de.uniluebeck.sourcegen.java;

/**
 * Annotation implementation for the annotation of Java classes.
 */
public class JClassAnnotationImpl extends JElemImpl implements JClassAnnotation {

	/**
	 * The actual annotation description.
	 */
	private final String description;

	/**
	 * Generate a class annotation with specified description.
	 *
	 * @param description The actual annotation description.
	 */
	public JClassAnnotationImpl(String description) {
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
