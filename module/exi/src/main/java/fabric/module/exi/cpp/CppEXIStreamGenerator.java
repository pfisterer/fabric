package fabric.module.exi.cpp;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.*;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;
import fabric.module.typegen.cpp.CppTypeHelper;

/**
 * Created by IntelliJ IDEA.
 * User: reichart
 * Date: 27.02.12
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class CppEXIStreamGenerator {

    /**
     * Name of the C++ header file
     */
    public static final String FILE_NAME = "EXIStream";

    /**
     * Header file with definitions of all required structs and methods for writing to an EXI stream
     */
    private static CppHeaderFile headerFile;

    /**
     * Header file with implementations of all required methods for writing to an EXI stream
     */
    private static CppSourceFile sourceFile;

    /**
     * C++ class.
     */
    private static CppClass clazz;

    /**
     * Private constructor, because all methods of this class are static.
     */
    private CppEXIStreamGenerator() {
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
        CppEXIStreamGenerator.createClass();
        CppEXIStreamGenerator.createHeader(workspace);
        CppEXIStreamGenerator.createCpp(workspace);
    }

    /**
     * Generates the C++ class EXIStream.
     */
    private static void createClass() throws CppDuplicateException, CppCodeValidationException {
        clazz = CppClass.factory.create("EXIStream");
        CppEXIStreamGenerator.createVariables();
        CppEXIStreamGenerator.createInitStream();
        CppEXIStreamGenerator.createCloseStream();
        CppEXIStreamGenerator.createWriteNextBit();
        CppEXIStreamGenerator.createWriteNBits();
        CppEXIStreamGenerator.createLog2Int();
        CppEXIStreamGenerator.createGetBitsNumber();
        CppEXIStreamGenerator.createMoveBitPointer();
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
        sourceFile = workspace.getC().getCppSourceFile(CppEXIStreamGenerator.FILE_NAME);
        sourceFile.setComment(new CCommentImpl("Methods for writing values to the EXI stream."));
        sourceFile.addInclude(headerFile);
    }

    /**
     * Generates the .hpp file in the given workspace.
     *
     * @param workspace
     * @throws Exception
     */
    private static void createHeader(Workspace workspace) throws Exception {
        // Create header file for stream definitions


        // Create header file for class
        headerFile = workspace.getC().getCppHeaderFile(CppEXIStreamGenerator.FILE_NAME);
        headerFile.setComment(new CCommentImpl("Methods for writing values to the EXI stream."));

        // Add includes
        headerFile.addInclude(CppTypeHelper.FILE_NAME + ".hpp");

        // Surround definitions with include guard
        headerFile.addBeforeDirective("ifndef EXISTREAM_HPP");
        headerFile.addBeforeDirective("define EXISTREAM_HPP");

        // Add macros and other required variable definitions
        headerFile.addBeforeDirective("define BUFFER_END_REACHED -2");
        headerFile.addBeforeDirective("define UNEXPECTED_ERROR -1");
        headerFile.addBeforeDirective("define ERR_OK 0");
        headerFile.addBeforeDirective("define REVERSE_BIT_POSITION(p) (7-p)");
        headerFile.addBeforeDirective("define NULL 0");
        headerFile.addBeforeDirective("define MASKS {0, 1, 3, 7, 15, 31, 63, 127, 255}");

        // Add structs and type definitions for the EXI streams
        headerFile.add(CppTypeDef.factory.create(Cpp.INT, "size_t"));

        CStruct streamContext = CStruct.factory.create("", "StreamContext", true,
                CParam.factory.create("size_t", "bufferIndx"),
                CParam.factory.create("unsigned char", "bitPointer"));
        streamContext.setComment(new CCommentImpl(
                "Holds the current position in the buffer (bytewise) " +
                        "and the current position within the current byte."));
        headerFile.add(streamContext);

        CStruct ioStream = CStruct.factory.create("", "IOStream", true,
                CParam.factory.create("size_t",
                        "(*readWriteToStream)(void* buf, size_t size, void* stream)"),
                CParam.factory.create("void*", "stream"));
        ioStream.setComment(new CCommentImpl("Representation of an Input/Output Stream"));
        headerFile.add(ioStream);

        CStruct exiStream = CStruct.factory.create("", "EXIEncodedStream", true,
                CParam.factory.create("char*", "buffer"),
                CParam.factory.create("size_t", "bufLen"),
                CParam.factory.create("size_t", "bufContent"),
                CParam.factory.create("IOStream", "ioStrm"),
                CParam.factory.create("StreamContext", "context"));
        exiStream.setComment(new CCommentImpl("Represents an EXI stream"));
        headerFile.add(exiStream);

        // Add class to the header file
        headerFile.add(clazz);

        // Close include guard
        headerFile.addAfterDirective("endif // EXISTREAM_HPP");
    }

    /**
     * Generates the member variables strm and bit_masks in the class.
     *
     * @throws CppCodeValidationException
     * @throws CppDuplicateException
     */
    private static void createVariables() throws CppCodeValidationException, CppDuplicateException {
        // Add variable strm
        CppVar var_strm = CppVar.factory.create(Cpp.PRIVATE, "EXIEncodedStream", "strm");
        var_strm.setComment(new CCommentImpl("EXI stream"));
        clazz.add(var_strm);

        // Add bit masks
        CppVar var_masks = CppVar.factory.create(Cpp.PRIVATE | Cpp.CONST, "unsigned int",
                "BIT_MASK[9] = {0, 1, 3, 7, 15, 31, 63, 127, 255}");
        var_masks.setComment(new CCommentImpl("Bit masks"));
        clazz.add(var_masks);
    }

    /**
     * Generates the function initStream(char* buf, uint32 len, IOStream stream).
     *
     * @throws CppDuplicateException
     */
    private static void createInitStream() throws CppDuplicateException {
        CppVar var_buf          = CppVar.factory.create(Cpp.CHAR | Cpp.POINTER, "buf");
        CppVar var_len          = CppVar.factory.create("size_t", "len");
        CppVar var_stream       = CppVar.factory.create("IOStream", "stream");
        CppFun fun_initStream   = CppFun.factory.create(Cpp.VOID, "initStream",
                var_buf, var_len, var_stream);
        String methodBody =
                "strm.buffer = buf;\n" +
                        "strm.context.bitPointer = 0;\n" +
                        "strm.bufLen = len;\n" +
                        "strm.bufContent = 0;\n" +
                        "strm.context.bufferIndx = 0;\n" +
                        "strm.ioStrm = stream;";
        String comment =
                "Initializes the EXI stream.";
        fun_initStream.appendCode(methodBody);
        fun_initStream.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_initStream);
    }

    /**
     * Generates the function closeStream.
     */
    private static void createCloseStream() throws CppDuplicateException {
        CppFun fun_closeStream = CppFun.factory.create(Cpp.INT, "closeStream");
        String methodBody =
                "if(strm.ioStrm.readWriteToStream != NULL)\n" +
                        "\t{\n" +
                        "\t\tif(strm.ioStrm.readWriteToStream(strm.buffer, strm.context.bufferIndx + 1, " +
                        "strm.ioStrm.stream) < strm.context.bufferIndx + 1)\n" +
                        "\t\t\treturn BUFFER_END_REACHED;\n" +
                        "\t}\n" +
                        "    return ERR_OK;";
        String comment =
                "Writes the content of the buffer to the stream and pads zeros to fill the last byte.";
        fun_closeStream.appendCode(methodBody);
        fun_closeStream.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_closeStream);
    }

    /**
     * Generates the function moveBitPointer.
     *
     * @throws CppDuplicateException
     */
    private static void createMoveBitPointer() throws CppDuplicateException {
        CppVar var_bitPositions     = CppVar.factory.create(Cpp.UNSIGNED | Cpp.INT, "bitPositions");
        CppFun fun_moveBitPointer   = CppFun.factory.create(Cpp.VOID, "moveBitPointer", var_bitPositions);
        String methodBody =
                "\tint nbits;\n\n" +
                        "\tstrm.context.bufferIndx += bitPositions/8;\n" +
                        "\tnbits = bitPositions % 8;\n" +
                        "\tif(nbits < 8 - strm.context.bitPointer)" +
                        " // The remaining (0-7) bit positions can be moved" +
                        " within the current byte\n" +
                        "\t{\n" +
                        "\t\tstrm.context.bitPointer += nbits;\n" +
                        "\t}\n" +
                        "\telse\n" +
                        "\t{\n" +
                        "\t\tstrm.context.bufferIndx += 1;\n" +
                        "\t\tstrm.context.bitPointer = nbits - (8 - strm.context.bitPointer);\n" +
                        "\t}";
        String comment =
                "@brief Moves the BitPointer with certain positions. Takes care of byteIndex increasing " +
                        "when\n" +
                        "the movement cross a byte boundary\n" +
                        "@param[in] bitPositions the number of bit positions to move the pointer";
        fun_moveBitPointer.appendCode(methodBody);
        fun_moveBitPointer.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PRIVATE, fun_moveBitPointer);
    }

    /**
     * Generates the function getBitsNumber.
     *
     * @throws CppDuplicateException
     */
    private static void createGetBitsNumber() throws CppDuplicateException {
        CppVar var_val = CppVar.factory.create("uint32", "val");
        CppFun fun_getBitsNumber = CppFun.factory.create(Cpp.UNSIGNED | Cpp.CHAR, "getBitsNumber", var_val);
        String methodBody =
                "if(val == 0)\n" +
                        "\t\treturn 0;\n" +
                        "\treturn log2INT(val) + 1;";
        String comment =
                "@brief Determine the number of bits needed to encode a unsigned integer value\n" +
                        "⌈ log 2 m ⌉ from the spec is equal to getBitsNumber(m - 1)\n" +
                        "\n" +
                        "@param[in] val unsigned integer value\n\n" +
                        "@return The number of bits needed";
        fun_getBitsNumber.appendCode(methodBody);
        fun_getBitsNumber.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_getBitsNumber);
    }

    /**
     * Generates the function log2INT.
     *
     * @throws CppDuplicateException
     */
    private static void createLog2Int() throws CppDuplicateException {
        CppVar var_val      = CppVar.factory.create("uint32", "val");
        CppFun fun_log2INT  = CppFun.factory.create(Cpp.UNSIGNED | Cpp.INT, "log2INT", var_val);
        String methodBody =
                "const uint32 b[] = {0x2, 0xC, 0xF0, 0xFF00, 0xFFFF0000};\n" +
                        "const unsigned int S[] = {1, 2, 4, 8, 16};\n" +
                        "int i;\n" +
                        "unsigned int r = 0; // result of log2(v) will go here\n\n" +
                        "for (i = 4; i >= 0; i--) // unroll for speed... \n" +
                        "{\n" +
                        "\tif (val & b[i])\n" +
                        "\t{\n" +
                        "\t\tval >>= S[i];\n" +
                        "\t\tr |= S[i];\n" +
                        "\t}\n" +
                        "}\n" +
                        "return r;";
        String comment =
                "@brief Log2 function. Used to determine the number of bits needed to encode" +
                        "an unsigned integer value\n" +
                        "The code taken from: " +
                        "http://www-graphics.stanford.edu/~seander/bithacks.html#IntegerLog\n" +
                        "@param[in] val uint32_t value\n\n" +
                        "@return The number of bits needed";
        fun_log2INT.appendCode(methodBody);
        fun_log2INT.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PRIVATE, fun_log2INT);
    }

    /**
     * Generates the function writeNBits.
     *
     * @throws CppDuplicateException
     */
    private static void createWriteNBits() throws CppDuplicateException {
        CppVar var_nbits        = CppVar.factory.create(Cpp.UNSIGNED | Cpp.CHAR, "nbits");
        CppVar var_bitsVal      = CppVar.factory.create(Cpp.UNSIGNED | Cpp.INT, "bits_val");
        CppFun fun_writeNBits   = CppFun.factory.create(Cpp.INT, "writeNBits", var_nbits, var_bitsVal);
        String methodBody =
                "unsigned int numBitsWrite = 0; // Number of the bits written so far\n" +
                        "\tunsigned char tmp = 0;\n" +
                        "\tint bits_in_byte = 0; // Number of bits written in one iteration\n" +
                        "\tunsigned int numBytesToBeWritten = " +
                        "((unsigned int) nbits) / 8 + (8 - strm.context.bitPointer < nbits % 8 );\n\n" +
                        "if(strm.bufLen <= strm.context.bufferIndx + numBytesToBeWritten)\n" +
                        "\t{\n" +
                        "\t\t// The buffer end is reached: there are fewer than nbits bits " +
                        "left in the buffer\n" +
                        "\t\tchar leftOverBits;\n" +
                        "\t\tsize_t numBytesWritten = 0;\n" +
                        "\t\tif(strm.ioStrm.readWriteToStream == NULL)\n" +
                        "\t\t\treturn BUFFER_END_REACHED;\n\n" +
                        "\t\tleftOverBits = strm.buffer[strm.context.bufferIndx];\n\n" +
                        "\t\tnumBytesWritten = strm.ioStrm.readWriteToStream(" +
                        "strm.buffer, strm.context.bufferIndx, strm.ioStrm.stream);\n" +
                        "\t\tif(numBytesWritten < strm.context.bufferIndx)\n" +
                        "\t\t\treturn BUFFER_END_REACHED;\n\n" +
                        "\t\tstrm.buffer[0] = leftOverBits;\n" +
                        "\t\tstrm.context.bufferIndx = 0;\n" +
                        "\t}\n\n" +
                        "\twhile(numBitsWrite < nbits)\n" +
                        "\t{\n" +
                        "\t\tif((unsigned int)(nbits - numBitsWrite) <= " +
                        "(unsigned int)(8 - strm.context.bitPointer))" +
                        "// The rest of the unwritten bits can be put in the current byte\n" +
                        "\t\t\tbits_in_byte = nbits - numBitsWrite;\n" +
                        "\t\telse // The rest of the unwritten bits are more than the bits " +
                        "in the current byte\n" +
                        "\t\t\tbits_in_byte = 8 - strm.context.bitPointer;\n\n" +
                        "\t\ttmp = (bits_val >> (nbits - numBitsWrite - bits_in_byte)) " +
                        "& BIT_MASK[bits_in_byte];\n" +
                        "\t\ttmp = tmp << (8 - strm.context.bitPointer - bits_in_byte);\n" +
                        "\t\tstrm.buffer[strm.context.bufferIndx] = strm.buffer[strm.context.bufferIndx] " +
                        "& (~BIT_MASK[8 - strm.context.bitPointer]); " +
                        "// Initialize the unused bits with 0s\n" +
                        "\t\tstrm.buffer[strm.context.bufferIndx] = strm.buffer[strm.context.bufferIndx] " +
                        "| tmp;\n\n" +
                        "\t\tnumBitsWrite += bits_in_byte;\n" +
                        "\t\tmoveBitPointer(bits_in_byte);\n" +
                        "\t}\n\n" +
                        "\treturn ERR_OK;";
        String comment =
                "@brief Writes an unsigned integer value to an EXI stream with nbits " +
                        "(possible 0 paddings)\n" +
                        "and moves the stream current bit pointer to the last bit written.\n" +
                        "@param[in] nbits number of bits\n" +
                        "@param[in] bits_val resulting bits value";
        fun_writeNBits.appendCode(methodBody);
        fun_writeNBits.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_writeNBits);
    }

    /**
     * Generates the function writeNextBit.
     *
     * @throws CppDuplicateException
     */
    private static void createWriteNextBit() throws CppDuplicateException {
        CppVar var_bitVal           = CppVar.factory.create(Cpp.UNSIGNED | Cpp.CHAR, "bit_val");
        CppFun fun_writeNextBit     = CppFun.factory.create(Cpp.INT, "writeNextBit", var_bitVal);
        String methodBody =
                "if(strm.bufLen <= strm.context.bufferIndx) // the whole buffer is filled! flush it!\n" +
                        "\t{\n" +
                        "\t\tsize_t numBytesWritten = 0;\n" +
                        "\t\tif(strm.ioStrm.readWriteToStream == NULL)\n" +
                        "\t\t\treturn BUFFER_END_REACHED;\n" +
                        "\t\tnumBytesWritten = strm.ioStrm.readWriteToStream(strm.buffer, strm.bufLen, " +
                        "strm.ioStrm.stream);\n" +
                        "\t\tif(numBytesWritten < strm.bufLen)\n" +
                        "\t\t\treturn BUFFER_END_REACHED;\n" +
                        "\t\tstrm.context.bitPointer = 0;\n" +
                        "\t\tstrm.context.bufferIndx = 0;\n" +
                        "\t}\n\n" +
                        "\tif(bit_val == 0)\n" +
                        "\t\tstrm.buffer[strm.context.bufferIndx] = strm.buffer[strm.context.bufferIndx]" +
                        " & (~(1<<REVERSE_BIT_POSITION(strm.context.bitPointer)));\n" +
                        "\telse\n" +
                        "\t\tstrm.buffer[strm.context.bufferIndx] = strm.buffer[strm.context.bufferIndx]" +
                        " | (1<<REVERSE_BIT_POSITION(strm.context.bitPointer));\n\n" +
                        "\tmoveBitPointer(1);\n\n" +
                        "\treturn ERR_OK;";
        String comment =
                "@brief Writes a single bit to an EXI stream and moves its current bit pointer\n" +
                        "@param[in] bit_val the value of the next bit: 0 or 1\n" +
                        "@return Error handling code\n";
        fun_writeNextBit.appendCode(methodBody);
        fun_writeNextBit.setComment(new CppFunCommentImpl(comment));
        clazz.add(Cpp.PUBLIC, fun_writeNextBit);
    }

}
