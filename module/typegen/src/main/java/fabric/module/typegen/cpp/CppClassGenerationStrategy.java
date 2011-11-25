package fabric.module.typegen.cpp;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.WorkspaceElement;

import de.uniluebeck.sourcegen.c.CppClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.base.ClassGenerationStrategy;

public class CppClassGenerationStrategy implements ClassGenerationStrategy
{
  @Override
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception
  {
    return CppClass.factory.create(container.getName(), null); // TODO
  }
  
  @Override
  public ArrayList<String> getRequiredDependencies()
  {
    return new ArrayList<String>(); // TODO
  }
}
