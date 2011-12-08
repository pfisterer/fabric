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
import de.uniluebeck.sourcegen.c.CppConstructor;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * This is a basic class to generate other examples
 *
 * - Compile with: g++ Inheritance.cpp -o inheritance
 * - Run with: ./inheritance
 *   Returns: Person is 48
 *            Otto is 48, male and owns 2 cars.
 *            Helga is 12, female and owns 18 shoes.
 *
 * @author Dennis Boldt
 *
 */

public class Example10_Inheritance {

	private Workspace workspace = null;

	public Example10_Inheritance(Workspace workspace) throws Exception {
    this.workspace = workspace;
    generate();
	}

	void generate() throws Exception {

		String fileName = "Inheritance";

		CppTypeGenerator type_int = new CppTypeGenerator(Cpp.INT);
		CppTypeGenerator type_string = new CppTypeGenerator("string");

		CppVar var_name = CppVar.factory.create(type_string, "name");
		CppVar var_age = CppVar.factory.create(type_int, "age");
		CppVar var_shoes = CppVar.factory.create(type_int, "shoes");
		CppVar var_cars = CppVar.factory.create(type_int, "cars");

		/**
		 * Class Person
		 */
		CppClass clazz_person = CppClass.factory.create("Person");

		// Variables
		clazz_person.add(Cpp.PROTECTED, var_name, var_age);

		// Constructor
		CppConstructor con_person = CppConstructor.factory.create();
		con_person.add(var_name);
		con_person.add(var_age);
		con_person.appendCode("this->name = name;");
		con_person.appendCode("this->age = age;");
		clazz_person.add(Cpp.PUBLIC, con_person);

		// Function
		CppFun fun_person = CppFun.factory.create(Cpp.VOID, "print");
		fun_person.appendCode(" cout << name << \" is \" << age << \"\\n\";");
		clazz_person.add(Cpp.PUBLIC, fun_person);

		/**
		 * Class Female
		 */
		CppClass clazz_female = CppClass.factory.create("Female");
		//clazz_female.addExtended(Cpp.PUBLIC, clazz_person); // Alternativ add it as string
		clazz_female.addExtended(Cpp.PUBLIC, "Person");
		clazz_female.add(Cpp.PRIVATE, var_shoes);

		// Constructor
		CppConstructor con_female = CppConstructor.factory.create();
		//con_female.add(con_person); // TODO: Buggy, use the sring version, see next line.
		con_female.add("Person(name, age)");
		con_female.appendCode("this->shoes = shoes;");
		con_female.add(var_name, var_age, var_shoes);
		clazz_female.add(Cpp.PUBLIC, con_female);

		// Function
		CppFun fun_female = CppFun.factory.create(Cpp.VOID, "print");
		fun_female.appendCode("cout << name << \" is \" << age << \", female and owns \" << shoes << \" shoes.\\n\";");
		clazz_female.add(Cpp.PUBLIC, fun_female);

		/**
		 * Class Male
		 */
		CppClass clazz_male = CppClass.factory.create("Male");
		clazz_male.addExtended(Cpp.PUBLIC, clazz_person);
		clazz_male.add(Cpp.PRIVATE, var_cars);

		// Constructor
		CppConstructor con_male = CppConstructor.factory.create();
		//con_male.add(con_person); // TODO: Buggy, use the sring version, see next line.
		con_male.add("Person(name, age)");
		con_male.appendCode("this->cars = cars;");
		con_male.add(var_name, var_age, var_cars);
		clazz_male.add(Cpp.PUBLIC, con_male);

		// Function
		CppFun fun_male = CppFun.factory.create(Cpp.VOID, "print");
		fun_male.appendCode("cout << name << \" is \" << age << \", male and owns \" << cars << \" cars.\\n\";");
		clazz_male.add(Cpp.PUBLIC, fun_male);

		/**
		 * cpp + hpp + main
		 */

    // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(fileName);

		// We also need a header
    CppSourceFile header = this.workspace.getC().getCppHeaderFile(fileName);
    header.addLibInclude("iostream");
    header.addUsingNamespace("std");
    file.addInclude(header);

    // Add the main function to the file
    CFun fun_main = CFun.factory.create("main", "int", null);
    fun_main.appendCode("Person p(\"Person\", 48);");
    fun_main.appendCode("Male otto(\"Otto\", 48, 2);");
    fun_main.appendCode("Female helga(\"Helga\", 12, 18);");
    fun_main.appendCode("");
    fun_main.appendCode("p.print();");
    fun_main.appendCode("otto.print();");
    fun_main.appendCode("helga.print();");
    file.add(fun_main);

    /**
     * Finally, add class to the file
     */
    header.add(clazz_person, clazz_female, clazz_male);
	}
}
