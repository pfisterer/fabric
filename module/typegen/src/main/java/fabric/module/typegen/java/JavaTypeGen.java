/** 21.09.2011 02:03 */
package fabric.module.typegen.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.xmlbeans.SchemaType;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JEnumAnnotationImpl;
import de.uniluebeck.sourcegen.java.JEnumCommentImpl;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JavaWorkspace;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FList;
import fabric.wsdlschemaparser.schema.FSchemaRestrictions;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.FSimpleType;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.base.TypeGen;
import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.MapperFactory;

/**
 * Type generator for Java. This class handles various calls from
 * the FabricTypeGenHandler class to create Java representations
 * of the data types, which were defined in the XML schema.
 *
 * @author reichart, seidel
 */
public class JavaTypeGen implements TypeGen
{
  // TODO: Fix restrictions for local simple types
  // TODO: Add support for minOccurs to list implementation (should be 2x addElementArray() calls)

  /*****************************************************************
   * SourceFileData inner class
   *****************************************************************/

  private static final class SourceFileData
  {
    /** Data type object (e.g. JClass or JEnum) */
    private JComplexType typeObject;

    /** Java imports, which are required for source code write-out */
    private ArrayList<String> requiredImports;

    /**
     * Parameterized constructor.
     *
     * @param typeObjects Finished data type object
     * @param requiredImports List of required Java imports
     */
    public SourceFileData(final JComplexType typeObjects, final ArrayList<String> requiredImports)
    {
      this.typeObject = typeObjects;
      this.requiredImports = requiredImports;
    }
  }

  /*****************************************************************
   * JavaTypeGen outer class
   *****************************************************************/
  
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaTypeGen.class);

  /** Workspace object for source code write-out */
  private Workspace workspace;

  /** Properties object with module configuration */
  private Properties properties;

  /** Mapper object for simple data types */
  private Mapper mapper;

  /** Stack of incomplete container classes */
  private Stack<AttributeContainer.Builder> incompleteBuilders;

  /** Map of finished data type objects */
  private HashMap<String, SourceFileData> generatedElements;

  /**
   * Constructor creates mapper for simple data types and initializes
   * various member variables.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public JavaTypeGen(Workspace workspace, Properties properties) throws Exception
  {
    mapper = MapperFactory.getInstance().createMapper(properties.getProperty("typegen.mapper_name"));

    this.workspace = workspace;
    this.properties = properties;

    this.incompleteBuilders = new Stack<AttributeContainer.Builder>();
    this.generatedElements = new HashMap<String, SourceFileData>();
  }
  
  /**
   * Create root container, which corresponds to the top-level
   * XML schema document.
   */
  @Override
  public void createRootContainer()
  {
    String rootContainerName = this.properties.getProperty("typegen.main_class_name");
    this.incompleteBuilders.push(AttributeContainer.newBuilder().setName(rootContainerName));

    LOGGER.debug(String.format("Created root container '%s'.", rootContainerName));
  }

  /**
   * Create source files from container classes and
   * write them to the language-specific workspace.
   *
   * @throws Exception Error during source file write-out
   */
  @Override
  public void writeSourceFiles() throws Exception
  {
    // This guard should never trigger! -- But never say never...
    if (!this.incompleteBuilders.empty())
    {
      LOGGER.error(String.format("End of schema reached, but not all containers were built (%d remained).",
              this.incompleteBuilders.size()));
      
      throw new IllegalStateException("JavaTypeGen reached an illegal state. Lapidate the programmer.");
    }

    JavaWorkspace javaWorkspace = this.workspace.getJava();
    JSourceFile jsf = null;

    // Create new source file for every container
    for (String name: this.generatedElements.keySet())
    {
      jsf = javaWorkspace.getJSourceFile(this.properties.getProperty("typegen.java.package_name"), name);
      JavaTypeGen.SourceFileData sourceFileData = this.generatedElements.get(name);

      // Add container to source file
      jsf.add(sourceFileData.typeObject);

      // Add imports to source file
      for (String requiredImport: sourceFileData.requiredImports)
      {
        jsf.addImport(requiredImport);
      }

      LOGGER.debug(String.format("Generated new source file '%s'.", name));
    }
  }

  /**
   * Create a new container class that represents a simple type
   * of the XML schema document. All elements and attributes that
   * belong to this type should be added to the container as
   * member variables.
   *
   * @param type FSimpleType object
   */
  @Override
  public void createNewContainer(FSimpleType type)
  {
    if (null != type)
    {
      // Type is a top-level enum
      if (FSchemaTypeHelper.isEnum(type))
      {
        try
        {
          this.createTopLevelEnum(type);
        }
        catch (Exception e)
        {
          LOGGER.error(String.format("Failed creating enum '%s'.", type.getName()));
        }

        LOGGER.debug(String.format("Created new enum '%s'.", type.getName()));
      }
      // Type is a list or single value
      else
      {
        // Create new container for simple type
        AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder().setName(type.getName());

        // Type either is a list...
        if (FSchemaTypeHelper.isList(type))
        {
          FList listType = (FList)type;
          newBuilder.addElementArray(this.mapper.lookup(this.getFabricTypeName(listType.getItemType())),
                  "values", FSchemaTypeHelper.getMaxLength(listType));
        }
        // ... or a single value
        else
        {
          newBuilder.addElement(this.mapper.lookup(this.getFabricTypeName(type)),
                  "value", this.createRestrictions(type));
        }
        this.incompleteBuilders.push(newBuilder);

        LOGGER.debug(String.format("Created new container '%s'.", type.getName()));
      }
    }
  }

  /**
   * Add a member variable to the current container class.
   * Type, name, initial value and restrictions of the
   * element will be mapped to Java where applicable.
   *
   * @param element FElement object
   */
  @Override
  public void addMemberVariable(FElement element)
  {
    if (!this.incompleteBuilders.empty())
    {
      // Determine element type
      String typeName = "";

      // Element is XSD base type (e.g. xs:string, xs:short, ...)
      if (element.getName().equals(element.getSchemaType().getName()))
      {
        typeName = this.mapper.lookup(this.getFabricTypeName(element.getSchemaType()));
      }
      // Element is custom type (e.g. some XSD base type itm:Simple02)
      else
      {
        typeName = element.getSchemaType().getName();
      }

      // Add member variable to current incomplete container
      AttributeContainer.Builder current = this.incompleteBuilders.pop();

      // Element is an array
      if (FSchemaTypeHelper.isArray(element))
      {
        current.addElementArray(typeName, element.getName(), element.getMinOccurs(), element.getMaxOccurs());
      }
      // Element is an enum
      else if (FSchemaTypeHelper.isEnum(element.getSchemaType()))
      {
        Object[] constants = FSchemaTypeHelper.extractEnumArray((FSimpleType)element.getSchemaType());
        String[] enumConstants = Arrays.copyOf(constants, constants.length, String[].class);
        current.addEnumElement(element.getName() + "Enum", element.getName(), enumConstants);
      }
      // Element is a list
      else if (FSchemaTypeHelper.isList(element))
      {
        current.addElementArray(typeName, element.getName(),
                FSchemaTypeHelper.getMaxLength((FList)element.getSchemaType()));
      }
      // Element has a default value
      else if (FSchemaTypeHelper.hasDefaultValue(element))
      {
        current.addElement(typeName, element.getName(), element.getDefaultValue());
      }
      // Element has a fixed value
      else if (FSchemaTypeHelper.hasFixedValue(element))
      {
        current.addConstantElement(typeName, element.getName(), element.getFixedValue());
      }
      // Element is a common member variable
      else
      {
        current.addElement(typeName, element.getName());
      }
      this.incompleteBuilders.push(current);
      
      LOGGER.debug(String.format("Added member variable '%s' of type '%s' to container '%s'.",
              element.getName(), typeName, current.getName()));
    }
  }

  /**
   * Finish the construction of the current container class by
   * building it. As soon as a container is built, no more new
   * member variables can be added to it. This function is usually
   * called, when the closing XML tag of a type definition is reached.
   *
   * @throws Exception Error while building container
   */
  @Override
  public void buildCurrentContainer() throws Exception
  {
    // Build current container
    if (!this.incompleteBuilders.empty())
    {
      // Create mapper for XML framework annotations and strategy
      AnnotationMapper xmlMapper = new AnnotationMapper(this.properties.getProperty("typegen.java.xml_framework"));
      JavaClassGenerationStrategy javaStrategy = new JavaClassGenerationStrategy(xmlMapper);

      JClass classObject = (JClass)this.incompleteBuilders.pop().build().asClassObject(javaStrategy);
      if (!this.generatedElements.containsKey(classObject.getName()))
      {
        this.generatedElements.put(classObject.getName(),
                new JavaTypeGen.SourceFileData(classObject, javaStrategy.getRequiredDependencies()));
      }

      LOGGER.debug(String.format("Built current container '%s'.", classObject.getName()));
    }
  }

  /**
   * Create an AttributeContainer.Restriction object according to
   * the restrictions, which are set in the provided type object.
   * This way we can add restrictions to a container class and
   * take them into account, when we do the source code write-out.
   *
   * @param type FSimpleType object (may be restricted)
   *
   * @return Restriction object for AttributeContainer
   */
  private AttributeContainer.Restriction createRestrictions(final FSimpleType type)
  {
    AttributeContainer.Restriction restrictions = new AttributeContainer.Restriction();
    
    // Determine restrictions, which are currently set on the type object
    FSchemaRestrictions schemaRestrictions = type.getRestrictions();
    List<Integer> validFacets = type.getValidFacets();

    // Iterate all possible restrictions...
    for (Integer facet: validFacets)
    {
      // ... and check which are set
      switch (facet)
      {
        // Type object is length restricted
        case SchemaType.FACET_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.length = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minLength restricted
        case SchemaType.FACET_MIN_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minLength = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxLength restricted
        case SchemaType.FACET_MAX_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxLength = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minInclusive restricted
        case SchemaType.FACET_MIN_INCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minInclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxInclusive restricted
        case SchemaType.FACET_MAX_INCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxInclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minExclusive restricted
        case SchemaType.FACET_MIN_EXCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minExclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxExclusive restricted
        case SchemaType.FACET_MAX_EXCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxExclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is pattern restricted
        case SchemaType.FACET_PATTERN:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.pattern = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is whiteSpace restricted
        case SchemaType.FACET_WHITE_SPACE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.whiteSpace = this.translateWhiteSpaceRestriction(
                    schemaRestrictions.getIntegerValue(facet));
          }
          break;

        // Type object is totalDigits restricted
        case SchemaType.FACET_TOTAL_DIGITS:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.totalDigits = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is fractionDigits restricted
        case SchemaType.FACET_FRACTION_DIGITS:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.fractionDigits = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is not restricted
        default:
          break;
      }
    }

    return restrictions;
  }

  /**
   * Create top-level JEnum from type object and add it to the
   * generated elements. A top-level enum must be written to
   * its own source file, so we bypass the AttributeContainer
   * mechanism here.
   *
   * @param type FSimpleType object (with enum restriction)
   *
   * @throws Exception Error during enum generation
   */
  private void createTopLevelEnum(final FSimpleType type) throws Exception
  {
    if (null != type && FSchemaTypeHelper.isEnum(type))
    {
      // Get enum constants and convert them to String array
      Object[] constants = FSchemaTypeHelper.extractEnumArray(type);
      String[] constantsAsString = Arrays.copyOf(constants, constants.length, String[].class);
      
      // Create enum and add it to generated elements
      if (!this.generatedElements.containsKey(type.getName()))
      {
        AnnotationMapper xmlMapper = new AnnotationMapper(this.properties.getProperty("typegen.java.xml_framework"));
        JEnum javaEnum = JEnum.factory.create(JModifier.PUBLIC, type.getName(), constantsAsString);

        javaEnum.setComment(new JEnumCommentImpl(String.format("The '%s' enumeration.", type.getName())));
        javaEnum.addAnnotation(new JEnumAnnotationImpl(xmlMapper.getAnnotation("attribute"))); // TODO: Change key to enum

        this.generatedElements.put(type.getName(),
                new JavaTypeGen.SourceFileData(javaEnum, xmlMapper.getUsedImports()));
      }
    }
  }

  /**
   * Return simple class name of the Fabric FSchemaType object.
   * This is used for the internal mapping of the basic XSD
   * data types (i.e. xs:string, xs:short, ...).
   *
   * @param type FSchemaType object
   *
   * @return Simple class name of type object
   */
  private String getFabricTypeName(final FSchemaType type)
  {
    return type.getClass().getSimpleName();
  }

  /**
   * Translate identifiers for 'whiteSpace' restriction from
   * XMLBeans constants to textual representations (e.g.
   * 'preserve' instead of Schema.WS_PRESERVE).
   *
   * @param xmlBeansConstant XMLBeans constant
   *
   * @return String representation of identifier
   */
  private String translateWhiteSpaceRestriction(final int xmlBeansConstant)
  {
    String result = "";

    switch (xmlBeansConstant)
    {
      case SchemaType.WS_PRESERVE:
        result = "preserve";
        break;

      case SchemaType.WS_REPLACE:
        result = "replace";
        break;

      case SchemaType.WS_COLLAPSE:
        result = "collapse";
        break;

      default:
        result = null;
        break;
    }

    return result;
  }
}
