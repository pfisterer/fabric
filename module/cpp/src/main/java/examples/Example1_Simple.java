package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppClassCommentImpl;
import de.uniluebeck.sourcegen.c.CppComment;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * CRectangle example.
 *
 * - Compile with: g++ CRectangleSimple.cpp -o simple
 * - Run with: ./simple
 * - Returns: 12
 *
 * @see: http://www.cplusplus.com/doc/tutorial/classes/
 * @author Dennis Boldt
 *
 */

public class Example1_Simple {

	private Workspace workspace = null;

	public Example1_Simple(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
	    try {
			generate();
		} catch (CDuplicateException e) {
		}
	}

	/**
	 * This method generate the CPP-files
	 *
	 * @throws CppDuplicateException
	 * @throws CDuplicateException
	 */
	void generate() throws CppDuplicateException, CDuplicateException{

		String className = "CRectangleSimple";

        // Generate the class -- without an explicit file
        CppClass class_CRectangleSimple = CppClass.factory.create(className);
        class_CRectangleSimple.setComment(new CppClassCommentImpl("A simple class"));

        // Generate two int variables
        CppVar var_x = CppVar.factory.create(Cpp.INT, "x");
        CppVar var_y = CppVar.factory.create(Cpp.INT, "y");
        class_CRectangleSimple.add(Cpp.PRIVATE, var_x, var_y);

        // Generate function set_values
        CppVar var_a = CppVar.factory.create(Cpp.INT, "a");
        CppVar var_b = CppVar.factory.create(Cpp.INT, "b");
        CppFun fun_set_values = CppFun.factory.create(class_CRectangleSimple, Cpp.VOID, "set_values", var_a, var_b);
        fun_set_values.appendCode("x = a;");
        fun_set_values.appendCode("y = b;");
        class_CRectangleSimple.add(Cpp.PUBLIC, fun_set_values);

        // Generate function area
        CppFun fun_area = CppFun.factory.create(class_CRectangleSimple, Cpp.INT, "area");
        fun_area.appendCode("return (x*y);");
        class_CRectangleSimple.add(Cpp.PUBLIC, fun_area);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        file.addInclude(header);
        header.add(class_CRectangleSimple);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNameSpace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(className + " rect;");
        fun_main.appendCode("rect.set_values (3,4);");
        fun_main.appendCode("cout << rect.area() << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add the file to the class
        class_CRectangleSimple.setSourceFile(file);
	}
}
