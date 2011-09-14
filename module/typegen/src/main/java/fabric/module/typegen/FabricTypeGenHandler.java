package fabric.module.typegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;
import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricDefaultHandler;

import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.FSequence;

/**
 * Fabric handler class for the type generator module. This class
 * defines a couple of callback methods which get called by the
 * treewalker while processing the XSD file. The FabricTypeGenHandler
 * acts upon those function calls and generates corresponding type
 * classes in the workspace for a specific programming language.
 *
 * @author seidel, reichart
 */
public class FabricTypeGenHandler extends FabricDefaultHandler
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(FabricTypeGenHandler.class);

  /** TypeGen object for type class generation */
  private TypeGen typeGenerator;

  /**
   * Constructor initializes the language-specific type generator.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public FabricTypeGenHandler(Workspace workspace, Properties properties) throws Exception
  {
    this.typeGenerator = TypeGenFactory.getInstance().createTypeGen(
            properties.getProperty("typegen.factory_name"), workspace, properties);
  }

  /**
   * Handle start of an XML schema document.
   *
   * @param schema FSchema object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called startSchema().");

    typeGenerator.createRootContainer();
  }

  /**
   * Handle end of an XML schema document.
   *
   * @param schema FSchema object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void endSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called endSchema().");

    typeGenerator.writeSourceFiles();
  }

  /**
   * Handle start of a top-level schema element. Each top-level
   * element is equivalent to a member variable in the corresponding
   * container class.
   *
   * @param element FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startTopLevelElement(FElement element) throws Exception
  {
    LOGGER.debug("Called startTopLevelElement().");

    if (null != element)
    {
      // TODO: Handle FSequence
      if (element.getSchemaType().getClass() == FSequence.class)
      {
        return;
      }

      typeGenerator.addMemberVariable(element);
    }
  }

  /**
   * Handle end of a top-level schema element.
   *
   * @param element FElement object
   */
  @Override
  public void endTopLevelElement(FElement element)
  {
    LOGGER.debug("Called endTopLevelElement().");

    // Nothing to do
  }

  /**
   * Handle start of a local schema element. Local elements only
   * apply to complex types, which are currently not supported
   * by the Fabric type generator module.
   * 
   * @param element FElement object
   * @param parent Parent FComplexType object
   * 
   * @throws Exception Error during processing
   */
  @Override
  public void startLocalElement(FElement element, FComplexType parent) throws Exception
  {
    LOGGER.debug("Called startLocalElement().");

    // TODO: Handle complex types
  }

  /**
   * Handle end of a local schema element.
   *
   * @param element FElement object
   * @param parent Parent FComplexType object
   */
  @Override
  public void endLocalElement(FElement element, FComplexType parent)
  {
    LOGGER.debug("Called endLocalElement().");

    // Nothing to do
  }

  /**
   * Handle start of a schema element reference. Element references
   * are part of XSD complex types, which are currently not supported
   * by the Fabric type generator module. Multiple local elements may
   * reference a global element for reuse. If several local elements
   * refer to the same global element, the referencing elements are
   * independent instances of the referenced element. They do not
   * share the same data, only structural information (type definition).
   *
   * @param element FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startElementReference(FElement element) throws Exception
  {
    LOGGER.debug("Called startElementReference().");

    // TODO: Handle complex types
  }

  /**
   * Handle end of a schema element reference.
   *
   * @param element FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void endElementReference(FElement element) throws Exception
  {
    LOGGER.debug("Called endElementReference().");

    // Nothing to do
  }

  /**
   * Handle start of a top-level simple type. Each schema simple type
   * corresponds to its own container class. The container on its
   * part may enforce various restrictions on the type's value.
   *
   * @param type FSimpleType object
   * @param parent Parent FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startTopLevelSimpleType().");

    if (null != type)
    {
      typeGenerator.createNewContainer(type);
    }
  }

  /**
   * Handle end of a top-level simple type. As soon as the construction
   * of a simple type is finished, we can close the current container
   * by building it.
   *
   * @param type FSimpleType object
   * @param parent Parent FElement object
   */
  @Override
  public void endTopLevelSimpleType(FSimpleType type, FElement parent)
  {
    LOGGER.debug("Called endTopLevelSimpleType().");

    try
    {
      typeGenerator.buildCurrentContainer();
    }
    catch (Exception e)
    {
      if (null != type && null != type.getName())
      {
        LOGGER.error(String.format("Failed building container for type '%s'.", type.getName()));
      }
      else
      {
        LOGGER.error("Failed building current container.");
      }
    }
  }

  /**
   * Handle start of a local simple type. Local simple types are added
   * to the parent container as a new member variable. This happens
   * in startTopLevelElement(), so we do not need to do anything here.
   *
   * @param type FSimpleType object
   * @param parent Parent FElement object
   *
   * @throws Exception Error during processing
   */
  @Override
  public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startLocalSimpleType().");

    // Nothing to do
  }

  /**
   * Handle end of a local simple type.
   *
   * @param type FSimpleType object
   * @param parent Parent FElement object
   */
  @Override
  public void endLocalSimpleType(FSimpleType type, FElement parent)
  {
    LOGGER.debug("Called endLocalSimpleType().");

    // Nothing to do
  }

  /**
   * Handle start of a top-level complex type. Complex types are currently
   * not supported by the Fabric type generator module.
   * 
   * @param type FComplexType object
   * @param parent Parent FElement object
   * 
   * @throws Exception Error during processing
   */
  @Override
  public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startTopLevelComplexType().");

    // TODO: Handle complex types
  }

  /**
   * Handle end of a top-level complex type.
   *
   * @param type FComplexType object
   * @param parent Parent FElement object
   */
  @Override
  public void endTopLevelComplexType(FComplexType type, FElement parent)
  {
    LOGGER.debug("Called endTopLevelComplexType().");

    // TODO: Handle complex types
  }

  /**
   * Handle start of a local complex type. Complex types are currently
   * not supported by the Fabric type generator module.
   * 
   * @param type FComplexType object
   * @param parent Parent FElement object
   * 
   * @throws Exception Error during processing
   */
  @Override
  public void startLocalComplexType(FComplexType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startLocalComplexType().");

    // TODO: Handle complex types
  }

  /**
   * Handle end of a local complex type. Complex types are currently
   * not supported by the Fabric type generator module.
   *
   * @param type FComplexType object
   * @param parent Parent FElement object
   */
  @Override
  public void endLocalComplexType(FComplexType type, FElement parent)
  {
    LOGGER.debug("Called endLocalComplexType().");

    // TODO: Handle complex types
  }
}
