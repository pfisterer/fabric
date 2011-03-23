/**
 * 
 */
package fabric.module.api;

import de.uniluebeck.sourcegen.Workspace;
import fabric.wsdlschemaparser.schema.FSchema;

/**
 * @author Marco Wegner
 */
public interface FabricModule {

    public abstract String getName( );

    public abstract String getDescription( );

    public abstract void handle(Workspace workspace, FSchema schema);

}
