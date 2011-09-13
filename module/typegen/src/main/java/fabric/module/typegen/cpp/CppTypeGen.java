package fabric.module.typegen.cpp;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;

import fabric.module.typegen.base.TypeGen;
import fabric.module.typegen.MapperFactory;

/**
 * Type generator for C++.
 *
 * @author seidel
 */
public class CppTypeGen implements TypeGen
{
  // TODO: Sort methods according to JavaTypeGen.java
  private MapperFactory mapperFactory;

  public CppTypeGen(Workspace workspace, Properties properties) throws Exception
  {
  }

  @Override
  public void createRootContainer()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeSourceFiles() throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void createNewContainer(FSimpleType type)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

//  @Override
//  public void createNewContainer(FComplexType type)
//  {
//    throw new UnsupportedOperationException("Not supported yet.");
//  }

  @Override
  public void addMemberVariable(FElement element)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void buildCurrentContainer() throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
