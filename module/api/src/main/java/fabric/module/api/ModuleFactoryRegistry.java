package fabric.module.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;

public class ModuleFactoryRegistry {
	private Map<String, ModuleFactory> moduleFactories = new HashMap<String, ModuleFactory>();

	public void register(ModuleFactory factory) throws Exception {

		if (moduleFactories.containsKey(factory.getName()))
			throw new Exception("Factory of name " + factory.getName() + " already exists.");

		moduleFactories.put(factory.getName(), factory);

	}

	public Module create(String name, Properties properties, Workspace workspace) throws Exception {
		
		if (!moduleFactories.containsKey(name))
			throw new Exception("Factory of name " + name + " unknown.");

		return moduleFactories.get(name).create(properties, workspace);
	}

	public Map<String, String> getNameAndDescriptions() {
		Map<String, String> nameAndDescription = new HashMap<String, String>();
		
		for(ModuleFactory factory : moduleFactories.values())
			nameAndDescription.put(factory.getName(), factory.getDescription());
			
		return nameAndDescription;
	}
}
