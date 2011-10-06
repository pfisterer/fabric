/** 25.09.2011 19:01 */
package fabric.module.typegen.java;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import java.util.IllegalFormatException;
import fabric.module.typegen.exceptions.UnsupportedXMLFrameworkException;

/**
 * The AnnotationMapper class can return framework-specific Java
 * annotations for JavaToXML conversions. The annotations on their
 * part can be attached to Java Beans in order to convert them to
 * XML documents later on. Partly supported frameworks are Simple,
 * XStream and JAXB (others may be added later).
 *
 * @author seidel
 */
public class AnnotationMapper
{
  /*****************************************************************
   * XMLFramework inner class
   *****************************************************************/

  private static final class XMLFramework
  {
    /** Name of the framework */
    public String name;

    /** Maps general key to required, framework-specific import */
    public HashMap<String, String[]> imports;

    /** Maps general key to framework-specific annotation */
    public HashMap<String, String[]> annotations;

    /**
     * Parameterized constructor.
     *
     * @param name Name of the framework
     * @param imports Map of required imports
     * @param annotations Map of available annotations
     */
    public XMLFramework(final String name, final HashMap<String, String[]> imports, final HashMap<String, String[]> annotations)
    {
      this.name = name;
      this.imports = imports;
      this.annotations = annotations;
    }
  }

  /*****************************************************************
   * AnnotationMapper outer class
   *****************************************************************/

  /** Map with framework data */
  // Make reference to map final (content is made unmodifiable in initMapping())
  private static final Map<String, XMLFramework> FRAMEWORKS = initFrameworks();

  /** Name of framework, which is currently being used */
  private String usedFramework;

  /** Imports, which are currently required */
  private ArrayList<String> usedImports;

  /**
   * Parameterless constructor uses Simple XML library on default.
   *
   * @throws Exception Required framework not supported
   */
  public AnnotationMapper() throws Exception
  {
    this("Simple");
  }

  /**
   * Parameterized constructor for framework selection. Valid
   * XML framework names are "Simple", "XStream" and "JAXB".
   *
   * @param usedFramework Name of framework to use
   *
   * @throws Exception Required framework not supported
   */
  public AnnotationMapper(final String usedFramework) throws Exception
  {
    // Check if desired framework is supported
    if (!AnnotationMapper.FRAMEWORKS.containsKey(usedFramework))
    {
      throw new UnsupportedXMLFrameworkException(String.format("Unsupported XML framework '%s' requested.", usedFramework));
    }

    this.usedFramework = usedFramework;
    this.usedImports = new ArrayList<String>();
  }

  /**
   * Static method to initialize map of XML frameworks.
   *
   * @return Map of XML frameworks
   */
  private static Map<String, XMLFramework> initFrameworks()
  {
    Map<String, XMLFramework> frameworks = new HashMap<String, XMLFramework>();

    // Add Simple XML library
    AnnotationMapper.XMLFramework simple = AnnotationMapper.initSimpleFramework();
    frameworks.put(simple.name, simple);

    // Add XStream library
    AnnotationMapper.XMLFramework xstream = AnnotationMapper.initXStreamFramework();
    frameworks.put(xstream.name, xstream);

    // Add JAXB library
    AnnotationMapper.XMLFramework jaxb = AnnotationMapper.initJAXBFramework();
    frameworks.put(jaxb.name, jaxb);

    // Return wrapped map that is unmodifiable
    return Collections.unmodifiableMap(frameworks);
  }

  /**
   * Static method to initialize mapping for Simple XML library.
   *
   * Link: http://simple.sourceforge.net
   *
   * @return Mapping for Simple XML library
   */
  private static AnnotationMapper.XMLFramework initSimpleFramework()
  {
    HashMap<String, String[]> imports = new HashMap<String, String[]>();
    imports.put("root", new String[]{"org.simpleframework.xml.Root"});
    imports.put("attribute", new String[]{"org.simpleframework.xml.Attribute"});
    imports.put("element", new String[]{"org.simpleframework.xml.Element"});
    imports.put("elementArray", new String[]{"org.simpleframework.xml.ElementList"});
    imports.put("enum", new String[]{"org.simpleframework.xml.Element"});

    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
    annotations.put("root", new String[]{"Root(name = \"%s\")"});
    annotations.put("attribute", new String[]{"Attribute"});
    annotations.put("element", new String[]{"Element"});
    annotations.put("elementArray", new String[]{"ElementList"});
    annotations.put("enum", new String[]{"Element"});

    return new AnnotationMapper.XMLFramework("Simple", imports, annotations);
  }

  /**
   * Static method to initialize mapping for XStream XML library.
   *
   * Link: http://xstream.codehaus.org
   *
   * @return Mapping for XStream XML library
   */
  private static AnnotationMapper.XMLFramework initXStreamFramework()
  {
    HashMap<String, String[]> imports = new HashMap<String, String[]>();
    imports.put("root", new String[]{"com.thoughtworks.xstream.annotations.XStreamAlias"});
    imports.put("attribute", new String[]{"com.thoughtworks.xstream.annotations.XStreamAsAttribute"});
    imports.put("element", new String[]{"com.thoughtworks.xstream.annotations.XStreamAlias"});
    imports.put("elementArray", new String[]{"com.thoughtworks.xstream.annotations.XStreamImplicit"});
    imports.put("enum", new String[]{"com.thoughtworks.xstream.annotations.XStreamAlias"});

    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
    annotations.put("root", new String[]{"XStreamAlias(\"%s\")"});
    annotations.put("attribute", new String[]{"XStreamAsAttribute"});
    annotations.put("element", new String[]{"XStreamAlias(\"%s\")"});
    annotations.put("elementArray", new String[]{"XStreamImplicit(itemFieldName=\"%s\")"});
    annotations.put("enum", new String[]{"XStreamAlias(\"%s\")"});

    return new AnnotationMapper.XMLFramework("XStream", imports, annotations);
  }

  /**
   * Static method to initialize mapping for JAXB XML library.
   *
   * Link: http://jaxb.java.net
   *
   * @return Mapping for JAXB XML library
   */
  private static AnnotationMapper.XMLFramework initJAXBFramework()
  {
    HashMap<String, String[]> imports = new HashMap<String, String[]>();
    imports.put("root", new String[]{   "javax.xml.bind.annotation.XmlRootElement",
                                        "javax.xml.bind.annotation.XmlAccessType",
                                        "javax.xml.bind.annotation.XmlAccessorType"});
    imports.put("attribute", new String[]{"javax.xml.bind.annotation.XmlAttribute"});
    imports.put("element", new String[]{"javax.xml.bind.annotation.XmlElement"});
    imports.put("elementArray", new String[]{"javax.xml.bind.annotation.XmlList"});
    imports.put("enum", new String[]{   "javax.xml.bind.annotation.XmlEnum",
                                        "javax.xml.bind.annotation.XmlAccessType",
                                        "javax.xml.bind.annotation.XmlAccessorType"});

    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
    annotations.put("root", new String[]{"XmlRootElement(name = \"%s\")", "XmlAccessorType(XmlAccessType.NONE)"});
    annotations.put("attribute", new String[]{"XmlAttribute"});
    annotations.put("element", new String[]{"XmlElement"});
    annotations.put("elementArray", new String[]{"XmlList"});
    annotations.put("enum", new String[]{"XmlEnum", "XmlAccessorType(XmlAccessType.NONE)"});

    return new AnnotationMapper.XMLFramework("JAXB", imports, annotations);
  }

  /**
   * Get name of XML framework that is currently being used.
   *
   * @return Name of used framework
   */
  public String getUsedFramework()
  {
    return this.usedFramework;
  }

  /**
   * Get list of imports, which are currently required.
   *
   * @return List of required imports
   */
  public ArrayList<String> getUsedImports()
  {
    return this.usedImports;
  }

  /**
   * Look-up framework-specific Java annotation for the given, general
   * annotation key. Valid keys are for example "root", "attribute",
   * "element", "elementArray" and "enum" (others may follow).
   *
   * @param key General key for annotation look-up
   *
   * @return Framework-specific Java annotation or null
   */
  public String[] getAnnotation(final String key)
  {
    // Get framework-specific annotation for general key
    String[] annotations = AnnotationMapper.FRAMEWORKS.get(this.usedFramework).annotations.get(key);

    // Add required import, if this was not done before
    String[] requiredImports = AnnotationMapper.FRAMEWORKS.get(this.usedFramework).imports.get(key);
    if (null != annotations)
    {
        for (String requiredImport : requiredImports) {
            if (!this.usedImports.contains(requiredImport)) {
                this.usedImports.add(requiredImport);
            }
        }
    }

    return annotations;
  }

  /**
   * Look-up framework-specific Java annotations for the given, general
   * annotation key. The second parameter can be used to pass a variable
   * amount of arguments to replace placeholders in the annotation
   * pattern (e.g. "%s" in "Root(name = "%s")").
   *
   * The method will try to replace as many placeholders as possible.
   * However, if an error occurs, the function will return the pattern
   * without replacing any placeholders.
   *
   * @param key General key for annotation look-up
   * @param arguments Arguments to replace placeholders
   *
   * @return Framework-specific Java annotation or null
   */
  public String[] getAnnotation(final String key, final String... arguments)
  {
    String[] annotations = this.getAnnotation(key);
      String[] ret = new String[annotations.length];

      for (int i = 0; i < annotations.length; i++) {
        // Try to replace any placeholder...
        try
        {
          ret[i] = String.format(annotations[i], (Object[])arguments);
        }
        // ... and return raw pattern in case of error
        catch (IllegalFormatException e)
        {
          // Exception ignored intentionally
        }

      }

    return ret;
  }
}
