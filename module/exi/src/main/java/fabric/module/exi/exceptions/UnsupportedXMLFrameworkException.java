package fabric.module.exi.exceptions;

/**
 * Custom exception for the XMLFrameworkFactory within the Fabric EXI module.
 * This exception can be thrown, when the developer tries to instanziate a
 * class with the factory method, that is not a valid XMLFramework implementation.
 *
 * @author seidel
 */
public class UnsupportedXMLFrameworkException extends FabricEXIException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedXMLFrameworkException()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor for exception.
   *
   * @param message Error message
   */
  public UnsupportedXMLFrameworkException(String message)
  {
    super(message);
  }
}
