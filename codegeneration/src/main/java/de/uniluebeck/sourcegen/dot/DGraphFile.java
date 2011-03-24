/**
 * 
 */
package de.uniluebeck.sourcegen.dot;

import java.io.File;
import java.util.LinkedList;
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
     * The list of graph elements contained in this graph.
     */
    private final List<DGraphElement> elements;

    {
        this.elements = new LinkedList<DGraphElement>( );
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
     * Adds another graph element to this graph source file.
     * 
     * @param e The graph element to be added.
     */
    public void add(DGraphElement e) {
        this.elements.add(e);
    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        addLine(buffer, tabCount, "digraph G {");
        for (final DGraphElement e : this.elements) {
            e.toString(buffer, tabCount + 1);
        }
        addLine(buffer, tabCount, "}");
    }
}
