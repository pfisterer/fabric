/** 11.10.2011 02:47 */
package fabric.module.exi.java.lib.xml;

/**
 * Converter class for the JAXB XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
 */
public class JAXB extends XMLLibrary
{
  /**
   * Parameterized constructor.
   *
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public JAXB(final String beanClassName) throws Exception
  {
    super(beanClassName);
  }

  /**
   * This method generates code that translates an annotated
   * Java object to a plain XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateJavaToXMLCode() throws Exception
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * This method generates code that translates a plain XML
   * document to a Java class instance.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateXMLToInstanceCode() throws Exception
  {
    // TODO: Implement method
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
