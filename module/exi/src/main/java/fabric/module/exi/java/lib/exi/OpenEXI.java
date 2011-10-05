package fabric.module.exi.java.lib.exi;

/**
 * EXI converter class for the OpenEXI library. This class
 * provides means to create code that de-/serializes XML
 * documents with EXI.
 *
 * @author seidel
 */
public class OpenEXI implements EXILibrary
{
  /**
   * This method generates code to serialize a plain
   * XML document with EXI.
   *
   * @return String with code for EXI serialization
   */
  @Override
  public String generateSerializeCode()
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * This method generates code to deserialize an EXI stream.
   *
   * @return String with code for EXI deserialization
   */
  @Override
  public String generateDeserializeCode()
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
