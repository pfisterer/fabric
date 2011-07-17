package fabric.module.typegen.base;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.wsdlschemaparser.schema.FSchemaType;

/**
 * Public interface for TypeGen implementations.
 *
 * @author seidel
 */
public interface TypeGen
{
  /**
   * This method parses an FSchemaType object and generates a corresponding
   * WorkspaceElement object (e.g. JClass or CppClass).
   * 
   * @param type FSchemaType object
   *
   * @return WorkspaceElement object for FSchemaType object
   */
  public WorkspaceElement parseType(FSchemaType type);
}
