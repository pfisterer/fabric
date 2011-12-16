package fabric.core.filegen.cpp;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppSourceFileImpl;
import fabric.core.filegen.base.SourceFileGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Abstract class for generating the expected CppSourceFile objects of a test case.
 */
public abstract class CppSourceFileGenerator extends SourceFileGenerator {
    /**
     * CppClass objects generated for the test XSD
     */
    List<CppClass> types;

    /**
     * Constructor
     */
    public CppSourceFileGenerator(Properties properties) {
        super(properties);
        types = new LinkedList<CppClass>();
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
            for (CppClass type : types) {
                file = new CppSourceFileImpl(type.getName());
                files.add(file.add(type));
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
