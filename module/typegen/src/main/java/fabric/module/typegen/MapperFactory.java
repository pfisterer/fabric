package fabric.module.typegen;

import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.exceptions.UnsupportedMapperException;

/**
 * Abstract factory for creation of concrete Mapper objects.
 *
 * @author seidel
 */
public class MapperFactory
{
  /** Factory instance for Singleton implementation */
  private static MapperFactory instance;

  /**
   * Private constructor for Singleton implementation.
   */
  private MapperFactory()
  {
    // Empty implementation
  }

  /**
   * Method for factory access. We either instantiate a new
   * object here or use the existing instance.
   *
   * @return MapperFactory object
   */
  public static synchronized MapperFactory getInstance()
  {
    // Instance does not exist, create a new one
    if (null == instance)
    {
      instance = new MapperFactory();
    }

    return instance;
  }

  /**
   * Factory method for Mapper creation. This method
   * takes the name of a concrete Mapper implementation
   * and tries to instantiate an object of this class.
   * On success, the object is returned, otherwise we
   * throw an exception.
   *
   * @param concreteMapperName Name of concrete Mapper class
   *
   * @return Mapper object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public Mapper createMapper(String concreteMapperName) throws Exception
  {
    Mapper concreteMapper = null;

    try
    {
      // Try to instantiate concrete Mapper class
      Class concreteClass = Class.forName(concreteMapperName);
      concreteMapper = (Mapper)concreteClass.newInstance();
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(String.format("No class found with name '%s'.", concreteMapperName));
    }
    catch (InstantiationException e)
    {
      throw new Exception(String.format("Could not instantiate class with name '%s'.", concreteMapperName));
    }
    catch (IllegalAccessException e)
    {
      throw new Exception(String.format("Illegal access to class '%s'.", concreteMapperName));
    }
    catch (ClassCastException e)
    {
      throw new UnsupportedMapperException(String.format("Class '%s' is not a valid Mapper implementation.", concreteMapperName));
    }
    
    return concreteMapper;
  }
}
