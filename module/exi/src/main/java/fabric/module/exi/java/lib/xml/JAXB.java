/** 11.10.2011 02:47 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;

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
	  JMethodSignature jms = JMethodSignature.factory.create(
			  JParameter.factory.create(JModifier.FINAL, this.beanClassName, "beanObject"));
	  JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
			  "instanceToXML", jms, new String[] { "Exception" });

	  String methodBody =
              "JAXBContext context = JAXBContext.newInstance(%s.class);\n" +
              "Marshaller marshaller = context.createMarshaller();\n" +
              "marshaller.setProperty(Marshaller.JAXB_ENCODING, \"UTF-8\");\n" +
              "StringWriter xmlDocument = new StringWriter();\n\n" +
              "marshaller.marshal(beanObject, xmlDocument);\n" +
              "return xmlDocument.toString();";

	  jm.getBody().appendSource(String.format(methodBody, this.beanClassName));
	  jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

	  this.converterClass.add(jm);

	  // Add required Java imports
	  this.addRequiredImport("javax.xml.bind.JAXBContext");
	  this.addRequiredImport("javax.xml.bind.Marshaller");
	  this.addRequiredImport("java.io.StringWriter");
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
	  JMethodSignature jms = JMethodSignature.factory.create(
			  JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
	  JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, this.beanClassName,
			  "xmlToInstance", jms, new String[] { "Exception" });

	  String methodBody =
		  	  "JAXBContext context = JAXBContext.newInstance(%s.class);\n" +
		  	  "Unmarshaller unmarshaller = context.createUnmarshaller();\n" +
		  	  "return (%s) unmarshaller.unmarshal(xmlDocument);";

	  jm.getBody().appendSource(String.format(methodBody, this.beanClassName, this.beanClassName));
	  jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));

	  this.converterClass.add(jm);

	  // Add required Java imports
	  this.addRequiredImport("javax.xml.bind.JAXBContext");
	  this.addRequiredImport("javax.xml.bind.Unmarshaller");
  }
}
