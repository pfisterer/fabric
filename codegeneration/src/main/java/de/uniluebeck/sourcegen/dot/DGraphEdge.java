/**
 * 
 */
package de.uniluebeck.sourcegen.dot;

/**
 * This class represents a directed edge between two nodes in a Graphviz dot
 * graph.
 * 
 * @author Marco Wegner
 */
public class DGraphEdge extends DGraphElement {

    /**
     * This edge's source node.
     */
    private final DGraphNode sourceNode;
    /**
     * This edge's target node.
     */
    private final DGraphNode targetNode;
    /**
     * 
     */
    private String label;
    /**
     * This edge's line style in the graph image.
     */
    private String lineStyle;

    {
        setLabel(null);
        this.lineStyle = null;
    }

    /**
     * Constructs a new graph edge that connects the specified source and target
     * nodes.
     * 
     * @param source The source node.
     * @param target The target node.
     */
    public DGraphEdge(DGraphNode source, DGraphNode target) {
        this.sourceNode = source;
        this.targetNode = target;
    }

    /**
     * Returns this edge's source node.
     * 
     * @return The source node.
     */
    public DGraphNode getSourceNode( ) {
        return this.sourceNode;
    }

    /**
     * Returns this edge's target node.
     * 
     * @return The target node.
     */
    public DGraphNode getTargetNode( ) {
        return this.targetNode;
    }

    /**
     * Sets this edge's label.
     * 
     * @param label The edge's new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns this edge's label.
     * 
     * @return This edge's label.
     */
    public String getLabel( ) {
        return this.label;
    }

    /**
     * Sets this edge's line style to applied in the graph image.
     * 
     * @param style The edge's new line style.
     */
    public void setLineStyle(String style) {
        this.lineStyle = style;
    }

    /**
     * Returns this edge's line style.
     * 
     * @return This edge's line style.
     */
    public String getLineStyle( ) {
        return this.lineStyle;
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        final StringBuilder sb = new StringBuilder( );
        if (this.label != null && !this.label.isEmpty( )) {
            sb.append(String.format("label = \"%s\"", this.label));
        }
        if (this.lineStyle != null && !this.lineStyle.isEmpty( )) {
            if (sb.length( ) > 0) {
                sb.append(", ");
            }
            sb.append(this.lineStyle);
        }
        final String s = sb.length( ) > 0 ? String.format(" [ %s ]", sb.toString( )) : "";
        addLine(buffer, tabCount, String.format("%s -> %s%s;", getSourceNode( ).getStringID( ),
                getTargetNode( ).getStringID( ), s));
    }
}
