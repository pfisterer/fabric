package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.CStruct;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * StructSample is similar to CRectangleSimple.
 *
 * - Compile with: g++ StructSample.cpp -o struct
 * - Run with: ./struct
 * - Returns: 12
 *
 * @author Dennis Boldt
 */

public class Example6_Struct {

	private Workspace workspace = null;

	public Example6_Struct(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
	    try {
			generate();
		} catch (CDuplicateException e) {
			e.printStackTrace();
		} catch (CCodeValidationException e) {
			e.printStackTrace();
		} catch (CConflictingModifierException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generate the CPP-files
	 *
	 * @throws CppDuplicateException
	 * @throws CDuplicateException
	 * @throws CCodeValidationException
	 * @throws CConflictingModifierException
	 */
	void generate() throws CppDuplicateException, CDuplicateException, CCodeValidationException, CConflictingModifierException{

		String className = "StructSample";

        // Generate the class -- without an explicit file
        CppClass clazz = CppClass.factory.create(className);

        CppTypeGenerator type_int = new CppTypeGenerator(Cpp.INT);

        /**
         * Lets generate a struct
         */
        CParam px = CParam.factory.create(type_int.getName(), "x");
        CParam py = CParam.factory.create(type_int.getName(), "y");
        CStruct struct = CStruct.factory.create("data_xy", "data", false, px, py);
        struct.setComment(new CCommentImpl("This struct holds the data"));

        // Generate two int variables
        CppVar var_x = CppVar.factory.create(struct, "d");
        clazz.add(Cpp.PRIVATE, var_x);

        // Generate function set_values
        CppVar var_a = CppVar.factory.create(struct, "data");
        CppFun fun_set_data = CppFun.factory.create(Cpp.VOID, "set_data", var_a);
        fun_set_data.appendCode("d = data;");

        clazz.add(Cpp.PUBLIC, fun_set_data);

        // Generate function area
        CppFun fun_area = CppFun.factory.create(type_int, "calc");

        fun_area.appendCode("return (d.x * d.y);");
        clazz.add(Cpp.PUBLIC, fun_area);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        header.add(clazz);

        file.addInclude(header);

        // Add an include to the file
        header.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        header.addUsingNameSpace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a class with a struct."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(struct.getTypeName() + " d;");
        fun_main.appendCode("d.x = 3;");
        fun_main.appendCode("d.y = 4;");
        fun_main.appendCode("");
        fun_main.appendCode(className + " obj;");
        fun_main.appendCode("obj.set_data(d);");
        fun_main.appendCode("cout << obj.calc() << \"\\n\";");
        fun_main.appendCode("return 0;");

        file.add(fun_main);
        header.add(struct);

        // Finally, add class to the file
        file.add(clazz);
	}
}
