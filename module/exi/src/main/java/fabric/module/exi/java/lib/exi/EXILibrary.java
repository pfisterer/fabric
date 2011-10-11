/** 11.10.2011 14:08 */
package fabric.module.exi.java.lib.exi;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.java.JClass;
import de.uniluebeck.sourcegen.java.JClassCommentImpl;
import de.uniluebeck.sourcegen.java.JModifier;

/**
 * Abstract base class for EXI libraries. Derived classes
 * generate code to serialize and deserialize XML documents
 * with EXI. Each implementation of the interface may operate
 * a different XML library.
 * 
 * @author seidel
 */
abstract public class EXILibrary
{
  /** List of required Java imports */
  protected ArrayList<String> requiredImports;

  /** Class for EXI serialization and deserialization */
  protected JClass serializerClass;

  /** Name of the target Java bean class */
  protected String beanClassName;
  
  /**
   * Default constructor is private. Use parameterized version instead.
   */
  private EXILibrary()
  {
    // Empty implementation
  }

  /**
   * Parameterized constructor initializes EXI converter class
   * and sets bean class name.
   * 
   * @param beanClassName Name of the target Java bean class
   *
   * @throws Exception Error during code generation
   */
  public EXILibrary(final String beanClassName) throws Exception
  {
    this.requiredImports = new ArrayList<String>();

    this.serializerClass = JClass.factory.create(JModifier.PUBLIC, "EXIConverter");
    this.serializerClass.setComment(new JClassCommentImpl("The EXI de-/serializer class."));

    this.beanClassName = beanClassName;
  }
  
  /**
   * Private helper method to add an import to the internal list
   * of required imports. All entries will later be written to
   * the Java source file.
   *
   * Multiple calls to this function with the same argument will
   * result in a single import statement on source code write-out.
   *
   * @param requiredImport Name of required import
   */
  protected void addRequiredImport(final String requiredImport)
  {
    if (null != requiredImport && !this.requiredImports.contains(requiredImport))
    {
      this.requiredImports.add(requiredImport);
    }
  }

  /**
   * Helper method to get a list of required imports for the
   * XML converter class.
   *
   * @return List of requires imports
   */
  public ArrayList<String> getRequiredImports()
  {
    return this.requiredImports;
  }

  /**
   * Initialize the EXILibrary implementation. This means we run
   * all methods that the interface defines, in order to create
   * the EXI de-/serializer class we return eventually.
   *
   * @return JClass object with EXI de-/serializer class
   *
   * @throws Exception Error during code generation
   */
  public JClass init() throws Exception
  {
    // Generate code for EXI serialization
    this.generateSerializeCode();
    
    // Generate code for EXI deserializer
    this.generateDeserializeCode();

    return this.serializerClass;
  }
  
  /**
   * Method that creates code to serialize an XML document with EXI.
   *
   * @throws Exception Error during code generation
   */
  abstract public void generateSerializeCode() throws Exception;

  /**
   * Method that creates code to deserialize an EXI byte stream.
   * 
   * @throws Exception Error during code generation
   */
  abstract public void generateDeserializeCode() throws Exception;
}
