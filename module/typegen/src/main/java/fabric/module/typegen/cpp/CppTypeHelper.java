package fabric.module.typegen.cpp;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;
import java.util.Properties;

/**
 * @author seidel
 */
public class CppTypeHelper
{
  private CppHeaderFile sourceFile;

  // TODO: Main method used for debug purposes only, remove later!
  public static void main(String[] args)
  {
    try
    {
      CppTypeHelper helper = new CppTypeHelper(new Workspace(new Properties()));

      System.out.println(helper.sourceFile.toString());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // TODO: Add documentation
  public CppTypeHelper(Workspace workspace) throws Exception {
    this.sourceFile = workspace.getC().getCppHeaderFile("simple_type_definitions.hpp");

    // TODO: Surround definitions with guard?
      sourceFile.addBeforeDirective("ifndef SIMPLE_TYPE_DEFINITIONS_HPP");
      sourceFile.addBeforeDirective("define SIMPLE_TYPE_DEFINITIONS_HPP");
      sourceFile.addAfterDirective("endif // SIMPLE_TYPE_DEFINITIONS_HPP");

      CTypeDef uint8    = CppTypeDef.factory.create("wiselib::uint8_t",   "uint8");
      CTypeDef int8     = CppTypeDef.factory.create("wiselib::int8_t",    "int8");
      CTypeDef uint16   = CppTypeDef.factory.create("wiselib::uint16_t",  "uint16");
      CTypeDef uint32   = CppTypeDef.factory.create("wiselib::uint32_t",  "uint32");
      CTypeDef int32    = CppTypeDef.factory.create("wiselib::int32_t",   "int32");
      CTypeDef uint64   = CppTypeDef.factory.create("wiselib::uint64_t",  "uint64");
      CTypeDef int64    = CppTypeDef.factory.create("wiselib::int64_t",   "int64");
      this.sourceFile.add(uint8, int8, uint16, uint32, int32, uint64, int64);

      createBase64BinaryDefinition();
      createHexBinaryDefinition();
      createDateDefinition();
      createTimeDefinition();
      createDateTimeDefinition();
      createDurationDefinition();
      createGYearDefinition();
      createGYearMonthDefinition();
      createGMonthDefinition();
      createGMonthDayDefinition();
      createGDayDefinition();
      createQNameDefinition();
      createNotationDefinition();
  }

    /**
     * Creates the struct definition for the xs:hexBinary type.
     *
     * typedef struct {
     *   uint16 length;
     *   int8* content;
     * } hexBinary_t;
     *
     * @throws Exception
     */
  public void createHexBinaryDefinition() throws Exception
  {
      CParam length     = CParam.factory.create("uint16", "length");
      CParam content    = CParam.factory.create("int8*", "content");
      CStruct struct    = CStruct.factory.create("", "hexBinary_t", true, length, content);
      struct.setComment(new CCommentImpl("xs:hexBinary"));
      this.sourceFile.add(struct);
  }

    /**
     * Creates the struct definition for the xs:base64Binary type.
     *
     * typedef struct {
     *   uint16 length;
     *   int8* content;
     * } base64Binary_t;
     *
     * @throws Exception
     */
    public void createBase64BinaryDefinition() throws Exception
    {
        CParam length     = CParam.factory.create("uint16", "length");
        CParam content    = CParam.factory.create("int8*", "content");
        CStruct struct    = CStruct.factory.create("", "base64Binary_t", true, length, content);
        struct.setComment(new CCommentImpl("xs:base64Binary"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the struct definition for the xs:dateTime type.
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
     * @throws Exception
     */
    public void createDateTimeDefinition() throws Exception
    {
        CParam year     = CParam.factory.create("uint16", "year");
        CParam month    = CParam.factory.create("uint8", "month");
        CParam day      = CParam.factory.create("uint8", "day");
        CParam hour     = CParam.factory.create("uint8", "hour");
        CParam minute   = CParam.factory.create("uint8", "minute");
        CParam second   = CParam.factory.create("uint8", "second");
        CStruct struct  = CStruct.factory.create("", "dateTime_t", true, year, month, day, hour, minute, second);
        struct.setComment(new CCommentImpl("xs:dateTime"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the struct definition for the xs:date type.
     *
     * typedef struct {
     *   uint16 year;
     *   uint8 month;
     *   uint8 day;
     * } date_t;
     *
     * @throws Exception
     */
    public void createDateDefinition() throws Exception
    {
        CParam year     = CParam.factory.create("uint16", "year");
        CParam month    = CParam.factory.create("uint8", "month");
        CParam day      = CParam.factory.create("uint8", "day");
        CStruct struct  = CStruct.factory.create("", "date_t", true, year, month, day);
        struct.setComment(new CCommentImpl("xs:date"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the struct definition for the xs:time type.
     *
     * typedef struct {
     *   uint8 hour;
     *   uint8 minute;
     *   uint8 second;
     *   uint16 millisecond
     * } time_t;
     *
     * @throws Exception
     */
    public void createTimeDefinition() throws Exception
    {
        CParam hour     = CParam.factory.create("uint8", "hour");
        CParam minute   = CParam.factory.create("uint8", "minute");
        CParam second   = CParam.factory.create("uint8", "second");
        CParam msecond  = CParam.factory.create("uint8", "millisecond");
        CStruct struct  = CStruct.factory.create("", "time_t", true, hour, minute, second, msecond);
        struct.setComment(new CCommentImpl("xs:time"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the struct definition for the xs:duration type.
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
     * @throws Exception
     */
    public void createDurationDefinition() throws Exception
    {
        CParam years    = CParam.factory.create("uint16", "years");
        CParam months   = CParam.factory.create("uint8", "months");
        CParam days     = CParam.factory.create("uint8", "days");
        CParam hours    = CParam.factory.create("uint8", "hours");
        CParam minutes  = CParam.factory.create("uint8", "minutes");
        CParam seconds  = CParam.factory.create("uint8", "seconds");
        CStruct struct  = CStruct.factory.create("", "duration_t", true, years, months, days, hours, minutes, seconds);
        struct.setComment(new CCommentImpl("xs:duration"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the type definition for xs:gYear.
     *
     * typedef uint8 gYear_t;
     *
     * @throws Exception
     */
    public void createGYearDefinition() throws Exception
    {
        CTypeDef gYear = CppTypeDef.factory.create("uint8", "gYear_t");
        this.sourceFile.add(gYear);
    }

    /**
     * Creates the struct definition for the xs:gYearMonth type.
     *
     * typedef struct {
     *   uint16 year;
     *   uint8 month;
     * } gYearMonth_t;
     *
     * @throws Exception
     */
    public void createGYearMonthDefinition() throws Exception
    {
        CParam year     = CParam.factory.create("uint16", "year");
        CParam month    = CParam.factory.create("uint8", "month");
        CStruct struct  = CStruct.factory.create("", "gYearMonth_t", true, year, month);
        struct.setComment(new CCommentImpl("xs:gYearMonth"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the type definition for xs:gMonth.
     *
     * typedef uint8 gMonth_t;
     *
     * @throws Exception
     */
    public void createGMonthDefinition() throws Exception
    {
        CTypeDef gMonth = CppTypeDef.factory.create("uint8", "gMonth_t");
        this.sourceFile.add(gMonth);
    }

    /**
     * Creates the struct definition for the xs:gMonthDay type.
     *
     * typedef struct {
     *   uint8 month;
     *   uint8 day;
     * } gMonthDay_t;
     *
     * @throws Exception
     */
    public void createGMonthDayDefinition() throws Exception
    {
        CParam month    = CParam.factory.create("uint8", "month");
        CParam day      = CParam.factory.create("uint8", "day");
        CStruct struct  = CStruct.factory.create("", "gMonthDay_t", true, month, day);
        struct.setComment(new CCommentImpl("xs:gMonthDay"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the type definition for xs:gDay.
     *
     * typedef uint8 gDay_t;
     *
     * @throws Exception
     */
    public void createGDayDefinition() throws Exception
    {
        CTypeDef gDay = CppTypeDef.factory.create("uint8", "gDay_t");
        this.sourceFile.add(gDay);
    }

    /**
     * Creates the struct definition for the xs:NOTATION type.
     *
     * typedef struct {
     *   char* namespaceURI;
     *   char* localPart;
     * } notation_t;
     *
     * @throws Exception
     */
    public void createNotationDefinition() throws Exception
    {
        CParam namespaceURI = CParam.factory.create("char*", "namespaceURI");
        CParam localPart    = CParam.factory.create("char*", "localPart");
        CStruct struct      = CStruct.factory.create("", "notation_t", true, namespaceURI, localPart);
        struct.setComment(new CCommentImpl("xs:NOTATION"));
        this.sourceFile.add(struct);
    }

    /**
     * Creates the struct definition for the xs:QName type.
     *
     * typedef struct {
     *   char* namespaceURI;
     *   char* localPart;
     * } qName_t;
     *
     * @throws Exception
     */
  public void createQNameDefinition() throws Exception
  {
    CParam namespaceURI = CParam.factory.create("char*", "namespaceURI");
    CParam localPart    = CParam.factory.create("char*", "localPart");
    CStruct struct      = CStruct.factory.create("", "qName_t", true, namespaceURI, localPart);
    struct.setComment(new CCommentImpl("xs:QName"));
    this.sourceFile.add(struct);
  }
}