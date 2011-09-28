package fabric.module.exi.exceptions;

/**
 * Custom exception for the EXICodeGenFactory within the Fabric EXI module.
 * This exception can be thrown, when the developer tries to instanziate a
 * class with the factory method, that is not a valid EXICodeGen implementation.
 * 
 * @author seidel
 */
public class UnsupportedEXICodeGenException extends FabricEXIException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedEXICodeGenException()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor for exception.
   * 
   * @param message Error message
   */
  public UnsupportedEXICodeGenException(String message)
  {
    super(message);
  }
}
