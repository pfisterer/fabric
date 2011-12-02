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
	    new CRectangleSimple(workspace);
	    new TwoClassesPerFile(workspace);
	    new Nested(workspace);
	    new NestedOfNested(workspace);
	    // ...

	    workspace.generate();
	}

	public static void main(String[] args) throws Exception {
		new Main();
	}

}
