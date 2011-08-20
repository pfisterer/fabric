package fabric.module.typegen.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author seidel
 */
public class AnnotationMapper
{
  public class XMLFramework
  {
    public String name;
    public HashMap<String, String> imports;
    public HashMap<String, String> annotations;

    public XMLFramework(final String name, final HashMap<String, String> imports, final HashMap<String, String> annotations)
    {
      this.name = name;
      this.imports = imports;
      this.annotations = annotations;
    }
  }

  // Make reference to map final (content is made unmodifiable in initMapping())
  private static final Map<String, XMLFramework> FRAMEWORKS = initMapping();

  private String usedFramework;

  private ArrayList<String> usedImports;

  public AnnotationMapper()
  {
    this("Simple");
  }

  public AnnotationMapper(final String usedFramework)
  {
    this.usedFramework = usedFramework;
    this.usedImports = new ArrayList<String>();
  }

  private static Map<String, XMLFramework> initMapping()
  {
    Map<String, XMLFramework> frameworks = new HashMap<String, XMLFramework>();

    AnnotationMapper.XMLFramework simple = AnnotationMapper.initSimpleFramework();
    frameworks.put(simple.name, simple);

    // Return wrapped map that is unmodifiable
    return Collections.unmodifiableMap(frameworks);
  }

  private static AnnotationMapper.XMLFramework initSimpleFramework()
  {
    HashMap<String, String> imports = new HashMap<String, String>();
    imports.put("root", "org.simpleframework.xml.Root");
    imports.put("attribute", "org.simpleframework.xml.Attribute");
    imports.put("element", "org.simpleframework.xml.Element");
    imports.put("elementArray", "org.simpleframework.xml.ElementArray");

    HashMap<String, String> annotations = new HashMap<String, String>();
    annotations.put("root", "Root");
    annotations.put("attribute", "Attribute");
    annotations.put("element", "Element");
    annotations.put("elementArray", "ElementArray");
    
    return (new AnnotationMapper()).new XMLFramework("Simple", imports, annotations);
  }

  public String getUsedFramework()
  {
    return this.usedFramework;
  }
  
  public ArrayList<String> getUsedImports()
  {
    return this.usedImports;
  }

  public String getAnnotation(final String key)
  {
    // Get framework-specific annotation for key
    String annotation = AnnotationMapper.FRAMEWORKS.get(this.usedFramework).annotations.get(key);

    // Add needed imports, if annotation was returned
    if (null != annotation && !this.usedImports.contains(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).imports.get(key)))
    {
      this.usedImports.add(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).imports.get(key));
    }

    return annotation;
  }
}
