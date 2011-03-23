package fabric.module.protobuf;

import java.io.File;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.Module;
import fabric.module.api.ModuleFactory;

public class ProtobufModuleFactory implements ModuleFactory {
	private static final String PROTOBUF_OUTFILE = "protobuf.outfile";

	@Override
	public String getName() {
		return "protobuf";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public Module create(Properties properties, Workspace workspace) throws Exception {
		return new ProtobufModule(new File(properties.getProperty(PROTOBUF_OUTFILE, "protobuf.prot")), workspace);
	}
}