package fabric.module.exi.exceptions;

/**
 * Custom exception for the EXILibraryFactory within the Fabric EXI module.
 * This exception can be thrown, when the developer tries to instanziate a
 * class with the factory method, that is not a valid EXILibrary implementation.
 *
 * @author seidel
 */
public class UnsupportedEXILibraryException extends FabricEXIException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedEXILibraryException()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor for exception.
   *
   * @param message Error message
   */
  public UnsupportedEXILibraryException(String message)
  {
    super(message);
  }
}
