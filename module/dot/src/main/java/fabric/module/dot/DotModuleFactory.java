package fabric.module.dot;

import java.io.File;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.Module;
import fabric.module.api.ModuleFactory;

public class DotModuleFactory implements ModuleFactory {
	private static final String DOT_OUTFILE = "dot.outfile";

	@Override
	public String getName() {
		return "dot";
	}

	@Override
	public String getDescription() {
		return "Creates a Graphviz DOT file. Options are '" + DOT_OUTFILE + "'";
	}

	@Override
	public Module create(Properties properties, Workspace workspace) throws Exception {
		return new DotModule(new File(properties.getProperty(DOT_OUTFILE, "dotfile.dot")), workspace);
	}
}