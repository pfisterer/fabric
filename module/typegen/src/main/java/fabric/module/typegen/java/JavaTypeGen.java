package fabric.module.typegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.FSchemaType;

/**
 * Type generator for Java.
 *
 * @author seidel
 */
public class JavaTypeGen implements TypeGen
{
  /** MapperFactory to create JavaMapper object */
  private MapperFactory mapperFactory;

  /**
   * This method parses an FSchemaType object and generates
   * a corresponding JClass object.
   *
   * @param type FSchemaType object
   *
   * @return JClass object for FSchemaType object
   */
  @Override
  public JClass parseType(FSchemaType type)
  {
    // TODO Implement method and return correct object

    return null;
  }
}
