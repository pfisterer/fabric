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
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppFunCommentImpl;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 *  One class is nested in another class.
 *
 * - Compile with: g++ NestedOfNested.cpp -o nestedOfNested
 * - Run with: ./nestedOfNested
 * - Returns: 110
 *
 * @author Dennis
 *
 */
public class  Example4_NestedOfNested {

	private Workspace workspace = null;

	public  Example4_NestedOfNested(Workspace workspace) throws Exception {
	    this.workspace = workspace;
		generate();
	}

	void generate() throws Exception {

		/*
		 * 1st class: Nested
		 */
		CppClass classOne = CppClass.factory.create("NestedOfNested");
		classOne.setComment(new CCommentImpl("This is the first nested class"));
		CppVar intA = CppVar.factory.create(Cpp.INT, "a");
		CppVar intX = CppVar.factory.create(Cpp.INT, "x");
		CppVar intY = CppVar.factory.create("int", "y"); // Use (String,String)
		classOne.add(Cpp.PUBLIC, intA);

		CppFun funSetA = CppFun.factory.create(Cpp.INT, "setA", intY);
		funSetA.appendCode("a = y;");
		CppFun funMult = CppFun.factory.create(Cpp.INT, "mult", intX);
		funMult.appendCode("return (a*x) + n2.x(x);");
		classOne.add(Cpp.PUBLIC, funSetA, funMult);

		CppTypeGenerator typeNested2 = new CppTypeGenerator("NestedOfNested2");
		CppVar n2 = CppVar.factory.create(typeNested2, "n2");
		classOne.add(Cpp.PRIVATE, n2);

		/*
		 * 2nd class: Nested2
		 */
		CppClass classTwo = CppClass.factory.create("NestedOfNested2");
		classTwo.setComment(new CCommentImpl("This is the most inner nested class"));
		CppFun funX = CppFun.factory.create(Cpp.INT, "x", intX);
		CppFunCommentImpl comment = new CppFunCommentImpl("The amazing method x.");
		comment.addParameter(intX, "The value.");
		comment.setReturnTypeDescription("Echos the given value.");
		funX.setComment(comment);
		funX.appendCode("return x;");
		classTwo.add(Cpp.PUBLIC, funX);

		/*
		 * 3nd class: Outer
		 */
		CppClass classThree = CppClass.factory.create("NestedOfOuter");
		classThree.setComment(new CCommentImpl("This is the most outer class"));

		CppTypeGenerator typeNested = new CppTypeGenerator("NestedOfNested");
		CppVar n = CppVar.factory.create(typeNested, "n");
		CppFun funCall = CppFun.factory.create(Cpp.INT, "call", intX);
		funCall.appendCode("n.setA(x);");
		funCall.appendCode("return n.mult(x);");

		classThree.add(Cpp.PRIVATE, n);
		classThree.add(Cpp.PUBLIC, intA);
		classThree.add(Cpp.PUBLIC, funCall);

		// Add the nested class Two to the outer class One
		classThree.add(Cpp.PRIVATE, classOne);
		classOne.add(Cpp.PRIVATE, classTwo);


		/*
		 * Generate the files (Nested.cpp + NestedHeader.hpp)
		 */
		CppSourceFile file = workspace.getC().getCppSourceFile("NestedOfNested");
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("NestedOfNested");
        file.addInclude(header);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNamespace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("NestedOfOuter out;");
        fun_main.appendCode("cout << out.call(10) << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add class to the file
        header.add(classThree);
	}

}
