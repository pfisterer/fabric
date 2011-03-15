package fabric;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.tr.util.Logging;
import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.Module;
import fabric.module.api.ModuleFactoryRegistry;
import fabric.module.dot.DotModuleFactory;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.wsdl.FWSDL;

public class Main {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

	private static Properties defaultProperties = new Properties();
	
	static {
		//Todo: set default properties
		//defaultProperties.put()
	}
	
	public static void main(String[] args) throws Exception {
		Logging.setLoggingDefaults();

		// create the command line parser
		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("x", "xsd", true, "The XML Schema file to load");
		options.addOption("w", "wsdl", true, "The WSDL file to load");
		options.addOption("t", "target", true, "The target system to generate code for");
		options.addOption("p", "properties", true, "The properties file to configure the modules");
		options.addOption("m", "modules", true, "Comma-separated list of modules to run");
		options.addOption("v", "verbose", false, "Verbose logging output");
		options.addOption("h", "help", false, "Help output");

		File wsdlFile = null;
		File schemaFile = null;
		List<Module> modulesToExecute = new LinkedList<Module>();
		Properties properties = new Properties(defaultProperties);
		
		//Register all known module factories
		ModuleFactoryRegistry moduleFactoryRegistry = new ModuleFactoryRegistry();
		moduleFactoryRegistry.register(new DotModuleFactory());

		//Parse the command line
		try {
			CommandLine line = parser.parse(options, args);

			//Check if verbose output should be used
			if (line.hasOption('v')) {
				Logger.getRootLogger().setLevel(Level.DEBUG);
			} else {
				Logger.getRootLogger().setLevel(Level.INFO);
			}

			//Output help and exit
			if (line.hasOption('h')) {
				usage(options, moduleFactoryRegistry);
			}
			//Output help and exit
			if (line.hasOption('h')) {
				usage(options, moduleFactoryRegistry);
			}

			//Load properties from file
			if (line.hasOption('p')) {
				String propertiesFile = line.getOptionValue('p');
				log.debug("Loading properties from {}", propertiesFile);
				properties.load(new FileReader(new File(propertiesFile)));
			}
			
			Workspace workspace = new Workspace(properties);
			
			//Create module instances
			if (line.hasOption('m')) {

				for(String moduleName : line.getOptionValue('m').split(","))
				{
					moduleName = moduleName.trim();
					log.debug("Creating instance of module {}", moduleName);
					modulesToExecute.add(moduleFactoryRegistry.create(moduleName, properties, workspace));
				}
			}

			
			if (line.hasOption('x') && line.hasOption('w')) {
				throw new Exception("Only one of -x or -w is allowed");
			} else if (line.hasOption('x')) {
				schemaFile = new File(line.getOptionValue('x'));
			} else if (line.hasOption('w')) {
				wsdlFile = new File(line.getOptionValue('w'));
			} else {
				throw new Exception("Supply one of -x or -w");
			}

		} catch (Exception e) {
			log.error("Invalid command line: " + e, e);
			usage(options, moduleFactoryRegistry);
		}

		if (wsdlFile != null) {
			FWSDL wsdl = new FWSDL(wsdlFile);
		} else if (schemaFile != null) {
			FSchema schema = new FSchema(schemaFile);
			System.out.println(schema.toString());

			Workspace workspace = new Workspace();

			for( Module module : modulesToExecute )
				module.handle(workspace, schema);
			
			for( Module module : modulesToExecute )
				module.done();
			
			workspace.generate();
			
		}

	}

	private static void usage(Options options, ModuleFactoryRegistry moduleFactoryRegistry) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(120, Main.class.getCanonicalName(), null, options, null);
		
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("Available modules: ");
		System.out.println("-----------------------------");
		for(Entry<String, String> nameAndDescription : moduleFactoryRegistry.getNameAndDescriptions().entrySet() )
			System.out.println("Module["+nameAndDescription.getKey()+"]: " + nameAndDescription.getValue());
		
		System.exit(1);
	}
}
