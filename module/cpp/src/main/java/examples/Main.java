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

		Properties properties = new Properties();
		//properties.put("output_directory", "/home/dennis/Desktop/cpp");
		Workspace workspace = new Workspace(properties);

		// Generate different classes
	    new Example1_Simple(workspace);
	    new Example2_TwoClassesPerFile(workspace);
	    new Example3_Nested(workspace);
	    new Example4_NestedOfNested(workspace);
	    new Example5_Constructor_Destructor(workspace);
	    new Example6_Struct(workspace);

	    workspace.generate();
	}

	public static void main(String[] args) throws Exception {
		new Main();
	}

}
