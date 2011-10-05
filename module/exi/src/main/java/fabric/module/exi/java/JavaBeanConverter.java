package fabric.module.exi.java;

import java.util.Properties;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JModifier;
import fabric.module.exi.FabricEXIModule;

import fabric.module.exi.java.lib.xml.XMLFramework;
import fabric.module.exi.java.lib.xml.XMLFrameworkFactory;

/**
 * @author seidel
 */
public class JavaBeanConverter
{
  private Properties properties;

  public JavaBeanConverter(Properties properties) throws Exception
  {
    this.properties = properties;
  }

  // TODO: Generate class with XML converter and all internal methods
  public JClass generateConverterClass() throws Exception
  {
    String beanObjectClassName = String.format("%s.%s",
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY));
            
    XMLFramework xmlFramework = XMLFrameworkFactory.getInstance().createXMLFramework(
            this.properties.getProperty(FabricEXIModule.XML_NAME_KEY),
            beanObjectClassName);
    
    return xmlFramework.init(null); // TODO: Pass elements to fixes values-problem
  }

  // TODO: Implement call to XML converter class and add comment
  public JMethod generateSerializeCall() throws Exception
  {
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, "java.io.File", "toXML");

    String methodBody = "return xmlConverter.javaToXML();";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert Java bean object to XML document."));

    return jm;
  }

  // TODO: Implement call to XML converter class and add comment
  public JMethod generateDeserializeCall() throws Exception
  {
    String className = String.format("%s.%s",
            this.properties.getProperty(FabricEXIModule.PACKAGE_NAME_KEY),
            this.properties.getProperty(FabricEXIModule.MAIN_CLASS_NAME_KEY));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC, className, "toInstance");

    String methodBody = "return xmlConverter.xmlToInstance();";

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Convert XML document to Java bean object."));

    return jm;
  }
}
