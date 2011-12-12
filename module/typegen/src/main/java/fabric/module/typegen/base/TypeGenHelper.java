/** 12.12.2011 01:16 */
package fabric.module.typegen.base;

import java.util.List;
import java.util.Stack;
import java.util.HashMap;

import org.apache.xmlbeans.SchemaType;

import fabric.module.typegen.AttributeContainer;
import fabric.wsdlschemaparser.schema.FSchemaRestrictions;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * This helper class contains various static methods that are
 * being used by both the JavaTypeGen and the CppTypeGen class.
 * The functions that are provided are of different kind. They
 * were externalized to increase readability and improve the
 * overall maintainability of the TypeGen implementations.
 *
 * @author seidel
 */
public class TypeGenHelper
{
  /**
   * Private constructor, because all methods of this class are static.
   */
  private TypeGenHelper()
  {
    // Empty implementation
  }

  /**
   * Create an AttributeContainer.Restriction object according to
   * the restrictions, which are set in the provided type object.
   * This way we can add restrictions to a container class and
   * take them into account, when we do the source code write-out.
   *
   * @param type FSimpleType object (may be restricted)
   *
   * @return Restriction object for AttributeContainer
   */
  public static AttributeContainer.Restriction createRestrictions(final FSimpleType type)
  {
    AttributeContainer.Restriction restrictions = new AttributeContainer.Restriction();

    // Determine restrictions, which are currently set on the type object
    FSchemaRestrictions schemaRestrictions = type.getRestrictions();
    List<Integer> validFacets = type.getValidFacets();

    // Iterate over all possible restrictions...
    for (Integer facet: validFacets)
    {
      // ... and check which are set
      switch (facet)
      {
        // Type object is length restricted
        case SchemaType.FACET_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.length = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minLength restricted
        case SchemaType.FACET_MIN_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minLength = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxLength restricted
        case SchemaType.FACET_MAX_LENGTH:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxLength = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minInclusive restricted
        case SchemaType.FACET_MIN_INCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minInclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxInclusive restricted
        case SchemaType.FACET_MAX_INCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxInclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is minExclusive restricted
        case SchemaType.FACET_MIN_EXCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.minExclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is maxExclusive restricted
        case SchemaType.FACET_MAX_EXCLUSIVE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.maxExclusive = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is pattern restricted
        case SchemaType.FACET_PATTERN:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.pattern = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is whiteSpace restricted
        case SchemaType.FACET_WHITE_SPACE:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.whiteSpace = TypeGenHelper.translateWhiteSpaceRestriction(
                    schemaRestrictions.getStringValue(facet));
          }
          break;

        // Type object is totalDigits restricted
        case SchemaType.FACET_TOTAL_DIGITS:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.totalDigits = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is fractionDigits restricted
        case SchemaType.FACET_FRACTION_DIGITS:
          if (schemaRestrictions.hasRestriction(facet))
          {
            restrictions.fractionDigits = schemaRestrictions.getStringValue(facet);
          }
          break;

        // Type object is not restricted
        default:
          break;
      }
    }

    return restrictions;
  }

  /**
   * Return simple class name of the Fabric FSchemaType object.
   * This is used for the internal mapping of the basic XSD
   * data types (i.e. xs:string, xs:short, ...).
   *
   * @param type FSchemaType object
   *
   * @return Simple class name of type object
   */
  public static String getFabricTypeName(final FSchemaType type)
  {
    return type.getClass().getSimpleName();
  }

  /**
   * Helper method to check, whether there are any incomplete local
   * builders for the container class, which has been added to the
   * top-level incompleteBuilders stack last.
   *
   * The boolean expression was moved to this function to increase
   * code readability at the location where it is being called.
   * 
   * @param incompleteBuilders Stack of incomplete builders
   * @param incompleteLocalBuilders Map of incomplete local builders
   *
   * @return True if incomplete local builders exist, false otherwise
   */
  public static boolean hasIncompleteLocalBuilders(final Stack<AttributeContainer.Builder> incompleteBuilders,
          HashMap<String, Stack<AttributeContainer.Builder>> incompleteLocalBuilders)
  {
    return (!incompleteBuilders.empty() && !incompleteLocalBuilders.isEmpty() && // Stack and Map must not be empty
            null != incompleteLocalBuilders.get(incompleteBuilders.peek().getName()) && // Stack must be initialized (not null)
            !incompleteLocalBuilders.get(incompleteBuilders.peek().getName()).empty()); // Stack must not be empty
  }

  /**
   * Helper method to check, whether the stack of incomplete local
   * builders for a given container class is empty or not.
   *
   * The boolean expression was moved to this function to increase
   * code readability in the loop where it is being called.
   *
   * @param incompleteLocalBuilders Map of incomplete local builders
   * @param className Name of outer container class
   *
   * @return True while stack is not empty, false otherwise
   */
  public static boolean stackIsNotEmpty(
          final HashMap<String, Stack<AttributeContainer.Builder>> incompleteLocalBuilders, final String className)
  {
    return (incompleteLocalBuilders.containsKey(className) && // Map must contain entry for class name
            null != incompleteLocalBuilders.get(className) && // Stack must be initialized (not null)
            !incompleteLocalBuilders.get(className).empty()); // Stack must not be empty
  }

  /**
   * Translate identifiers for 'whiteSpace' restriction from weird
   * XMLBeans values to proper textual representation. XMLBeans may
   * either deliver strings or numeric identifiers, when we call
   * schemaRestrictions.getStringValue(facet), so we need this rather
   * dirty hack to get a clean textual representation of the current
   * 'whiteSpace' value.
   *
   * @param xmlBeansConstant XMLBeans identifier for 'whiteSpace' value
   *
   * @return Proper string representation of identifier or 'null', if
   * 'whiteSpace' restriction is not set or has unknown value
   */
  public static String translateWhiteSpaceRestriction(final String xmlBeansConstant)
  {
    String result = "";

    if (("preserve").equals(xmlBeansConstant) || ("1").equals(xmlBeansConstant))
    {
      result = "preserve";
    }
    else if (("replace").equals(xmlBeansConstant) || ("2").equals(xmlBeansConstant))
    {
      result = "replace";
    }
    else if (("collapse").equals(xmlBeansConstant) || ("3").equals(xmlBeansConstant))
    {
      result = "collapse";
    }
    else
    {
      result = null;
    }

    return result;
  }
}
