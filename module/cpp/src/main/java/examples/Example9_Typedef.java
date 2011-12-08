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
import de.uniluebeck.sourcegen.c.CParam;
import de.uniluebeck.sourcegen.c.CStruct;
import de.uniluebeck.sourcegen.c.CTypeDef;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeDef;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.exceptions.CCodeValidationException;
import de.uniluebeck.sourcegen.exceptions.CConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Use typedefs
 *
 * - Compile with: g++ Typedef.cpp -o typedef
 * - Run with: ./typedef
 * - Returns: 3.14159
 *
 * @author Dennis Boldt
 *
 */

public class Example9_Typedef {

	private Workspace workspace = null;

	public Example9_Typedef(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
	    try {
			generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generate the CPP-files
	 * @throws CConflictingModifierException
	 * @throws CCodeValidationException
	 */
	void generate() throws CppDuplicateException, CDuplicateException, CPreProcessorValidationException, CConflictingModifierException, CCodeValidationException{

		String className = "Typedef";

        // Generate the class -- without an explicit file
        CppClass clazz = CppClass.factory.create(className);

        // Generate an int variable
        CppTypeGenerator type_double = new CppTypeGenerator(Cpp.DOUBLE);

        // Generate the print-function
        CppFun fun_print = CppFun.factory.create(type_double, "print");
        fun_print.appendCode("return 0;");
        clazz.add(Cpp.PUBLIC, fun_print);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);

		// We also need a header
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        header.add(clazz);
        header.addLibInclude("iostream");
        header.addLibInclude("string");
        header.addUsingNamespace("std");

        // Let's generate some typedefs
        CTypeDef uint = CppTypeDef.factory.create("unsigned int", "uint");
        CTypeDef uintArray = CppTypeDef.factory.create(uint.getAlias(), "intArray[3]");
        CTypeDef stringArray = CppTypeDef.factory.create("string", "sData[3]");
        CTypeDef pDouble = CppTypeDef.factory.create("double*", "pDouble");

        // Add all typedefs
        header.add(uint);
        header.add(uintArray);
        header.add(stringArray);
        header.add(pDouble);

        CParam px = CParam.factory.create(type_double.getName(), "re");
        CParam py = CParam.factory.create(type_double.getName(), "im");
        CStruct struct = CStruct.factory.create("var", "complex", true, px, py);
        header.add(struct);

        // Add the header to the file
        file.addInclude(header);

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);

        fun_main.appendCode(uint.getAlias() + " temperature = 12;");
        fun_main.appendCode(uintArray.getAlias() + " numers = {0,8,15};");
        fun_main.appendCode(stringArray.getAlias() + " sData = {\"abc\",\"def\",\"ghi\"};");
        fun_main.appendCode(pDouble.getAlias() + " dData = new double(10.20);");

        fun_main.appendCode("cout << temperature << \"\\n\";");

        fun_main.appendCode(struct.getTypeName() + " c;");
        fun_main.appendCode("c.re = 3.8;");
        fun_main.appendCode("c.im = 4.2;");

        //fun_main.appendCode(className + " obj;");
        //fun_main.appendCode("cout << obj.print() << \"\\n\";");
        //fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add class to the file
        file.add(clazz);
	}
}
