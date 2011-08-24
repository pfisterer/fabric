package fabric.module.typegen.base;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.module.typegen.AttributeContainer;

/**
 * Interface for class generation strategies. It defines a method
 * to convert AttributeContainer objects to language-specific
 * class objects (e.g. JClass or CppClass).
 *
 * @author seidel
 */
public interface ClassGenerationStrategy
{
  /**
   * Generate language-specific class object from AttributeContainer.
   *
   * @param container AttributeContainer for class creation
   *
   * @return Created class object (WorkspaceElement is base type
   * of both JClass and CppClass)
   *
   * @throws Exception Error during class object creation
   */
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception;

  /**
   * Get list of dependencies that are required to save the class
   * object as a valid source file. Depending on the implementation,
   * the method may return imports (Java) or includes (C++), for
   * example.
   *
   * @return List of required dependencies
   */
  public ArrayList<String> getRequiredDependencies();
}
