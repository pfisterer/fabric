package fabric.module.exi.exceptions;

/**
 * Custom exception for the XMLLibraryFactory within the Fabric EXI module.
 * This exception can be thrown, when the developer tries to instanziate a
 * class with the factory method, that is not a valid XMLLibrary implementation.
 *
 * @author seidel
 */
public class UnsupportedXMLLibraryException extends FabricEXIException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedXMLLibraryException()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor for exception.
   *
   * @param message Error message
   */
  public UnsupportedXMLLibraryException(String message)
  {
    super(message);
  }
}
