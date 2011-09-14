package fabric.module.typegen.base;

import java.util.HashMap;

/**
 * Abstract base class for all language mapper implementations.
 * This class defines various methods, which are being used for
 * mapping creation and datatype look-up.
 *
 * @author seidel
 */
abstract public class Mapper
{
  /** Mapping between Fabric's XSD built-in datatypes and language-specific ones */
  protected HashMap<String, String> types = new HashMap<String, String>();

  /**
   * Constructor initializes the internal datatype mapping.
   */
  public Mapper()
  {
    createMapping();
  }

  /**
   * Look-up the language-specific representation of an XSD built-in type.
   *
   * @param type Fabric's type name for look-up
   *
   * @return String with the name of the corresponding language-specific datatype
   *
   * @throws IllegalArgumentException No matching mapping found
   */
  public String lookup(final String type) throws IllegalArgumentException
  {
    if (types.containsKey(type))
    {
      return types.get(type);
    }

    throw new IllegalArgumentException(String.format("No mapping found for datatype '%s'.", type));
  }

  /**
   * This method populates the internal map with
   * language-specific datatype mappings.
   */
  abstract protected void createMapping();
}
