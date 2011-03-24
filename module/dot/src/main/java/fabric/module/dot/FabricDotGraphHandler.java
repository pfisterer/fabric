/**
 * 
 */
package fabric.module.dot;

import java.io.IOException;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.dot.DGraphEdge;
import de.uniluebeck.sourcegen.dot.DGraphFile;
import de.uniluebeck.sourcegen.dot.DGraphNode;
import fabric.module.api.FabricDefaultHandler;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchemaObject;
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

    /**
     * 
     */
    private final DGraphFile graphSource;

    {
        this.topLevelElementAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_ELEMENT_ATTRIBUTES;
        this.localElementAttributes = DotGraphConstants.DEFAULT_LOCAL_ELEMENT_ATTRIBUTES;
        this.topLevelSimpleTypeAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_SIMPLE_TYPE_ATTRIBUTES;
        this.localSimpleTypeAttributes = DotGraphConstants.DEFAULT_LOCAL_SIMPLE_TYPE_ATTRIBUTES;
        this.topLevelComplexTypeAttributes = DotGraphConstants.DEFAULT_TOP_LEVEL_COMPLEX_TYPE_ATTRIBUTES;
        this.localComplexTypeAttributes = DotGraphConstants.DEFAULT_LOCAL_COMPLEX_TYPE_ATTRIBUTES;
    }

    /**
     * Constructs a new handler for dot graph generation.
     * 
     * @param workspace The workspace where the graph source file resides.
     * @param properties The properties used to customise this handler.
     */
    public FabricDotGraphHandler(Workspace workspace, Properties properties) {
        this.graphSource = workspace.getDotHelper( ).getDefaultSourceFile( );
        // TODO use the properties to customise the styles
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
     * @see <a href="http://www.graphviz.org">Graphviz Dot Homepage</a>
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
    public void startTopLevelElement(FElement element) throws Exception {
        createGraphNode(element.getID( ), element.getName( ), this.topLevelElementAttributes);
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
        createGraphEdge(createGraphNode(element.getID( ), element.getName( ),
                this.localElementAttributes), parent, null);
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type) throws Exception {
        createGraphNode(type.getID( ), type.getName( ), this.topLevelSimpleTypeAttributes);
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        createGraphEdge(createGraphNode(type.getID( ), type.getName( ),
                this.localSimpleTypeAttributes), parent, null);
    }

    @Override
    public void startTopLevelComplexType(FComplexType type) throws Exception {
        createGraphNode(type.getID( ), type.getName( ), this.topLevelComplexTypeAttributes);
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        createGraphEdge(createGraphNode(type.getID( ), type.getName( ),
                this.localComplexTypeAttributes), parent, null);
    }

    /**
     * Creates and returns a single node. This node represents either an element
     * or a type. The newly created node is also added to the graph source file.
     * 
     * @param id The new node's ID.
     * @param label The node's label.
     * @param attributes The node's attributes as comma-separated list.
     * @return The newly created graph node.
     * @throws IOException If an error occurs while trying to write the node
     *         data.
     */
    private DGraphNode createGraphNode(int id, String label, String attributes) throws IOException {
        final DGraphNode node = new DGraphNode(id);
        node.setLabel(label);
        node.setNodeStyle(attributes);
        this.graphSource.add(node);
        return node;
    }

    /**
     * Creates and returns an edge. The edge is also added to the graph source
     * file.
     * 
     * @param node The target node.
     * @param parent The parent object. The object's ID is used to retrieve the
     *        corresponding node which will function as the edge's source node.
     * @param attributes The edge's attributes as comma-separated list.
     * @return The newly created graph edge.
     * @throws IOException If an error occurs while trying to write the node
     *         data.
     */
    private DGraphEdge createGraphEdge(DGraphNode node, FSchemaObject parent, String attributes)
            throws IOException {
        // retrieve the parent object's node
        final DGraphNode parentNode = this.graphSource.getNodeByID(parent.getID( ));
        // create the edge between the two nodes
        final DGraphEdge edge = new DGraphEdge(parentNode, node);
        edge.setLineStyle(attributes);
        this.graphSource.add(edge);
        return edge;
    }
}
