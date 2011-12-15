/** 15.12.2011 18:02 */
package fabric.module.typegen.cpp;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;

/**
 * This helper class contains various methods that are being used
 * to create C++ type definitions and structs for XSD simple types
 * that cannot be mapped to C++ data types directly. This is the
 * case, for example, for QName objects, since they consist of two
 * components (namespace and local part) and there is no such type
 * in C++ on default.
 * 
 * So we create a file with our own XSD simple type definitions
 * that can be included in type classes that are generated by
 * the C++ type generator later on.
 * 
 * @author reichart, seidel
 */
public class CppTypeHelper
{
  /** Name of the C++ header file */
  public static final String FILE_NAME = "simple_type_definitions";

  /** Header file for XSD simple type definitions */
  private static CppHeaderFile sourceFile;

  /**
   * Private constructor, because all methods of this class are static.
   */
  private CppTypeHelper()
  {
    // Empty implementation
  }

  /**
   * Create a header file in the workspace and write type definitions
   * and structs for all required XSD built-in types to it.
   *
   * @param workspace Workspace object for code write-out
   *
   * @throws Exception Error during code generation
   */
  public static void init(Workspace workspace) throws Exception
  {
    // Create header file
    CppTypeHelper.sourceFile = workspace.getC().getCppHeaderFile(CppTypeHelper.FILE_NAME);
    CppTypeHelper.sourceFile.setComment(new CCommentImpl("Type definitions for required XSD simple types."));

    // Surround definitions with include guard
    CppTypeHelper.sourceFile.addBeforeDirective("ifndef SIMPLE_TYPE_DEFINITIONS_HPP");
    CppTypeHelper.sourceFile.addBeforeDirective("define SIMPLE_TYPE_DEFINITIONS_HPP");

    // Create type definitions and structs
    CppTypeHelper.sourceFile.add(CppTypeHelper.createTypeDefs());
    CppTypeHelper.sourceFile.add(CppTypeHelper.createStructs());

    // Close include guard
    CppTypeHelper.sourceFile.addAfterDirective("endif // SIMPLE_TYPE_DEFINITIONS_HPP");
  }

  /**
   * Private helper method to create all custom type definitions.
   *
   * @return Array of CTypeDef objects
   */
  private static CTypeDef[] createTypeDefs()
  {
    ArrayList<CTypeDef> typeDefinitions = new ArrayList<CTypeDef>();

    // Add custom type definitions
    // TODO: Readd after presentation of milestone 3
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::uint8_t", "uint8"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::int8_t", "int8"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::uint16_t", "uint16"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::int16_t", "int16"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::uint32_t", "uint32"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::int32_t", "int32"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::uint64_t", "uint64"));
//    typeDefinitions.add(CppTypeDef.factory.create("wiselib::int64_t", "int64"));

    // TODO: Remove after presentation of milestone 3
    typeDefinitions.add(CppTypeDef.factory.create("unsigned char", "uint8"));
    typeDefinitions.add(CppTypeDef.factory.create("signed char", "int8"));
    typeDefinitions.add(CppTypeDef.factory.create("unsigned int", "uint16"));
    typeDefinitions.add(CppTypeDef.factory.create("signed int", "int16"));
    typeDefinitions.add(CppTypeDef.factory.create("unsigned long int", "uint32"));
    typeDefinitions.add(CppTypeDef.factory.create("signed long int", "int32"));
    typeDefinitions.add(CppTypeDef.factory.create("unsigned long long int", "uint64"));
    typeDefinitions.add(CppTypeDef.factory.create("signed long long int", "int64"));
    
    // Add type definitions for XSD simple types
    typeDefinitions.add(CppTypeHelper.createGYearDefinition());
    typeDefinitions.add(CppTypeHelper.createGMonthDefinition());
    typeDefinitions.add(CppTypeHelper.createGDayDefinition());

    // Return list as array
    return typeDefinitions.toArray(new CTypeDef[0]);
  }

  /**
   * Private helper method to create C structs for all required
   * XSD simple types.
   *
   * @return Array of CStruct objects
   *
   * @throws Exception Error during code generation
   */
  private static CStruct[] createStructs() throws Exception
  {
    ArrayList<CStruct> structs = new ArrayList<CStruct>();

    structs.add(CppTypeHelper.createHexBinaryDefinition());
    structs.add(CppTypeHelper.createBase64BinaryDefinition());
    structs.add(CppTypeHelper.createDateTimeDefinition());
    structs.add(CppTypeHelper.createTimeDefinition());
    structs.add(CppTypeHelper.createDateDefinition());
    // xs:gDate is added as type definition
    structs.add(CppTypeHelper.createGYearMonthDefinition());
    // xs:gMonth is added as type definition
    structs.add(CppTypeHelper.createGMonthDayDefinition());
    // xs:gDay is added as type definition
    structs.add(CppTypeHelper.createDurationDefinition());
    structs.add(CppTypeHelper.createNotationDefinition());
    structs.add(CppTypeHelper.createQNameDefinition());

    return structs.toArray(new CStruct[0]);
  }

  /**
   * Create struct definition for the xs:hexBinary type.
   *
   * typedef struct {
   *   uint16 length;
   *   int8* content;
   * } hexBinary_t;
   *
   * @return Struct with xs:hexBinary definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createHexBinaryDefinition() throws Exception
  {
    CParam length = CParam.factory.create("uint16", "length");
    CParam content = CParam.factory.create("int8*", "content");

    CStruct hexBinaryDefinition = CStruct.factory.create("", "xs_hexBinary_t", true, length, content);
    hexBinaryDefinition.setComment(new CCommentImpl("xs:hexBinary"));

    return hexBinaryDefinition;
  }

  /**
   * Create struct definition for the xs:base64Binary type.
   *
   * typedef struct {
   *   uint16 length;
   *   int8* content;
   * } base64Binary_t;
   *
   * @return Struct with xs:base64Binary definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createBase64BinaryDefinition() throws Exception
  {
    CParam length = CParam.factory.create("uint16", "length");
    CParam content = CParam.factory.create("int8*", "content");

    CStruct base64BinaryDefinition = CStruct.factory.create("", "xs_base64Binary_t", true, length, content);
    base64BinaryDefinition.setComment(new CCommentImpl("xs:base64Binary"));

    return base64BinaryDefinition;
  }

  /**
   * Create struct definition for the xs:dateTime type.
   *
   * typedef struct {
   *   uint16 year;
   *   uint8 month;
   *   uint8 day;
   *   uint8 hour;
   *   uint8 minute;
   *   uint8 second;
   * } dateTime_t;
   *
   * @return Struct with xs:dateTime definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createDateTimeDefinition() throws Exception
  {
    CParam year = CParam.factory.create("uint16", "year");
    CParam month = CParam.factory.create("uint8", "month");
    CParam day = CParam.factory.create("uint8", "day");
    CParam hour = CParam.factory.create("uint8", "hour");
    CParam minute = CParam.factory.create("uint8", "minute");
    CParam second = CParam.factory.create("uint8", "second");

    CStruct dateTimeDefinition = CStruct.factory.create("", "xs_dateTime_t", true, year, month, day, hour, minute, second);
    dateTimeDefinition.setComment(new CCommentImpl("xs:dateTime"));

    return dateTimeDefinition;
  }

  /**
   * Create struct definition for the xs:time type.
   *
   * typedef struct {
   *   uint8 hour;
   *   uint8 minute;
   *   uint8 second;
   *   uint16 millisecond
   * } time_t;
   *
   * @return Struct with xs:time definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createTimeDefinition() throws Exception
  {
    CParam hour = CParam.factory.create("uint8", "hour");
    CParam minute = CParam.factory.create("uint8", "minute");
    CParam second = CParam.factory.create("uint8", "second");
    CParam msecond = CParam.factory.create("uint8", "millisecond");

    CStruct timeDefinition = CStruct.factory.create("", "xs_time_t", true, hour, minute, second, msecond);
    timeDefinition.setComment(new CCommentImpl("xs:time"));

    return timeDefinition;
  }

  /**
   * Create struct definition for the xs:date type.
   *
   * typedef struct {
   *   uint16 year;
   *   uint8 month;
   *   uint8 day;
   * } date_t;
   * 
   * @return Struct with xs:date definition
   * 
   * @throws Exception Error during code generation
   */
  private static CStruct createDateDefinition() throws Exception
  {
    CParam year = CParam.factory.create("uint16", "year");
    CParam month = CParam.factory.create("uint8", "month");
    CParam day = CParam.factory.create("uint8", "day");

    CStruct dateDefinition = CStruct.factory.create("", "xs_date_t", true, year, month, day);
    dateDefinition.setComment(new CCommentImpl("xs:date"));

    return dateDefinition;
  }

  /**
   * Create type definition for xs:gYear.
   *
   * typedef uint8 gYear_t;
   *
   * @return Type definition for xs:gYear
   */
  private static CTypeDef createGYearDefinition()
  {
    return CppTypeDef.factory.create("uint8", "xs_gYear_t");
  }

  /**
   * Create struct definition for the xs:gYearMonth type.
   *
   * typedef struct {
   *   uint16 year;
   *   uint8 month;
   * } gYearMonth_t;
   *
   * @return Struct with xs:gYearMonth definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createGYearMonthDefinition() throws Exception
  {
    CParam year = CParam.factory.create("uint16", "year");
    CParam month = CParam.factory.create("uint8", "month");

    CStruct gYearMonthDefinition = CStruct.factory.create("", "xs_gYearMonth_t", true, year, month);
    gYearMonthDefinition.setComment(new CCommentImpl("xs:gYearMonth"));

    return gYearMonthDefinition;
  }

  /**
   * Create type definition for xs:gMonth.
   *
   * typedef uint8 gMonth_t;
   *
   * @return Type definition for xs:gMonth
   */
  private static CTypeDef createGMonthDefinition()
  {
    return CppTypeDef.factory.create("uint8", "xs_gMonth_t");
  }

  /**
   * Create struct definition for the xs:gMonthDay type.
   *
   * typedef struct {
   *   uint8 month;
   *   uint8 day;
   * } gMonthDay_t;
   *
   * @return Struct with xs:gMonthDay definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createGMonthDayDefinition() throws Exception
  {
    CParam month = CParam.factory.create("uint8", "month");
    CParam day = CParam.factory.create("uint8", "day");

    CStruct gMonthDayDefinition = CStruct.factory.create("", "xs_gMonthDay_t", true, month, day);
    gMonthDayDefinition.setComment(new CCommentImpl("xs:gMonthDay"));

    return gMonthDayDefinition;
  }

  /**
   * Create type definition for xs:gDay.
   *
   * typedef uint8 gDay_t;
   * 
   * @return Type definition for xs:gDay
   */
  private static CTypeDef createGDayDefinition()
  {
    return CppTypeDef.factory.create("uint8", "xs_gDay_t");
  }

  /**
   * Create struct definition for the xs:duration type.
   *
   * typedef struct {
   *   uint16 years;
   *   uint8 months;
   *   uint8 days
   *   uint8 hours
   *   uint8 minutes
   *   uint8 seconds
   * } duration_t;
   *
   * @return Struct with xs:duration definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createDurationDefinition() throws Exception
  {
    CParam years = CParam.factory.create("uint16", "years");
    CParam months = CParam.factory.create("uint8", "months");
    CParam days = CParam.factory.create("uint8", "days");
    CParam hours = CParam.factory.create("uint8", "hours");
    CParam minutes = CParam.factory.create("uint8", "minutes");
    CParam seconds = CParam.factory.create("uint8", "seconds");

    CStruct durationDefinition = CStruct.factory.create("", "xs_duration_t", true, years, months, days, hours, minutes, seconds);
    durationDefinition.setComment(new CCommentImpl("xs:duration"));

    return durationDefinition;
  }

  /**
   * Create struct definition for the xs:NOTATION type.
   *
   * typedef struct {
   *   char* namespaceURI;
   *   char* localPart;
   * } notation_t;
   *
   * @return Struct with xs:NOTATION definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createNotationDefinition() throws Exception
  {
    CParam namespaceURI = CParam.factory.create("char*", "namespaceURI");
    CParam localPart = CParam.factory.create("char*", "localPart");

    CStruct notationDefinition = CStruct.factory.create("", "xs_notation_t", true, namespaceURI, localPart);
    notationDefinition.setComment(new CCommentImpl("xs:NOTATION"));

    return notationDefinition;
  }

  /**
   * Create struct definition for the xs:QName type.
   *
   * typedef struct {
   *   char* namespaceURI;
   *   char* localPart;
   * } qName_t;
   *
   * @return Struct with xs:QName definition
   *
   * @throws Exception Error during code generation
   */
  private static CStruct createQNameDefinition() throws Exception
  {
    CParam namespaceURI = CParam.factory.create("char*", "namespaceURI");
    CParam localPart = CParam.factory.create("char*", "localPart");

    CStruct qNameDefinition = CStruct.factory.create("", "xs_qName_t", true, namespaceURI, localPart);
    qNameDefinition.setComment(new CCommentImpl("xs:QName"));

    return qNameDefinition;
  }
}
