package fabric.module.typegen.exceptions;

/**
 * Custom exception for the TypeGenFactory within the Fabric TypeGen Module.
 * This exception can be throws, when the developer tries to instanziate a
 * class with the factory method, that is not a valid TypeGen implementation.
 * 
 * @author seidel
 */
public class UnsupportedTypeGenException extends FabricTypeGenException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedTypeGenException()
  {
    // Empty implementation
  }
  
  /**
   * Parameterizes constructor for exception.
   * 
   * @param message Error message
   */
  public UnsupportedTypeGenException(String message)
  {
    super(message);
  }
}
