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

        final FabricSchemaTreeItemHandler handler = itemHandler != null ? itemHandler : new FabricDefaultHandler( );
        setItemHandler(handler);

        handler.startSchema(schema);
        log.debug("Start handling Schema object tree");

        final FTopLevelObjectList tlo = schema.getTopLevelObjectList( );

        for (final FElement e : tlo.getTopLevelElements( )) {
            handleElement(e);
        }

        handler.endSchema(schema);
        log.debug("Done handling Schema object tree");
    }

    /**
     * Handles an element in the Schema object tree.
     * 
     * @param e The element to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleElement(FElement e) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String elemName = e.getName( );

        if (e.isTopLevel( )) {
            handler.startTopLevelElement(e);
            log.debug("Start handling top-level element '{}'", elemName);
            handleSchemaType(e.getSchemaType( ));
            handler.endTopLevelElement(e);
            log.debug("Done handling top-level element '{}'", elemName);
        } else {
            handler.startLocalElement(e);
            log.debug("Start handling local element '{}'", elemName);
            handleSchemaType(e.getSchemaType( ));
            handler.endLocalElement(e);
            log.debug("Done handling local element '{}'", elemName);
        }
    }

    /**
     * Generic handling method for Schema types.
     * 
     * @param type The type to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleSchemaType(FSchemaType type) throws Exception {
        if (type instanceof FSimpleType) {
            handleSimpleType((FSimpleType)type);
        } else if (type instanceof FComplexType) {
            handleComplexType((FComplexType)type);
        } else {
            throw new Exception("Unknown SchemaType: " + type.toString( ));
        }
    }

    /**
     * Handles a simple type.
     * 
     * @param type The simple type to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleSimpleType(FSimpleType type) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String typeName = type.getName( );

        if (type.isTopLevel( )) {
            handler.startTopLevelSimpleType(type);
            log.debug("Start handling top-level simple type '{}'", typeName);
            // TODO
            handler.endTopLevelSimpleType(type);
            log.debug("Done handling top-level simple type '{}'", typeName);
        } else {
            handler.startLocalSimpleType(type);
            log.debug("Start handling local simple type '{}'", typeName);
            // TODO
            handler.endLocalSimpleType(type);
            log.debug("Done handling local simple type '{}'", typeName);
        }
    }

    /**
     * Handles a complex type.
     * 
     * @param type The complex type to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleComplexType(FComplexType type) throws Exception {
        final FabricSchemaTreeItemHandler handler = getItemHandler( );
        final String typeName = type.getName( );

        if (type.isTopLevel( )) {
            handler.startTopLevelComplexType(type);
            log.debug("Start handling top-level complex type '{}'", typeName);
            handleComplexTypeChildren(type);
            handler.endTopLevelComplexType(type);
            log.debug("Done handling top-level complex type '{}'", typeName);
        } else {
            handler.startLocalComplexType(type);
            log.debug("Start handling local complex type '{}'", typeName);
            handleComplexTypeChildren(type);
            handler.endLocalComplexType(type);
            log.debug("Done handling local complex type '{}'", typeName);
        }
    }

    /**
     * Handles the children of a complex type.
     * 
     * @param parent The complex type whose children are to be handled.
     * @throws Exception If an error occurs while walking the tree.
     */
    private void handleComplexTypeChildren(FComplexType parent) throws Exception {
        for (final FSchemaObject o : parent.getChildObjects( )) {
            if (o instanceof FElement) {
                handleElement((FElement)o);
            } else if (o instanceof FComplexType) {
                handleComplexType((FComplexType)o);
            } else {
                throw new Exception("Unknown child object: " + o.toString( ));
            }
        }
    }
}
