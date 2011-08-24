package fabric.module.typegen.exceptions;

/**
 * Custom exception for the AnnotationMapper within the Fabric TypeGen Module.
 * This exception can be thrown, when the developer tries to request an XML
 * framework in the AnnotationMapper, that is not supported.
 *
 * @author seidel
 */
public class UnsupportedXMLFrameworkException extends FabricTypeGenException
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
