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
