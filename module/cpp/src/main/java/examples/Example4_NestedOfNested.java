package examples;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.CFun;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppClassCommentImpl;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

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

	public  Example4_NestedOfNested(Workspace workspace) throws CppDuplicateException {
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
		 * 1st class: Nested
		 */
		CppClass classOne = CppClass.factory.create("NestedOfNested");
		classOne.setComment(new CppClassCommentImpl("This is the first nested class"));
		CppVar intA = CppVar.factory.create(Cpp.INT, "a");
		CppVar intX = CppVar.factory.create(Cpp.INT, "x");
		CppVar intY = CppVar.factory.create("int", "y"); // Use (String,String)
		classOne.add(Cpp.PUBLIC, intA);

		CppFun funSetA = CppFun.factory.create(classOne, Cpp.INT, "setA", intY);
		funSetA.appendCode("a = y;");
		CppFun funMult = CppFun.factory.create(classOne, Cpp.INT, "mult", intX);
		funMult.appendCode("return (a*x) + n2.x(x);");
		classOne.add(Cpp.PUBLIC, funSetA, funMult);

		CppTypeGenerator typeNested2 = new CppTypeGenerator("NestedOfNested2");
		CppVar n2 = CppVar.factory.create(typeNested2, "n2");
		classOne.add(Cpp.PRIVATE, n2);

		/*
		 * 2nd class: Nested2
		 */
		CppClass classTwo = CppClass.factory.create("NestedOfNested2");
		classTwo.setComment(new CppClassCommentImpl("This is the most inner nested class"));
		CppFun funX = CppFun.factory.create(classTwo, Cpp.INT, "x", intX);
		funX.appendCode("return x;");
		classTwo.add(Cpp.PUBLIC, funX);

		/*
		 * 3nd class: Outer
		 */
		CppClass classThree = CppClass.factory.create("NestedOfOuter");
		classThree.setComment(new CppClassCommentImpl("This is the most outer class"));

		CppTypeGenerator typeNested = new CppTypeGenerator("NestedOfNested");
		CppVar n = CppVar.factory.create(typeNested, "n");
		CppFun funCall = CppFun.factory.create(classThree, Cpp.INT, "call", intX);
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
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("NestedOfNestedHeader");
        file.addInclude(header);
        header.add(classThree);

        // Add an include to the file
        file.addLibInclude("iostream");

        // Add namespace for the standard library to the file
        file.addUsingNameSpace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode("NestedOfOuter out;");
        fun_main.appendCode("cout << out.call(10) << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add the file to the class
        classOne.setSourceFile(file);

	}

}
