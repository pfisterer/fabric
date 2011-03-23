/**
 * 
 */
package fabric.module.dot;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.wsdlschemaparser.schema.FSchema;

/**
 * @author Marco Wegner
 */
public class FabricDotGraphModule implements FabricModule {

    /**
     * Option key used for the Dot graph output file.
     */
    private static final String DOT_OUTFILE = "dot.outfile";

    /**
	 * 
	 */
    public FabricDotGraphModule( ) {
        super( );
    }

    @Override
    public void handle(Workspace workspace, FSchema schema) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName( ) {
        return "dot";
    }

    @Override
    public String getDescription( ) {
        return String.format("Creates a Graphviz DOT file. Valid options are '{0}'.", DOT_OUTFILE);
    }
}
