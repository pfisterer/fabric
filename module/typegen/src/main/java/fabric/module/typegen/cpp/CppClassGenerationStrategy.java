package fabric.module.typegen.cpp;

import de.uniluebeck.sourcegen.WorkspaceElement;
import de.uniluebeck.sourcegen.java.*;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.MemberVariable;
import fabric.module.typegen.base.ClassGenerationStrategy;
import fabric.module.typegen.exceptions.FabricTypeGenException;
import fabric.module.typegen.java.AnnotationMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CppClassGenerationStrategy implements ClassGenerationStrategy
{

    /**
     * Generate language-specific class object from AttributeContainer.
     *
     * @param container AttributeContainer for class creation
     * @return Created class object (WorkspaceElement is base type
     *         of both JClass and CppClass)
     * @throws Exception Error during class object creation
     */
    @Override public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Generate language-specific class object from AttributeContainer with given modifiers.
     *
     * @param container AttributeContainer for class creation
     * @param modifiers modifiers for language-specific class object
     * @return Created class object (WorkspaceElement is base type
     *         of both JClass and CppClass)
     * @throws Exception Error during class object creation
     */
    @Override public WorkspaceElement generateClassObject(AttributeContainer container, int modifiers)
            throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get list of dependencies that are required to save the class
     * object as a valid source file. Depending on the implementation,
     * the method may return imports (Java) or includes (C++), for
     * example.
     *
     * @return List of required dependencies
     */
    @Override public ArrayList<String> getRequiredDependencies() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
