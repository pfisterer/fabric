/**
 * 
 */
package fabric.module.dot;

import java.util.Properties;

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
        return String.format("Creates a Graphviz DOT file. Valid options are '{0}'.",
                KEY_DOT_OUTFILE);
    }

    @Override
    public Properties getDefaultProperties( ) {
        // TODO actually add any default properties
        return new Properties( );
    }

    @Override
    public FabricSchemaTreeItemHandler getHandler(Properties p) throws Exception {
        return new FabricDotGraphHandler(p.getProperty(KEY_DOT_OUTFILE));
    }
}
