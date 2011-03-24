/**
 * 
 */
package fabric.module.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaObject;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.FTopLevelObjectList;

/**
 * This class represents the walker on the Schema object tree.
 * 
 * @author Marco Wegner
 */
public final class FabricSchemaTreeWalker {

    /**
     * The logging instance.
     */
    private static final Logger log = LoggerFactory.getLogger(FabricSchemaTreeWalker.class);

    /**
     * The handler for specific Schema object tree items.
     */
    private FabricSchemaTreeItemHandler itemHandler;

    /**
     * Construct a new Schema object tree walker.
     */
    public FabricSchemaTreeWalker( ) {
        super( );
    }

    /**
     * Sets the handler for specific Schema object tree items.
     * 
     * @param itemHandler The new Schema object item tree handler.
     */
    private void setItemHandler(FabricSchemaTreeItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    /**
     * Returns the handler for specific Schema object tree items.
     * 
     * @return The Schema object item tree handler.
     */
    private FabricSchemaTreeItemHandler getItemHandler( ) {
        return this.itemHandler;
    }

    /**
     * Walk a Schema object tree. Fabric's default handler for Schema object
     * tree items is used.
     * 
     * @param schema The root of the Schema tree to be walked.
     * @throws Exception If an error occurs while walking the tree.
     */
    public void walk(FSchema schema) throws Exception {
        walk(schema, null);
    }

    /**
     * Walk a Schema object tree.
     * 
     * @param schema The root of the Schema tree to be walked.
     * @param itemHandler The tree item handler to use when walking the tree. If
     *        <code>null</code>, Fabric's default handler for Schema object tree
     *        items is used.
     * @throws Exception If an error occurs while walking the tree.
     */
    public void walk(FSchema schema, FabricSchemaTreeItemHandler itemHandler) throws Exception {

        final FabricSchemaTreeItemHandler handler;
        if (itemHandler == null) {
            handler = new FabricDefaultHandler( );
        } else {
            handler = itemHandler;
        }
        setItemHandler(handler);

        handler.startSchema(schema);
        log.debug("Start handling Schema object tree");

        final FTopLevelObjectList tlo = schema.getTopLevelObjectList( );

        for (final FElement e : tlo.getTopLevelElements( )) {
            handleElement(e, null);
        }

        handler.endSchema(schema);
        log.debug("Done handling Schema object tree");
    }

    /**
     * Handles an element in the Schema object tree.
     * 
     * @param e The element to be handled.
     * @param parent The element's parent. If <code>null</code> this usually
     *        signifies a top-level element.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleElement(FElement e, FComplexType parent) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String elemName = e.getName( );

        if (e.isReference( )) {
            handler.startElementReference(e);
            log.debug("Start handling element reference '{}'", elemName);
            // TODO handle element reference
            handler.endElementReference(e);
            log.debug("Done handling element reference '{}'", elemName);
        } else if (e.isTopLevel( )) {
            handler.startTopLevelElement(e);
            log.debug("Start handling top-level element '{}'", elemName);
            handleSchemaType(e.getSchemaType( ), e);
            handler.endTopLevelElement(e);
            log.debug("Done handling top-level element '{}'", elemName);
        } else {
            handler.startLocalElement(e, parent);
            log.debug("Start handling local element '{}'", elemName);
            handleSchemaType(e.getSchemaType( ), e);
            handler.endLocalElement(e, parent);
            log.debug("Done handling local element '{}'", elemName);
        }
    }

    /**
     * Generic handling method for Schema types.
     * 
     * @param type The type to be handled.
     * @param parent The Schema type's parent element.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleSchemaType(FSchemaType type, FElement parent) throws Exception {
        if (type instanceof FSimpleType) {
            handleSimpleType((FSimpleType)type, parent);
        } else if (type instanceof FComplexType) {
            handleComplexType((FComplexType)type, parent);
        } else {
            throw new Exception("Unknown SchemaType: " + type.toString( ));
        }
    }

    /**
     * Handles a simple type.
     * 
     * @param type The simple type to be handled.
     * @param parent The simple type's parent element.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleSimpleType(FSimpleType type, FElement parent) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String typeName = type.getName( );

        if (type.isTopLevel( )) {
            handler.startTopLevelSimpleType(type);
            log.debug("Start handling top-level simple type '{}'", typeName);
            handleSimpleTypeContent( );
            handler.endTopLevelSimpleType(type);
            log.debug("Done handling top-level simple type '{}'", typeName);
        } else {
            handler.startLocalSimpleType(type, parent);
            log.debug("Start handling local simple type '{}'", typeName);
            handleSimpleTypeContent( );
            handler.endLocalSimpleType(type, parent);
            log.debug("Done handling local simple type '{}'", typeName);
        }
    }

    /**
     * 
     */
    private void handleSimpleTypeContent( ) {
        //
    }

    /**
     * Handles a complex type.
     * 
     * @param type The complex type to be handled.
     * @param parent The complex type's parent element.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleComplexType(FComplexType type, FElement parent) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String typeName = type.getName( );

        if (type.isTopLevel( )) {
            handler.startTopLevelComplexType(type);
            log.debug("Start handling top-level complex type '{}'", typeName);
            handleComplexContent(type);
            handler.endTopLevelComplexType(type);
            log.debug("Done handling top-level complex type '{}'", typeName);
        } else {
            handler.startLocalComplexType(type, parent);
            log.debug("Start handling local complex type '{}'", typeName);
            handleComplexContent(type);
            handler.endLocalComplexType(type, parent);
            log.debug("Done handling local complex type '{}'", typeName);
        }
    }

    /**
     * Handles the content of a complex type. The content usually comprises more
     * types, elements and/or attributes.
     * 
     * @param parent The complex type whose children are to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleComplexContent(FComplexType parent) throws Exception {
        for (final FSchemaObject o : parent.getChildObjects( )) {
            if (o instanceof FElement) {
                handleElement((FElement)o, null);
            } else if (o instanceof FComplexType) {
                // TODO fix parent
                handleComplexType((FComplexType)o, null);
            } else {
                throw new Exception("Unknown child object: " + o.toString( ));
            }
        }
    }
}
