package fabric.module.typegen;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;
import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.*;

/**
 * Fabric handler class for the type generator module. This class
 * defines a couple of callback methods which get called by the
 * treewalker while processing the XSD file. The FabricTypeGenHandler
 * acts upon those function calls and generates corresponding type
 * classes in the workspace for a specific programming language.
 *
 * @author seidel
 */
public class FabricTypeGenHandler extends FabricDefaultHandler
{
    /**
     * TypeGen
     */
    private TypeGen typeGen;

    /**
     * Workspace
     */
    private Workspace workspace;

   /**
    * Constructor initializes internal class properties.
    *
    * @param workspace Workspace for file output
    * @param properties Properties object with various options
    */
    public FabricTypeGenHandler(Workspace workspace, Properties properties) {
        try {
            // TODO: Hier muss auf die Properties zugegriffen werden!
            typeGen = TypeGenFactory.getInstance().createTypeGen("JavaTypeGen");
            this.workspace = workspace;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        typeGen.generateRootContainer();
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        typeGen.generateSourceFiles(workspace);
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void endTopLevelElement(FElement element) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception {
        typeGen.addSimpleType(type, parent);
    }

    @Override
    public void endTopLevelSimpleType(FSimpleType type, FElement parent) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        // TODO: Soll es in den Klassen einen Unterschied zwischen lokalen und globalen SimpleTypes geben?
        typeGen.addSimpleType(type, parent);
    }

    @Override
    public void endLocalSimpleType(FSimpleType type, FElement parent) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception {
        typeGen.generateNewContainer(type);
    }

    @Override
    public void endTopLevelComplexType(FComplexType type, FElement parent) throws Exception {
        /*
        Check for xs:simpleContent with xs:restriction
         */
        if (type.isSimpleContent() && type.getRestrictions().getCount() > 0) {
            typeGen.generateNewExtendedClass(((FSimpleType) type.getChildObjects().get(0)).getName());
        } else {
            typeGen.generateNewClass();
        }

        /*
        Check for xs:complexContent
         */
        // TODO: in Fabric not supported yet!
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void endLocalElement(FElement element, FComplexType parent) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // TODO: Soll es in den Klassen einen Unterschied zwischen lokalen und globalen ComplexTypes geben?
        typeGen.generateNewContainer(type);
    }

    @Override
    public void endLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // TODO: Soll es in den Klassen einen Unterschied zwischen lokalen und globalen ComplexTypes geben?
        /*
        Check for xs:simpleContent with xs:restriction
         */
        if (type.isSimpleContent() && type.getRestrictions().getCount() > 0) {
            typeGen.generateNewExtendedClass(((FSimpleType) type.getChildObjects().get(0)).getName());
        } else {
            typeGen.generateNewClass();
        }

        /*
        Check for xs:complexContent
         */
        // TODO: in Fabric not supported yet!
    }

    @Override
    public void startElementReference(FElement element) throws Exception {
        /**
         * TODO: Was soll hier passieren?
         */
    }

    @Override
    public void endElementReference(FElement element) throws Exception {
        /**
         * TODO: Was soll hier passieren?
         */
    }
}
