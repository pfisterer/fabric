package fabric.module.exi.base;

import java.util.ArrayList;

import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

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
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixSimpleLists XML lists with simple-typed items,
   * where value-tags need to be fixed
   * @param fixNonSimpleLists XML lists with non-simple-typed items,
   * where value-tags need to be fixed
   *
   * @throws Exception Error during code generation
   */
  public void generateCode(final ArrayList<ElementData> fixElements,
                           final ArrayList<ArrayData> fixArrays,
                           final ArrayList<SimpleListData> fixSimpleLists,
                           final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception;

  /**
   * Create source file and write it to language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFile() throws Exception;
}
