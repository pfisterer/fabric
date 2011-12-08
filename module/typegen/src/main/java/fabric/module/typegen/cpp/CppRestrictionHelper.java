/** 07.10.2011 02:00 */
package fabric.module.typegen.cpp;

import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.exceptions.FabricTypeGenException;

/**
 * This helper class contains various static methods to create
 * C++ code that checks XML schema restrictions. These functions
 * were externalized, simply to increase maintainability of the
 * CppTypeGen class.
 */
public class CppRestrictionHelper
{
  /**
   * Private constructor, because all methods of this class are static.
   */
  private CppRestrictionHelper()
  {
    // Empty implementation
  }

  /**
   * Helper method to create a string that checks a boolean expression
   * and throws an IllegalArgumentException with predefined message on
   * failure.
   *
   * This function is used to add parameter and restriction checks
   * to setter methods.
   *
   * @param expression Boolean expression for if-statement
   * @param message Error message to show in exception
   * @param comment Text to add as comment
   *
   * @return String with parameter check code
   */
  public static String createCheckCode(final String expression, final String message, final String comment)
  {
    String result = "";

    // Add comment, if any text is set
    if (!("").equals(comment))
    {
      result += String.format("// %s\n", comment);
    }

    result += String.format("if (%s)", expression);
    result += "\n{";
    result += String.format("\n\tthrow \"%s\";", message);
    result += "\n}\n\n";

    return result;
  }

  /**
   * Helper method for check code creation. This function is provided
   * for convenience. A call to it is equivalent to calling
   * createCheckCode(expression, message, "").
   *
   * @param expression Boolean expression for if-statement
   * @param message Error message to show in exception
   *
   * @return String with parameter check code
   */
  public static String createCheckCode(final String expression, final String message)
  {
    return CppRestrictionHelper.createCheckCode(expression, message, "");
  }

  /**
   * Helper method to create a string that matches a member variable
   * against a predefined regular expression. If the check fails or
   * the XSD pattern is not compatible to Java regular expressions,
   * an exception will be raised.
   *
   * The XML schema pattern language is not fully compatible to
   * Java regular expressions, so we have to catch pattern syntax
   * exceptions in the code we generate.
   *
   * @param memberName Name of member variable to check
   * @param pattern XSD regular expression for pattern matching
   * @param message Error message to show in exception
   *
   * @return String with pattern check code
   */
  public static String createPatternCheckCode(final String memberName, final String pattern, final String message)
  {
    String result = "";
    // TODO: Use Boost library!

    return result;
  }

  /**
   * Helper method to create a string that enforces the 'whiteSpace'
   * restriction on the given member variable. Possible values for
   * 'whiteSpace' are 'preserve', 'replace' and 'collapse'.
   *
   * Value 'preserve' means that the value of the member variable
   * remains unchanged. Value 'replace' means that all whitespace
   * characters (line feeds, tabs, spaces and carriage returns)
   * will be replaced by spaces. Value 'collapse' means that multiple
   * whitespace characters will be replaced with a single space
   * plus leading and tailing whitespace characters will be trimmed.
   * 
   * @param memberName Name of member variable to check
   * @param whiteSpace Either 'preserve', 'replace' or 'collapse'
   *
   * @return String that enforces 'whiteSpace' restriction
   *
   * @throws Exception Invalid value for 'whiteSpace' provided
   */
  public static String createWhiteSpaceCheckCode(final String memberName, final String whiteSpace) throws Exception
  {
    String result = "";
    String message = "// Enforce '%s' for the 'whiteSpace' restriction\n";

    // Preserve whitespace characters, i.e. leave value unchanged
    if (("preserve").equals(whiteSpace))
    {
      result += String.format(message, "preserve");
      result += String.format("// Nothing to do: %s = %s;", memberName, memberName);
    }
    // Replace all whitespace characters with spaces
    else if (("replace").equals(whiteSpace))
    {
      result += String.format(message, "replace");
      result += "unsigned int i;\n";
      result += String.format("for (i=0; i < sizeof(%s)-1; i++)\n{\n", memberName);
      result += String.format("\t%s[i] = (isspace(%s[i]) ? ' ' : %s[i]);\n}\n", memberName, memberName, memberName);
    }
    // Replace multiple whitespace characters with single space and trim
    else if (("collapse").equals(whiteSpace))
    {
      result += String.format(message, "collapse");
      result += String.format("char* pos = %s;\n", memberName);
      result += "// Remove leading whitespaces.\n" +
                "while (isspace (*pos))\n{\n" +
                "\tpos++;\n" +
                "}\n";
      result += String.format(
                "// Replace multiple whitespaces with single space.\n" +
                "while (*pos)\n{\n" +
                "\t*%s++ = *pos\n" +
                "\tif (!isspace (*pos))\n" +
                "\t\tpos++;\n" +
                "\telse\n" +
                "\t\twhile (isspace (*++pos)) {}\n" +
                "}\n", memberName);
      result += String.format(
                "// Remove trailing whitespaces.\n" +
                "*%s = '\\0';\n" +
                "while (isspace (*--%s))\n{\n" +
                "\t*%s = '\\0';\n" +
                "}", memberName, memberName, memberName);
    }
    // Value of 'whiteSpace' was invalid
    else
    {
      throw new FabricTypeGenException(String.format("Unknown value '%s' for the 'whiteSpace' " +
              "restriction on member variable '%s'.", whiteSpace, memberName));
    }

    result += "\n\n";

    return result;
  }

  /**
   * Helper method to create code that checks 'totalDigits'
   * restriction. This function is provided for convenience and
   * calls createDigitsCheckCode() internally.
   *
   * @param memberName Name of member variable to check
   * @param totalDigits Allowed number of total digits
   *
   * @return String with 'totalDigits' check code
   */
  public static String createTotalDigitsCheckCode(final String memberName, final String totalDigits)
  {
    return CppRestrictionHelper.createDigitsCheckCode(memberName, Integer.valueOf(totalDigits), true);
  }

  /**
   * Helper method to create code that checks 'fractionDigits'
   * restriction. This function is provided for convenience and
   * calls createDigitsCheckCode() internally.
   *
   * @param memberName Name of member variable to check
   * @param fractionDigits Allowed number or fraction digits
   *
   * @return String with 'fractionDigits' check code
   */
  public static String createFractionDigitsCheckCode(final String memberName, final String fractionDigits)
  {
    return CppRestrictionHelper.createDigitsCheckCode(memberName, Integer.valueOf(fractionDigits), false);
  }

  /**
   * Private helper method to create code that checks either
   * 'totalDigits' or 'fractionDigits' restriction and returns
   * code as a string.
   *
   * @param memberName Name of member variable to check
   * @param digits Allowed number of digits
   * @param isTotal True to check 'totalDigits' or false
   * to check 'fractionDigits'
   *
   * @return String with digits check code
   */
  private static String createDigitsCheckCode(final String memberName, final int digits, final boolean isTotal)
  {
    String message = "Restriction '%s' violated for member variable '%s'.";
    String comment = "// Check the '%s' restriction\n";

    String result  = "char numberAsString[20];\n"; // TODO: The size of allocated memory depends on the datatype of the number!
    result += "unsigned int digits = sprintf (numberAsString, \"%f\", number);\n";

    if (isTotal)
    {
      result += String.format(comment, "totalDigits");
      // Calculate total digits: Take string representation of value,
      // remove decimal point if any and determine length
      result += "unsigned int totalDigits = digits;\n" +
                "unsigned int i;\n" +
                "for (i=0; i < digits; i++)\n{\n" +
                "\tif (numberAsString[i] == '.')\n\t{\n" +
                "\t\ttotalDigits--;\n\t}\n}\n";
      result += CppRestrictionHelper.createCheckCode(
                    String.format("totalDigits > %d", digits),
                    String.format(message, "totalDigits", memberName));
    }
    else
    {
      result += String.format(comment, "fractionDigits");
      // Calculate fraction digits: Take string representation of value,
      // search for substring that starts after decimal point and determine
      // length.
      if (digits == 0) {
        result += "unsigned int i;\n" +
                  "for (i=0; i < digits; i++)\n{\n";
        result += CppRestrictionHelper.createCheckCode(
                    "numberAsString[i] == '.'",
                    String.format(message, "fractionDigits", memberName));
        result += "\n}\n";
      } else {
        result += CppRestrictionHelper.createCheckCode(
                    String.format("digits > %d && numberAsString[digits - 1 - %d] != '.'", digits, digits),
                    String.format(message, "fractionDigits", memberName));
      }
    }

    return result;
  }

  /**
   * Build expression to check the 'minInclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String minInclusiveExpression(AttributeContainer.RestrictedElementBase member)
  {
    String result = String.format("%s < %d", member.name, Long.parseLong(member.restrictions.minInclusive));

    return result;
  }

  /**
   * Build expression to check the 'maxInclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String maxInclusiveExpression(AttributeContainer.RestrictedElementBase member)
  {
    String result = String.format("%s > %d", member.name, Long.parseLong(member.restrictions.maxInclusive));

    return result;
  }

  /**
   * Build expression to check the 'minExclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String minExclusiveExpression(AttributeContainer.RestrictedElementBase member)
  {
    String result = String.format("%s <= %d", member.name, Long.parseLong(member.restrictions.minExclusive));

    return result;
  }

  /**
   * Build expression to check the 'maxExclusive' restriction, depending
   * on the data type of the member variable.
   *
   * @param member Member variable for comparison in expression
   *
   * @return Expression for restriction check
   */
  public static String maxExclusiveExpression(AttributeContainer.RestrictedElementBase member)
  {
    String result = String.format("%s >= %d", member.name, Long.parseLong(member.restrictions.maxExclusive));

    return result;
  }
}
