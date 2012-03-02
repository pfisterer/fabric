package fabric.module.exi.cpp;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;
import fabric.module.typegen.cpp.CppTypeHelper;

/**
 * Created by IntelliJ IDEA.
 * User: reichart
 * Date: 27.02.12
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class CppEXITypeEncoderGenerator {

    /**
     * Name of the C++ header file
     */
    public static final String FILE_NAME = "EXITypeEncoder";

    /**
     * Header file with the definitions of EXI encoding methods and structs
     */
    private static CppHeaderFile headerFile;

    /**
     * Header file with the implementations of EXI encoding methods
     */
    private static CppSourceFile sourceFile;

    /**
     * C++ class.
     */
    private static CppClass clazz;

    /**
     * Private constructor, because all methods of this class are static.
     */
    private CppEXITypeEncoderGenerator() {
        // Empty implementation
    }

    /**
     * Create a header file in the workspace and write all required
     * type definitions and structs to it.
     *
     * @param workspace Workspace object for code write-out
     * @throws Exception Error during code generation
     */
    public static void init(Workspace workspace) throws Exception {
        CppEXITypeEncoderGenerator.createClass();
        CppEXITypeEncoderGenerator.createHeader(workspace);
        CppEXITypeEncoderGenerator.createCpp(workspace);
    }

    /**
     * Generates the C++ class EXITypeEncoder.
     */
    private static void createClass() throws CppDuplicateException {
        clazz = CppClass.factory.create("EXITypeEncoder");
        CppEXITypeEncoderGenerator.createEncodeBoolean();
        CppEXITypeEncoderGenerator.createEncodeInteger();
        CppEXITypeEncoderGenerator.createEncodeUnsignedInteger();
        CppEXITypeEncoderGenerator.createEncodeNBitUnsignedInteger();
        CppEXITypeEncoderGenerator.createEncodeFloat();
        CppEXITypeEncoderGenerator.createEncodeDecimal();
        CppEXITypeEncoderGenerator.createEncodeBinary();
        CppEXITypeEncoderGenerator.createEncodeString();
    }

    /**
     * Generates the .cpp file in the given workspace.
     *
     * @param workspace
     * @throws CPreProcessorValidationException
     * @throws CppDuplicateException
     */
    private static void createCpp(Workspace workspace) throws CPreProcessorValidationException, CppDuplicateException {
        // Create Cpp file
        sourceFile = workspace.getC().getCppSourceFile(CppEXITypeEncoderGenerator.FILE_NAME);
        sourceFile.setComment(new CCommentImpl("Methods for encoding values for the EXI stream."));
        sourceFile.addInclude(headerFile);
    }

    /**
     * Generates the .hpp file in the given workspace.
     *
     * @param workspace
     * @throws Exception
     */
    private static void createHeader(Workspace workspace) throws Exception {
        // Create header file
        headerFile = workspace.getC().getCppHeaderFile(CppEXITypeEncoderGenerator.FILE_NAME);
        headerFile.setComment(new CCommentImpl("Methods for encoding values for the EXI stream."));

        // Surround definitions with include guard
        headerFile.addBeforeDirective("ifndef EXITYPEENCODER_HPP");
        headerFile.addBeforeDirective("define EXITYPEENCODER_HPP");

        // Add includes
        headerFile.addInclude(CppEXIStreamGenerator.FILE_NAME + ".hpp");

        // Add class to the header file
        headerFile.add(clazz);

        // Close include guard
        headerFile.addAfterDirective("endif // EXITYPEENCODER_HPP");
    }

    /**
     * Generates the function encodeString.
     *
     * @throws CppDuplicateException
     */
    private static void createEncodeString() throws CppDuplicateException {
        CppVar var_strm         = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_strVal       = CppVar.factory.create(Cpp.CONST | Cpp.CHAR | Cpp.POINTER, "str_val");
        CppFun fun_encString    = CppFun.factory.create(Cpp.INT, "encodeString", var_strm, var_strVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Encodes string values.";
        fun_encString.appendCode(methodBody);
        fun_encString.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encString);
    }

    private static void createEncodeBinary() {
        // TODO: implement!
    }

    private static void createEncodeFloat() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_flVal    = CppVar.factory.create(Cpp.FLOAT, "fl_val");
        CppFun fun_encFloat = CppFun.factory.create(Cpp.INT, "encodeFloat",
                var_strm, var_flVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Encodes float values.";
        fun_encFloat.appendCode(methodBody);
        fun_encFloat.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encFloat);
    }

    private static void createEncodeDecimal() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_decVal   = CppVar.factory.create(Cpp.CHAR | Cpp.POINTER, "dec_val");
        CppFun fun_encDec   = CppFun.factory.create(Cpp.INT, "encodeDecimal",
                var_strm, var_decVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Encodes decimal values.";
        fun_encDec.appendCode(methodBody);
        fun_encDec.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encDec);
    }

    private static void createEncodeNBitUnsignedInteger() {
        // TODO: implement!
    }

    private static void createEncodeUnsignedInteger() throws CppDuplicateException {
        CppVar var_strm         = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_intVal       = CppVar.factory.create("uint32", "int_val");
        CppFun fun_encUnsInt    = CppFun.factory.create(Cpp.INT, "encodeUnsignedInteger",
                var_strm, var_intVal);
        String methodBody =
                "int tmp_err_code = UNEXPECTED_ERROR;\n" +
                        "\tunsigned int nbits = strm->getBitsNumber(int_val);\n" +
                        "\tunsigned int nbyte7 = nbits / 7 + (nbits % 7 != 0);\n" +
                        "\tunsigned int tmp_byte_buf = 0;\n" +
                        "\tunsigned int i = 0;\n" +
                        "\tif(nbyte7 == 0)\n" +
                        "\t\tnbyte7 = 1;  // the 0 Unsigned Integer is encoded with one 7bit byte\n" +
                        "\tfor(i = 0; i < nbyte7; i++)\n" +
                        "\t{\n" +
                        "\t\ttmp_byte_buf = (unsigned int) ((int_val >> (i * 7)) & 0x7F);\n" +
                        "\t\tif(i == nbyte7 - 1)\n" +
                        "\t\t\tstrm->writeNextBit(0);\n" +
                        "\t\telse\n" +
                        "\t\t\tstrm->writeNextBit(1);\n" +
                        "        \n" +
                        "\t\ttmp_err_code = strm->writeNBits(7, tmp_byte_buf);\n" +
                        "\t\tif(tmp_err_code != ERR_OK)\n" +
                        "\t\t\treturn tmp_err_code;\n" +
                        "\t}\n" +
                        "\treturn ERR_OK;";
        String comment =
                "Encodes unsigned integer values.";
        fun_encUnsInt.appendCode(methodBody);
        fun_encUnsInt.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encUnsInt);
    }

    private static void createEncodeInteger() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_sintVal  = CppVar.factory.create("int32", "sint_val");
        CppFun fun_encInt   = CppFun.factory.create(Cpp.INT, "encodeInteger",
                var_strm, var_sintVal);
        String methodBody =
                "int tmp_err_code = UNEXPECTED_ERROR;\n" +
                        "\tuint32 uval;\n" +
                        "\tunsigned char sign;\n" +
                        "\tif(sint_val >= 0)\n" +
                        "\t{\n" +
                        "\t\tsign = 0;\n" +
                        "\t\tuval = (uint32) sint_val;\n" +
                        "\t}\n" +
                        "\telse\n" +
                        "\t{\n" +
                        "\t\tsint_val += 1;\n" +
                        "\t\tuval = (uint32) -sint_val;\n" +
                        "\t\tsign = 1;\n" +
                        "\t}\n" +
                        "\ttmp_err_code = strm->writeNextBit(sign);\n" +
                        "\tif(tmp_err_code != ERR_OK)\n" +
                        "\t\treturn tmp_err_code;\n" +
                        "    return encodeUnsignedInteger(strm, uval);";
        String comment =
                "Encodes integer values (both negative and positive).";
        fun_encInt.appendCode(methodBody);
        fun_encInt.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encInt);
    }

    private static void createEncodeBoolean() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_boolVal  = CppVar.factory.create(Cpp.BOOL, "bool_val");
        CppFun fun_encBool  = CppFun.factory.create(Cpp.INT, "encodeBoolean",
                var_strm, var_boolVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Encodes boolean values.";
        fun_encBool.appendCode(methodBody);
        fun_encBool.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_encBool);
    }
}
