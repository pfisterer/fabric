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
     * Creates an ID string from the node's numerical identifier.
     * 
     * @return The string ID.
     */
    public String getStringID( ) {
        return String.format("ID%08d", getIdentifier( ));
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        final StringBuilder sb = new StringBuilder( );
        // add the node's label, if any
        if (this.label != null && !this.label.isEmpty( )) {
            sb.append(String.format("label = \"%s\"", this.label));
        }
        // add the node's style attributes, if any
        if (this.nodeStyle != null && !this.nodeStyle.isEmpty( )) {
            if (sb.length( ) > 0) {
                sb.append(", ");
            }
            sb.append(this.nodeStyle);
        }
        // put it all together
        final String s = sb.length( ) > 0 ? String.format(" [ %s ]", sb.toString( )) : "";
        addLine(buffer, tabCount, String.format("%s%s;", getStringID( ), s));
    }
}
