package fabric.module.exi.java.lib.exi;

import java.lang.reflect.Constructor;

import fabric.module.exi.exceptions.UnsupportedEXILibraryException;

/**
 * Abstract factory for creation of concrete EXILibrary objects.
 *
 * @author seidel
 */
public class EXILibraryFactory
{
  /** Factory instance for Singleton implementation */
  private static EXILibraryFactory instance;

  /**
   * Private constructor for Singleton implementation.
   */
  private EXILibraryFactory()
  {
    // Empty implementation
  }

  /**
   * Method for factory access. We either instantiate a new
   * object here or use the existing instance.
   *
   * @return EXILibraryFactory object
   */
  public static synchronized EXILibraryFactory getInstance()
  {
    // Instance does not exist, create a new one
    if (null == instance)
    {
      instance = new EXILibraryFactory();
    }

    return instance;
  }

  /**
   * Factory method for EXILibrary creation. This method
   * takes the name of a concrete EXILibrary implementation
   * and tries to instantiate an object of this class.
   * On success, the object is returned, otherwise we
   * throw an exception.
   *
   * @param concreteEXILibraryName Name of concrete EXILibrary class
   * @param beanClassName Name of the main Java bean class
   * 
   * @return EXILibrary object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public EXILibrary createEXILibrary(String concreteEXILibraryName, String beanClassName) throws Exception
  {
    EXILibrary concreteEXILibrary = null;

    try
    {
      // Try to instantiate concrete EXILibrary class
      Class concreteClass = Class.forName(concreteEXILibraryName);
      Class[] argumentsClass = new Class[] { String.class };

      Constructor constructor = concreteClass.getConstructor(argumentsClass);
      Object[] arguments = new Object[] { beanClassName };

      concreteEXILibrary = (EXILibrary)constructor.newInstance(arguments);
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(String.format("No class found with name '%s'.", concreteEXILibraryName));
    }
    catch (InstantiationException e)
    {
      throw new Exception(String.format("Could not instantiate class with name '%s'.", concreteEXILibraryName));
    }
    catch (IllegalAccessException e)
    {
      throw new Exception(String.format("Illegal access to class '%s'.", concreteEXILibraryName));
    }
    catch (ClassCastException e)
    {
      throw new UnsupportedEXILibraryException(String.format("Class '%s' is not a valid EXILibrary implementation.", concreteEXILibraryName));
    }

    return concreteEXILibrary;
  }
}
