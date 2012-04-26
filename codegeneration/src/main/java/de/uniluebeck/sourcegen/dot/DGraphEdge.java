/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
