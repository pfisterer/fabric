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
import de.uniluebeck.sourcegen.c.CTypeDef;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTypeDef;
import de.uniluebeck.sourcegen.c.CppVar;

/**
 * This is a basic class to generate other examples
 *
 * Compile with Wislib
 *
 * @author Dennis Boldt
 * @see https://github.com/ibr-alg/wiselib/blob/master/apps/generic_apps/example_app/example_app.cpp
 *
 */

public class Example11_Wislib {

	private Workspace workspace = null;

	public Example11_Wislib(Workspace workspace) throws Exception {
	    this.workspace = workspace;
		generate();
	}

	void generate() throws Exception {

    	/**
    	 * Generate the file
    	 */
    	CppSourceFile file = workspace.getC().getCppSourceFile("Wislib");
    	file.setComment(new CCommentImpl("Simple Wiselib Example from\n * https://github.com/ibr-alg/wiselib/blob/master/apps/generic_apps/example_app/example_app.cpp"));

        /**
         * Add the external libs
         */
        file.addInclude("external_interface/external_interface.h");
        file.addInclude("algorithms/routing/tree/tree_routing.h");
        /**
         * Generate typdef
         */
        CTypeDef typedef_os = CppTypeDef.factory.create("wiselib::OSMODEL", "Os");
        file.add(typedef_os);

        /**
         * Generate the class
         */
        CppClass clazz = CppClass.factory.create("ExampleApplication");
        file.add(clazz);

        /**
         * Generate the init function
         */
        CppVar var_value = CppVar.factory.create("Os::AppMainParameter&", "value");
        CppFun fun_init = CppFun.factory.create(Cpp.VOID, "init", var_value);
        fun_init.appendCode("radio_ = &wiselib::FacetProvider<Os, Os::Radio>::get_facet( value );");
        fun_init.appendCode("timer_ = &wiselib::FacetProvider<Os, Os::Timer>::get_facet( value );");
        fun_init.appendCode("debug_ = &wiselib::FacetProvider<Os, Os::Debug>::get_facet( value );");
        fun_init.appendCode("");
        fun_init.appendCode("debug_->debug( \"Hello World from Example Application!\\n\" );");
        fun_init.appendCode("radio_->reg_recv_callback<ExampleApplication, &ExampleApplication::receive_radio_message>( this );");
        fun_init.appendCode("");
        fun_init.appendCode("timer_->set_timer<ExampleApplication,&ExampleApplication::start>( 5000, this, 0 );");
        clazz.add(Cpp.PUBLIC, fun_init);

        /**
         * Generate the start function
         */
        CppVar void_pointer = CppVar.factory.create("void*", "");
        CppFun fun_start = CppFun.factory.create(Cpp.VOID, "start", void_pointer);
        fun_start.appendCode("debug_->debug( \"broadcast message at %d \\n\", radio_->id() );");
        fun_start.appendCode("Os::Radio::block_data_t message[] = \"hello world!\\0\";");
        fun_start.appendCode("radio_->send( Os::Radio::BROADCAST_ADDRESS, sizeof(message), message );");
        clazz.add(Cpp.PUBLIC, fun_start);

        /**
         * Generate the receive_radio_message function
         */
        CppVar from = CppVar.factory.create("Os::Radio::node_id_t", "from");
        CppVar len = CppVar.factory.create("Os::Radio::size_t", "len");
        CppVar buf = CppVar.factory.create("Os::Radio::block_data_t", "*buf");
        CppFun fun_receive_radio_message = CppFun.factory.create(Cpp.VOID, "receive_radio_message", from, len, buf);
        fun_receive_radio_message.appendCode("debug_->debug( \"received msg at %u from %u\\n\", radio_->id(), from );");
        fun_receive_radio_message.appendCode("debug_->debug( \"message is %s\\n\", buf );");
        clazz.add(Cpp.PUBLIC, fun_receive_radio_message);

        /**
         * Add some private vars
         */
        CppVar radio_ = CppVar.factory.create("Os::Radio::self_pointer_t", "radio_");
        CppVar timer_ = CppVar.factory.create("Os::Timer::self_pointer_t", "timer_");
        CppVar debug_ = CppVar.factory.create("Os::Debug::self_pointer_t", "debug_");
        clazz.add(Cpp.PRIVATE, radio_, timer_, debug_);

        /**
         * Add the var
         */
        CppVar example_app = CppVar.factory.create("wiselib::WiselibApplication<Os, ExampleApplication>", "example_app");
        file.add(example_app);

        /**
         * Generate the main
         */
        CParam value2 = CParam.factory.create("Os::AppMainParameter&", "value");
        CFunSignature sig = CFunSignature.factory.create(value2);
        CFun fun_main = CFun.factory.create("application_main", "void", sig);
        fun_main.appendCode("example_app.init( value );");
        file.add(fun_main);
	}
}
