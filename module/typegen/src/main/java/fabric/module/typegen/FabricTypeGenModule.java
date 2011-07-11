package fabric.module.typegen;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;

public class FabricTypeGenModule  implements FabricModule {
    private static final String TYPEGEN_OUTFILE = "helloworld.outfile";
    
    private Properties properties = null;
    
    public FabricTypeGenModule(Properties p) {
        this.properties = p;
        this.properties.put(TYPEGEN_OUTFILE, "TypeGen"); // No file extension!
    }
	
	@Override
	public String getName() {
		return "typegen";
	}

	@Override
	public String getDescription() {
        return String.format("TypeGen module for generation of XML abstractions. Valid options are '%s'.", TYPEGEN_OUTFILE);
	}

	@Override
	public FabricSchemaTreeItemHandler getHandler(Workspace workspace) throws Exception {
        return new FabricTypeGenHandler(workspace, this.properties);
	}

}
