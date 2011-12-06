package fabric.module.typegen.cpp;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.c.CPreProcessorDirective;
import de.uniluebeck.sourcegen.c.Cpp;

import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppVar;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.MemberVariable;
import fabric.module.typegen.base.ClassGenerationStrategy;
import java.util.Iterator;
import java.util.Map;

public class CppClassGenerationStrategy implements ClassGenerationStrategy
{
  @Override
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception
  {
    CppClass classObject = CppClass.factory.create(container.getName(), null); // TODO

    CppVar variable = CppVar.factory.create(Cpp.CHAR, "test");

    classObject.add(Cpp.PUBLIC, variable); // TODO: Remove
    classObject.add(Cpp.PUBLIC, CppFun.factory.create(classObject, "void", "init", variable)); // TODO: Remove

    classObject.addBeforeDirective(CPreProcessorDirective.factory.create(true, "ifdef TEST_HPP"));
    classObject.addBeforeDirective(CPreProcessorDirective.factory.create(true, "define TEST_HPP"));
    classObject.addAfterDirective(CPreProcessorDirective.factory.create(true, "endif"));

    Iterator iterator = container.getMembers().entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry<String, MemberVariable> item = (Map.Entry)iterator.next();
      MemberVariable member = item.getValue();

      if (member.getClass() == AttributeContainer.Element.class || member.getClass() == AttributeContainer.Attribute.class)
      {
        variable = CppVar.factory.create(member.type + " " + member.name);
        classObject.add(Cpp.PUBLIC, variable);
      }
    }

    System.out.println(classObject.toString()); // TODO: Remove

    return classObject;
  }

  @Override
  public ArrayList<String> getRequiredDependencies()
  {
    ArrayList<String> result = new ArrayList<String>();
    result.add("test1.hpp");
    result.add("test2.hpp");

    return result;
  }
}
