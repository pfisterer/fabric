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
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * One class is nested in another class.
 *
 * - Compile with: g++ Nested.cpp -o nested
 * - Run with: ./nested
 * - Returns: 100
 *
 * @author Dennis
 *
 */
public class Example3_Nested {

	private Workspace workspace = null;

	public Example3_Nested(Workspace workspace) throws Exception {
	    this.workspace = workspace;
		generate();
	}

	void generate() throws Exception {

		String className = "Nested";

		/*
		 * 1st class
		 */
		CppClass classNested = CppClass.factory.create("Nested");
		CppVar intA = CppVar.factory.create(Cpp.INT, "a");
		CppVar intX = CppVar.factory.create(Cpp.INT, "x");
		CppVar intY = CppVar.factory.create(Cpp.INT, "y");

		CppFun funSetA = CppFun.factory.create(Cpp.INT, "setA", intY);
		funSetA.appendCode("a = y;");

		CppFun funMult = CppFun.factory.create(Cpp.INT, "mult", intX);
		funMult.appendCode("return a*x;");

		CppFun fungetA = CppFun.factory.create(Cpp.INT, "getA");
		fungetA.appendCode("return a;");

		classNested.add(Cpp.PUBLIC, intA);
		classNested.add(Cpp.PUBLIC, funSetA, funMult, fungetA);

		/*
		 * 2nd class
		 */
		CppClass classOuter = CppClass.factory.create("Outer");
		CppTypeGenerator typeNested = new CppTypeGenerator(classNested);
		CppVar n = CppVar.factory.create(typeNested, "n");

		CppFun funCall = CppFun.factory.create(Cpp.INT, "call", intX);
		funCall.appendCode("n.setA(x);");
		funCall.appendCode("return n.mult(x);");

		CppFun funGetNested = CppFun.factory.create(typeNested.toString(), "getNested");
		funGetNested.appendCode("return n;");

		classOuter.add(Cpp.PRIVATE, n);
		classOuter.add(Cpp.PUBLIC, intA);
		classOuter.add(Cpp.PUBLIC, funCall, funGetNested);

		// Add the nested class Two to the outer class One
		classOuter.add(Cpp.PUBLIC, classNested);

		/*
		 * Generate the files (Nested.cpp + NestedHeader.hpp)
		 */
		CppSourceFile file = workspace.getC().getCppSourceFile(className);
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        file.addInclude(header);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNamespace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("Outer out;");
        fun_main.appendCode("cout << out.call(10) << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add class to the file
        header.add(classOuter);

	}

}
