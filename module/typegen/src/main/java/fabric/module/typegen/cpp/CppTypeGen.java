package fabric.module.typegen.cpp;

import java.util.Properties;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;

import de.uniluebeck.sourcegen.Workspace;

import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.TypeGen;

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
  public void generateRootContainer()
  {
  }

  @Override
  public void generateSourceFiles(Workspace workspace)
  {
  }

  @Override
  public void addSimpleType(FSimpleType type, FElement parent)
  {
  }

  @Override
  public void generateNewContainer(FComplexType type)
  {
  }

  @Override
  public void generateNewClass() throws Exception
  {
  }

  @Override
  public void generateNewContainer(FSimpleType type)
  {
  }

  @Override
  public void addAttribute(FElement element)
  {
  }
}
