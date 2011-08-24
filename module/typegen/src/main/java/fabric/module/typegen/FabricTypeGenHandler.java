package fabric.module.typegen;

import java.util.Properties;
import java.util.Stack;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;
import fabric.module.typegen.java.JavaTypeGen;
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
     * TypeGenFactory object for TypeGen access
     */
    private TypeGenFactory typeGenFactory;

    /**
     * TypeGen
     *
     * TODO: In TypeGen ändern!
     */
    private JavaTypeGen typeGen;

    /**
     * Workspace
     */
    private Workspace workspace;

    /**
     * Stack with yet incomplete attribute containers.
     */
    private Stack<AttributeContainer> containers;

    /**
     * Class name of root element.
     */
    private static final String ROOT = "Main";

   /**
    * Constructor initializes internal class properties.
    *
    * @param workspace Workspace for file output
    * @param properties Properties object with various options
    */
    public FabricTypeGenHandler(Workspace workspace, Properties properties) {
        try {
            // TODO: Hier muss auf den Übergabeparameter (Java oder Cpp) zugegriffen werden!
            typeGen = (JavaTypeGen)typeGenFactory.getInstance().createTypeGen("JavaTypeGen");
            containers = new Stack<AttributeContainer>();
            this.workspace = workspace;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        typeGen.generateRootContainer();
        /**
        AttributeContainer root = AttributeContainer.newBuilder()
                                    .setName(ROOT)
                                    .build();
        containers.push(root);
         */
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        typeGen.generateRootClass();
        /**
        AttributeContainer root = containers.pop();
        root.asClassObject();
         */
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
        typeGen.addSimpleType();
        /**
         containers.push(containers.pop().toBuilder()
                            .addElement("Hier muss der Typ hin (siehe Mapper)", type.getName())
                            .build());
         */

        /**
         * Der SimpleType muss als Variable der Main-Klasse hinzugefügt werden.
         * typegen.addAttributeToMain(type);
         *
         * in Java: mittels des AttributeContainer-Objekts der Main-Klasse
         *
         * Im TypeGen muss auf den Language-Mapper zurückgegriffen werden!
         */
    }

    @Override
    public void endTopLevelSimpleType(FSimpleType type, FElement parent) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        typeGen.addSimpleType();

        /**
         * Der SimpleType muss als Variable der aktuellen Klasse hinzugefügt werden.
         * typegen.addAttribute(type);
         *
         * in Java: mittels des aktuellen AttributeContainer-Objekts
         *
         * Im TypeGen muss auf den Language-Mapper zurückgegriffen werden!
         */
    }

    @Override
    public void endLocalSimpleType(FSimpleType type, FElement parent) {
        /**
         * Nothing to do.
         */
    }

    @Override
    public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception {

        /**
         * Für den ComplexType muss eine neue Klasse in TypeGen erstellt werden. Diese wird
         * der Liste der noch nicht vollständigen Klassen hinzugefügt. Außerdem muss ein Objekt
         * dieser Klasse in der aktuellen Klasse hinzugefügt werden.
         * typegen.generateClass(type);
         * typegen.addClassToParent();
         *
         * in Java: mittels eines neuen AttributeContainer-Objekts
         */
    }

    @Override
    public void endTopLevelComplexType(FComplexType type, FElement parent) {
        /**
         * Die Klasse wird aus der Liste der noch nicht vollständigen Klassen entfernt.
         * Das SourceFile der Klasse muss im Workspace erstellt werden.
         * typegen.generateSourceFile();
         * typegen.removeClassFromList();
         *
         * in Java: JSourceFile mittels des aktuellen AttributeContainer-Objekts erstellen
         */
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
        /**
         * Hier muss nichts gemacht werden.
         */
    }

    @Override
    public void endLocalElement(FElement element, FComplexType parent) {
        /**
         * Hier muss nichts gemacht werden.
         */
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        /**
         * Siehe startTopLevelComplexType --> in private Funktion auslagern!
         */
    }

    @Override
    public void endLocalComplexType(FComplexType type, FElement parent) {
        /**
         * Siehe startTopLevelComplexType --> in private Funktion auslagern!
         */
    }

    @Override
    public void startElementReference(FElement element) throws Exception {
        /**
         * Was ist das?
         */
    }

    @Override
    public void endElementReference(FElement element) throws Exception {
        /**
         * Was ist das?
         */
    }
}
