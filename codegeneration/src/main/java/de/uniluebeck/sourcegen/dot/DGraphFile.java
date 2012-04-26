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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniluebeck.sourcegen.SourceFile;

/**
 * This class represents a source file used to generate graph images with
 * Graphviz dot.
 * 
 * @author Marco Wegner
 */
public class DGraphFile extends DGraphElement implements SourceFile {

    /**
     * This graph source file's path relative to the workspace.
     */
    private final File dotFile;

    /**
     * The nodes contained in this graph mapped to their respective node IDs.
     */
    private final Map<Integer, DGraphNode> nodes;

    /**
     * The list of edges contained in this graph.
     */
    private final List<DGraphEdge> edges;

    {
        this.nodes = new HashMap<Integer, DGraphNode>( );
        this.edges = new ArrayList<DGraphEdge>( );
    }

    /**
     * Constructs a new graph source file with the specified file name.
     * 
     * @param fileName The new source file's path relative to the workspace.
     */
    public DGraphFile(String fileName) {
        this.dotFile = new File(fileName);
    }

    @Override
    public String getFileName( ) {
        return this.dotFile.getPath( );
    }

    /**
     * Adds another node to this graph source file.
     * 
     * @param node The node to be added.
     */
    public void add(DGraphNode node) {
        this.nodes.put(node.getIdentifier( ), node);
    }

    /**
     * Returns the node that has the specified ID.
     * 
     * @param id The node ID.
     * @return The node or <code>null</code> if no such node could be found in
     *         this graph.
     */
    public DGraphNode getNodeByID(int id) {
        return this.nodes.get(id);
    }

    /**
     * Adds another edge to this graph source file.
     * 
     * @param edge The edge to be added.
     */
    public void add(DGraphEdge edge) {
        this.edges.add(edge);
    }

    /**
     * Returns the edge that has a source and target with the specified IDs.
     * 
     * @param sourceID The source node's ID.
     * @param targetID The target node's ID.
     * @return The edge or <code>null</code> if no such edge could be found in
     *         this graph.
     */
    public DGraphEdge getEdgeByIDs(int sourceID, int targetID) {
        DGraphEdge edge = null;
        for (final DGraphEdge e : this.edges) {
            if (sourceID == e.getSourceNode( ).getIdentifier( )
                    && targetID == e.getTargetNode( ).getIdentifier( )) {
                edge = e;
                break;
            }
        }
        return edge;
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        addLine(buffer, tabCount, "digraph G {");
        for (final DGraphNode n : this.nodes.values( )) {
            n.toString(buffer, tabCount + 1);
        }
        for (final DGraphEdge e : this.edges) {
            e.toString(buffer, tabCount + 1);
        }
        addLine(buffer, tabCount, "}");
    }
}
