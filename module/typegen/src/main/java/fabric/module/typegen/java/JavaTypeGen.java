package fabric.module.typegen.java;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.java.*;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.*;
import org.apache.xmlbeans.SchemaType; // TODO seidel: Is this import correct? Doesn't Fabric have its own facet-enum?

import java.util.*;

/**
 * Type generator for Java.
 *
 * @author seidel
 */
public class JavaTypeGen implements TypeGen
{
  /**
   * JavaMapper object
   */
  private Mapper mapper;

  /**
   * Class generation strategy for Java.
   */
  private JavaClassGenerationStrategy strategy;

  /**
   * Stack with yet incomplete AttributeContainer.Builder objects.
   */
  private Stack<AttributeContainer.Builder> incompleteBuilders;

  /**
   * Map with already generated JComplexType objects (e.g. JClass, JEnum, ...).
   */
  private Map<String, JComplexType> generatedElements;

  /**
   * Class name of root element.
   */
  private static final String ROOT = "Main";  // TODO: Wie wollen wir die Root-Klasse nennen?

  /**
   * Package name for the generated source files.
   */
  private static final String PACKAGE = "main";   // TODO: Nur ein Package? Oder mehrere?

  /**
   * Constructor
   */
  public JavaTypeGen()
  {
    try
    {
      mapper = MapperFactory.getInstance().createMapper("fabric.module.typegen.java.JavaMapper");
      strategy = new JavaClassGenerationStrategy(); // TODO: AnnotationMapper übergeben!
      incompleteBuilders = new Stack<AttributeContainer.Builder>();
      generatedElements = new HashMap<String, JComplexType>();
    }
    catch (Exception e)
    {
      e.printStackTrace(); // TODO: Log exception
    }
  }

  @Override
  public void generateRootContainer()
  {
    incompleteBuilders.push(AttributeContainer.newBuilder().setName(ROOT));

    System.out.println(String.format("Created root container '%s'.", ROOT));
  }

  @Override
  public void generateSourceFiles(Workspace workspace) throws Exception
  {
    /*
    Add root element as last element to the map.
     */
    //addClassToMap(); // TODO seidel: Just called once? Shouldn't we iterate all incomplete classes?
    while (!incompleteBuilders.empty())
    {
      JClass newClass = (JClass)incompleteBuilders.pop().build().asClassObject(strategy);
      generatedElements.put(newClass.getName(), newClass);
    }

    /*
    Get JavaWorkspace object
     */
    JavaWorkspace jWorkspace = workspace.getJava();
    JSourceFile jsf;

    /*
    Generate one source file in the workspace for each JComplexType object in the map.
     */
    for (String name: generatedElements.keySet())
    {
      jsf = jWorkspace.getJSourceFile(PACKAGE, name);
      jsf.add(generatedElements.get(name));
      
      System.out.println(String.format("Generated new source file '%s'.", name)); // TODO: Remove
    }
  }

  @Override
  public void addAttribute(FElement element)
  {
    // TODO: Remove this line and add comment with example of return values
    // (1 == 2 => XSD base type in 3, 1 != 2 => 2 is name of custom type)
    System.out.println(element.getName() + element.getSchemaType().getName() + this.getFabricTypeName(element.getSchemaType()));
    
    String typeName = "";
    if (element.getName().equals(element.getSchemaType().getName()))
    {
      typeName = mapper.lookup(this.getFabricTypeName(element.getSchemaType()));
    }
    else
    {
      typeName = element.getSchemaType().getName();
    }

    AttributeContainer.Builder current = incompleteBuilders.pop();
    current.addElement(typeName, element.getName());
    incompleteBuilders.push(current);
    
    System.out.println(String.format("Added attribute '%s' of type '%s' to container '%s'.",
            element.getName(), typeName, current.getName()));
  }

  @Override
  public void addSimpleType(FSimpleType type, FElement parent) throws Exception
  {
    /*
    Check if element with given name already exists in the map
     */
    if (generatedElements.containsKey(type.getName()))
    {
      System.out.println("addSimpleType: SIMPLE TYPE ALREADY EXISTS.");
      // TODO: Was soll passieren, wenn es ein Element dieses Namens bereits gibt?
    }
    else
    {
      System.out.println("addSimpleType: CREATING NEW SIMPLE TYPE.");
      /*
      Check if type is xs:list
       */
      // TODO: in Fabric not supported yet!

      /*
      Check restrictions
       */
      // HIER checkRestrictions(type);    // TODO: Einschränkungen müssen in den Container übernommen werden!

      /*
      Check if type has a default or a fixed value
       */
      // TODO: in Fabric not supported yet!

      /*
      Add variable to current AttributeContainer.Builder object
       */
      AttributeContainer.Builder current = incompleteBuilders.pop();
      if (FSchemaTypeHelper.isArray(parent))
      {  // Element is an array
        current.addElementArray(mapper.lookup(this.getFabricTypeName(type)), type.getName(), parent.getMaxOccurs());
      }
//            else if (FSchemaTypeHelper.isEnum(type)) {    // Element is an enum
//                // TODO: Wie soll die Enum-Variable genannt werden?
//                current.addElement(type.getName(), type.getName().toLowerCase());
//            }
      else
      {    // Element is a variable of a simple datatype
        current.addElement(mapper.lookup(this.getFabricTypeName(type)), type.getName());
      }
      incompleteBuilders.push(current);
    }
  }

  // TODO: Remove block
  public void generateNewContainer(FSimpleType type)
  {
    /*
    Generate new builder for new class
     */
    AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder().setName(type.getName());
    newBuilder.addElement(mapper.lookup(this.getFabricTypeName(type)), "value");

    /*
    Add builder to yet incomplete builders
     */
    incompleteBuilders.push(newBuilder);
  }
  // TODO: Remove block

  @Override
  public void generateNewContainer(FComplexType type)
  {
    /*
    Generate new builder for new class
     */
    AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder().setName(type.getName());

    /*
    Add all attributes of the ComplexType to the builder
     */
    List<FSchemaAttribute> attributes = type.getAttributes();
    for (FSchemaAttribute attr: attributes)
    {
      System.out.println(attr.getName() + ": " + this.getFabricTypeName(attr.getSchemaType())); // TODO: Remove this line!

      newBuilder.addAttribute(mapper.lookup(this.getFabricTypeName(attr.getSchemaType())), attr.getName());
    }

    /*
    Add builder to yet incomplete builders
     */
    incompleteBuilders.push(newBuilder);
  }

  @Override
  public void generateNewClass() throws Exception
  {
    addClassToMap();
  }

  @Override
  public void generateNewExtendedClass(String name) throws Exception
  {
    addClassToMap(name);
  }

  /**
   * This method restricts the values of the class variable according to the restrictions of the
   * corresponding FSimpleType object.
   *
   * @param type FSimpleType object that has to be checked for restrictions
   */
  private void checkRestrictions(FSimpleType type) throws Exception
  {
    FSchemaRestrictions restrictions = type.getRestrictions();
    List<Integer> validFacets = type.getValidFacets();

    /*
    Only consider valid facets of the given FSimpleType object.
     */
    for (Integer facet: validFacets)
    {
      switch (facet)
      {

        /*
        Check for xs:enumeration
         */
        case SchemaType.FACET_ENUMERATION:
          if (FSchemaTypeHelper.isEnum(type))
          {
            createEnum(type);
          }
          break;

        /*
        Check for xs:pattern
         */
        case SchemaType.FACET_PATTERN:
          if (restrictions.hasRestriction((facet)))
          {
            // TODO: in Fabric not supported yet!
          }
          break;

        /*
        Check for xs:whiteSpace
         */
        case SchemaType.FACET_WHITE_SPACE:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: in Fabric not supported yet!
          }
          break;

        /*
        Check for xs:length
         */
        case SchemaType.FACET_LENGTH:
          // TODO: Hier muss die Länge der Variable im Setter überprüft werden!
          // length = restrictions.getIntegerValue(facet);
          break;

        /*
        Check for xs:minLength
         */
        case SchemaType.FACET_MIN_LENGTH:
          // TODO: Hier muss die Länge der Variable im Setter überprüft werden!
          // minLength = restrictions.getIntegerValue(facet);
          break;

        /*
        Check for xs:maxLength
         */
        case SchemaType.FACET_MAX_LENGTH:
          // TODO: Hier muss die Länge der Variable im Setter überprüft werden!
          // maxLength = restrictions.getIntegerValue(facet);
          break;

        /*
        Check for xs:totalDigits
         */
        case SchemaType.FACET_TOTAL_DIGITS:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: in Fabric not supported yet!
          }
          break;

        /*
        Check for xs:fractionDigits
         */
        case SchemaType.FACET_FRACTION_DIGITS:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: in Fabric not supported yet!
          }
          break;

        /*
        Check for xs:minInclusive
         */
        case SchemaType.FACET_MIN_INCLUSIVE:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: Hier muss der Wert der numerischen Variable im Setter überprüft werden!
          }
          break;

        /*
        Check for xs:minExclusive
         */
        case SchemaType.FACET_MAX_INCLUSIVE:
          if (restrictions.hasRestriction((facet)))
          {
            // TODO: Hier muss der Wert der numerischen Variable im Setter überprüft werden!
          }
          break;

        /*
        Check for xs:maxInclusive
         */
        case SchemaType.FACET_MIN_EXCLUSIVE:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: Hier muss der Wert der numerischen Variable im Setter überprüft werden!
          }
          break;

        /*
        Check for xs:maxExclusive
         */
        case SchemaType.FACET_MAX_EXCLUSIVE:
          if (restrictions.hasRestriction(facet))
          {
            // TODO: Hier muss der Wert der numerischen Variable im Setter überprüft werden!
          }
          break;

        /*
        Not a valid restriction
         */
        default:
          break;
      }
    }
  }

  /**
   * This methods creates a JEnum object corresponding to the FSimpleType object.
   * Please make sure that the given FSimpleType object is an enum!
   *
   * @param type FSimpleType object with restriction xs:enumeration
   */
  private void createEnum(FSimpleType type) throws Exception
  {
    /*
    Get enumeration constants
     */
    Object[] constants = FSchemaTypeHelper.extractEnumArray(type);
    String[] constantsAsString = Arrays.copyOf(constants,
            constants.length,
            String[].class);
    /*
    Create JEnum source file in workspace
     */
    generatedElements.put(type.getName(),
            JEnum.factory.create(JModifier.PUBLIC,
            type.getName(),
            constantsAsString));
  }

  /**
   * Generates a JClass object corresponding to the last builder in the
   * stack of incomplete builders and adds it to the map containing the
   * already generated classes.
   *
   * @throws Exception
   */
  private void addClassToMap(String... extendedClasses) throws Exception
  {
    JClass newClass = (JClass)incompleteBuilders.pop().build().asClassObject(strategy);
    for (String extendedClass: extendedClasses)
    {
      newClass.setExtends(extendedClass);
    }
    generatedElements.put(newClass.getName(), newClass);
  }

  // TODO: Add documentation
  private String getFabricTypeName(final FSchemaType typeObject)
  {
    return typeObject.getClass().getSimpleName();
  }
}
