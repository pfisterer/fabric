/** 11.10.2011 14:12 */
package fabric.module.exi.java.lib.exi;

/**
 * EXI converter class for the OpenEXI library. This class
 * provides means to create code that de-/serializes XML
 * documents with EXI.
 *
 * @author seidel
 */
public class OpenEXI extends EXILibrary
{
  /**
   * Parameterized constructor.
   *    
   * @param xsdDocumentPath Path to the input XSD document
   *
   * @throws Exception Error during code generation
   */
  public OpenEXI(final String xsdDocumentPath) throws Exception
  {
    super(xsdDocumentPath);
  }

  /**
   * This method generates code to serialize a plain
   * XML document with EXI.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateSerializeCode() throws Exception
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * This method generates code to deserialize an EXI stream.
   * 
   * @throws Exception Error during code generation
   */
  @Override
  public void generateDeserializeCode() throws Exception
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
