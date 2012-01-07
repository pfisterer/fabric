/** 07.01.2012 22:57 */
package fabric.module.typegen.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

    // Create source files
    CWorkspace cWorkspace = this.workspace.getC();
    CppHeaderFile cpphf = null;
    CppSourceFile cppsf = null;

    // Create file with definitions for XSD built-in types once
    CppTypeHelper.init(workspace);
    
    // Create file with namespace for util functions once
    CppUtilHelper.init(workspace);

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

      // Add include for XSD built-in types
      if (!cpphf.getFileName().equals(CppTypeHelper.FILE_NAME))
      {
        cpphf.addInclude(CppTypeHelper.FILE_NAME + ".hpp");
      }

      // Add internally required includes (e.g. vector class)
      for (String requiredInclude: sourceFileData.requiredIncludes)
      {
        cpphf.addLibInclude(requiredInclude);
      }

      // Add includes for private member variables with custom data type
      for (CppVar member: sourceFileData.typeObject.getVars(Cpp.PRIVATE))
      {
        // Extract plain type name from typed collections (e.g. vector)
        String typeName = CppTypeGen.extractPlainType(member.getName());

        if (!this.mapper.isBuiltInType(typeName) && // No includes for built-in types
            !this.isLocalEnum(sourceFileData.typeObject, member) && // Do not include local enums
            !typeName.equals(name) && // Do no self-inclusion
            !cpphf.containsInclude(typeName + ".hpp") && // Do no duplicate inclusion
            !this.incompleteLocalBuilders.containsKey(name)) // Do not add include for inner classes
        {
          cpphf.addInclude(typeName + ".hpp");
        }
      }
      
      // Add includes for types that are used in inner classes
      for (CppClass classObject: sourceFileData.typeObject.getNested(Cpp.PUBLIC))
      {
        for (CppVar member: classObject.getVars(Cpp.PRIVATE))
        {
          // Extract plain type name from typed collections (e.g. vector)
          String typeName = CppTypeGen.extractPlainType(member.getName());

          if (!this.mapper.isBuiltInType(typeName) && // No includes for built-in types
              !this.isLocalEnum(classObject, member) && // Do not include local enums
              !typeName.equals(name) && // Do no self-inclusion
              !cpphf.containsInclude(typeName + ".hpp")) // Do no duplicate inclusion
              // No need to check for inner classes here, we do one-level-nesting only
          {
            cpphf.addInclude(typeName + ".hpp");
          }
        }
      }

      // Add namespace
      cpphf.addUsingNamespace("std");

      // Add include guards to header file
      cpphf.addBeforeDirective("ifndef " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addBeforeDirective("define " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
      cpphf.addAfterDirective("endif // " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));

      LOGGER.debug(String.format("Generated new header file '%s'.", name));

      /*****************************************************************
       * Create C++ source file
       *****************************************************************/
      cppsf = cWorkspace.getCppSourceFile(name);
      // No need to add sourceFileData.typeObject here, will be taken from cpphf automatically
      cppsf.setComment(new CCommentImpl(String.format("The '%s' source file.", name)));

      // Add includes
      cppsf.addInclude(cpphf);

      // Add include for util functions once
      if (!cppsf.getFileName().equals(CppUtilHelper.FILE_NAME))
      {
        cppsf.addInclude(CppUtilHelper.FILE_NAME + ".hpp");
      }

      // Add namespace
      cppsf.addUsingNamespace("std");

      LOGGER.debug(String.format("Generated new source file '%s'.", name));
    }

    // Create source file with main method for application
    this.createMainApplication();
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
        classObject.add(Cpp.PUBLIC, innerClassObject);
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
   * Create top-level CEnum from type object and write it to
   * a C++ header file. A top-level enum must be written to
   * its own file, so we bypass the AttributeContainer
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

      // Create enum and write it to a header file
      if (!this.generatedElements.containsKey(type.getName()))
      {
        CEnum cEnum = CEnum.factory.create(type.getName(), "", false, constantsAsString);

        // Create header file and add enum
        CppHeaderFile cpphf = this.workspace.getC().getCppHeaderFile(type.getName());
        cpphf.add(cEnum);
        cpphf.setComment(new CCommentImpl(String.format("The '%s' header file.", type.getName())));

        // Add include guards to header file
        cpphf.addBeforeDirective("ifndef " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
        cpphf.addBeforeDirective("define " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));
        cpphf.addAfterDirective("endif // " + CppTypeGen.createIncludeGuardName(cpphf.getFileName()));

        LOGGER.debug(String.format("Generated new header file '%s'.", type.getName()));
      }
    }
  }

  /**
   * Private helper method to check, whether a member variable
   * is a local enum within the CppClass object or not. This
   * function is being used, to determine necessary includes
   * in writeSourceFiles().
   *
   * @param classObject Class object with possible local enums
   * @param member Member variable to check
   *
   * @return True if member variable is a local enum,
   * false otherwise
   */
  private boolean isLocalEnum(final CppClass classObject, final CppVar member)
  {
    boolean result = false;

    // Check if member variable is a local enum within class object
    for (CEnum enumElement: classObject.getEnums(Cpp.PUBLIC))
    {
      if (enumElement.getName().equals(member.getName()))
      {
        result = true;
      }
    }

    return result;
  }

  /**
   * Create source file for application. It will contain a main
   * method that initializes the root container name, so that
   * one can easily test the C++ type generator or create a
   * new application based on the source file's code.
   *
   * @throws Exception Error during enum generation
   */
  private void createMainApplication() throws Exception
  {
    String rootContainerName = this.properties.getProperty(FabricTypeGenModule.MAIN_CLASS_NAME_KEY);

    // Create source file for application
    CppSourceFile cppsf = workspace.getC().getCppSourceFile("main");
    cppsf.setComment(new CCommentImpl("The application's main file."));

    // Add includes and namespace
    cppsf.addLibInclude("cstdlib");
    cppsf.addLibInclude("iostream");
    cppsf.addInclude(rootContainerName + ".hpp");
    cppsf.addUsingNamespace("std");

    // Create main method
    CParam argc = CParam.factory.create("int", "argc");
    CParam argv = CParam.factory.create("char*", "argv[]");
    CFunSignature mainSignature = CFunSignature.factory.create(argc, argv);
    CFun mainMethod = CFun.factory.create("main", "int", mainSignature);
    mainMethod.setComment(new CCommentImpl("Main function of the application."));

    String methodBody = String.format(
            "%s *%s = new %s();\n\n" +
            "try {\n" +
            "\t// TODO: Add your custom initialization code here\n" +
            "}\n" +
            "catch (char const *e) {\n" +
            "\tcout << e << endl;\n" +
            "}\n\n" +
            "delete %s;\n\n" +
            "return EXIT_SUCCESS;",
            this.firstLetterCapital(rootContainerName), rootContainerName.toLowerCase(),
            this.firstLetterCapital(rootContainerName), rootContainerName.toLowerCase());

    // Add main method to source file
    mainMethod.appendCode(methodBody);
    cppsf.add(mainMethod);

    LOGGER.debug(String.format("Generated application source file '%s'.", cppsf.getFileName()));
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

  /**
   * Private helper method to extract the plain type name
   * from typed collections (e.g. vector<T>). If the type
   * definition is not a typed collection, it will be
   * returned unchanged.
   *
   * For example 'vector<FooType>' will return 'FooType',
   * whereas 'BarType' will remain unchanged.
   *
   * @param typeDefinition Type definition that may or may
   * not contain a typed collection (e.g. a vector)
   *
   * @return Plain type name
   */
  private static String extractPlainType(final String typeDefinition)
  {
    String result = typeDefinition;

    // Match any string that contains a pair of angle brackets:
    // [^<>]* matches 0 or more caracters of any kind (except < and >)
    // <(.*)> groups any characters between the < and > brackets
    Pattern pattern = Pattern.compile("[^<>]*<(.*)>[^<>]*");
    Matcher matcher = pattern.matcher(typeDefinition);

    // Extract plain type name
    if (matcher.find())
    {
      result = matcher.group(1);
    }

    return result;
  }

  /**
   * Private helper method to capitalize the first letter of a string.
   * Function will return null, if argument was null.
   *
   * @param text Text to process
   *
   * @return Text with first letter capitalized or null
   */
  private String firstLetterCapital(final String text)
  {
    return (null == text ? null : text.substring(0, 1).toUpperCase() + text.substring(1, text.length()));
  }
}
