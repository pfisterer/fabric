/** 05.03.2012 11:56 */
package fabric.module.typegen;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * Fabric module for type generation. This class uses the input
 * from the treewalker to convert all datatypes of an XSD file
 * to their equivalents in a specific programming language (e.g.
 * Java or C++).
 *
 * @author seidel
 */
public class FabricTypeGenModule implements FabricModule
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(FabricTypeGenModule.class);

  /** Key for target language in properties object */
  public static final String TARGET_LANGUAGE_KEY = "typegen.target_language";

  /** Key for main class name in properties object */
  public static final String MAIN_CLASS_NAME_KEY = "typegen.main_class_name";

  /** Key for XML framework name in properties object */
  public static final String XML_FRAMEWORK_KEY = "typegen.java.xml_framework";

  /** Key for main package name in properties object */
  public static final String PACKAGE_NAME_KEY = "typegen.java.package_name";

  /** Key for flag to create main.cpp file in properties object */
  public static final String CREATE_MAIN_KEY = "typegen.cpp.create_main_cpp";

  /** Key for vector type name in properties object */
  public static final String VECTOR_NAME_KEY = "typegen.cpp.vector_type_name";

  /** Key for vector include in properties object */
  public static final String VECTOR_INCLUDE_KEY = "typegen.cpp.vector_include";

  /** Key for name of TypeGen class used in TypeGenFactory */
  public static final String FACTORY_CLASS_KEY = "typegen.typegen_name";

  /** Key for name of Mapper class used in MapperFactory */
  public static final String MAPPER_CLASS_KEY = "typegen.mapper_name";

  /** Properties object for module configuration */
  private Properties properties = null;

  /**
   * Constructor initializes the internal properties object.
   *
   * @param properties Properties object with module options
   */
  public FabricTypeGenModule(Properties properties)
  {
    this.properties = properties;
  }

  /**
   * Helper method to return module name.
   *
   * @return Module name
   */
  @Override
  public String getName()
  {
    return "typegen";
  }

  /**
   * Helper method to return module description.
   *
   * @return Module description
   */
  @Override
  public String getDescription()
  {
    return String.format("TypeGen module for container class generation. "
            + "Valid options are '%s', '%s', '%s', '%s', '%s' and '%s'.",
            TARGET_LANGUAGE_KEY, MAIN_CLASS_NAME_KEY,
            XML_FRAMEWORK_KEY, PACKAGE_NAME_KEY,
            CREATE_MAIN_KEY, VECTOR_NAME_KEY,
            VECTOR_INCLUDE_KEY);
  }

  /**
   * This method returns a Fabric handler object for the type generator
   * module. It is instantiated with the current workspace and module
   * options. The FabricTypeGenHandler on its turn is used for XSD file
   * processing and the actual, language-specific type class generation.
   *
   * @param workspace Workspace object for type class output
   *
   * @return FabricTypeGenHandler object
   *
   * @throws Exception Error during handler instantiation
   */
  @Override
  public FabricSchemaTreeItemHandler getHandler(Workspace workspace) throws Exception
  {
    this.validateProperties();

    return new FabricTypeGenHandler(workspace, this.properties);
  }

  /**
   * This method validates all module-specific options and translates
   * them where needed (e.g. class names for factory methods). The
   * constructor of this class is called during Fabric setup. At that
   * time, however, the Java properties file from the command line is
   * not yet processed. This is why this method must not be called in
   * the module's constructor, but in the getHandler() method.
   *
   * @throws Exception Error during validation
   */
  private void validateProperties() throws Exception
  {
    // Early exit, if properties object is null
    if (null == this.properties)
    {
      throw new IllegalStateException("Properties object is null. Maybe it was not initialized properly?");
    }

    // Check properties
    this.checkTargetLanguage();
    this.checkMainClassName();
    this.checkXMLFramework();
    this.checkPackageName();
    this.checkCreateMain();
    this.checkVectorTypeName();
    this.checkVectorInclude();

    // Print typegen properties for debug purposes
    for (String key: this.properties.stringPropertyNames())
    {
      if (key.startsWith("typegen."))
      {
        LOGGER.debug(String.format("Property '%s' has value '%s'.", key, this.properties.getProperty(key)));
      }
    }
  }

  /**
   * Check parameter for target language. This property is mandatory
   * and must either be "java" or "cpp". Further target languages
   * might be added later. In case of an illegal argument, an exception
   * is thrown.
   *
   * @throws Exception No or invalid properties passed to module
   */
  private void checkTargetLanguage() throws Exception
  {
    String targetLanguage = this.properties.getProperty(TARGET_LANGUAGE_KEY);

    // No target language defined
    if (null == targetLanguage)
    {
      throw new FabricTypeGenException("No target language specified. Please provide a valid option.");
    }
    // Use Java type generator
    else if (targetLanguage.toLowerCase().equals("java"))
    {
      this.properties.setProperty(FACTORY_CLASS_KEY, "fabric.module.typegen.java.JavaTypeGen");
      this.properties.setProperty(MAPPER_CLASS_KEY, "fabric.module.typegen.java.JavaMapper");
    }
    // Use C++ type generator
    else if (targetLanguage.toLowerCase().equals("cpp"))
    {
      this.properties.setProperty(FACTORY_CLASS_KEY, "fabric.module.typegen.cpp.CppTypeGen");
      this.properties.setProperty(MAPPER_CLASS_KEY, "fabric.module.typegen.cpp.CppMapper");
    }
    // Invalid target language provided
    else
    {
      throw new FabricTypeGenException(String.format("Invalid target language '%s'. Use one of [java, cpp].", targetLanguage));
    }
  }

  /**
   * Check parameter for the main class name. This property is optional.
   * However, it is strongly recommended to provide a value, because
   * otherwise "Main" is used as default.
   */
  private void checkMainClassName()
  {
    String className = this.properties.getProperty(MAIN_CLASS_NAME_KEY, "Main");

    // Capitalize first letter of class name
    if (null != className)
    {
      this.properties.setProperty(MAIN_CLASS_NAME_KEY,
              className.substring(0, 1).toUpperCase() + className.substring(1, className.length()));
    }
  }

  /**
   * Check parameter for XML framework. This property is optional,
   * because it only applies to the Java type generator. In case
   * that no framework name is provided, the Simple XML library is
   * used as default.
   */
  private void checkXMLFramework()
  {
    String xmlFramework = this.properties.getProperty(XML_FRAMEWORK_KEY, "Simple");

    // Use Simple as default XML library
    if (null != xmlFramework && !xmlFramework.equals("Simple") && !xmlFramework.equals("XStream") && !xmlFramework.equals("JAXB"))
    {
      this.properties.setProperty(XML_FRAMEWORK_KEY, "Simple");
    }
  }

  /**
   * Check parameter for the package name. This property is optional.
   * However, it is strongly recommended to provide a value, because
   * otherwise "fabric.package.default" is used as default.
   */
  private void checkPackageName()
  {
    String packageName = this.properties.getProperty(PACKAGE_NAME_KEY, "fabric.package.default");

    // Convert package name to lower case
    if (null != packageName)
    {
      this.properties.setProperty(PACKAGE_NAME_KEY, packageName.toLowerCase());
    }
  }

  /**
   * Check parameter that determines, whether a main.cpp file
   * should be created or not. This property is optional.
   * However, it is strongly recommended to provide a value,
   * because otherwise "true" is used as default.
   * 
   * @throws Exception Invalid value passed for property
   */
  private void checkCreateMain() throws Exception
  {
    String createMain = this.properties.getProperty(CREATE_MAIN_KEY);

    // Create main.cpp on default or if desired
    if (null == createMain || createMain.toLowerCase().equals("true"))
    {
      this.properties.setProperty(CREATE_MAIN_KEY, "true");
    }
    // Do not create file otherwise
    else if (createMain.toLowerCase().equals("false"))
    {
      this.properties.setProperty(CREATE_MAIN_KEY, "false");
    }
    // Invalid value provided
    else
    {
      throw new FabricTypeGenException(String.format("Invalid value '%s' for flag to create main.cpp. Use one of [true, false].", createMain));
    }
  }

  /**
   * Check parameter for the vector type name. This property is
   * optional. However, it is strongly recommended to provide a
   * value, because otherwise "std::vector" is used as default.
   */
  private void checkVectorTypeName()
  {
    String vectorTypeName = this.properties.getProperty(VECTOR_NAME_KEY);

    // Use vector from C++ Standard Library on default
    if (null == vectorTypeName)
    {
      this.properties.setProperty(VECTOR_NAME_KEY, "std::vector");
    }
  }

  /**
   * Check parameter for vector include. This property is optional.
   * However, it is strongly recommended to provide a value, because
   * otherwise "vector" is used as default. If the property was set
   * to "vector.h", it is automatically replaced by "vector", because
   * "vector.h" is deprecated.
   */
  private void checkVectorInclude()
  {
    String vectorInclude = this.properties.getProperty(VECTOR_INCLUDE_KEY);

    // Check for deprecated vector.h
    if (("vector.h").equals(vectorInclude))
    {
      this.properties.setProperty(VECTOR_INCLUDE_KEY, "vector");
      LOGGER.warn("The header file 'vector.h' is deprecated. Will use 'vector' instead.");
    }

    // Use vector from C++ Standard Library on default
    if (null == vectorInclude)
    {
      this.properties.setProperty(VECTOR_INCLUDE_KEY, "vector");
    }
  }
}
