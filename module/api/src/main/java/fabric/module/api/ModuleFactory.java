package fabric.module.api;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;

public interface ModuleFactory {
	
	String getName();
	
	String getDescription();
	
	Module create(Properties properties, Workspace workspace) throws Exception;
}
