package fabric.module.typegen;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import fabric.module.api.FabricDefaultHandler;

public class FabricTypeGenHandler extends FabricDefaultHandler {
	private TypeGenFactory typeGenFactory;
	
    private final JSourceFileImpl typeGenSource;
    
    public FabricTypeGenHandler(Workspace workspace, Properties properties) {
    	this.typeGenSource = workspace.getHelloWorldHelper( ).getDefaultSourceFile( );
    }

}
