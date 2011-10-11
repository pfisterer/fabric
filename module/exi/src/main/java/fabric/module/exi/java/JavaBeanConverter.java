package fabric.module.exi.java;

import java.util.Properties;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import de.uniluebeck.sourcegen.java.JSourceFile;

import fabric.module.exi.FabricEXIModule;
import fabric.module.exi.exceptions.FabricEXIException;

import fabric.module.exi.java.lib.xml.XMLLibrary;
import fabric.module.exi.java.lib.xml.XMLLibraryFactory;

/**
 * @author seidel
 */
public class JavaBeanConverter
{
  private Properties properties;

  private String beanClassName;

  private String converterClassName;

  public JavaBeanConverter(Properties properties) throws Exception
  {
    this.properties = properties;
    
    this.beanClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY);

    this.converterClassName = this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY) + "Converter";
  }

  // TODO: Generate class with XML converter and all internal methods
  public void generateConverterClass(JSourceFile sourceFile) throws Exception
  {
    if (null == sourceFile)
    {
      throw new FabricEXIException("Cannot create XML converter class. Source file is null.");
    }
    else
    {
      XMLLibrary xmlFramework = XMLLibraryFactory.getInstance().createXMLLibrary(
              this.properties.getProperty(FabricEXIModule.XMLLIBRARY_NAME_KEY),
              this.beanClassName);
    
      sourceFile.add(xmlFramework.init(null)); // TODO: Pass elements to fixes values-problem
      
      // Add required imports AFTER initialization
      for (String requiredImport: xmlFramework.getRequiredImports())
      {
        sourceFile.addImport(requiredImport);
      }
    }
  }

  // TODO: Implement call to XML converter class and add comment
  public JMethod generateSerializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, beanClassName, "beanObject"));

    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, "String", "toXML", jms, new String[] { "Exception" });
    
    String methodBody = String.format("return %s.instanceToXML(beanObject);", this.converterClassName);

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert Java bean object to XML document."));

    return jm;
  }

  // TODO: Implement call to XML converter class and add comment
  public JMethod generateDeserializeCall() throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));

    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, beanClassName, "toInstance", jms, new String[] { "Exception" });

    String methodBody = String.format("return %s.xmlToInstance(xmlDocument);", this.converterClassName);

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert XML document to Java bean object."));

    return jm;
  }
}
