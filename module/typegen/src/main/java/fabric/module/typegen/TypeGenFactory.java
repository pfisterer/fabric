package fabric.module.typegen;

import java.util.Properties;
import java.lang.reflect.Constructor;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.typegen.base.TypeGen;
import fabric.module.typegen.exceptions.UnsupportedTypeGenException;

/**
 * Abstract factory for creation of concrete TypeGen objects.
 *
 * @author seidel
 */
public class TypeGenFactory
{
  /** Factory instance for Singleton implementation */
  private static TypeGenFactory instance;

  /**
   * Private constructor for Singleton implementation.
   */
  private TypeGenFactory()
  {
    // Empty implementation
  }

  /**
   * Method for factory access. We either instantiate a new
   * object here or use the existing instance.
   *
   * @return TypeGenFactory object
   */
  public static synchronized TypeGenFactory getInstance()
  {
    // Instance does not exist, create a new one
    if (null == instance)
    {
      instance = new TypeGenFactory();
    }

    return instance;
  }

  /**
   * Factory method for TypeGen creation. This method
   * takes the name of a concrete TypeGen implementation
   * and tries to instantiate an object of this class.
   * On success, the object is returned, otherwise we
   * throw an exception.
   *
   * @param concreteTypeGenName Name of concrete TypeGen class
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   *
   * @return TypeGen object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public TypeGen createTypeGen(String concreteTypeGenName, Workspace workspace, Properties properties) throws Exception
  {
    TypeGen concreteTypeGen = null;

    try
    {
      // Try to instantiate concrete TypeGen class     
      Class concreteClass = Class.forName(concreteTypeGenName);
      Class[] argumentsClass = new Class[] { Workspace.class, Properties.class };

      Constructor constructor = concreteClass.getConstructor(argumentsClass);
      Object[] arguments = new Object[] { workspace, properties };

      concreteTypeGen = (TypeGen)constructor.newInstance(arguments);
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(String.format("No class found with name '%s'.", concreteTypeGenName));
    }
    catch (InstantiationException e)
    {
      throw new Exception(String.format("Could not instantiate class with name '%s'.", concreteTypeGenName));
    }
    catch (IllegalAccessException e)
    {
      throw new Exception(String.format("Illegal access to class '%s'.", concreteTypeGenName));
    }
    catch (ClassCastException e)
    {
      throw new UnsupportedTypeGenException(String.format("Class '%s' is not a valid TypeGen implementation.", concreteTypeGenName));
    }

    return concreteTypeGen;
  }
}
