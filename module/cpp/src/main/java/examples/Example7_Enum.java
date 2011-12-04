package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CEnum;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * StructSample is similar to CRectangleSimple.
 *
 * - Compile with: g++ EnumSample.cpp -o enum
 * - Run with: ./enum
 * - Returns: 2
 *
 * @author Dennis Boldt
 */

public class Example7_Enum {

	private Workspace workspace = null;

	public Example7_Enum(Workspace workspace) throws CppDuplicateException {
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

		String className = "EnumSample";

        // Generate the class -- without an explicit file
        CppClass clazz = CppClass.factory.create(className);

        /**
         * Lets generate a struct
         */
        CEnum enumm = CEnum.factory.create("TrafficLight", "", false);
        enumm.addConstant("RED");
        enumm.addConstant("YELLOW");
        enumm.addConstant("GREEN");
        enumm.setComment(new CCommentImpl("The traffic light"));

        // Generate two int variables
        CppVar var_x = CppVar.factory.create(enumm, "val");
        clazz.add(Cpp.PRIVATE, var_x);

        // Generate function set_values
        CppVar var_a = CppVar.factory.create(enumm, "data");
        CppFun fun_set_data = CppFun.factory.create(Cpp.VOID, "set", var_a);
        fun_set_data.appendCode("val = data;");

        clazz.add(Cpp.PUBLIC, fun_set_data);

        // Generate function area
        CppFun fun_print = CppFun.factory.create(Cpp.VOID, "print");
        fun_print.appendCode("cout << val << \"\\n\";");
        clazz.add(Cpp.PUBLIC, fun_print);

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

        fun_main.appendCode(enumm.getTypeName() + " e;");
        fun_main.appendCode("e = GREEN;");
        fun_main.appendCode("");
        fun_main.appendCode("EnumSample s;");
        fun_main.appendCode("s.set(e);");
        fun_main.appendCode("s.print();");


/*        fun_main.appendCode(struct.getTypeName() + " d;");
        fun_main.appendCode("d.x = 3;");
        fun_main.appendCode("d.y = 4;");
        fun_main.appendCode("");
        fun_main.appendCode(className + " obj;");
        fun_main.appendCode("obj.set_data(d);");
        fun_main.appendCode("cout << obj.calc() << \"\\n\";");
        fun_main.appendCode("return 0;");
*/
        file.add(fun_main);
        header.add(enumm);

        // Finally, add class to the file
        file.add(clazz);
	}
}
