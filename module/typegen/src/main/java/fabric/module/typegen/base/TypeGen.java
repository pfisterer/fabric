package fabric.module.typegen.base;

import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.FComplexType;

/**
 * Public interface for TypeGen implementations.
 *
 * @author seidel
 */
public interface TypeGen
{
  /**
   * Create root container, which corresponds to the top-level
   * XML schema document.
   */
  public void createRootContainer();

  /**
   * Create source files from container classes and
   * write them to the language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFiles() throws Exception;

  /**
   * Create a new container class that represents a simple type
   * of the XML schema document. All elements and attributes that
   * belong to this type should be added to the container as
   * member variables.
   * 
   * @param type FSimpleType object
   */
  public void createNewContainer(FSimpleType type);

  /**
   * Create a new container class that represents a complex type
   * of the XML schema document. All elements and attributes that
   * belong to this type should be added to the container as
   * member variables.
   *
   * @param type FComplexType object
   */
  public void createNewContainer(FComplexType type);

  /**
   * Add a member variable to the current container class.
   * Type, name, initial value and restrictions of the
   * element will be mapped to the specific target language,
   * where applicable.
   *
   * @param element FElement object
   * @param isTopLevel True if the element is a top-level element
   * or part of a top-level complex type; false if the element is
   * part of a local complex type
   */
  public void addMemberVariable(FElement element, boolean isTopLevel);

  /**
   * Finish the construction of the current container class by
   * building it. As soon as a container is built, no more new
   * member variables can be added to it. This function is usually
   * called, when the closing XML tag of a type definition is reached.
   *
   * @throws Exception Error while building container
   */
  public void buildCurrentContainer() throws Exception;
}
