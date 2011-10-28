/** 28.10.2011 16:19 */
package fabric.module.typegen.java;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

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
   * AnnotationData inner class
   *****************************************************************/
  
  private static final class AnnotationData
  {
    /** Required Java import */
    private String[] requiredImports;

    /** Textual representation of the annotation */
    private String annotation;
    
    /**
     * Parameterized constructor.
     *
     * @param requiredImports Array of required Java imports
     * @param annotation Textual representation of the annotation
     */
    public AnnotationData(final String[] requiredImports, final String annotation)
    {
      this.requiredImports = requiredImports;
      this.annotation = annotation;
    }
  }
  
  /*****************************************************************
   * XMLFramework inner class
   *****************************************************************/

  // TODO: Test implementation and change comments
  private static abstract class XMLFramework
  {
    /** Name of the framework */
    public String name;

    /** Maps general key to required, framework-specific imports */
    // TODO: Remove public HashMap<String, String[]> imports;

    /** Maps general key to framework-specific annotations */
    // TODO: Remove public HashMap<String, String[]> annotations;

    // TODO: Change order, so that it complies with order in AttributeContainer
    abstract public AnnotationData[] buildRootAnnotations(final String rootName);

    abstract public AnnotationData[] buildElementAnnotations(final String elementName);

    abstract public AnnotationData[] buildAttributeAnnotations(final String attributeName);

    abstract public AnnotationData[] buildEnumAnnotations(final String enumName);

    abstract public AnnotationData[] buildMainClassListAnnotations(final String listName, final String itemName, final String itemClassName);

    abstract public AnnotationData[] buildLinkedClassListAnnotations(final String listName, final String listClassName, final String itemName, final String itemClassName);

    abstract public AnnotationData[] buildArrayAnnotations(final String arrayName, final String arrayClassName, final String itemName, final String itemClassName);
  }

  // TODO: Add comments
  private static class Simple extends XMLFramework
  {
    public Simple()
    {
      this.name = "Simple";
    }
    
    @Override
    public AnnotationData[] buildRootAnnotations(String rootName)
    {
      AnnotationData rootAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Root" },
              String.format("Root(name = \"%s\")", rootName));
      
      return new AnnotationData[] { rootAnnotation };
    }

    @Override
    public AnnotationData[] buildElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Element" },
              "Element");
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] buildAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Attribute" },
              "Attribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] buildEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Element" },
              "Element");
      
      return new AnnotationData[] { enumAnnotation };
    }

    @Override
    public AnnotationData[] buildMainClassListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData mainClassListAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.ElementList" },
              String.format("ElementList(entry = \"%s\")", itemName));
      
      return new AnnotationData[] { mainClassListAnnotation };
    }

    @Override
    public AnnotationData[] buildLinkedClassListAnnotations(String listName, String listClassName, String itemName, String itemClassName)
    {
      AnnotationData linkedClassListAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.ElementList" },
              String.format("ElementList(inline = true, entry = \"%s\")", itemName));
      
      return new AnnotationData[] { linkedClassListAnnotation };
    }

    @Override
    public AnnotationData[] buildArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.ElementList" },
              String.format("ElementList(inline = true, entry = \"%s\")", itemName));
      
      return new AnnotationData[] { arrayAnnotation };
    }
  }
  
  private static class XStream extends XMLFramework
  {
    public XStream()
    {
      this.name = "XStream";
    }
    
    @Override
    public AnnotationData[] buildRootAnnotations(String rootName)
    {
      AnnotationData rootAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", rootName));
      
      return new AnnotationData[] { rootAnnotation };
    }

    @Override
    public AnnotationData[] buildElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", elementName));
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] buildAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAsAttribute" },
              "XStreamAsAttribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] buildEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", enumName));
      
      return new AnnotationData[] { enumAnnotation };
    }
    
    @Override
    public AnnotationData[] buildMainClassListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData mainClassListAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" },
              "XStreamImplicit");
      
      return new AnnotationData[] { mainClassListAnnotation };
    }

    @Override
    public AnnotationData[] buildLinkedClassListAnnotations(String listName, String listClassName, String itemName, String itemClassName)
    {
      AnnotationData linkedClassListAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" },
              "XStreamImplicit");
      
      return new AnnotationData[] { linkedClassListAnnotation };
    }

    @Override
    public AnnotationData[] buildArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" },
              "XStreamImplicit");
      
      return new AnnotationData[] { arrayAnnotation };
    }    
  }
  
  private static class JAXB extends XMLFramework
  {
    public JAXB()
    {
      this.name = "JAXB";
    }
    
    @Override
    public AnnotationData[] buildRootAnnotations(String rootName)
    {
      AnnotationData rootElementAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlRootElement" },
              String.format("XmlRootElement(name = \"%s\")", rootName));
      
      AnnotationData accessorTypeAnnotation = new AnnotationData(
              new String[] {
                "javax.xml.bind.annotation.XmlAccessorType",
                "javax.xml.bind.annotation.XmlAccessType"
              },
              "XmlAccessorType(XmlAccessType.NONE)");
      
      return new AnnotationData[] { rootElementAnnotation, accessorTypeAnnotation };
    }

    @Override
    public AnnotationData[] buildElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              "XmlElement");
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] buildAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlAttribute" },
              "XmlAttribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] buildEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlEnum" },
              "XmlEnum");
      
      AnnotationData accessorTypeAnnotation = new AnnotationData(
              new String[] {
                "javax.xml.bind.annotation.XmlAccessorType",
                "javax.xml.bind.annotation.XmlAccessType"
              },
              "XmlAccessorType(XmlAccessType.NONE)");
      
      return new AnnotationData[] { enumAnnotation, accessorTypeAnnotation };
    }

    @Override
    public AnnotationData[] buildMainClassListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData mainClassListAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              String.format("XmlElement(name = \"%s\")", itemName));
      
      AnnotationData elementWrapperAnnotation = new AnnotationData(
              new String[] { "	 javax.xml.bind.annotation.XmlElementWrapper" },
              String.format("XmlElementWrapper(name = \"%s\")", listName));
      
      return new AnnotationData[] { mainClassListAnnotation, elementWrapperAnnotation };
    }
    
    @Override
    public AnnotationData[] buildLinkedClassListAnnotations(String listName, String listClassName, String itemName, String itemClassName)
    {
      AnnotationData linkedClassListAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              String.format("XmlElement(name = \"%s\")", itemName));
      
      return new AnnotationData[] { linkedClassListAnnotation };
    }

    @Override
    public AnnotationData[] buildArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              String.format("XmlElement(name = \"%s\")", itemName));
      
      return new AnnotationData[] { arrayAnnotation };
    }
  }

// TODO: Remove when refactoring is done
//  private static final class XMLFramework
//  {
//    /** Name of the framework */
//    public String name;
//
//    /** Maps general key to required, framework-specific imports */
//    public HashMap<String, String[]> imports;
//
//    /** Maps general key to framework-specific annotations */
//    public HashMap<String, String[]> annotations;
//
//    /**
//     * Parameterized constructor.
//     *
//     * @param name Name of the framework
//     * @param imports Map of required imports
//     * @param annotations Map of available annotations
//     */
//    public XMLFramework(final String name, final HashMap<String, String[]> imports, final HashMap<String, String[]> annotations)
//    {
//      this.name = name;
//      this.imports = imports;
//      this.annotations = annotations;
//    }
//  }


  // TODO: Define constants for keys used for annotation lookup

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

  // TODO: Add method header comment
  private static Map<String, XMLFramework> initFrameworks()
  {    
    Map<String, XMLFramework> frameworks = new HashMap<String, XMLFramework>();

    // Add Simple XML library
    AnnotationMapper.XMLFramework simple = new AnnotationMapper.Simple();
    frameworks.put(simple.name, simple);

    // Add XStream library
    AnnotationMapper.XMLFramework xstream = new AnnotationMapper.XStream();
    frameworks.put(xstream.name, xstream);

    // Add JAXB library
    AnnotationMapper.XMLFramework jaxb = new AnnotationMapper.JAXB();
    frameworks.put(jaxb.name, jaxb);

    // Return wrapped map that is unmodifiable
    return Collections.unmodifiableMap(frameworks);
  }

// TODO: Remove unused code later
//  /**
//   * Static method to initialize map of XML frameworks.
//   *
//   * @return Map of XML frameworks
//   */
//  private static Map<String, XMLFramework> initFrameworks()
//  {
//    Map<String, XMLFramework> frameworks = new HashMap<String, XMLFramework>();
//
//    // Add Simple XML library
//    AnnotationMapper.XMLFramework simple = AnnotationMapper.initSimpleFramework();
//    frameworks.put(simple.name, simple);
//
//    // Add XStream library
//    AnnotationMapper.XMLFramework xstream = AnnotationMapper.initXStreamFramework();
//    frameworks.put(xstream.name, xstream);
//
//    // Add JAXB library
//    AnnotationMapper.XMLFramework jaxb = AnnotationMapper.initJAXBFramework();
//    frameworks.put(jaxb.name, jaxb);
//
//    // Return wrapped map that is unmodifiable
//    return Collections.unmodifiableMap(frameworks);
//  }
//
//  /**
//   * Static method to initialize mapping for Simple XML library.
//   *
//   * Link: http://simple.sourceforge.net
//   *
//   * @return Mapping for Simple XML library
//   */
//  private static AnnotationMapper.XMLFramework initSimpleFramework()
//  {
//    HashMap<String, String[]> imports = new HashMap<String, String[]>();
//    imports.put("root", new String[] { "org.simpleframework.xml.Root" });
//    imports.put("attribute", new String[] { "org.simpleframework.xml.Attribute" });
//    imports.put("element", new String[] { "org.simpleframework.xml.Element" });
//    imports.put("elementArray", new String[] { "org.simpleframework.xml.ElementList" });
//    imports.put("elementList", new String[] { "org.simpleframework.xml.ElementList" });
//    imports.put("enum", new String[] { "org.simpleframework.xml.Element" });
//
//    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
//    annotations.put("root", new String[] { "Root(name = \"%s\")" });
//    annotations.put("attribute", new String[] { "Attribute" });
//    annotations.put("element", new String[] { "Element" });
//    annotations.put("elementArray", new String[] { "ElementList(inline = true, entry = \"%s\")" });
//    annotations.put("elementList", new String[] { "ElementList" });
//    annotations.put("enum", new String[] { "Element" });
//
//    return new AnnotationMapper.XMLFramework("Simple", imports, annotations);
//  }
//
//  /**
//   * Static method to initialize mapping for XStream XML library.
//   *
//   * Link: http://xstream.codehaus.org
//   *
//   * @return Mapping for XStream XML library
//   */
//  private static AnnotationMapper.XMLFramework initXStreamFramework()
//  {
//    HashMap<String, String[]> imports = new HashMap<String, String[]>();
//    imports.put("root", new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" });
//    imports.put("attribute", new String[] { "com.thoughtworks.xstream.annotations.XStreamAsAttribute" });
//    imports.put("element", new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" });
//    imports.put("elementArray", new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" });
//    imports.put("elementList", new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" });
//    imports.put("enum", new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" });
//
//    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
//    annotations.put("root", new String[] { "XStreamAlias(\"%s\")" });
//    annotations.put("attribute", new String[] { "XStreamAsAttribute" });
//    annotations.put("element", new String[] { "XStreamAlias(\"%s\")" });
//    annotations.put("elementArray", new String[] { "XStreamImplicit(itemFieldName = \"%s\")" });
//    annotations.put("elementList", new String[] { "XStreamImplicit(itemFieldName = \"%s\")" });
//    annotations.put("enum", new String[] { "XStreamAlias(\"%s\")" });
//
//    return new AnnotationMapper.XMLFramework("XStream", imports, annotations);
//  }
//
//  /**
//   * Static method to initialize mapping for JAXB XML library.
//   *
//   * Link: http://jaxb.java.net
//   *
//   * @return Mapping for JAXB XML library
//   */
//  private static AnnotationMapper.XMLFramework initJAXBFramework()
//  {
//    HashMap<String, String[]> imports = new HashMap<String, String[]>();
//    imports.put("root", new String[] {
//      "javax.xml.bind.annotation.XmlRootElement",
//      "javax.xml.bind.annotation.XmlAccessorType",
//      "javax.xml.bind.annotation.XmlAccessType"
//    });
//    imports.put("attribute", new String[] { "javax.xml.bind.annotation.XmlAttribute" });
//    imports.put("element", new String[] { "javax.xml.bind.annotation.XmlElement" });
//    imports.put("elementArray", new String[] { "javax.xml.bind.annotation.XmlList" });
//    imports.put("elementList", new String[] { "javax.xml.bind.annotation.XmlList" });
//    imports.put("enum", new String[] {
//      "javax.xml.bind.annotation.XmlEnum",
//      "javax.xml.bind.annotation.XmlAccessorType",
//      "javax.xml.bind.annotation.XmlAccessType"
//    });
//
//    HashMap<String, String[]> annotations = new HashMap<String, String[]>();
//    annotations.put("root", new String[] {
//      "XmlRootElement(name = \"%s\")",
//      "XmlAccessorType(XmlAccessType.NONE)"
//    });
//    annotations.put("attribute", new String[] { "XmlAttribute" });
//    annotations.put("element", new String[] { "XmlElement" });
//    annotations.put("elementArray", new String[] { "XmlElements(value = @XmlElement(name = \"%s\"))" });
//    annotations.put("elementList", new String[] { "XmlList" });
//    annotations.put("enum", new String[] {
//      "XmlEnum",
//      "XmlAccessorType(XmlAccessType.NONE)"
//    });
//
//    return new AnnotationMapper.XMLFramework("JAXB", imports, annotations);
//  }

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

  // TODO: Add comment
  private void addUsedImport(final String requiredImport)
  {
    // Add required imports, if this was not done before
    if (!this.usedImports.contains(requiredImport))
    {
      this.usedImports.add(requiredImport);
    }
  }

  // TODO: Add comment
  private void addUsedImports(final String[] requiredImports)
  {
    // Add multiple required imports
    if (null != requiredImports)
    {
      for (String requiredImport: requiredImports)
      {
        this.addUsedImport(requiredImport);
      }
    }
  }
  
  // TODO: Add comment
  private String[] handleAnnotationData(AnnotationData[] annotationData)
  {
    String[] annotations = null;

    if (null != annotationData)
    {
      annotations = new String[annotationData.length];

      for (int i = 0; i < annotationData.length; ++i)
      {
        // Extract all annotation strings
        annotations[i] = annotationData[i].annotation;

        // Collect required Java imports
        this.addUsedImports(annotationData[i].requiredImports);
      }
    }

    return annotations;
  }

  // TODO: Add comment
  public String[] getRootAnnotations(final String rootName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildRootAnnotations(rootName));
  }
  
  public String[] getElementAnnotations(final String elementName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildElementAnnotations(elementName));
  }
  
  public String[] getAttributeAnnotations(final String attributeName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildAttributeAnnotations(attributeName));
  }
  
  public String[] getEnumAnnotations(final String enumName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildEnumAnnotations(enumName));
  }
  
  public String[] getMainClassListAnnotations(final String listName, final String itemName, final String itemClassName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildMainClassListAnnotations(listName, itemName, itemClassName));
  }
  
  public String[] getLinkedClassListAnnotations(final String listName, final String listClassName, final String itemName, final String itemClassName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildLinkedClassListAnnotations(listName, listClassName, itemName, itemClassName));
  }
  
  public String[] getArrayAnnotations(final String arrayName, final String arrayClassName, final String itemName, final String itemClassName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).buildArrayAnnotations(arrayName, arrayClassName, itemName, itemClassName));
  }

// TODO: Remove unused code after refactoring
//  /**
//   * Look-up framework-specific Java annotations for the given, general
//   * annotation key. Valid keys are for example "root", "attribute",
//   * "element", "elementArray", "elementList" and "enum" (others may follow).
//   *
//   * @param key General key for annotation look-up
//   *
//   * @return Framework-specific Java annotations or null
//   */
//  public String[] getAnnotations(final String key)
//  {
//    // Get framework-specific annotations for general key
//    String[] annotations = AnnotationMapper.FRAMEWORKS.get(this.usedFramework).annotations.get(key);
//
//    // Add required imports, if this was not done before
//    String requiredImports[] = AnnotationMapper.FRAMEWORKS.get(this.usedFramework).imports.get(key);
//    if (null != requiredImports)
//    {
//      for (String requiredImport: requiredImports)
//      {
//        if (!this.usedImports.contains(requiredImport))
//        {
//          this.usedImports.add(requiredImport);
//        }
//      }
//    }
//
//    return annotations;
//  }
//
//  /**
//   * Look-up framework-specific Java annotations for the given, general
//   * annotation key. The second parameter can be used to pass a variable
//   * amount of arguments to replace placeholders in the annotation
//   * pattern (e.g. "%s" in "Root(name = "%s")").
//   *
//   * The method will try to replace as many placeholders as possible.
//   * However, if an error occurs, the function will return the pattern
//   * without replacing any placeholders.
//   *
//   * @param key General key for annotation look-up
//   * @param arguments Arguments to replace placeholders
//   *
//   * @return Framework-specific Java annotations or null
//   */
//  public String[] getAnnotations(final String key, final String... arguments)
//  {
//    String[] annotations = this.getAnnotations(key);
//
//    if (null != annotations)
//    {
//      for (int i = 0; i < annotations.length; ++i)
//      {
//        // Try to replace any placeholder...
//        try
//        {
//          annotations[i] = String.format(annotations[i], (Object[])arguments);
//        }
//        // ... or return raw pattern in case of error
//        catch (IllegalFormatException e)
//        {
//          // Exception ignored intentionally
//        }
//      }
//    }
//
//    return annotations;
//  }
}
