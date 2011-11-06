package fabric.module.exi.cpp;

import java.util.ArrayList;

import fabric.module.exi.base.EXICodeGen;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * EXI code generator for C++.
 *
 * @author seidel
 */
public class CppEXICodeGen implements EXICodeGen
{
  @Override
  public void generateCode(ArrayList<ElementData> fixElements,
                           ArrayList<ArrayData> fixArrays,
                           ArrayList<ListData> fixLists) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeSourceFile() throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
