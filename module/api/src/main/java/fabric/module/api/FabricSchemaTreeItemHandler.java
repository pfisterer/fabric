/**
 * 
 */
package fabric.module.api;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * @author Marco Wegner
 */
public interface FabricSchemaTreeItemHandler {

    /**
     * @param schema
     * @throws Exception
     */
    public abstract void startSchema(FSchema schema) throws Exception;

    /**
     * @param schema
     * @throws Exception
     */
    public abstract void endSchema(FSchema schema) throws Exception;

    /**
     * @param element
     * @throws Exception
     */
    public abstract void startTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @throws Exception
     */
    public abstract void endTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @throws Exception
     */
    public abstract void startLocalElement(FElement element) throws Exception;

    /**
     * @param element
     * @throws Exception
     */
    public abstract void endLocalElement(FElement element) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void startTopLevelSimpleType(FSimpleType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void endTopLevelSimpleType(FSimpleType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void startLocalSimpleType(FSimpleType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void endLocalSimpleType(FSimpleType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void startTopLevelComplexType(FComplexType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void endTopLevelComplexType(FComplexType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void startLocalComplexType(FComplexType type) throws Exception;

    /**
     * @param type
     * @throws Exception
     */
    public abstract void endLocalComplexType(FComplexType type) throws Exception;

}
