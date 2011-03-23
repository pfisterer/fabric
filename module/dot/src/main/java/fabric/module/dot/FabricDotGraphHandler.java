/**
 * 
 */
package fabric.module.dot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import fabric.module.api.FabricDefaultHandler;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Fabric handler class that creates Graphviz dot graph files from a parsed
 * Schema tree. The resulting file can be used to visualise the Schema tree.
 * 
 * @author Marco Wegner
 * @see <a href="http://www.graphviz.org">Graphviz Dot Homepage</a>
 */
public class FabricDotGraphHandler extends FabricDefaultHandler {

    /**
     * The output writer used for creating the dot file.
     */
    private final BufferedWriter writer;

    /**
     * The Graphviz dot attributes used for top-level-element nodes.
     */
    private String topLevelElementAttributes;

    /**
     * The Graphviz dot attributes used for local-element nodes.
     */
    private String localElementAttributes;

    /**
     * The Graphviz dot attributes used for top-level-simple-type nodes.
     */
    private String topLevelSimpleTypeAttributes;

    /**
     * The Graphviz dot attributes used for local-simple-type nodes.
     */
    private String localSimpleTypeAttributes;

    /**
     * The Graphviz dot attributes used for top-level-complex-type nodes.
     */
    private String topLevelComplexTypeAttributes;

    /**
     * The Graphviz dot attributes used for local-complex-type nodes.
     */
    private String localComplexTypeAttributes;

    {
        this.topLevelElementAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_ELEMENT_ATTRIBUTES;
        this.localElementAttributes = DotGraphConstants.DEFAULT_LOCAL_ELEMENT_ATTRIBUTES;
        this.topLevelSimpleTypeAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_SIMPLE_TYPE_ATTRIBUTES;
        this.localSimpleTypeAttributes = DotGraphConstants.DEFAULT_LOCAL_SIMPLE_TYPE_ATTRIBUTES;
        this.topLevelComplexTypeAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_COMPLEX_TYPE_ATTRIBUTES;
        this.localComplexTypeAttributes = DotGraphConstants.DEFAULT_LOCAL_COMPLEX_TYPE_ATTRIBUTES;
    }

    /**
     * Constructs a new handler class for Graphviz dot graph files.
     * 
     * @param outputPath The resulting dot file's path name. The path name may
     *        be <code>null</code> - in that case the output is written to
     *        {@link System#out}.
     * @throws Exception If an error occurs during output file creation.
     */
    public FabricDotGraphHandler(String outputPath) throws Exception {
        this(outputPath == null ? null : new File(outputPath));
    }

    /**
     * Constructs a new handler class for Graphviz dot graph files.
     * 
     * @param outputFile The resulting dot file's abstract file path. The path
     *        may be <code>null</code> - in that case the output is written to
     *        {@link System#out}.
     * @throws Exception If an error occurs during output file creation.
     */
    public FabricDotGraphHandler(File outputFile) throws Exception {
        Writer w;
        if (outputFile == null) {
            w = new PrintWriter(System.out);
        } else {
            w = new FileWriter(outputFile);
        }
        this.writer = new BufferedWriter(w);
    }

    /**
     * <p>
     * Sets the attributes for top-level-element nodes.
     * </p>
     * <p>
     * Examples:
     * <ul>
     * <li>shape = box</li>
     * <li>shape = ellipse, style = filled, fillcolor = red</li>
     * <li>shape = polygon, sides = 4, skew = 0.4</li>
     * </ul>
     * </p>
     * 
     * @param attributes The new attributes.
     */
    public void setTopLevelElementAttributes(String attributes) {
        this.topLevelElementAttributes = attributes;
    }

    /**
     * Sets the attributes for local-element nodes.
     * 
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalElementAttributes(String attributes) {
        this.localElementAttributes = attributes;
    }

    /**
     * Sets the attributes for top-level-simple-type nodes.
     * 
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setTopLevelSimpleTypeAttributes(String attributes) {
        this.topLevelSimpleTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for local-simple-type nodes.
     * 
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalSimpleTypeAttributes(String attributes) {
        this.localSimpleTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for top-level-complex-type nodes.
     * 
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setTopLevelComplexTypeAttributes(String attributes) {
        this.topLevelComplexTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for local-complex-type nodes.
     * 
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalComplexTypeAttributes(String attributes) {
        this.localComplexTypeAttributes = attributes;
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        writeLine("digraph G {");
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        writeLine("}");
        this.writer.flush( );
        this.writer.close( );
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
        createGraphNode(element.getName( ), this.topLevelElementAttributes);
    }

    @Override
    public void startLocalElement(FElement element) throws Exception {
        createGraphNode(element.getName( ), this.localElementAttributes);
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type) throws Exception {
        createGraphNode(type.getName( ), this.topLevelSimpleTypeAttributes);
    }

    @Override
    public void startLocalSimpleType(FSimpleType type) throws Exception {
        createGraphNode(type.getName( ), this.localSimpleTypeAttributes);
    }

    @Override
    public void startTopLevelComplexType(FComplexType type) throws Exception {
        createGraphNode(type.getName( ), this.topLevelComplexTypeAttributes);
    }

    @Override
    public void startLocalComplexType(FComplexType type) throws Exception {
        createGraphNode(type.getName( ), this.localComplexTypeAttributes);
    }

    /**
     * Creates and writes a single node. This can be either an element or a
     * type.
     * 
     * @param caption The node's caption.
     * @param attributes The node's attributes as comma-separated list.
     * @throws IOException If an error occurs while trying to write the node
     *         data.
     */
    private void createGraphNode(String caption, String attributes) throws IOException {
        writeLine(String.format("%s [%s];", caption, attributes));
    }

    /**
     * Creates and writes an edge. Edges are transitions and can occur from
     * elements to types, from types to types and from types to elements.
     * 
     * @param source The edge's source.
     * @param destination The edge's destination.
     * @param attributes The edge's attributes as comma-separated list.
     * @throws IOException If an error occurs while trying to write the node
     *         data.
     */
    private void createGraphEdge(String source, String destination, String attributes)
            throws IOException {
        writeLine(String.format("%s -> %s [%s]", source, destination, attributes));
    }

    /**
     * Writes a single line to the writer instance.
     * 
     * @param line The line to be written. The newline character should be
     *        omitted from this string, since it is automatically added by this
     *        method.
     * @throws IOException If an I/O error occurs while trying to write the line
     *         to the writer.
     */
    private void writeLine(String line) throws IOException {
        this.writer.write(line);
        this.writer.newLine( );
    }
}
