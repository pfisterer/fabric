package fabric.module.typegen.cpp;

import de.uniluebeck.sourcegen.c.CppClass;
import fabric.module.typegen.MapperFactory;
import fabric.module.typegen.base.TypeGen;
import fabric.wsdlschemaparser.schema.FSchemaType;

/**
 * Type generator for C++.
 *
 * @author seidel
 */
public class CppTypeGen implements TypeGen
{
  /** MapperFactory to create CppMapper object */
  private MapperFactory mapperFactory;

  /**
   * This method parses an FSchemaType object and generates
   * a corresponding CppClass object.
   *
   * @param type FSchemaType object
   *
   * @return CppClass object for FSchemaType object
   */
  @Override
  public CppClass parseType(FSchemaType type)
  {
    // TODO Implement method and return correct object

    return null;
  }
}
