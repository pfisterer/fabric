package fabric.module.typegen.exceptions;

/**
 * Base class for custom exceptions thrown inside the Fabric TypeGen.
 * 
 * @author seidel
 */
public class FabricTypeGenException extends Exception
{
  /**
   * Default constructor for exception.
   */
  public FabricTypeGenException()
  {
    // Empty implementation
  }
  
  /**
   * Parameterized constructor for exception.
   * 
   * @param message Error message
   */
  public FabricTypeGenException(String message)
  {
    super(message);
  }
}
