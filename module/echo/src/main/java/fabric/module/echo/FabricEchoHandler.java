/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
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
package fabric.module.echo;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.dot.DGraphEdge;
import de.uniluebeck.sourcegen.dot.DGraphFile;
import de.uniluebeck.sourcegen.dot.DGraphNode;
import de.uniluebeck.sourcegen.echo.EchoFile;
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
public class FabricEchoHandler extends FabricDefaultHandler {

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
     * The Graphviz dot attributes used edges which signify that a top-level
     * type has been referenced.
     */
    private String edgeTopLevelTypeReference;

    private final EchoFile echoSource;
    
    
    /**
     * 
     */
   /* private final DGraphFile graphSource;

    {
        this.topLevelElementAttributes = EchoConstants.DEFAULT_TOP_LEVEL_ELEMENT_ATTRIBUTES;
        this.localElementAttributes = EchoConstants.DEFAULT_LOCAL_ELEMENT_ATTRIBUTES;
        this.topLevelSimpleTypeAttributes = EchoConstants.DEFAULT_TOP_LEVEL_SIMPLE_TYPE_ATTRIBUTES;
        this.localSimpleTypeAttributes = EchoConstants.DEFAULT_LOCAL_SIMPLE_TYPE_ATTRIBUTES;
        this.topLevelComplexTypeAttributes = EchoConstants.DEFAULT_TOP_LEVEL_COMPLEX_TYPE_ATTRIBUTES;
        this.localComplexTypeAttributes = EchoConstants.DEFAULT_LOCAL_COMPLEX_TYPE_ATTRIBUTES;
        this.edgeTopLevelTypeReference = EchoConstants.DEFAULT_EDGE_TOP_LEVEL_TYPE;
    }*/

    /**
     * Constructs a new handler for dot graph generation.
     * 
     * @param workspace The workspace where the graph source file resides.
     * @param properties The properties used to customise this handler.
     */
    public FabricEchoHandler(Workspace workspace, Properties properties) {
        this.echoSource = workspace.getEchoHelper( ).getDefaultSourceFile( );
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
        this.echoSource.add(createNodeLabel(element));
    	//createGraphNode(element, this.topLevelElementAttributes);
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
    	this.echoSource.add(createNodeLabel(element));
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception {
        
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        
    }

    @Override
    public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception {
        
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        
    }

    /**
     * <p>
     * Creates and returns the corresponding graph node for the specified Schema
     * object.
     * </p>
     * <p>
     * This method does <b>not</b> check if a node with the specified ID already
     * exists in the graph. If it is not apparent whether a node must be created
     * or just needs to be retrieved, use
     * {@link #getOrCreateGraphNode(FSchemaObject, String, String)} instead.
     * </p>
     * <p>
     * The newly created node is also added to the graph source file.
     * </p>
     * 
     * @param object The Schema object for which the corresponding node is to be
     *        created.
     * @param attributes The node's attributes as comma-separated list.
     * @return The newly created graph node.
     */
    private String createGraphNode(FSchemaObject object, String attributes) {
        //final DGraphNode node = new DGraphNode(object.getID( ));
        String label = createNodeLabel(object);
        //node.setNodeStyle(attributes);
        //this.graphSource.add(node);
        return label;
    }

    /**
     * Creates a corresponding graph node for the specified Schema object if,
     * and only if, no such node currently exists in the graph. In this case the
     * node is also added to the graph. If, on the other hand, the node does
     * exist it is simply returned.
     * 
     * @param object The Schema object for which the corresponding node is to be
     *        queried and/or created.
     * @param label The node's label.
     * @param attributes The node's attributes as comma-separated list.
     * @return The newly created graph node.
     */
    private DGraphNode getOrCreateGraphNode(FSchemaObject object, String label, String attributes) {
        
        return null;
    }

    /**
     * Creates and returns an edge. The edge is also added to the graph source
     * file.
     * 
     * @param source The new edge's source node.
     * @param target The new edge's target node.
     * @param attributes The edge's attributes as comma-separated list.
     * @return The newly created graph edge.
     */
    private DGraphEdge createGraphEdge(DGraphNode source, DGraphNode target, String attributes) {
        return null;
    }

    /**
     * Creates and returns an edge. The edge is also added to the graph source
     * file.
     * 
     * @param object The parent Schema object. That object's ID is used to
     *        retrieve the corresponding graph node which will function as the
     *        new edge's source node.
     * @param target The new edge's target node.
     * @param attributes The edge's attributes as comma-separated list.
     * @return The newly created graph edge.
     */
    

    /**
     * Creates the label for a node. This label is used in the graph to give the
     * node a more descriptive text.
     * 
     * @param object The Schema object to create the label for.
     * @return The node's new label.
     */
    private String createNodeLabel(FSchemaObject object) {
        final String label;
        if (object instanceof FElement) {
            label = createElementLabel((FElement)object);
        } else if (object instanceof FSimpleType) {
            label = createSimpleTypeLabel((FSimpleType)object);
        } else if (object instanceof FComplexType) {
            label = createComplexTypeLabel((FComplexType)object);
        } else {
            label = null;
        }
        return label;
    }

    /**
     * Creates the label for an element node.
     * 
     * @param e The Schema element to create a label for.
     * @return The element node's new label.
     */
    private String createElementLabel(FElement e) {
        return e.isReference( ) ? "<<reference>>" : e.getName( );
    }

    /**
     * Creates the label for a simple type node.
     * 
     * @param type The Schema simple type to create the label for.
     * @return The type node's new label.
     */
    private String createSimpleTypeLabel(FSimpleType type) {
        final String label;
        if (type.isTopLevel( )) {
            label = type.getName( );
        } else {
            label = null;
        }
        return label;
    }

    /**
     * Creates the label for a complex type node.
     * 
     * @param type The Schema complex type to create the label for.
     * @return The type node's new label.
     */
    private String createComplexTypeLabel(FComplexType type) {
        final String label;
        if (type.isTopLevel( )) {
            label = type.getName( );
        } else {
            label = null;
        }
        return label;
    }
}
