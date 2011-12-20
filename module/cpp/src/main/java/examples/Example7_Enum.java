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
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CEnum;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * StructSample is similar to CRectangleSimple.
 *
 * - Compile with: g++ Enum.cpp -o enum
 * - Run with: ./enum
 * - Returns: 2
 *
 * @author Dennis Boldt
 */

public class Example7_Enum {

	private Workspace workspace = null;

	public Example7_Enum(Workspace workspace) throws Exception {
	    this.workspace = workspace;
		generate();
	}

	void generate() throws Exception {

		String className = "Enum";

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
        file.addInclude(header);

        // Add an include to the file
        header.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        header.addUsingNamespace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a class with a struct."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);

        fun_main.appendCode(enumm.getName() + " e;");
        fun_main.appendCode("e = GREEN;");
        fun_main.appendCode("");
        fun_main.appendCode(className + " s;");
        fun_main.appendCode("s.set(e);");
        fun_main.appendCode("s.print();");

        file.add(fun_main);
        header.add(enumm);

        // Finally, add class to the file
        header.add(clazz);
	}
}
