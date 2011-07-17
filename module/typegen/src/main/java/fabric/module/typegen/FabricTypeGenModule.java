package fabric.module.typegen;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;

/**
 * Fabric module for type generation. This class uses the input
 * from the treewalker to convert all datatypes of an XSD file
 * to their equivalents in a specific programming language (e.g.
 * Java or C++).
 *
 * @author seidel
 */
public class FabricTypeGenModule implements FabricModule
{
  /** Key for output filename in the properties object */
  private static final String TYPEGEN_OUTFILE_KEY = "typegen.outfile";

  /** Properties object for module options */
  private Properties properties = null;

  /**
   * Constructor initializes the internal properties object
   * and adds module-specific options.
   *
   * @param p Properties object with module options
   */
  public FabricTypeGenModule(Properties p)
  {
    this.properties = p;
    this.properties.put(TYPEGEN_OUTFILE_KEY, "TypeGen"); // No file extension!
  }

  /**
   * Helper method to return module name.
   *
   * @return Module name
   */
  @Override
  public String getName()
  {
    return "typegen";
  }

  /**
   * Helper method to return module description.
   *
   * @return Module description
   */
  @Override
  public String getDescription()
  {
    return String.format("TypeGen module for generation of XML abstractions. Valid options are '%s'.", TYPEGEN_OUTFILE_KEY);
  }

  /**
   * This method returns a Fabric handler object for the type generator
   * module. It is instantiated with the current workspace and module
   * options. The FabricTypeGenHandler on its turn is used for XSD file
   * processing and the actual, language-specific type class generation.
   *
   * @param workspace Workspace object for type class output
   *
   * @return FabricTypeGenHandler object
   *
   * @throws Exception Error during handler instantiation
   */
  @Override
  public FabricSchemaTreeItemHandler getHandler(Workspace workspace) throws Exception
  {
    return new FabricTypeGenHandler(workspace, this.properties);
  }
}
