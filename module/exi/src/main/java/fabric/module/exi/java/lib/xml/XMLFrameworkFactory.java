package fabric.module.exi.java.lib.xml;

import java.util.Properties;
import java.lang.reflect.Constructor;

import fabric.module.exi.exceptions.UnsupportedXMLFrameworkException;

/**
 * Abstract factory for creation of concrete XMLFramework objects.
 *
 * @author seidel
 */
public class XMLFrameworkFactory
{
  /** Factory instance for Singleton implementation */
  private static XMLFrameworkFactory instance;

  /**
   * Private constructor for Singleton implementation.
   */
  private XMLFrameworkFactory()
  {
    // Empty implementation
  }

  /**
   * Method for factory access. We either instantiate a new
   * object here or use the existing instance.
   *
   * @return XMLFrameworkFactory object
   */
  public static synchronized XMLFrameworkFactory getInstance()
  {
    // Instance does not exist, create a new one
    if (null == instance)
    {
      instance = new XMLFrameworkFactory();
    }

    return instance;
  }

  /**
   * Factory method for XMLFramework creation. This method
   * takes the name of a concrete XMLFramework implementation
   * and tries to instantiate an object of this class.
   * On success, the object is returned, otherwise we
   * throw an exception.
   *
   * @param concreteXMLFrameworkName Name of concrete XMLFramework class
   * @param beanClassName Name of the main Java bean class
   *
   * @return XMLFramework object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public XMLFramework createXMLFramework(String concreteXMLFrameworkName, String beanClassName) throws Exception
  {
    XMLFramework concreteXMLFramework = null;

    try
    {
      // Try to instantiate concrete XMLFramework class
      Class concreteClass = Class.forName(concreteXMLFrameworkName);
      Class[] argumentsClass = new Class[] { String.class };

      Constructor constructor = concreteClass.getConstructor(argumentsClass);
      Object[] arguments = new Object[] { beanClassName };

      concreteXMLFramework = (XMLFramework)constructor.newInstance(arguments);
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(String.format("No class found with name '%s'.", concreteXMLFrameworkName));
    }
    catch (InstantiationException e)
    {
      throw new Exception(String.format("Could not instantiate class with name '%s'.", concreteXMLFrameworkName));
    }
    catch (IllegalAccessException e)
    {
      throw new Exception(String.format("Illegal access to class '%s'.", concreteXMLFrameworkName));
    }
    catch (ClassCastException e)
    {
      throw new UnsupportedXMLFrameworkException(String.format("Class '%s' is not a valid XMLFramework implementation.", concreteXMLFrameworkName));
    }

    return concreteXMLFramework;
  }
}
