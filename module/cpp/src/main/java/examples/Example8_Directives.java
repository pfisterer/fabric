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
import de.uniluebeck.sourcegen.c.CPreProcessorDirective;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.exceptions.CDuplicateException;
import de.uniluebeck.sourcegen.exceptions.CPreProcessorValidationException;
import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;

/**
 * Use #define, #ifdef, #else, #endif
 *
 * - Compile with: g++ Directives.cpp -o directives
 * - Run with: ./directives
 * - Returns: 3.14159
 *
 * @author Dennis Boldt
 *
 */

public class Example8_Directives {

	private Workspace workspace = null;

	public Example8_Directives(Workspace workspace) throws CppDuplicateException {
	    this.workspace = workspace;
	    try {
			generate();
		} catch (CDuplicateException e) {
			e.printStackTrace();
		} catch (CPreProcessorValidationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generate the CPP-files
	 *
	 * @throws CppDuplicateException
	 * @throws CDuplicateException
	 * @throws CPreProcessorValidationException
	 */
	void generate() throws CppDuplicateException, CDuplicateException, CPreProcessorValidationException{

		String className = "Directives";

        // Generate the class -- without an explicit file
        CppClass class_CRectangleSimple = CppClass.factory.create(className);

        // Generate two int variables
        CppTypeGenerator type_int = new CppTypeGenerator(Cpp.DOUBLE);

        // Generate function area
        CppFun fun_print = CppFun.factory.create(type_int, "print");

        CPreProcessorDirective preIfdef = CPreProcessorDirective.factory.create("ifdef PRINT_PI");
        CPreProcessorDirective preIfdefPI = CPreProcessorDirective.factory.create("ifdef PI");
        CPreProcessorDirective preElse = CPreProcessorDirective.factory.create("else");
        CPreProcessorDirective preEndif = CPreProcessorDirective.factory.create("endif");

        fun_print.appendCode(preIfdefPI.toString());
        fun_print.appendCode("return PI;");
        fun_print.appendCode(preElse.toString());
        fun_print.appendCode("return 0;");
        fun_print.appendCode(preEndif.toString());
        class_CRectangleSimple.add(Cpp.PUBLIC, fun_print);

        // Generate the files (cpp + hpp)
		CppSourceFile file = workspace.getC().getCppSourceFile(className);

		// We also need a header
        CppSourceFile header = this.workspace.getC().getCppHeaderFile(className);
        header.add(class_CRectangleSimple);
        file.addInclude(header);

        // The following line can be added to the file as well
        file.addBeforeDirective("define PRINT_PI");
        file.addBeforeDirective(preIfdef);
        file.addBeforeDirective("define PI 3.14159");
        file.addBeforeDirective(preEndif);
        file.addLibInclude("iostream");
        file.addUsingNameSpace("std");

        // Add the main function to the file
        CFun fun_main = CFun.factory.create("main", "int", null);
        fun_main.appendCode(className + " obj;");
        fun_main.appendCode("cout << obj.print() << \"\\n\";");
        fun_main.appendCode("return 0;");
        file.add(fun_main);

        // Finally, add class to the file
        file.add(class_CRectangleSimple);
	}
}
