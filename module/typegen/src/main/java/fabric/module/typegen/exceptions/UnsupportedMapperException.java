package fabric.module.typegen.exceptions;

/**
 * Custom exception for the MapperFactory within the Fabric TypeGen Module.
 * This exception can be throws, when the developer tries to instanziate a
 * class with the factory method, that is not a valid Mapper implementation.
 * 
 * @author seidel
 */
public class UnsupportedMapperException extends FabricTypeGenException
{
  /**
   * Default constructor for exception.
   */
  public UnsupportedMapperException()
  {
    // Empty implementation
  }
  
  /**
   * Parameterizes constructor for exception.
   * 
   * @param message Error message
   */
  public UnsupportedMapperException(String message)
  {
    super(message);
  }
}
