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
  private static final String TARGET_LANGUAGE_KEY = "typegen.target_language";

  /** Key for main class name in properties object */
  private static final String MAIN_CLASS_NAME_KEY = "typegen.main_class_name";

  /** Key for XML framework name in properties object */
  private static final String XML_FRAMEWORK_KEY = "typegen.java.xml_framework";

  /** Key for main package name in properties object */
  private static final String PACKAGE_NAME_KEY = "typegen.java.package_name";

  /** Key for TypeGen factory name in properties object */
  private static final String FACTORY_NAME_KEY = "typegen.factory_name";

  /** Key for Mapper factory name in properties object */
  private static final String MAPPER_NAME_KEY = "typegen.mapper_name";

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
    return String.format("TypeGen module for generation of XML abstractions. "
            + "Valid options are '%s', '%s', '%s' and '%s'.",
            TARGET_LANGUAGE_KEY, XML_FRAMEWORK_KEY, MAIN_CLASS_NAME_KEY, PACKAGE_NAME_KEY);
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
      this.properties.setProperty(FACTORY_NAME_KEY, "fabric.module.typegen.java.JavaTypeGen");
      this.properties.setProperty(MAPPER_NAME_KEY, "fabric.module.typegen.java.JavaMapper");
    }
    // Use C++ type generator
    else if (targetLanguage.toLowerCase().equals("cpp"))
    {
      this.properties.setProperty(FACTORY_NAME_KEY, "fabric.module.typegen.cpp.CppTypeGen");
      this.properties.setProperty(MAPPER_NAME_KEY, "fabric.module.typegen.cpp.CppMapper");
    }
    // Invalid target language provided
    else
    {
      throw new FabricTypeGenException(String.format("Invalid target language '%'. Use one of [java, cpp].", targetLanguage));
    }

    this.properties.remove(TARGET_LANGUAGE_KEY);
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
    String xmlFramework = this.properties.getProperty(XML_FRAMEWORK_KEY);

    // Use Simple as default XML library
    if (!xmlFramework.equals("Simple") && !xmlFramework.equals("XStream") && !xmlFramework.equals("JAXB"))
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
}
