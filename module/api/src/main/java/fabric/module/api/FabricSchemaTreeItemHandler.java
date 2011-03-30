/**
 * 
 */
package fabric.module.api;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Handler interface for specific items of the Schema object tree. This handler
 * supplies callback methods which are called when encountering specific Schema
 * object tree items during a Schema tree-walk.
 * 
 * @author Marco Wegner
 */
public interface FabricSchemaTreeItemHandler {

    /**
     * Signifies that walking the Schema object tree is about to start.
     * 
     * @param schema The root element of the Schema object tree to be walked.
     * @throws Exception If an error occurs.
     */
    public abstract void startSchema(FSchema schema) throws Exception;

    /**
     * Signifies that walking the Schema object tree has just finished.
     * 
     * @param schema The root element of the recently walked Schema object tree.
     * @throws Exception If an error occurs.
     */
    public abstract void endSchema(FSchema schema) throws Exception;

    /**
     * @param element
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalElement(FElement element, FComplexType parent) throws Exception;

    /**
     * @param element
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalElement(FElement element, FComplexType parent) throws Exception;

    /**
     * Signifies that handling the specified element reference is about to
     * start.
     * 
     * @param element The element reference to be handled.
     * @throws Exception If an error occurs.
     */
    public abstract void startElementReference(FElement element) throws Exception;

    /**
     * Signifies that handling the specified element reference has just
     * finished.
     * 
     * @param element The recently handled element reference.
     * @throws Exception If an error occurs.
     */
    public abstract void endElementReference(FElement element) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalComplexType(FComplexType type, FElement parent) throws Exception;

}
