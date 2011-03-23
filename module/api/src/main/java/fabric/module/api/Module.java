package fabric.module.api;

import de.uniluebeck.sourcegen.Workspace;
import fabric.wsdlschemaparser.schema.FSchema;

public interface Module {

	void handle(Workspace workspace, FSchema schema) throws Exception;
	
	void done() throws Exception;
}
