package fabric.module.exi.java;

import java.util.Properties;

import fabric.module.exi.java.lib.xml.XMLFramework;
import fabric.module.exi.java.lib.xml.XMLFrameworkFactory;

/**
 * @author seidel
 */
public class JavaBeanConverter
{
  private XMLFramework xmlFramework;

  public JavaBeanConverter(Properties properties) throws Exception
  {
    // TODO: Implement and instanziate XMLFramework
    this.xmlFramework = XMLFrameworkFactory.getInstance().createXMLFramework(null, properties);
  }

  // TODO: Implement and add comment
  public String generateJavaToXMLCode()
  {
    return this.xmlFramework.generateJavaToXMLCode();
  }

  // TODO: Implement and add comment
  public String generateXMLToInstanceCode()
  {
    return this.xmlFramework.generateXMLToInstanceCode();
  }
}
