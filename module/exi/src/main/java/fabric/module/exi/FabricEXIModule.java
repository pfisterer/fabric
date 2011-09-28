package fabric.module.exi;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;

/**
 * TODO: description!
 */
public class FabricEXIModule implements FabricModule
{

    /**
     * Returns this module's name.
     *
     * @return The module name.
     */
    @Override public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns this module's description.
     *
     * @return The module description.
     */
    @Override public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Creates and returns the handler for this module. That handler is used when walking a Schema object tree.
     *
     * @param workspace  TODO
     * @return The Schema tree item handler.
     * @throws Exception If an error occurs.
     */
    @Override public FabricSchemaTreeItemHandler getHandler(Workspace workspace) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
