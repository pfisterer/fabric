package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Two classes in one Source file. The class Outer is using the
 * class Nested.
 *
 * @author Dennis
 *
 */
public class Nested {

	private Workspace workspace = null;

	public Nested(Workspace workspace) throws CppDuplicateException {
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
		CppClass classOne = CppClass.factory.create("Nested");
		CppVar intA = CppVar.factory.create(Cpp.INT, "a");
		CppVar intX = CppVar.factory.create(Cpp.INT, "x");
		CppVar intY = CppVar.factory.create(Cpp.INT, "y");

		CppFun funSetA = CppFun.factory.create(classOne, Cpp.INT, "setA", intY);
		funSetA.appendCode("a = y;");

		CppFun funMult = CppFun.factory.create(classOne, Cpp.INT, "mult", intX);
		funMult.appendCode("return a*x;");

		classOne.add(Cpp.PUBLIC, intA);
		classOne.add(Cpp.PUBLIC, funSetA, funMult);

		/*
		 * 2nd class
		 */
		CppClass classTwo = CppClass.factory.create("Outer");
		CppTypeGenerator typeNested = new CppTypeGenerator("Nested");
		CppVar n = CppVar.factory.create(typeNested, "n");
		CppFun funCall = CppFun.factory.create(classTwo, Cpp.INT, "call", intX);
		funCall.appendCode("n.setA(x);");
		funCall.appendCode("return n.mult(x);");

		classTwo.add(Cpp.PRIVATE, n);
		classTwo.add(Cpp.PUBLIC, intA);
		classTwo.add(Cpp.PUBLIC, funCall);

		// Add the nested class Two to the outer class One
		classTwo.add(Cpp.PRIVATE, classOne);

		/*
		 * Generate the files (Nested.cpp + NestedHeader.hpp)
		 */
		CppSourceFile file = workspace.getC().getCppSourceFile("Nested");
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("NestedHeader");
        file.addInclude(header);
        header.add(classTwo);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNameSpace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("Outer out;");
        fun_main.appendCode("cout << \"Call: \" << out.call(5) << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add the file to the class
        classOne.setSourceFile(file);

	}

}
