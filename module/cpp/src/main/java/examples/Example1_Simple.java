package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppFunCommentImpl;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
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
        class_CRectangleSimple.setComment(new CCommentImpl("A simple class"));

        // Generate two int variables
        CppTypeGenerator type_int = new CppTypeGenerator(Cpp.INT);
        CppVar var_x = CppVar.factory.create(type_int, "x");
        var_x.setComment(new CCommentImpl("A nice comment for the varable x"));

        CppVar var_y = CppVar.factory.create(Cpp.PRIVATE, type_int.toString() , "y", "-1");
        var_y.setComment(new CCommentImpl("A nice comment for the varable y"));

        class_CRectangleSimple.add(Cpp.PRIVATE, var_x);
        class_CRectangleSimple.add(var_y);

        // Generate function set_values
        CppVar var_a = CppVar.factory.create(type_int, "a");
        CppVar var_b = CppVar.factory.create(type_int, "b");
        CppFun fun_set_values = CppFun.factory.create(Cpp.VOID, "set_values", var_a, var_b);
        fun_set_values.appendCode("x = a;");
        fun_set_values.appendCode("y = b;");

        // Add a comment to the function set_values
        CppFunCommentImpl comment_fun_set_values = new  CppFunCommentImpl("This function sets the values x and y.");
        comment_fun_set_values.addParameter(var_a, "An integer a");
        comment_fun_set_values.addParameter(var_b, "An integer b");
        fun_set_values.setComment(comment_fun_set_values);

        class_CRectangleSimple.add(Cpp.PUBLIC, fun_set_values);

        // Generate function area
        CppFun fun_area = CppFun.factory.create(type_int, "area");
        // Add a comment to the function set_values
        CppFunCommentImpl comment_fun_area = new  CppFunCommentImpl("This function multiplies two values.");
        comment_fun_area.setReturnTypeDescription("The value of x*y.");
        fun_area.setComment(comment_fun_area);
        fun_area.appendCode("return (x*y);");
        class_CRectangleSimple.add(Cpp.PUBLIC, fun_area);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        file.addInclude(header);
        header.add(class_CRectangleSimple);

        header.setComment(new CCommentImpl("The header file."));

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNameSpace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a simple class."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(className + " rect;");
        fun_main.appendCode("rect.set_values (3,4);");
        fun_main.appendCode("cout << rect.area() << \"\\n\";");
        fun_main.appendCode("return 0;");
        fun_main.setComment(new CCommentImpl("Here is the main."));

        file.add(fun_main);

        // Finally, add class to the file
        file.add(class_CRectangleSimple);
	}
}
