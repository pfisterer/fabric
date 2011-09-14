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
    addRestriction(SchemaType.FACET_MAX_LENGTH, MAX_LENGTH);
    addRestriction(SchemaType.FACET_WHITE_SPACE, SchemaType.WS_COLLAPSE);
  }
}
