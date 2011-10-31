/** 25.10.2011 13:01 */
package fabric.module.exi.java.lib.xml;

import de.uniluebeck.sourcegen.java.JField;
import de.uniluebeck.sourcegen.java.JFieldCommentImpl;
import de.uniluebeck.sourcegen.java.JMethod;
import de.uniluebeck.sourcegen.java.JMethodCommentImpl;
import de.uniluebeck.sourcegen.java.JMethodSignature;
import de.uniluebeck.sourcegen.java.JModifier;
import de.uniluebeck.sourcegen.java.JParameter;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.NonSimpleListData;
import fabric.module.exi.java.FixValueContainer.SimpleListData;

import java.util.ArrayList;

/**
 * Converter class for the XStream XML library. This class
 * provides means to create code that translates annotated
 * Java objects to XML and vice versa.
 *
 * @author seidel
 */
public class XStream extends XMLLibrary
{
  /**
   * Parameterized constructor.
   *
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public XStream(final String beanClassName) throws Exception
  {
    super(beanClassName);
    
    this.generateStreamInitializationCode();
  }

  /**
   * Private helper method to generate code that creates and
   * initializes the XStream de-/serializer member variable.
   *
   * @throws Exception Error during code generation
   */
  private void generateStreamInitializationCode() throws Exception
  {
    // Create member variable for XStream object
    JField streamObject = JField.factory.create(JModifier.PRIVATE | JModifier.STATIC, "XStream", "stream", "null");
    streamObject.setComment(new JFieldCommentImpl("The XStream de-/serializer member variable."));
    this.converterClass.add(streamObject);

    // Initialize XStream member variable
    this.converterClass.appendStaticCode(String.format("%s.setupStreamObject();", this.converterClass.getName()));

    // Create method for XStream object setup
    JMethod setupStreamObject = JMethod.factory.create(JModifier.PRIVATE | JModifier.STATIC, "void", "setupStreamObject");

    String methodBody = String.format(
            "// Create XStream object\n" +
            "%s.stream = new XStream(new Sun14ReflectionProvider() {\n" +
            "\t@Override\n" +
            "\tprotected boolean fieldModifiersSupported(Field field) {\n" +
            "\t\tint modifiers = field.getModifiers();\n\n" +
            "\t\t// Write static fields to XML document as well when serializing\n" +
            "\t\treturn !(Modifier.isTransient(modifiers));\n" +
            "\t}\n\n" +
            "\t@Override\n" +
            "\tpublic void writeField(Object object, String fieldName, Object value, Class definedIn) {\n" +
            "\t\t// Ignore static fields when deserializing, content is already there!\n" +
            "\t\tif (!Modifier.isStatic(fieldDictionary.field(object.getClass(), fieldName, definedIn).getModifiers())) {\n" +
            "\t\t\tsuper.writeField(object, fieldName, value, definedIn);\n" +
            "\t\t}\n" +
            "\t}\n" +
            "});",
            this.converterClass.getName());
    
    setupStreamObject.getBody().appendSource(methodBody);
    setupStreamObject.setComment(new JMethodCommentImpl("Setup XStream de-/serializer member variable."));

    this.converterClass.add(setupStreamObject);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
    this.addRequiredImport("com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider");
    this.addRequiredImport("java.lang.reflect.Field");
    this.addRequiredImport("java.lang.reflect.Modifier");
  }

  /**
   * This method generates code that translates an annotated
   * Java object to a plain XML document.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateJavaToXMLCode(final ArrayList<ArrayData> fixArrays,
                                    final ArrayList<SimpleListData> fixSimpleLists,
                                    final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, this.beanClassName, "beanObject"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, "String",
            "instanceToXML", jms, new String[] { "Exception" });

    String methodBody = "";

    for (ArrayData array : fixArrays) {
        methodBody += "stream.addImplicitCollection("
                + array.getArrayType()
                + ", \"values\", \"value\", "
                + array.getItemType() + ".class);\n";
    }
    for (SimpleListData list : fixSimpleLists) {
        methodBody += "stream.alias(\"value\", " + list.getItemType() + ".class);\n";
    }
    for (NonSimpleListData list : fixNonSimpleLists) {
        methodBody += "stream.alias(\"value\", " + list.getItemType() + ".class);\n";
    }

    methodBody += String.format(
            "%s.stream.alias(\"%s\", %s.class);\n\n" +
            "StringWriter xmlDocument = new StringWriter();\n" +
            "BufferedWriter serializer = new BufferedWriter(xmlDocument);\n" +
            "serializer.write(%s.stream.toXML(beanObject));\n" +
            "serializer.close();\n\n" +
            "return xmlDocument.toString();",
            this.converterClass.getName(), this.beanClassName.toLowerCase(), this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Serialize bean object to XML document."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
    this.addRequiredImport("java.io.BufferedWriter");
    this.addRequiredImport("java.io.StringWriter");
  }

  /**
   * This method generates code that translates a plain XML
   * document to a Java class instance.
   *
   * @throws Exception Error during code generation
   */
  @Override
  public void generateXMLToInstanceCode(final ArrayList<ArrayData> fixArrays,
                                        final ArrayList<SimpleListData> fixSimpleLists,
                                        final ArrayList<NonSimpleListData> fixNonSimpleLists) throws Exception
  {
    JMethodSignature jms = JMethodSignature.factory.create(
            JParameter.factory.create(JModifier.FINAL, "String", "xmlDocument"));
    JMethod jm = JMethod.factory.create(JModifier.PUBLIC | JModifier.STATIC, this.beanClassName,
            "xmlToInstance", jms, new String[] { "Exception" });

    String methodBody = "";

    for (ArrayData array : fixArrays) {
        methodBody += "stream.addImplicitCollection("
                + array.getArrayType()
                + ", \"values\", \"value\", "
                + array.getItemType() + ".class);\n";
    }
    for (SimpleListData list : fixSimpleLists) {
        methodBody += "stream.alias(\"value\", " + list.getItemType() + ".class);\n";
    }
    for (NonSimpleListData list : fixNonSimpleLists) {
        methodBody += "stream.alias(\"value\", " + list.getItemType() + ".class);\n";
    }
    
    methodBody += String.format(
            "%s.stream.alias(\"%s\", %s.class);\n\n" +
            "return (%s)%s.stream.fromXML(xmlDocument);",
            this.converterClass.getName(), this.beanClassName.toLowerCase(), this.beanClassName, this.beanClassName, this.converterClass.getName());

    jm.getBody().appendSource(methodBody);
    jm.setComment(new JMethodCommentImpl("Deserialize XML document to bean object."));

    this.converterClass.add(jm);

    // Add required Java imports
    this.addRequiredImport("com.thoughtworks.xstream.XStream");
  }
}
