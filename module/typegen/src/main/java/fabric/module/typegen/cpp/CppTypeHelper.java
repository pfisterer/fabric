package fabric.module.typegen.cpp;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.CStruct;
import de.uniluebeck.sourcegen.c.CppHeaderFile;
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

//      helper.createHexBinaryDefinition();
      helper.createQNameDefinition();

      System.out.println(helper.sourceFile.toString());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // TODO: Add documentation
  public CppTypeHelper(Workspace workspace)
  {
    this.sourceFile = workspace.getC().getCppHeaderFile("simple_type_definitions.hpp");

    // TODO: Surround definitions with guard?

    // TODO: Create typedefs for int* and uint*
    ///*
    // * Use wiselib::uint8_t instead later:
    // *
    // * typedef wiselib::uint8_t uint8;
    // */
    //typedef signed char int8;
    //typedef signed int int16;
    //typedef signed long int int32;
    //typedef signed long long int int64;
    //
    //typedef unsigned char uint8;
    //typedef unsigned int uint16;
    //typedef unsigned long int uint32;
    //typedef unsigned long long int uint64;
  }

  // TODO: Add documentation
  //
  // /* xs:hexBinary */
  // typedef struct {
  //   uint16 length;
  //   int8* content;
  // } hexBinary_t;
//  public void createHexBinaryDefinition() throws Exception
//  {
//    CParam length = CParam.factory.create("uint16", "length");
//    CParam content = CParam.factory.create("int8*", "content");
//
//    CStruct hexBinaryDefinition = CStruct.factory.create("", "hexBinary_t", true, length, content);
//    // TODO: How can we add a comment to a struct?
//
//    this.sourceFile.add(hexBinaryDefinition);
//  }

  // TODO: Add documentation
  //
  // /* xs:QName */
  // typedef struct {
  //   char* namespaceURI;
  //   char* localPart;
  // } qname_t;
  public void createQNameDefinition() throws Exception
  {
    CParam namespaceURI = CParam.factory.create("char*", "namespaceURI");
    CParam localPart = CParam.factory.create("char*", "localPart");

    CStruct qnameDefinition = CStruct.factory.create("", "qname_t", true, namespaceURI, localPart);
    // TODO: How can we add a comment to a struct?

    this.sourceFile.add(qnameDefinition);
  }
}
