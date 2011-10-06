package classes.java;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JSourceFileImpl;
import classes.base.SourceFileGenerator;

import java.util.*;

/**
 * Abstract class for generating the expected JSourceFile objects of a test case.
 */
public abstract class JSourceFileGenerator extends SourceFileGenerator {

    /**
     * JComplexType objects (JClass, JEnum, ...) generated for the test XSD with its required imports as a string list
     */
    HashMap<JComplexType, ArrayList<String>> types;

    /**
     * Package name
     */
    String packageName;

    /**
     * XML Framework
     */
    String xmlFramework;

    /**
     * Constructor
     */
    public JSourceFileGenerator(Properties properties) {
        super(properties);
        try {
            packageName     = properties.getProperty("typegen.java.package_name");
            xmlFramework    = properties.getProperty("typegen.java.xml_framework");
            types           = new HashMap<JComplexType, ArrayList<String>>();
            generateClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the JSourceFile objects representing the generated types.
     *
     * @return JSourceFile objects in a list
     */
    @Override
    public List<SourceFile> getSourceFiles() {
        List<SourceFile> files = new LinkedList<SourceFile>();
        JSourceFile file;
        try {
            for (JComplexType type : types.keySet()) {
                // Generate new JSourceFile object
                file = new JSourceFileImpl(packageName, type.getName());
                // Add imports to source file
                for (String requiredImport: types.get(type)) {
                    file.addImport(requiredImport);
                }
                // Add source file to list
                files.add(file.add(type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    abstract void generateClasses() throws Exception;
}
