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
import de.uniluebeck.sourcegen.c.CFunSignature;
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppNamespace;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * This is a basic class to generate other examples
 *
 * - Compile with: g++ Namespace.cpp -o ns
 * - Run with: ./ns
 * - Returns: n/a
 *
 * @author Dennis Boldt
 *
 */

public class Example15_Namespaces {

    private Workspace workspace = null;

    public Example15_Namespaces(Workspace workspace) throws Exception {
        this.workspace = workspace;
        generate();
    }

    void generate() throws Exception {
        String name = "Test";
        String fileName = "Namespace";

        // Generate the class -- without an explicit file
        CppNamespace ns = CppNamespace.factory.create(name);

        CParam p = CParam.factory.create("int", "value");
        CFunSignature sig = CFunSignature.factory.create(p);

        CFun fun_print = CFun.factory.create("print", "int", sig);
        fun_print.appendCode("return 0;");
        fun_print.setComment(new CCommentImpl("Print function"));
        ns.add(Cpp.PUBLIC, fun_print);

        CFun fun_test = CFun.factory.create("test", "int");
        fun_test.appendCode("return 0;");
        fun_test.setComment(new CCommentImpl("Test function"));
        ns.add(Cpp.PUBLIC, fun_test);

        CppClass cppClass1 = CppClass.factory.create("SimpleClass");
        CppVar cppVar = CppVar.factory.create("int", "val");
        CppFun cppFun = CppFun.factory.create("int", "test", cppVar);
        cppFun.appendCode("return 0;");
        cppClass1.add(Cpp.PUBLIC, cppFun);
        ns.add(cppClass1);

        CppClass cppClass2 = CppClass.factory.create("InnerSimpleClass");
        CppVar cppVar2 = CppVar.factory.create("int", "val");
        CppFun cppFun2 = CppFun.factory.create("int", "test", cppVar2);
        cppFun2.appendCode("return 0;");
        cppClass2.add(Cpp.PUBLIC, cppFun2);

        CppFun cppFun3 = CppFun.factory.create("int", "print");
        cppFun3.appendCode("return 0;");
        cppClass2.add(Cpp.PUBLIC, cppFun3);

        cppClass1.add(Cpp.PUBLIC, cppClass2);

        // Generate the files (cpp + hpp)
      	CppSourceFile file = workspace.getC().getCppSourceFile(fileName);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(fileName + "Header");
        header.add(ns);

        // We also need a header
        file.addInclude(header);
        file.addLibInclude("iostream");
        file.addUsingNamespace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("Test::print(1);");
        fun_main.appendCode("Test::SimpleClass c;");
        fun_main.appendCode("c.test(2);");
        file.add(fun_main);
    }

}
