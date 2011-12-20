/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

/**
 * Simple class with constructor and destructor
 *
 * - Compile with: g++ ConstructorDestructor.cpp -o conDes
 * - Run with: ./condes
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

	public  Example5_Constructor_Destructor(Workspace workspace) throws Exception {
	    this.workspace = workspace;
      generate();
	}

	void generate() throws Exception {

    		String className = "ConstructorDestructor";

        // Generate the class -- without an explicit file
        CppClass person = CppClass.factory.create(className);
        person.setComment(new CCommentImpl("A simple class"));

        // Get the standard constructor
        CppConstructor c = CppConstructor.factory.create();
        c.appendCode("this->name = \"No name\";");
        c.appendCode("this->age = -1;");

        person.add(Cpp.PUBLIC, c);

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

        // Generate function print
        CppFun fun_print = CppFun.factory.create(Cpp.VOID, "print");
        fun_print.appendCode("cout << name << \" is \" << age << \"\\n\";");
        person.add(Cpp.PUBLIC, fun_print);

        // Generate the files (cpp + hpp)
        CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        file.addInclude(header);

        // Add an include to the file
        header.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        header.addUsingNamespace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a simple class."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(className + " empty;");
        fun_main.appendCode(className + " otto(\"Otto\", 48);");
        fun_main.appendCode(className + " helge(\"Helge\", 12);");
        fun_main.appendCode("empty.print();");
        fun_main.appendCode("otto.print();");
        fun_main.appendCode("helge.print();");

        file.add(fun_main);

        // Finally, add class to the file
        header.add(person);

	}

}
