package fabric.module.typegen.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xmlbeans.SchemaType;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JComplexType;
import de.uniluebeck.sourcegen.java.JEnum;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JSourceFile;
import de.uniluebeck.sourcegen.java.JavaWorkspace;
import fabric.wsdlschemaparser.schema.FElement;
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
  // TODO: Restrictions for local simple types!
  
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

  /** Map of finished data type objects (e.g. JClass, JEnum and others) */
  private HashMap<String, JComplexType> generatedElements;

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
    this.generatedElements = new HashMap<String, JComplexType>();
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
   * Build all incomplete container classes and write them to
   * source files in the language-specific workspace.
   *
   * @throws Exception Error during source file write-out
   */
  @Override
  public void writeSourceFiles() throws Exception
  {
    JavaClassGenerationStrategy strategy = null;
    
    // Build root container (and other incomplete containers, but when
    // we reach this point, there should not be any left)
    while (!this.incompleteBuilders.empty())
    {
      // Create mapper for XML framework annotations and strategy
      AnnotationMapper xmlMapper = new AnnotationMapper(this.properties.getProperty("typegen.java.xml_framework"));
      strategy = new JavaClassGenerationStrategy(xmlMapper);
      
      JClass classObject = (JClass)this.incompleteBuilders.pop().build().asClassObject(strategy);      
      if (!this.generatedElements.containsKey(classObject.getName()))
      {
        this.generatedElements.put(classObject.getName(), classObject);
      }
      
      LOGGER.debug(String.format("Built incomplete container '%s'.", classObject.getName()));
    }
    
    JavaWorkspace javaWorkspace = this.workspace.getJava();
    JSourceFile jsf = null;
    
    // Create new source file for every container
    for (String name: this.generatedElements.keySet())
    {
      jsf = javaWorkspace.getJSourceFile(this.properties.getProperty("typegen.java.package_name"), name);

      // Add container to source file
      jsf.add(this.generatedElements.get(name));

      // Add imports to source file
      if (null != strategy)
      {
        for (String requiredImport: strategy.getRequiredDependencies())
        {
          jsf.addImport(requiredImport);
        }
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
    if (null != type && FSchemaTypeHelper.isEnum(type)) // TODO: Check and remove
    {
      try
      {
        this.generatedElements.put(type.getName(), this.createEnum(type));
      }
      catch (Exception e)
      {
        LOGGER.error(String.format("Failed creating enum '%s'.", type.getName()));
      }
      
      LOGGER.debug(String.format("Created new enum '%s'.", type.getName()));      
    }
    else
    {
      // Create new container for simple type (may not contain array
      // as value, but member variable may be restricted in some way)
      AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder().setName(type.getName());
      newBuilder.addElement(this.mapper.lookup(this.getFabricTypeName(type)), "value", this.createRestrictions(type));
      this.incompleteBuilders.push(newBuilder);

      LOGGER.debug(String.format("Created new container '%s'.", type.getName()));
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
        current.addElementArray(typeName, element.getName(), element.getMaxOccurs());
      }
      // Element is an enum
      else if (FSchemaTypeHelper.isEnum(element.getSchemaType()))
      {
        Object[] constants = FSchemaTypeHelper.extractEnumArray((FSimpleType)element.getSchemaType());
        String[] enumConstants = Arrays.copyOf(constants, constants.length, String[].class);
        current.addEnumElement(element.getName() + "Enum", element.getName(), enumConstants);
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
        this.generatedElements.put(classObject.getName(), classObject);
      }

      LOGGER.debug(String.format("Built current container '%s'.", classObject.getName()));
    }
  }

  /**
   * Create an AttributeContainer.Restriction object accorting to
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

        // Type object is not restricted
        default:
          break;
      }
    }

    return restrictions;
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
  
  // TODO: Check and add comments
  private JEnum createEnum(final FSimpleType type) throws Exception
  {
    JEnum javaEnum = null;
    
    if (null != type && FSchemaTypeHelper.isEnum(type))
    {
      Object[] constants = FSchemaTypeHelper.extractEnumArray(type);
      String[] constantsAsString = Arrays.copyOf(constants, constants.length, String[].class);
      
      if (!this.generatedElements.containsKey(type.getName()))
      {
        javaEnum = JEnum.factory.create(JModifier.PUBLIC, type.getName(), constantsAsString);
        this.generatedElements.put(type.getName(), javaEnum);
      }
    }
    
    return javaEnum;
  }

// TODO: Remove the following lines before release:
//  
//  @Override
//  public void createNewContainer(FComplexType type)
//  {
//    /*
//    Generate new builder for new class
//     */
//    AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder().setName(type.getName());
//
//    /*
//    Add all attributes of the ComplexType to the builder
//     */
//    List<FSchemaAttribute> attributes = type.getAttributes();
//    for (FSchemaAttribute attr: attributes)
//    {
//      System.out.println(attr.getName() + ": " + this.getFabricTypeName(attr.getSchemaType())); // TODO: Remove this line!
//
//      newBuilder.addAttribute(mapper.lookup(this.getFabricTypeName(attr.getSchemaType())), attr.getName());
//    }
//
//    /*
//    Add builder to yet incomplete builders
//     */
//    incompleteBuilders.push(newBuilder);
//  }
//
//  @Override
//  public void addSimpleType(FSimpleType type, FElement parent) throws Exception
//  {
//    /*
//    Check if element with given name already exists in the map
//     */
//    if (generatedElements.containsKey(type.getName()))
//    {
//      System.out.println("addSimpleType: SIMPLE TYPE ALREADY EXISTS.");
//    }
//    else
//    {
//      System.out.println("addSimpleType: CREATING NEW SIMPLE TYPE.");
//
//      /*
//      Add variable to current AttributeContainer.Builder object
//       */
//      AttributeContainer.Builder current = incompleteBuilders.pop();
//      if (FSchemaTypeHelper.isArray(parent))
//      {  // Element is an array
//        current.addElementArray(mapper.lookup(this.getFabricTypeName(type)), type.getName(), parent.getMaxOccurs());
//      }
////            else if (FSchemaTypeHelper.isEnum(type)) {    // Element is an enum
////                current.addElement(type.getName(), type.getName().toLowerCase());
////            }
//      else
//      {    // Element is a variable of a simple datatype
//        current.addElement(mapper.lookup(this.getFabricTypeName(type)), type.getName());
//      }
//      incompleteBuilders.push(current);
//    }
//  } 
//
//  /**
//   * This method restricts the values of the class variable according to the restrictions of the
//   * corresponding FSimpleType object.
//   *
//   * @param type FSimpleType object that has to be checked for restrictions
//   */
//  private void checkRestrictions(FSimpleType type) throws Exception
//  {
//    FSchemaRestrictions restrictions = type.getRestrictions();
//    List<Integer> validFacets = type.getValidFacets();
//
//    /*
//    Only consider valid facets of the given FSimpleType object.
//     */
//    for (Integer facet: validFacets)
//    {
//      switch (facet)
//      {
//
//        /*
//        Check for xs:enumeration
//         */
//        case SchemaType.FACET_ENUMERATION:
//          if (FSchemaTypeHelper.isEnum(type))
//          {
//            createEnum(type);
//          }
//          break;
//
//        /*
//        Check for xs:pattern
//         */
//        case SchemaType.FACET_PATTERN:
//          if (restrictions.hasRestriction((facet)))
//          {
//            // TODO: in Fabric not supported yet!
//          }
//          break;
//
//        /*
//        Check for xs:whiteSpace
//         */
//        case SchemaType.FACET_WHITE_SPACE:
//          if (restrictions.hasRestriction(facet))
//          {
//            // TODO: in Fabric not supported yet!
//          }
//          break;
//
//        /*
//        Check for xs:totalDigits
//         */
//        case SchemaType.FACET_TOTAL_DIGITS:
//          if (restrictions.hasRestriction(facet))
//          {
//            // TODO: in Fabric not supported yet!
//          }
//          break;
//
//        /*
//        Check for xs:fractionDigits
//         */
//        case SchemaType.FACET_FRACTION_DIGITS:
//          if (restrictions.hasRestriction(facet))
//          {
//            // TODO: in Fabric not supported yet!
//          }
//          break;
//      }
//    }
//  }
}
