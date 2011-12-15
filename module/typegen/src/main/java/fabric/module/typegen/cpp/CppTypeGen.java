/** 12.12.2011 19:41 */
package fabric.module.typegen.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;
import fabric.wsdlschemaparser.schema.*;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.FabricTypeGenModule;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.Mapper;
import fabric.module.typegen.base.TypeGen;
import fabric.module.typegen.base.TypeGenHelper;

/**
 * Type generator for C++. This class handles various calls from
 * the FabricTypeGenHandler class to create C++ representations
 * of the data types, which were defined in the XML schema.
 *
 * @author seidel
 */
public class CppTypeGen implements TypeGen
{
  /*****************************************************************
   * SourceFileData inner class
   *****************************************************************/

  private static final class SourceFileData
  {
    /** CppClass objects */
    /** TODO: Remove, if wrong: Data type object (e.g. JClass or JEnum) */
    private CppClass typeObject;

    /** C++ includes, which are required for source code write-out */
    private ArrayList<String> requiredIncludes;

    /**
     * Parameterized constructor.
     *
     * @param typeObject Finished data type object
     * @param requiredIncludes List of required C++ includes
     */
    public SourceFileData(final CppClass typeObject, final ArrayList<String> requiredIncludes)
    {
      this.typeObject = typeObject;

      // Create empty list, if there are no required includes
      if (null == requiredIncludes)
      {
        this.requiredIncludes = new ArrayList<String>();
      }
      else
      {
        this.requiredIncludes = requiredIncludes;
      }
    }
  }

  /*****************************************************************
   * CppTypeGen outer class
   *****************************************************************/
  
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(CppTypeGen.class);

  /** Workspace object for source code write-out */
  private Workspace workspace;

  /** Properties object with module configuration */
  private Properties properties;

  /** Mapper object for simple data types */
  private Mapper mapper;

  /** Stack of incomplete container classes */
  private Stack<AttributeContainer.Builder> incompleteBuilders;
  
  /** Map with one stack of incomplete local builders per outer class */
  private HashMap<String, Stack<AttributeContainer.Builder>> incompleteLocalBuilders;

  /** Map of finished data type objects */
  private HashMap<String, SourceFileData> generatedElements;

  /**
   * Constructor creates mapper for simple data types and initializes
   * various member variables.
   *
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   */
  public CppTypeGen(Workspace workspace, Properties properties) throws Exception
  {
    this.mapper = MapperFactory.getInstance().createMapper(properties.getProperty(FabricTypeGenModule.MAPPER_CLASS_KEY));

    this.workspace = workspace;
    this.properties = properties;

    this.incompleteBuilders = new Stack<AttributeContainer.Builder>();
    this.incompleteLocalBuilders = new HashMap<String, Stack<AttributeContainer.Builder>>();
    this.generatedElements = new HashMap<String, SourceFileData>();
  }
  
  /**
   * Create root container, which corresponds to the top-level
   * XML schema document.
   */
  @Override
  public void createRootContainer()
  {
    String rootContainerName = this.properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY);
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
      
      throw new IllegalStateException("CppTypeGen reached an illegal state. Lapidate the programmer.");
    }

    CWorkspace cWorkspace = this.workspace.getC();
    CppHeaderFile cpphf = null;
    CppSourceFile cppsf = null;
    
    // Create new source file for every container
    for (String name: this.generatedElements.keySet())
    {
      // Get source file data
      CppTypeGen.SourceFileData sourceFileData = this.generatedElements.get(name);      
      
      /*****************************************************************
       * Create C++ header file
       *****************************************************************/
      cpphf = cWorkspace.getCppHeaderFile(name);
      cpphf.add(sourceFileData.typeObject);
      cpphf.setComment(new CCommentImpl(String.format("The '%s' header file.", name)));

      // TODO: Check this block
      // Add includes
      String rootContainerName = this.properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY);
      if (name.equals(rootContainerName))
      {
        for (String include: this.generatedElements.keySet())
        {
          if (!include.equals(name))
          {
            cpphf.addLibInclude(include + ".hpp"); // TODO: Change to addInclude(String include + ".hpp") later
          }
        }
      }
      // TODO: Block end
      
      // Add include guards to header file
      cpphf.addBeforeDirective("ifndef " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addBeforeDirective("define " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addAfterDirective("endif // " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
      
      /*****************************************************************
       * Create C++ source file
       *****************************************************************/
      cppsf = cWorkspace.getCppSourceFile(name);
      // No need to add sourceFileData.typeObject here, will be taken from cpphf automatically
      cppsf.setComment(new CCommentImpl(String.format("The '%s' source file.", name)));

      // Add includes
      cppsf.addInclude(cpphf);
      cppsf.addLibInclude(CppTypeHelper.FILE_NAME); // TODO: Change to addInclude(String include) later
      cppsf.addLibInclude("iostream"); // Needed for text output
      cppsf.addLibInclude("string.h"); // Needed strlen() in restriction checks
      cppsf.addUsingNamespace("std");
      
      // Add includes for own data types
      for (String requiredInclude: sourceFileData.requiredIncludes)
      {
        cppsf.addLibInclude(requiredInclude); // TODO: How do we add user header files?
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
        newBuilder.addElementList(
                this.mapper.lookup(TypeGenHelper.getFabricTypeName(listType.getItemType())), "values",
                FSchemaTypeHelper.getMinLength(listType), FSchemaTypeHelper.getMaxLength(listType));
      }
      // ... or a single value
      else
      {
        newBuilder.addElement(this.mapper.lookup(TypeGenHelper.getFabricTypeName(type)),
                "value", TypeGenHelper.createRestrictions(type));
      }
      this.incompleteBuilders.push(newBuilder);

      LOGGER.debug(String.format("Created new container '%s' for simple type.", type.getName()));
    }
  }
  
  /**
   * Create a new container class that represents a complex type
   * of the XML schema document. All elements and attributes that
   * belong to this type should be added to the container as
   * member variables.
   *
   * @param type FComplexType object
   */
  @Override
  public void createNewContainer(FComplexType type)
  {
    // Create new container for complex type
    AttributeContainer.Builder newBuilder = AttributeContainer.newBuilder();

    // Type is a top-level complex type
    if (type.isTopLevel())
    {
      this.incompleteBuilders.push(newBuilder.setName(type.getName()));
      LOGGER.debug(String.format("Created new container '%s' for top-level complex type.", type.getName()));
    }
    // Type is a local complex type
    else if (!this.incompleteBuilders.empty())
    {
      String parentContainerName = this.incompleteBuilders.peek().getName();
      String typeName = type.getName() + "Type";

      Stack<AttributeContainer.Builder> currentStack = this.incompleteLocalBuilders.get(parentContainerName);
      if (null == currentStack)
      {
        // Stack for inner classes was not created yet
        currentStack = new Stack<AttributeContainer.Builder>();
      }
      currentStack.push(newBuilder.setName(typeName));

      this.incompleteLocalBuilders.put(parentContainerName, currentStack);
      LOGGER.debug(String.format("Created new container '%s' for local complex type.", typeName));
    }
  }

  /**
   * Add a member variable to the current container class.
   * Type, name, initial value and restrictions of the
   * element will be mapped to Cpp where applicable.
   *
   * @param element FElement object
   * @param isTopLevel True if the element is a top-level element
   * or part of a top-level complex type; false if the element is
   * part of a local complex type
   */
  @Override
  public void addMemberVariable(FElement element, boolean isTopLevel)
  {
    // Only add member variable, if we have an incomplete top-level or local container
    if ((isTopLevel && !this.incompleteBuilders.empty()) || TypeGenHelper.hasIncompleteLocalBuilders(incompleteBuilders, incompleteLocalBuilders))
    {
      // Determine element type
      String typeName = "";
      boolean isCustomTyped;
      
      // Element is XSD base-typed (e.g. xs:string, xs:short, ...)
      if (SchemaHelper.isBuiltinTypedElement(element))
      {
        FSchemaType ftype = null;

        // Get item type, if element is a list
        if (FSchemaTypeHelper.isList(element))
        {
          FList listType = (FList)element.getSchemaType();
          ftype = listType.getItemType();
        }
        else
        {
          ftype = element.getSchemaType();
        }

        typeName = this.mapper.lookup(TypeGenHelper.getFabricTypeName(ftype));
        LOGGER.debug(String.format("Type '%s' is an XSD built-in type.", typeName));
        
        isCustomTyped = false;
      }
      // Element is custom-typed (e.g. itm:Simple02)
      else
      {
        typeName = element.getSchemaType().getName();
        LOGGER.debug(String.format("Type '%s' is a custom type.", typeName));

        // Create artificial name for local complex type (i.e. an inner class)
        if (!element.getSchemaType().isTopLevel() && !element.getSchemaType().isSimple())
        {
          typeName += "Type";
        }
        
        isCustomTyped = true;
      }

      // Add member variable to current incomplete container
      AttributeContainer.Builder current = (isTopLevel ? this.incompleteBuilders.pop() : this.incompleteLocalBuilders.get(this.incompleteBuilders.peek().getName()).pop());

      // Enforce restrictions for local simple types
      AttributeContainer.Restriction restrictions = new AttributeContainer.Restriction();
      if (element.getSchemaType().isSimple() && !element.getSchemaType().isTopLevel())
      {
        restrictions = TypeGenHelper.createRestrictions((FSimpleType)(element.getSchemaType()));
      }

      // Element is an enum
      if (!element.getSchemaType().isTopLevel() && FSchemaTypeHelper.isEnum(element.getSchemaType()))
      {
        Object[] constants = FSchemaTypeHelper.extractEnumArray((FSimpleType)element.getSchemaType());
        String[] enumConstants = Arrays.copyOf(constants, constants.length, String[].class);
        current.addEnumElement(element.getName() + "Enum", element.getName(), enumConstants);
      }
      // Element is an array
      else if (FSchemaTypeHelper.isArray(element))
      {
        current.addElementArray(typeName, element.getName(), element.getMinOccurs(), element.getMaxOccurs());
      }
      // Element is a list
      else if (FSchemaTypeHelper.isList(element) && !isCustomTyped)
      {
        FList listType = (FList)element.getSchemaType();
        current.addElementList(typeName, element.getName(), FSchemaTypeHelper.getMinLength(listType), FSchemaTypeHelper.getMaxLength(listType));
      }
      // Element has a default value
      else if (FSchemaTypeHelper.hasDefaultValue(element))
      {
        current.addElement(typeName, element.getName(), element.getDefaultValue(), restrictions);
      }
      // Element has a fixed value
      else if (FSchemaTypeHelper.hasFixedValue(element))
      {
        current.addConstantElement(typeName, element.getName(), element.getFixedValue());
      }
      // Element is a common member variable
      else
      {
        current.addElement(typeName, element.getName(), restrictions);
      }

      // Type is a top-level type
      if (isTopLevel)
      {
        this.incompleteBuilders.push(current);
      }
      // Type is a local complex type
      else
      {
        String parentContainerName = this.incompleteBuilders.peek().getName();

        Stack<AttributeContainer.Builder> currentStack = this.incompleteLocalBuilders.get(parentContainerName);
        if (null == currentStack)
        {
          // Stack for inner classes was not created yet
          currentStack = new Stack<AttributeContainer.Builder>();
        }
        currentStack.push(current);

        this.incompleteLocalBuilders.put(parentContainerName, currentStack);
      }

      LOGGER.debug(String.format("Added member variable '%s' of %s '%s' to container '%s'.", element.getName(),
              (SchemaHelper.isBuiltinTypedElement(element) ? "built-in " : "") + "type", typeName, current.getName()));
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
      // Create strategy for code generation
      CppClassGenerationStrategy cppStrategy = new CppClassGenerationStrategy();

      CppClass classObject = (CppClass)this.incompleteBuilders.pop().build().asClassObject(cppStrategy);

      // Build current local containers
      while (TypeGenHelper.stackIsNotEmpty(incompleteLocalBuilders, classObject.getName()))
      {
        CppClass innerClassObject = (CppClass)cppStrategy.generateClassObject(
                this.incompleteLocalBuilders.get(classObject.getName()).pop().build());
        classObject.add(Cpp.PRIVATE, innerClassObject); // TODO: Is this correct for inner classes in C++? Other modifiers?
        LOGGER.debug(String.format("Built inner class '%s' for current container '%s'.", innerClassObject.getName(), classObject.getName()));
      }

      if (!this.generatedElements.containsKey(classObject.getName()))
      {
        this.generatedElements.put(classObject.getName(),
                new CppTypeGen.SourceFileData(classObject, cppStrategy.getRequiredDependencies()));
      }

      LOGGER.debug(String.format("Built current container '%s'.", classObject.getName()));
    }
  }

  /**
   * Create top-level CEnum from type object and add it to the
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
// TODO: Top-level enums in C++ moeglich?
//    if (null != type && FSchemaTypeHelper.isEnum(type))
//    {
//      // Get enum constants and convert them to String array
//      Object[] constants = FSchemaTypeHelper.extractEnumArray(type);
//      String[] constantsAsString = Arrays.copyOf(constants, constants.length, String[].class);
//
//      // Create enum and add it to generated elements
//      if (!this.generatedElements.containsKey(type.getName()))
//      {
//        CEnum cEnum = CEnum.factory.create(type.getName(), "", false, constantsAsString); // TODO: Call should be equal to struct creation
//
//        cEnum.setComment(new CCommentImpl(String.format("The '%s' enumeration.", type.getName())));
//
//        this.generatedElements.put(type.getName(),
//                new CppTypeGen.SourceFileData(cEnum, null)); // TODO: Cannot pass CEnum here and what about required includes?
//      }
//    }
  }

  /**
   * Private helper method to translate a source file name to a string
   * that can be used as C++ include guard (e.g. simple_types.hpp will
   * create SIMPLE_TYPES_HPP as output).
   * 
   * @param fileName File name as string (with or without '.hpp')
   * 
   * @return String that can be used as include guard name
   */
  private static String createIncludeGuardName(String fileName)
  {
    // Source file objects from Fabric workspace usually have
    // no file extension, so we need to add one here
    if (!fileName.endsWith(".hpp"))
    {
      fileName += ".hpp";
    }
    
    return fileName.replaceAll("\\.", "_").toUpperCase();
  }
}
