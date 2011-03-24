/**
 * 
 */
package de.uniluebeck.sourcegen.dot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     * The list of nodes contained in this graph.
     */
    private final List<DGraphNode> nodes;

    /**
     * The list of edges contained in this graph.
     */
    private final List<DGraphEdge> edges;

    {
        this.nodes = new ArrayList<DGraphNode>( );
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
        this.nodes.add(node);
    }

    /**
     * Returns the node that has the specified ID.
     * 
     * @param id The node ID.
     * @return The node or <code>null</code> if no such node could be found in
     *         this graph.
     */
    public DGraphNode getNodeByID(int id) {
        DGraphNode node = null;
        for (final DGraphNode n : this.nodes) {
            if (n.getIdentifier( ) == id) {
                node = n;
                break;
            }
        }
        return node;
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
        for (final DGraphNode n : this.nodes) {
            n.toString(buffer, tabCount + 1);
        }
        for (final DGraphEdge e : this.edges) {
            e.toString(buffer, tabCount + 1);
        }
        addLine(buffer, tabCount, "}");
    }
}
