/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/** 09.11.2011 22:22 */
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
    /** Required Java imports */
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

  private static abstract class XMLFramework
  {
    /** Name of the framework */
    public String name;

    /**
     * Create annotation for root of XML document.
     *
     * @param rootName Name of the XML root element
     *
     * @return AnnotationData for XML root element
     */
    abstract public AnnotationData[] createRootAnnotations(final String rootName);

    /**
     * Create annotation for XML elements.
     *
     * @param elementName Name of the XML element
     *
     * @return AnnotationData for XML element
     */
    abstract public AnnotationData[] createElementAnnotations(final String elementName);

    /**
     * Create annotation for XML attributes.
     *
     * @param attributeName Name of the XML attribute
     *
     * @return AnnotationData for XML attribute
     */
    abstract public AnnotationData[] createAttributeAnnotations(final String attributeName);

    /**
     * Create annotation for XML enumeration.
     *
     * @param enumName Name of the XML enumeration
     *
     * @return AnnotationData for XML enumeration
     */
    abstract public AnnotationData[] createEnumAnnotations(final String enumName);

    /**
     * Create annotation for XML array.
     *
     * @param arrayName Name of the XML array
     * @param arrayClassName Data type of XML array
     * @param itemName Name of array items (e.g. "value" or "item")
     * @param itemClassName Data type of array items (usually equal to arrayClassName)
     *
     * @return AnnotationData for XML array
     */
    abstract public AnnotationData[] createArrayAnnotations(final String arrayName, final String arrayClassName, final String itemName, final String itemClassName);

    /**
     * Create annotation for XML list.
     *
     * @param listName Name of the XML list
     * @param itemName Name of list item (e.g. "values" or "items")
     * @param itemClassName Data type of list items
     *
     * @return AnnotationData for XML list
     */
    abstract public AnnotationData[] createListAnnotations(final String listName, final String itemName, final String itemClassName);
  }

  /*****************************************************************
   * Inner class for the Simple XML library
   *****************************************************************/

  private static class Simple extends XMLFramework
  {
    public Simple()
    {
      this.name = "Simple";
    }
    
    @Override
    public AnnotationData[] createRootAnnotations(String rootName)
    {
      AnnotationData rootAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Root" },
              String.format("Root(name = \"%s\")", rootName));
      
      return new AnnotationData[] { rootAnnotation };
    }

    @Override
    public AnnotationData[] createElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Element" },
              "Element");
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] createAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Attribute" },
              "Attribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] createEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.Element" },
              "Element");
      
      return new AnnotationData[] { enumAnnotation };
    }
    
    @Override
    public AnnotationData[] createArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.ElementList" },
              String.format("ElementList(inline = true, entry = \"%s\")", itemName));

      return new AnnotationData[] { arrayAnnotation };
    }

    @Override
    public AnnotationData[] createListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData listAnnotation = new AnnotationData(
              new String[] { "org.simpleframework.xml.ElementList" },
              String.format("ElementList(entry = \"%s\")", itemName));
      
      return new AnnotationData[] { listAnnotation };
    }
  }

  /*****************************************************************
   * Inner class for the XStream XML library
   *****************************************************************/
  
  private static class XStream extends XMLFramework
  {
    public XStream()
    {
      this.name = "XStream";
    }
    
    @Override
    public AnnotationData[] createRootAnnotations(String rootName)
    {
      AnnotationData rootAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", rootName));
      
      return new AnnotationData[] { rootAnnotation };
    }

    @Override
    public AnnotationData[] createElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", elementName));
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] createAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAsAttribute" },
              "XStreamAsAttribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] createEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamAlias" },
              String.format("XStreamAlias(\"%s\")", enumName));
      
      return new AnnotationData[] { enumAnnotation };
    }

    @Override
    public AnnotationData[] createArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" },
              String.format("XStreamImplicit(itemFieldName = \"%s\")", arrayName));
      
      return new AnnotationData[] { arrayAnnotation };
    }
    
    @Override
    public AnnotationData[] createListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData listAnnotation = new AnnotationData(
              new String[] { "com.thoughtworks.xstream.annotations.XStreamImplicit" },
              String.format("XStreamImplicit(itemFieldName = \"%s\")", listName));

      return new AnnotationData[] { listAnnotation };
    }
  }

  /*****************************************************************
   * Inner class for the JAXB XML library
   *****************************************************************/

  private static class JAXB extends XMLFramework
  {
    public JAXB()
    {
      this.name = "JAXB";
    }
    
    @Override
    public AnnotationData[] createRootAnnotations(String rootName)
    {
      AnnotationData rootElementAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlRootElement" },
              String.format("XmlRootElement(name = \"%s\")", rootName));

      // This will make JAXB ignore setter/getter methods, because
      // otherwise all data would show up twice in the output
      AnnotationData accessorTypeAnnotation = new AnnotationData(
              new String[] {
                "javax.xml.bind.annotation.XmlAccessorType",
                "javax.xml.bind.annotation.XmlAccessType"
              },
              "XmlAccessorType(XmlAccessType.NONE)");

      return new AnnotationData[] { rootElementAnnotation, accessorTypeAnnotation };
    }

    @Override
    public AnnotationData[] createElementAnnotations(String elementName)
    {
      AnnotationData elementAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              "XmlElement");
      
      return new AnnotationData[] { elementAnnotation };
    }

    @Override
    public AnnotationData[] createAttributeAnnotations(String attributeName)
    {
      AnnotationData attributeAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlAttribute" },
              "XmlAttribute");
      
      return new AnnotationData[] { attributeAnnotation };
    }

    @Override
    public AnnotationData[] createEnumAnnotations(String enumName)
    {
      AnnotationData enumAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlEnum" },
              "XmlEnum");
      
      // This will prevent JAXB from printing data twice
      AnnotationData accessorTypeAnnotation = new AnnotationData(
              new String[] {
                "javax.xml.bind.annotation.XmlAccessorType",
                "javax.xml.bind.annotation.XmlAccessType"
              },
              "XmlAccessorType(XmlAccessType.NONE)");
      
      return new AnnotationData[] { enumAnnotation, accessorTypeAnnotation };
    }

    @Override
    public AnnotationData[] createArrayAnnotations(String arrayName, String arrayClassName, String itemName, String itemClassName)
    {
      AnnotationData arrayAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlElement" },
              String.format("XmlElement(name = \"%s\")", itemName));
      
      return new AnnotationData[] { arrayAnnotation };
    }
    
    @Override
    public AnnotationData[] createListAnnotations(String listName, String itemName, String itemClassName)
    {
      AnnotationData listAnnotation = new AnnotationData(
              new String[] { "javax.xml.bind.annotation.XmlList" },
              "XmlList");
      
      return new AnnotationData[] { listAnnotation };
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
   * Private helper method to add a single package to the
   * list of required Java imports.
   *
   * @param requiredImport Required Java import
   */
  private void addUsedImport(final String requiredImport)
  {
    // Add required import, if this was not done before
    if (!this.usedImports.contains(requiredImport))
    {
      this.usedImports.add(requiredImport);
    }
  }
  
  /**
   * Private helper method to add multiple packages to
   * the list of required Java imports.
   *
   * @param requiredImports Array of required Java imports
   */
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

  /**
   * Private helper method to handle the data that is returned
   * by the implementations of the XMLFramework interface. The
   * method tries to add all required Java imports to an internal
   * list and then returns a String array that contains the
   * textual representation of all requested annotations.
   *
   * @param annotationData AnnotationData from XMLFramework implementation
   *
   * @return String array with annotations
   */
  private String[] handleAnnotationData(AnnotationData[] annotationData)
  {
    String[] annotations = null;

    if (null != annotationData)
    {
      annotations = new String[annotationData.length];

      for (int i = 0; i < annotationData.length; ++i)
      {
        // Collect required Java imports
        this.addUsedImports(annotationData[i].requiredImports);

        // Extract all annotation strings
        annotations[i] = annotationData[i].annotation;
      }
    }

    return annotations;
  }

  /**
   * Get annotations for root of XML document.
   *
   * @param rootName Name of the XML root element
   *
   * @return Annotations for the XML root element
   */
  public String[] getRootAnnotations(final String rootName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createRootAnnotations(rootName));
  }

  /**
   * Get annotations for XML elements.
   *
   * @param elementName Name of the XML element
   *
   * @return Annotations for the XML element
   */
  public String[] getElementAnnotations(final String elementName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createElementAnnotations(elementName));
  }

  /**
   * Get annotations for XML attributes.
   *
   * @param attributeName Name of the XML attribute
   *
   * @return Annotations for the XML attribute
   */
  public String[] getAttributeAnnotations(final String attributeName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createAttributeAnnotations(attributeName));
  }

  /**
   * Get annotations for XML enumerations.
   *
   * @param enumName Name of the XML enumeration
   *
   * @return Annotations for the XML enumeration
   */
  public String[] getEnumAnnotations(final String enumName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createEnumAnnotations(enumName));
  }

  /**
   * Get annotations for XML array.
   *
   * @param arrayName Name of the XML array
   * @param arrayClassName Data type of XML array
   * @param itemName Name of array items (e.g. "value" or "item")
   * @param itemClassName Data type of array items (usually equal to arrayClassName)
   * 
   * @return Annotations for the XML array
   */
  public String[] getArrayAnnotations(final String arrayName, final String arrayClassName, final String itemName, final String itemClassName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createArrayAnnotations(arrayName, arrayClassName, itemName, itemClassName));
  }

  /**
   * Get annotations for XML lists.
   *
   * @param listName Name of the XML list
   * @param itemName Name of list item (e.g. "values" or "items")
   * @param itemClassName Data type of list items
   *
   * @return Annotations for the XML list
   */
  public String[] getListAnnotations(final String listName, final String itemName, final String itemClassName)
  {
    return this.handleAnnotationData(AnnotationMapper.FRAMEWORKS.get(this.usedFramework).createListAnnotations(listName, itemName, itemClassName));
  }
}
