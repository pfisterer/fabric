package fabric.module.exi.cpp;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Created by IntelliJ IDEA.
 * User: reichart
 * Date: 02.03.12
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class CppEXITypeDecoderGenerator {

    /**
     * Name of the C++ header file
     */
    public static final String FILE_NAME = "EXITypeDecoder";

    /**
     * Header file with the definitions of EXI decoding methods and structs
     */
    private static CppHeaderFile headerFile;

    /**
     * Header file with the implementations of EXI decoding methods
     */
    private static CppSourceFile sourceFile;

    /**
     * C++ class.
     */
    private static CppClass clazz;

    /**
     * Private constructor, because all methods of this class are static.
     */
    private CppEXITypeDecoderGenerator() {
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
        CppEXITypeDecoderGenerator.createClass();
        CppEXITypeDecoderGenerator.createHeader(workspace);
        CppEXITypeDecoderGenerator.createCpp(workspace);
    }

    /**
     * Generates the C++ class EXITypeDecoder.
     */
    private static void createClass() throws CppDuplicateException {
        clazz = CppClass.factory.create(FILE_NAME);
        CppEXITypeDecoderGenerator.createDecodeBoolean();
        CppEXITypeDecoderGenerator.createDecodeInteger();
        CppEXITypeDecoderGenerator.createDecodeUnsignedInteger();
        CppEXITypeDecoderGenerator.createDecodeNBitUnsignedInteger();
        CppEXITypeDecoderGenerator.createDecodeFloat();
        CppEXITypeDecoderGenerator.createDecodeDecimal();
        CppEXITypeDecoderGenerator.createDecodeBinary();
        CppEXITypeDecoderGenerator.createDecodeString();
    }

    /**
     * Generates the .cpp file in the given workspace.
     *
     * @param workspace
     * @throws de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException
     * @throws CppDuplicateException
     */
    private static void createCpp(Workspace workspace) throws CPreProcessorValidationException, CppDuplicateException {
        // Create Cpp file
        sourceFile = workspace.getC().getCppSourceFile(FILE_NAME);
        sourceFile.setComment(new CCommentImpl("Methods for decoding values for the EXI stream."));
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
        headerFile = workspace.getC().getCppHeaderFile(FILE_NAME);
        headerFile.setComment(new CCommentImpl("Methods for decoding values for the EXI stream."));

        // Surround definitions with include guard
        headerFile.addBeforeDirective("ifndef EXITYPEDECODER_HPP");
        headerFile.addBeforeDirective("define EXITYPEDECODER_HPP");

        // Add includes
        headerFile.addInclude(CppEXIStreamGenerator.FILE_NAME + ".hpp");

        // Add class to the header file
        headerFile.add(clazz);

        // Close include guard
        headerFile.addAfterDirective("endif // EXITYPEDECODER_HPP");
    }

    /**
     * Generates the function decodeString.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeString() throws CppDuplicateException {
        CppVar var_strm         = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_strVal       = CppVar.factory.create(Cpp.CONST | Cpp.CHAR | Cpp.POINTER, "str_val");
        CppFun fun_decString    = CppFun.factory.create(Cpp.INT, "decodeString", var_strm, var_strVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Decodes string values.";
        fun_decString.appendCode(methodBody);
        fun_decString.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decString);
    }

    private static void createDecodeBinary() {
        // TODO: implement!
    }

    /**
     * Generates the function decodeFloat.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeFloat() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_flVal    = CppVar.factory.create(Cpp.FLOAT | Cpp.POINTER, "fl_val");
        CppFun fun_decFloat = CppFun.factory.create(Cpp.INT, "decodeFloat",
                var_strm, var_flVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Decodes float values.";
        fun_decFloat.appendCode(methodBody);
        fun_decFloat.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decFloat);
    }

    /**
     * Generates the function decodeDecimal.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeDecimal() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_decVal   = CppVar.factory.create(Cpp.CHAR | Cpp.POINTER, "dec_val");
        CppFun fun_decDec   = CppFun.factory.create(Cpp.INT, "decodeDecimal",
                var_strm, var_decVal);
        String methodBody =
                "return UNEXPECTED_ERROR;";
        String comment =
                "Decodes decimal values.";
        fun_decDec.appendCode(methodBody);
        fun_decDec.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decDec);
    }

    /**
     * Generates the function decodeNBitUnsignedInteger.
     */
    private static void createDecodeNBitUnsignedInteger() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_n        = CppVar.factory.create(Cpp.UNSIGNED | Cpp.CHAR, "n");
        CppVar var_intVal   = CppVar.factory.create(Cpp.UNSIGNED | Cpp.INT | Cpp.POINTER, "int_val");
        CppFun fun_decNBitUnsInt = CppFun.factory.create(Cpp.INT, "decodeNBitUnsignedInteger",
                var_strm, var_n, var_intVal);
        String methodBody =
                "return strm->readNBits(n, int_val);";
        String comment =
                "Decodes n-bit unsigned integer values.";
        fun_decNBitUnsInt.appendCode(methodBody);
        fun_decNBitUnsInt.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decNBitUnsInt);
    }

    /**
     * Generates the function decodeUnsignedInteger.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeUnsignedInteger() throws CppDuplicateException {
        CppVar var_strm         = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_intVal       = CppVar.factory.create("uint32*", "int_val");
        CppFun fun_decUnsInt    = CppFun.factory.create(Cpp.INT, "decodeUnsignedInteger",
                var_strm, var_intVal);
        String methodBody =
                "unsigned int mask_7bits = 0x7F;\n" +
                        "unsigned int mask_8th_bit = 0x80;\n" +
                        "unsigned int i = 0;\n" +
                        "int tmp_err_code = UNEXPECTED_ERROR;\n" +
                        "unsigned int tmp_byte_buf = 0;\n" +
                        "unsigned int more_bytes_to_read = 0;\n" +
                        "*int_val = 0;\n\n" +
                        "do\n" +
                        "{\n" +
                        "\ttmp_err_code = strm->readNBits(8, &tmp_byte_buf);\n" +
                        "\tif(tmp_err_code != ERR_OK)\n" +
                        "\t\treturn tmp_err_code;\n\n" +
                        "\tmore_bytes_to_read = tmp_byte_buf & mask_8th_bit;\n" +
                        "\ttmp_byte_buf = tmp_byte_buf & mask_7bits;\n" +
                        "\t*int_val += ((uint32) tmp_byte_buf) << (7*i);\n" +
                        "\ti++;\n" +
                        "}\n" +
                        "while(more_bytes_to_read != 0);\n\n" +
                        "return ERR_OK;";
        String comment =
                "Decodes unsigned integer values.";
        fun_decUnsInt.appendCode(methodBody);
        fun_decUnsInt.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decUnsInt);
    }

    /**
     * Generates the function decodeInteger.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeInteger() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_sintVal  = CppVar.factory.create("int32*", "sint_val");
        CppFun fun_decInt   = CppFun.factory.create(Cpp.INT, "decodeInteger",
                var_strm, var_sintVal);
        String methodBody =
                "int tmp_err_code = UNEXPECTED_ERROR;\n" +
                        "bool bool_val = false;\n" +
                        "uint32 val;\n" +
                        "tmp_err_code = decodeBoolean(strm, &bool_val);\n" +
                        "if(tmp_err_code != ERR_OK)\n" +
                        "\treturn tmp_err_code;\n\n" +
                        "tmp_err_code = decodeUnsignedInteger(strm, &val);\n" +
                        "if(tmp_err_code != ERR_OK)\n" +
                        "\treturn tmp_err_code;\n\n" +
                        "*sint_val = (int32) val;\n" +
                        "if(bool_val) // negative integer\n" +
                        "{\n" +
                        "\t*sint_val += 1;\n" +
                        "\t*sint_val *= -1;\n" +
                        "}\n" +
                        "return ERR_OK;";
        String comment =
                "Decodes integer values (both negative and positive).";
        fun_decInt.appendCode(methodBody);
        fun_decInt.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decInt);
    }

    /**
     * Generates the function decodeBoolean.
     *
     * @throws CppDuplicateException
     */
    private static void createDecodeBoolean() throws CppDuplicateException {
        CppVar var_strm     = CppVar.factory.create("EXIStream*", "strm");
        CppVar var_boolVal  = CppVar.factory.create(Cpp.BOOL | Cpp.POINTER, "bool_val");
        CppFun fun_decBool  = CppFun.factory.create(Cpp.INT, "decodeBoolean",
                var_strm, var_boolVal);
        String methodBody =
                "return strm->readNextBit((unsigned char*) bool_val);";
        String comment =
                "Decodes boolean values.";
        fun_decBool.appendCode(methodBody);
        fun_decBool.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_decBool);
    }
}
