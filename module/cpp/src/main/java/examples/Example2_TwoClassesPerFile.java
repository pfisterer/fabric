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
import de.uniluebeck.sourcegen.c.CCommentImpl;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Two classes in one Source file. The class Second is using the
 * class First.
 *
 * - Compile with: g++ Two.cpp -o two
 * - Run with: ./two
 * - Returns: 25
 *
 * @author Dennis
 *
 */
public class Example2_TwoClassesPerFile {

	private Workspace workspace = null;

	public Example2_TwoClassesPerFile(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
		try {
			generate();
		} catch (CDuplicateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generate the CPP-files
	 *
	 * @throws CppDuplicateException
	 * @throws CDuplicateException
	 */
	void generate() throws CppDuplicateException, CDuplicateException {

		/*
		 * 1st class
		 */
		CppClass classOne = CppClass.factory.create("First");
		classOne.setComment(new CCommentImpl("The first class"));
		CppVar intA = CppVar.factory.create(Cpp.INT, "a");
		CppVar intX = CppVar.factory.create(Cpp.INT, "x");
		CppVar intY = CppVar.factory.create(Cpp.INT, "y");
		CppFun funSetA = CppFun.factory.create(Cpp.INT, "setA", intY);
		funSetA.appendCode("a = y;");
		CppFun funMult = CppFun.factory.create(Cpp.INT, "mult", intX);
		funMult.appendCode("return a*x;");
		classOne.add(Cpp.PUBLIC, intA);
		classOne.add(Cpp.PUBLIC, funSetA, funMult);

		/*
		 * 2nd class
		 */
		CppClass classTwo = CppClass.factory.create("Second");
		classTwo.setComment(new CCommentImpl("The second class"));
		CppTypeGenerator typeNested = new CppTypeGenerator("First");
		CppVar n = CppVar.factory.create(typeNested, "n");
		CppFun funCall = CppFun.factory.create(Cpp.INT, "call", intX);
		funCall.appendCode("n.setA(x);");
		funCall.appendCode("return n.mult(x);");

		classTwo.add(Cpp.PRIVATE, n);
		classTwo.add(Cpp.PUBLIC, intA);
		classTwo.add(Cpp.PUBLIC, funCall);

		/*
		 * Generate the files (cpp + hpp)
		 */
		CppSourceFile file = workspace.getC().getCppSourceFile("Two");
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("TwoHeader");
        file.addInclude(header);
        header.add(classOne);
        header.add(classTwo);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNameSpace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("Second out;");
        fun_main.appendCode("cout << out.call(5) << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add class to the file
        file.add(classOne);

	}

}
