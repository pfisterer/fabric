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
 * actually does nothing.
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
        //
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        //
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
        //
    }

    @Override
    public void endTopLevelElement(FElement element) {
        //
    }

    @Override
    public void startLocalElement(FElement element) throws Exception {
        //
    }

    @Override
    public void endLocalElement(FElement element) {
        //
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type) throws Exception {
        //
    }

    @Override
    public void endTopLevelSimpleType(FSimpleType type) {
        //
    }

    @Override
    public void startLocalSimpleType(FSimpleType type) throws Exception {
        //
    }

    @Override
    public void endLocalSimpleType(FSimpleType type) {
        //
    }

    @Override
    public void startTopLevelComplexType(FComplexType type) throws Exception {
        //
    }

    @Override
    public void endTopLevelComplexType(FComplexType type) {
        //
    }

    @Override
    public void startLocalComplexType(FComplexType type) throws Exception {
        //
    }

    @Override
    public void endLocalComplexType(FComplexType type) {
        //
    }
}
