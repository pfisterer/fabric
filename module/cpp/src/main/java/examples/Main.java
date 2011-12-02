package examples;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;

/**
 * THis is a stand alone application to show different code generations
 *
 * @author Dennis Boldt
 *
 */
public class Main {

	public Main() throws Exception {
		Workspace workspace = new Workspace(new Properties());

		// Generate different classes
	    new Example1_Simple(workspace);
	    new Example2_TwoClassesPerFile(workspace);
	    new Example3_Nested(workspace);
	    new Example4_NestedOfNested(workspace);

	    workspace.generate();
	}

	public static void main(String[] args) throws Exception {
		new Main();
	}

}
