package classes.cpp;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppSourceFileImpl;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.cpp.CppClassGenerationStrategy;
import classes.base.SourceFileGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Abstract class for generating the expected CppSourceFile objects of a test case.
 */
public abstract class CppSourceFileGenerator extends SourceFileGenerator {
    /**
     * AttributeContainer objects generated for the test XSD
     */
    List<AttributeContainer> containers;

    /**
     * Constructor
     */
    public CppSourceFileGenerator(CppClassGenerationStrategy strategy, Properties properties) {
        super(strategy, properties);
        containers = new LinkedList<AttributeContainer>();
        generateClasses();
    }

    /**
     * Returns the CppSourceFile objects representing the generated containers.
     *
     * @return CppSourceFile objects in a list
     */
    @Override
    public List<SourceFile> getSourceFiles() {
        List<SourceFile> files = new LinkedList<SourceFile>();
        CppSourceFileImpl file;
        try {
            for (AttributeContainer container : containers) {
                file = new CppSourceFileImpl(container.getName());
                files.add(file.add((CppClass)container.asClassObject(strategy)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Generates the AttributeContainer objects corresponding to the test XSD.
     */
    abstract void generateClasses();
}
