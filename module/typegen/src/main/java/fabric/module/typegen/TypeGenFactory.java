package fabric.module.typegen;

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
  public synchronized TypeGenFactory getInstance()
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
   *
   * @return TypeGen object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public TypeGen createTypeGen(String concreteTypeGenName) throws Exception
  {
    TypeGen concreteTypeGen = null;

    try
    {
      // Try to instantiate concrete TypeGen class
      Class concreteClass = Class.forName(concreteTypeGenName);
      concreteTypeGen = (TypeGen)concreteClass.newInstance();
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

    return concreteTypeGen;
  }
}
