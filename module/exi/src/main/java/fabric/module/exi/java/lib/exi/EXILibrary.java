package fabric.module.exi.java.lib.exi;

/**
 * Public interface for EXI libraries. Implementing classes
 * generate code, to operate a specific external EXI library.
 *
 * @author seidel
 */
public interface EXILibrary
{
  /**
   * Method that creates code to serialize an XML document with EXI.
   *
   * @return String with code for EXI serialization
   */
  public String generateSerializeCode();

  /**
   * Method that creates code to deserialize an EXI byte stream.
   *
   * @return String with code for EXI deserialization
   */
  public String generateDeserializeCode();
}
