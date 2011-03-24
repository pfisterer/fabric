/**
 * 
 */
package fabric.module.api;

import java.util.Properties;

/**
 * <p>
 * Basic module interface in Fabric.
 * </p>
 * <p>
 * A module has a name and description as well and can supply default values for
 * any properties which are used to customise the behaviour of this module.
 * </p>
 * 
 * @author Marco Wegner
 */
public interface FabricModule {

    /**
     * Returns this module's name.
     * 
     * @return The module name.
     */
    public abstract String getName( );

    /**
     * Returns this module's description.
     * 
     * @return The module description.
     */
    public abstract String getDescription( );

    /**
     * Returns this module's default properties.
     * 
     * @return The module properties.
     */
    public abstract Properties getDefaultProperties( );

    /**
     * Creates and returns the handler for this module. That handler is used
     * when walking a Schema object tree.
     * 
     * @param properties The properties used to set up the handler.
     * @return The Schema tree item handler.
     * @throws Exception If an error occurs.
     */
    public abstract FabricSchemaTreeItemHandler getHandler(Properties properties) throws Exception;

}
