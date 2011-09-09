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
 * @author reichart, seidel
 */
public class FabricTypeGenHandler extends FabricDefaultHandler
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(FabricTypeGenHandler.class);

  /** TypeGen object for type class generation */
  private TypeGen typeGen;

  /**
   * Constructor initializes internal class properties.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public FabricTypeGenHandler(Workspace workspace, Properties properties) throws Exception
  {
    this.typeGen = TypeGenFactory.getInstance().createTypeGen(
            properties.getProperty("typegen.factory_name"), workspace, properties);
  }

  @Override
  public void startSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called startSchema().");

    typeGen.createRootContainer();
  }

  @Override
  public void endSchema(FSchema schema) throws Exception
  {
    LOGGER.debug("Called endSchema().");

    typeGen.writeSourceFiles();
  }

  @Override
  public void startTopLevelElement(FElement element) throws Exception
  {
    LOGGER.debug("Called startTopLevelElement().");

    if (null != element)
    {
      // TODO: Handle FSequence
      if (element.getSchemaType() instanceof FSequence)
      {
        return;
      }

      typeGen.addMemberVariable(element);
    }
  }

  @Override
  public void endTopLevelElement(FElement element)
  {
    LOGGER.debug("Called endTopLevelElement().");

    // Nothing to do
  }

  @Override
  public void startLocalElement(FElement element, FComplexType parent) throws Exception
  {
    LOGGER.debug("Called startLocalElement().");

    // TODO: addMemberVariable?
    //typeGen.addAttribute(element.getSchemaType()); // TODO: Remove
  }

  @Override
  public void endLocalElement(FElement element, FComplexType parent)
  {
    LOGGER.debug("Called endLocalElement().");

    // Nothing to do
  }

  /**
   * An XSD element reference references a global element in another
   * local element. If several local elements reference the same
   * global element, the referencing elements are independent instances
   * of the referenced element.
   * 
   * Element references are not supported (yet?), because
   * org.apache.xmlbeans.impl.values.XmlObjectBase.getQNameValue
   * throws an exception "Could not get a Java QName type from a Schema
   * unknown type".
   * 
   * @param element
   * @throws Exception 
   */
  @Override
  public void startElementReference(FElement element) throws Exception
  {
    LOGGER.debug("Called startElementReference().");

    // TODO: Check why Apache XMLBeans crashes here
    // TODO: addMemberVariable?

    // Element references are not supported by Apache XMLBeans
  }

  @Override
  public void endElementReference(FElement element) throws Exception
  {
    LOGGER.debug("Called endElementReference().");

    // Nothing to do
  }

  @Override
  public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startTopLevelSimpleType().");

    if (null != type) // TODO Remove: && null != type.getName())
    {
      //typeGen.addSimpleType(type, parent); // TODO: Remove
      typeGen.createNewContainer(type);
    }
  }

  @Override
  public void endTopLevelSimpleType(FSimpleType type, FElement parent)
  {
    LOGGER.debug("Called endTopLevelSimpleType().");

    try
    {
      typeGen.buildCurrentContainer();
    }
    catch (Exception e)
    {
      // TODO: Log exception
    }
  }

  /**
   * Local simple types are added to the parent container as a
   * new member variable e.g. in startTopLevelElement(), so we
   * do not need to do anything here.
   *
   * @param type
   * @param parent
   *
   * @throws Exception
   */
  @Override
  public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    LOGGER.debug("Called startLocalSimpleType().");

    // Nothing to do
  }

  @Override
  public void endLocalSimpleType(FSimpleType type, FElement parent)
  {
    LOGGER.debug("Called endLocalSimpleType().");

    // Nothing to do
  }

//  @Override
//  public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception
//  {
//    typeGen.generateNewContainer(type);
//  }
//
//  @Override
//  public void endTopLevelComplexType(FComplexType type, FElement parent)
//  {
//    try
//    {
//      /*
//      Check for xs:simpleContent with xs:restriction
//       */
//      if (type.isSimpleContent() && type.getRestrictions().getCount() > 0)
//      {
//        typeGen.generateNewExtendedClass(((FSimpleType)type.getChildObjects().get(0)).getName());
//      }
//      else
//      {
//        typeGen.generateNewClass();
//      }
//
//      /*
//      Check for xs:complexContent
//       */
//      // TODO: in Fabric not supported yet!
//    }
//    catch (Exception e)
//    {
//      // TODO: Log exception
//    }
//  }
//
//  @Override
//  public void startLocalComplexType(FComplexType type, FElement parent) throws Exception
//  {
//    // TODO: Soll es in den Klassen einen Unterschied zwischen lokalen und globalen ComplexTypes geben?
//    typeGen.generateNewContainer(type);
//  }
//
//  @Override
//  public void endLocalComplexType(FComplexType type, FElement parent)
//  {
//    // TODO: Soll es in den Klassen einen Unterschied zwischen lokalen und globalen ComplexTypes geben?
//
//    try
//    {
//      /*
//      Check for xs:simpleContent with xs:restriction
//       */
//      if (type.isSimpleContent() && type.getRestrictions().getCount() > 0)
//      {
//        typeGen.generateNewExtendedClass(((FSimpleType)type.getChildObjects().get(0)).getName());
//      }
//      else
//      {
//        typeGen.generateNewClass();
//      }
//    }
//    catch (Exception e)
//    {
//      // TODO: Log exception
//    }
//
//    /*
//    Check for xs:complexContent
//     */
//    // TODO: in Fabric not supported yet!
//  }
}
