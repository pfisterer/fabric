/**
 * 
 */
package de.uniluebeck.sourcegen.dot;

/**
 * This class represents a single node in a Grapviz dot graph.
 * 
 * @author Marco Wegner
 */
public class DGraphNode extends DGraphElement {

    /**
     * The node's identifier. This identifier must be unique within a graph.
     */
    private final int id;

    /**
     * This node's label.
     */
    private String label;

    /**
     * This node's style to be applied in the graph image.
     */
    private String nodeStyle;

    {
        this.label = null;
        this.nodeStyle = null;
    }

    /**
     * Constructs a new graph node with the specified identifier.
     * 
     * @param id The new node's identifier.
     */
    public DGraphNode(int id) {
        this.id = id;
    }

    /**
     * Returns this node's identifier.
     * 
     * @return The node identifier.
     */
    public int getIdentifier( ) {
        return this.id;
    }

    /**
     * Sets this node's label.
     * 
     * @param label The node's new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns this node's label.
     * 
     * @return The node label.
     */
    public String getLabel( ) {
        return this.label;
    }

    /**
     * Sets this node's style to be applied in the graph image.
     * 
     * @param style The node's new style.
     */
    public void setNodeStyle(String style) {
        this.nodeStyle = style;
    }

    /**
     * Returns this node's style.
     * 
     * @return The node style.
     */
    public String getNodeStyle( ) {
        return this.nodeStyle;
    }

    /**
     * Helper method that from a node identifier creates a string ID to be used
     * in as dot graph node identifier.
     * 
     * @return The string ID.
     */
    public String getStringID( ) {
        return String.format("ID%08d", getIdentifier( ));
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        final StringBuilder sb = new StringBuilder( );
        if (this.label != null && !this.label.isEmpty( )) {
            sb.append(String.format("label = \"%s\"", this.label));
        }
        if (this.nodeStyle != null && !this.nodeStyle.isEmpty( )) {
            if (sb.length( ) > 0) {
                sb.append(", ");
            }
            sb.append(this.nodeStyle);
        }
        final String s = sb.length( ) > 0 ? String.format(" [ %s ]", sb.toString( )) : "";
        addLine(buffer, tabCount, String.format("%s%s;", getStringID( ), s));
    }
}
