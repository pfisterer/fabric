package fabric.module.typegen.base;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.module.typegen.AttributeContainer;

/**
 * @author seidel
 */
public interface ClassGenerationStrategy
{
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception;
}
