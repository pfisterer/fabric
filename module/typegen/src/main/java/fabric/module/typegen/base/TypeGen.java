package fabric.module.typegen.base;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;

import de.uniluebeck.sourcegen.Workspace;
import java.util.Properties;

/**
 * Public interface for TypeGen implementations.
 *
 * @author reichart, seidel
 */
public interface TypeGen
{
  /**
   * Generates the container corresponding to the root element
   * of the XML schema.
   */
  public void generateRootContainer();

  /**
   * Generates the class in the current workspace corresponding
   * to the root element of the XML schema.
   */
  public void generateSourceFiles(Workspace workspace) throws Exception;

  /**
   * Adds a variable corresponding to the given
   * FSimpleType object to the current container.
   *
   * @param type  Current FSimpleType object
   * @param parent    Parent element of type
   *
   */
  public void addSimpleType(FSimpleType type, FElement parent) throws Exception;

  // TODO: Add comment
  public void addAttribute(FElement element);
  public void generateNewContainer(FSimpleType type);

  /**
   * Generates a new container corresponding to the given
   * FComplexType object.
   *
   * @param type  Current FComplexType object
   */
  public void generateNewContainer(FComplexType type);

  /**
   * Generates a new class corresponding to the last container.
   *
   * @throws Exception
   */
  public void generateNewClass() throws Exception;

  /**
   * Generates a new class corresponding to the last container
   * that extends the class with the given name.
   *
   * @param name  Name of the class to be extended
   * @throws Exception
   */
  public void generateNewExtendedClass(String name) throws Exception;
}
