/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fabric.module.helloworld;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CEnum;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CHeaderFile;
import de.uniluebeck.sourcegen.c.CPreProcessorDirective;
import de.uniluebeck.sourcegen.c.CSourceFile;
import de.uniluebeck.sourcegen.c.CStruct;
import de.uniluebeck.sourcegen.c.CUnion;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppConstructor;
import de.uniluebeck.sourcegen.c.CppDestructor;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppSourceFileImpl;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;
import de.uniluebeck.sourcegen.java.*;
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
public class FabricHelloWorldHandler extends FabricDefaultHandler {

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

//    private final JSourceFileImpl helloWorldSource;
    private final CppSourceFileImpl helloWorldSource;
    private Workspace workspace;
    /**
     * Constructs a new handler for dot graph generation.
     *
     * @param workspace The workspace where the graph source file resides.
     * @param properties The properties used to customise this handler.
     */
    public FabricHelloWorldHandler(Workspace workspace, Properties properties) {
        this.workspace = workspace;
    	this.helloWorldSource = this.workspace.getHelloWorldHelper( ).getDefaultSourceFile( );
        
     
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
        //this.topLevelElementAttributes = attributes;
    }

    /**
     * Sets the attributes for local-element nodes.
     *
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalElementAttributes(String attributes) {
        //this.localElementAttributes = attributes;
    }

    /**
     * Sets the attributes for top-level-simple-type nodes.
     *
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setTopLevelSimpleTypeAttributes(String attributes) {
        //this.topLevelSimpleTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for local-simple-type nodes.
     *
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalSimpleTypeAttributes(String attributes) {
        //this.localSimpleTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for top-level-complex-type nodes.
     *
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setTopLevelComplexTypeAttributes(String attributes) {
        //this.topLevelComplexTypeAttributes = attributes;
    }

    /**
     * Sets the attributes for local-complex-type nodes.
     *
     * @param attributes The new attributes.
     * @see #setTopLevelElementAttributes(String) Style attribute examples
     */
    public void setLocalComplexTypeAttributes(String attributes) {
        //this.localComplexTypeAttributes = attributes;
    }

    /**
     * This method creates a simple Hello World! application
     * and writes it to the Java workspace of Fabric.
     *
     * @throws Exception Error during code generation
     */
    private void createHelloWorldFile() throws Exception {
    	    	
    	// include stdio.h
    	//this.helloWorldSource.addLibInclude("stdio");
    	
    	// include the header file
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("helloWorldHeader");
        this.helloWorldSource.addInclude(header);
      
        // generate the class
        CppClass helloWorldClass = CppClass.factory.create("HelloWorld", this.helloWorldSource);
        header.add(helloWorldClass);
    
        // arguments
        CppTypeGenerator argcType = new CppTypeGenerator("int");
        // NOTE: const has been set manually...is there a function doing this for me?
        // 		CppTypeGenerator("int", Cpp.CONST) delivered false values
        CppTypeGenerator argvType = new CppTypeGenerator("const int");
        CppVar argcParameter = CppVar.factory.create(argcType, "argc");
        // NOTE: the array has been set manually...is there a function doing this for me?
        CppVar argvParameter = CppVar.factory.create(argvType, "argv[]");
        
        // main method declaration
        CppFun mainFun = CppFun.factory.create(helloWorldClass, Cpp.INT, "main", argcParameter, argvParameter);
    
        // some function content
        mainFun.appendCode("/*" + Cpp.newline);
        mainFun.appendCode(" * This is a comment" + Cpp.newline);
        mainFun.appendCode(" */" + Cpp.newline);
        mainFun.appendCode(Cpp.newline);
        mainFun.appendCode(" // a test variable of type string" + Cpp.newline);
        mainFun.appendCode("String testVariable = \"WoooW\";" + Cpp.newline);
        mainFun.appendCode(Cpp.newline);
        // NOTE: return value has been set manually...is there a method doing this for me?
        mainFun.appendCode("return 0;" + Cpp.newline);
        
        helloWorldClass.add(Cpp.PUBLIC, mainFun);
        this.helloWorldSource.add(helloWorldClass);
           
    }

    /**
     * This method creates a simple Java class for testing
     * the support of annotations and writes it to the Java
     * workspace of Fabric.
     *
     * @throws Exception Error during code generation
     */
/*    private void createAnnotationTestFile() throws Exception {
      JMethodSignature jms = JMethodSignature.factory.create(JParameter.factory.create("String[]", "args"));
      JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "void", "main", jms);
      jm.getBody().appendSource("System.out.println(\"Dies ist ein Test!\");");
      jm.setComment(new JMethodCommentImpl("Dies ist eine Methode mit Annotationen."));
      jm.addAnnotation(new JMethodAnnotationImpl("SuppressWarnings(\"unchecked\")"));
      jm.addAnnotation(new JMethodAnnotationImpl("Deprecated"));
      JField jf = JField.factory.create(JModifier.PUBLIC, "String", "attribute");
      jf.addAnnotation(new JFieldAnnotationImpl("Attribute"));
      JClass jc = JClass.factory.create(JModifier.PUBLIC, "AnnotationTest");
      jc.add(jm);
      jc.add(jf);
      jc.addAnnotation(new JClassAnnotationImpl("Override"));
      this.helloWorldSource.add(jc);
    }*/

    /**
     * This method is run only once (hopefully).
     *
     * @param schema XSD schema object
     *
     * @throws Exception Error during schema processing
     */
    @Override
    public void startSchema(FSchema schema) throws Exception {
      // Generate Hello World! application
      this.createHelloWorldFile();
      // this.createAnnotationTestFile();
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
      //createGraphNode(element, this.topLevelElementAttributes);
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
      //this.helloWorldSource.add(createNodeLabel(element));
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

//    /**
//     * <p>
//     * Creates and returns the corresponding graph node for the specified Schema
//     * object.
//     * </p>
//     * <p>
//     * This method does <b>not</b> check if a node with the specified ID already
//     * exists in the graph. If it is not apparent whether a node must be created
//     * or just needs to be retrieved, use
//     * {@link #getOrCreateGraphNode(FSchemaObject, String, String)} instead.
//     * </p>
//     * <p>
//     * The newly created node is also added to the graph source file.
//     * </p>
//     *
//     * @param object The Schema object for which the corresponding node is to be
//     *        created.
//     * @param attributes The node's attributes as comma-separated list.
//     * @return The newly created graph node.
//     */
//    private String createGraphNode(FSchemaObject object, String attributes) {
//        //final DGraphNode node = new DGraphNode(object.getID( ));
//        String label = createNodeLabel(object);
//        //node.setNodeStyle(attributes);
//        //this.graphSource.add(node);
//        return label;
//    }
//
//    /**
//     * Creates a corresponding graph node for the specified Schema object if,
//     * and only if, no such node currently exists in the graph. In this case the
//     * node is also added to the graph. If, on the other hand, the node does
//     * exist it is simply returned.
//     *
//     * @param object The Schema object for which the corresponding node is to be
//     *        queried and/or created.
//     * @param label The node's label.
//     * @param attributes The node's attributes as comma-separated list.
//     * @return The newly created graph node.
//     */
//    private DGraphNode getOrCreateGraphNode(FSchemaObject object, String label, String attributes) {
//
//        return null;
//    }
//
//    /**
//     * Creates and returns an edge. The edge is also added to the graph source
//     * file.
//     *
//     * @param source The new edge's source node.
//     * @param target The new edge's target node.
//     * @param attributes The edge's attributes as comma-separated list.
//     * @return The newly created graph edge.
//     */
//    private DGraphEdge createGraphEdge(DGraphNode source, DGraphNode target, String attributes) {
//        return null;
//    }
//
//    /**
//     * Creates and returns an edge. The edge is also added to the graph source
//     * file.
//     *
//     * @param object The parent Schema object. That object's ID is used to
//     *        retrieve the corresponding graph node which will function as the
//     *        new edge's source node.
//     * @param target The new edge's target node.
//     * @param attributes The edge's attributes as comma-separated list.
//     * @return The newly created graph edge.
//     */
//
//
//    /**
//     * Creates the label for a node. This label is used in the graph to give the
//     * node a more descriptive text.
//     *
//     * @param object The Schema object to create the label for.
//     * @return The node's new label.
//     */
//    private String createNodeLabel(FSchemaObject object) {
//        final String label;
//        if (object instanceof FElement) {
//            label = createElementLabel((FElement)object);
//        } else if (object instanceof FSimpleType) {
//            label = createSimpleTypeLabel((FSimpleType)object);
//        } else if (object instanceof FComplexType) {
//            label = createComplexTypeLabel((FComplexType)object);
//        } else {
//            label = null;
//        }
//        return label;
//    }
//
//    /**
//     * Creates the label for an element node.
//     *
//     * @param e The Schema element to create a label for.
//     * @return The element node's new label.
//     */
//    private String createElementLabel(FElement e) {
//        return e.isReference( ) ? "<<reference>>" : e.getName( );
//    }
//
//    /**
//     * Creates the label for a simple type node.
//     *
//     * @param type The Schema simple type to create the label for.
//     * @return The type node's new label.
//     */
//    private String createSimpleTypeLabel(FSimpleType type) {
//        final String label;
//        if (type.isTopLevel( )) {
//            label = type.getName( );
//        } else {
//            label = null;
//        }
//        return label;
//    }
//
//    /**
//     * Creates the label for a complex type node.
//     *
//     * @param type The Schema complex type to create the label for.
//     * @return The type node's new label.
//     */
//    private String createComplexTypeLabel(FComplexType type) {
//        final String label;
//        if (type.isTopLevel( )) {
//            label = type.getName( );
//        } else {
//            label = null;
//        }
//        return label;
//    }
}
