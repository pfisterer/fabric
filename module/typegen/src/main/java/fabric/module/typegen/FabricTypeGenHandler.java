package fabric.module.typegen;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

/**
 * Fabric handler class for the type generator module. This class
 * defines a couple of callback methods which get called by the
 * treewalker while processing the XSD file. The FabricTypeGenHandler
 * acts upon those function calls and generates corresponding type
 * classes in the workspace for a specific programming language.
 *
 * @author seidel
 */
public class FabricTypeGenHandler extends FabricDefaultHandler
{
  /** TypeGenFactory object for TypeGen access */
  private TypeGenFactory typeGenFactory;

  /**
   * Constructor initializes internal class properties.
   *
   * @param workspace Workspace for file output
   * @param properties Properties object with various options
   */
  public FabricTypeGenHandler(Workspace workspace, Properties properties)
  {
    // TODO Implement constructor
  }
}
