package fabric.module.typegen.cpp;

import java.util.Stack;
import java.util.HashMap;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.FComplexType;

import fabric.module.typegen.base.TypeGen;
import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.AttributeContainer;

/**
 * Type generator for C++.
 *
 * @author seidel
 */
public class CppTypeGen implements TypeGen
{
  // TODO: Sort methods according to JavaTypeGen.java
  
  private Workspace workspace;

  private Properties properties;

  private Mapper mapper;

  private Stack<AttributeContainer.Builder> incompleteBuilders;

  private HashMap<String, CComplexType> generatedElements;

  public CppTypeGen(Workspace workspace, Properties properties) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
  
  @Override
  public void createNewContainer(FComplexType type)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void addMemberVariable(FElement element, boolean topLevel)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void buildCurrentContainer() throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
