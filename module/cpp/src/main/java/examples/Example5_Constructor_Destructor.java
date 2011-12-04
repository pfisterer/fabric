package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CComment;
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppConstructor;
import de.uniluebeck.sourcegen.c.CppConstructorCommentImpl;
import de.uniluebeck.sourcegen.c.CppDestructor;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Simple class with constructor and destructor
 *
 * - Compile with: g++ Person.cpp -o person
 * - Run with: ./person
 * - Returns:
 *
 * Otto is 48
 * Helge is 12
 * Helge will be destructed
 * Otto will be destructed
 *
 * @author Dennis
 *
 */
public class Example5_Constructor_Destructor {

	private Workspace workspace = null;

	public  Example5_Constructor_Destructor(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
		try {
			generate();
		} catch (CDuplicateException e) {
			e.printStackTrace();
		}
	}

	void generate() throws CppDuplicateException, CDuplicateException{

		String className = "Person";

        // Generate the class -- without an explicit file
        CppClass person = CppClass.factory.create(className);
        person.setComment(new CCommentImpl("A simple class"));

        // Generate two int variables
        CppTypeGenerator type_string = new CppTypeGenerator("string");
        CppVar var_name = CppVar.factory.create(type_string, "name");
        CppVar var_age = CppVar.factory.create(Cpp.INT, "age");
        person.add(Cpp.PRIVATE, var_name, var_age);

        // Generate the constructor
        CppConstructor con = CppConstructor.factory.create(var_name, var_age);
        con.appendCode("this->name = name;");
        con.appendCode("this->age = age;");

        CppConstructorCommentImpl comment_cons = new CppConstructorCommentImpl("Generate a person.");
        comment_cons.addParameter(var_name, "The name of a person.");
        comment_cons.addParameter(var_age, "The age of a person.");
        con.setComment(comment_cons);

        person.add(Cpp.PUBLIC, con);

        // Generate the destructor
        CppDestructor des = CppDestructor.factory.create();
        des.appendCode("cout << name << \" will be destructed\" << \"\\n\";");
        des.appendCode("age = 0;");
        des.appendCode("name = \"\";");

        CComment comment_des = new CppConstructorCommentImpl("Erase a person.");
        des.setComment(comment_des);

        person.add(Cpp.PUBLIC, des);


/*
Person::~Person() {
	cout << name << " will be destructed" << "\n";
	age = 0;
	name = "";
}
*/
        // Generate function print
        CppFun fun_print = CppFun.factory.create(Cpp.VOID, "print");
        fun_print.appendCode("cout << name << \" is \" << age << \"\\n\";");
        person.add(Cpp.PUBLIC, fun_print);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        file.addInclude(header);
        header.add(person);

        // Add an include to the file
        header.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        header.addUsingNameSpace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a simple class."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(className + " otto(\"Otto\", 48);");
        fun_main.appendCode(className + " helge(\"Helge\", 12);");
        fun_main.appendCode("otto.print();");
        fun_main.appendCode("helge.print();");

        file.add(fun_main);

        // Finally, add class to the file
        file.add(person);

	}

}
