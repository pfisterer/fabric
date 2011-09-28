package fabric.module.exi.exceptions;

/**
 * Base class for custom exceptions thrown inside the Fabric EXI module.
 * 
 * @author seidel
 */
public class FabricEXIException extends Exception
{
  /**
   * Default constructor for exception.
   */
  public FabricEXIException()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor for exception.
   * 
   * @param message Error message
   */
  public FabricEXIException(String message)
  {
    super(message);
  }
}
