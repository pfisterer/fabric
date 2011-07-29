package fabric.wsdlschemaparser.schema;

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.SchemaType;

/**
 * This class represents the Fabric variation of a normalized string,
 * as specified by xs:NOTATION in XML Schema.
 *
 * @author seidel
 */
public class FNOTATION extends FString
{
  /**
   * Parameterless constructor.
   */
  public FNOTATION()
  {
    this(null);
  }

  /**
   * Parameterized constructor creates a normalized string
   * having a type name.
   *
   * @param typeName String's type name
   */
  public FNOTATION(String typeName)
  {
    super(typeName);

    initialize();
  }

  /**
   * Initialize the normalized string. Restrictions can be set here.
   */
  private void initialize()
  {
    addRestriction(SchemaType.FACET_MIN_LENGTH, 0);
    addRestriction(SchemaType.FACET_MAX_LENGTH, MAX_LENGTH);
  }

  /**
   * Get list of valid facets on this datatype. Facets are returned
   * as defined at http://www.w3.org/TR/xmlschema-2/#NOTATION on
   * 28 October 2004.
   *
   * The use of length, minLength and maxLength on datatypes derived
   * from NOTATION is deprecated. Future versions of the specification
   * may remove these facets for this datatype.
   *
   * @return List of valid facets for XSD type NOTATION
   */
  @Override
  public List<Integer> getValidFacets()
  {
    return Arrays.asList(new Integer[]
            {
              SchemaType.FACET_LENGTH,
              SchemaType.FACET_MIN_LENGTH,
              SchemaType.FACET_MAX_LENGTH,
              SchemaType.FACET_PATTERN,
              SchemaType.FACET_ENUMERATION,
              SchemaType.FACET_WHITE_SPACE
            });
  }
}
