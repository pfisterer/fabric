/**
 * 
 */
package fabric.module.api;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Default handler class for XML Schema object trees. This implementation
 * actually doesn't do anything.
 * 
 * @author Marco Wegner
 */
public class FabricDefaultHandler implements FabricSchemaTreeItemHandler {

    /**
     * Constructs a new handler for a tree of objects parsed from an XML Schema
     * document.
     */
    public FabricDefaultHandler( ) {
        super( );
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        // doesn't do anything
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelElement(FElement element) {
        // doesn't do anything
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalElement(FElement element, FComplexType parent) {
        // doesn't do anything
    }

    @Override
    public void startElementReference(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endElementReference(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelSimpleType(FSimpleType type, FElement parent) {
        // doesn't do anything
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalSimpleType(FSimpleType type, FElement parent) {
        // doesn't do anything
    }

    @Override
    public void startTopLevelComplexType(FComplexType type) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelComplexType(FComplexType type) {
        // doesn't do anything
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalComplexType(FComplexType type, FElement parent) {
        // doesn't do anything
    }
}
