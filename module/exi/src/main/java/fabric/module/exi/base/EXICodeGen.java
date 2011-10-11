package fabric.module.exi.base;

import java.util.ArrayList;

/**
 * Public interface for EXICodeGen implementations.
 * 
 * @author seidel
 */
public interface EXICodeGen
{
  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * 
   * @param fixElements Elements where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  public void generateCode(final ArrayList<String> fixElements) throws Exception;

  /**
   * Create source file and write it to language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFile() throws Exception;
}
