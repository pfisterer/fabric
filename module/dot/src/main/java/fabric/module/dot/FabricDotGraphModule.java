/**
 * 
 */
package fabric.module.dot;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;

/**
 * Fabric module used for creating Graphviz dot visualisations of the Schema
 * object tree.
 * 
 * @author Marco Wegner
 */
public class FabricDotGraphModule implements FabricModule {

    /**
     * Option key used for the Dot graph output file.
     */
    private static final String KEY_DOT_OUTFILE = "dot.outfile";

    /**
     * Constructs a new module.
     */
    public FabricDotGraphModule( ) {
        super( );
    }

    @Override
    public String getName( ) {
        return "dot";
    }

    @Override
    public String getDescription( ) {
        return String.format("Creates a Graphviz DOT file. Valid options are '%s'.",
                KEY_DOT_OUTFILE);
    }

    @Override
    public Properties getDefaultProperties( ) {
        final Properties p = new Properties( );
        p.put(KEY_DOT_OUTFILE, "dotfile.dot");
        return p;
    }

    @Override
    public FabricSchemaTreeItemHandler getHandler(Workspace workspace, Properties properties)
            throws Exception {
        return new FabricDotGraphHandler(workspace, properties);
    }
}
