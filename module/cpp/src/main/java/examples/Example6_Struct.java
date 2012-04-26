/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
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
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.CStruct;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * StructSample is similar to CRectangleSimple.
 *
 * - Compile with: g++ Struct.cpp -o struct
 * - Run with: ./struct
 * - Returns: 12
 *
 * @author Dennis Boldt
 */

public class Example6_Struct {

	private Workspace workspace = null;

	public Example6_Struct(Workspace workspace) throws Exception {
	    this.workspace = workspace;
		generate();
	}

	void generate() throws Exception {

		String className = "Struct";

        // Generate the class -- without an explicit file
        CppClass clazz = CppClass.factory.create(className);

        CppTypeGenerator type_int = new CppTypeGenerator(Cpp.INT);

        /**
         * Lets generate a struct
         */
        CParam px = CParam.factory.create(type_int.toString(), "x");
        CParam py = CParam.factory.create(type_int.toString(), "y");
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

        file.addInclude(header);

        // Add an include to the file
        header.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        header.addUsingNamespace("std");

        // Add a comment
        file.setComment(new CCommentImpl("This file contains a class with a struct."));

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("data_xy d;");
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
        header.add(clazz);
	}
}
