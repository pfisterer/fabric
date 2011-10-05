package fabric.module.exi.base;

/**
 * Public interface for EXICodeGen implementations.
 * 
 * @author seidel
 */
public interface EXICodeGen
{
  /**
   * Generate a class with EXI serializer and deserializer code.
   *
   * @throws Exception Error during code generation
   */
  public void generateCode() throws Exception;

  /**
   * Create source file and write it to language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFile() throws Exception;
}
