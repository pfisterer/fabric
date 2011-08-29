package fabric.module.typegen.cpp;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Type generator for C++.
 *
 * @author seidel
 */
public class CppTypeGen implements TypeGen {
    /**
     * MapperFactory to create CppMapper object
     */
    private MapperFactory mapperFactory;

    @Override public void generateRootContainer() {

    }

    @Override public void generateSourceFiles(Workspace workspace) {

    }

    @Override public void addSimpleType(FSimpleType type, FElement parent) {

    }

    @Override public void generateNewContainer(FComplexType type) {

    }

    @Override public void generateNewClass() throws Exception {

    }

    @Override public void generateNewExtendedClass(String name) throws Exception {

    }
}
