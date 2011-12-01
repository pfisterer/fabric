/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *      - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *        disclaimer.
 *      - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *        following disclaimer in the documentation and/or other materials provided with the distribution.
 *      - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *        products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 *
 */
package test;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.c.Cpp;
import de.uniluebeck.sourcegen.c.CppClass;
import de.uniluebeck.sourcegen.c.CppFun;
import de.uniluebeck.sourcegen.c.CppSourceFile;
import de.uniluebeck.sourcegen.c.CppTemplateHelper;
import de.uniluebeck.sourcegen.c.CppTypeDef;
import de.uniluebeck.sourcegen.c.CppTypeGenerator;
import de.uniluebeck.sourcegen.c.CppVar;
import fabric.module.api.FabricDefaultHandler;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Cpp-Module-handler
 * Generates the example_app.cpp from Wiselib
 *
 * @see <a href="http://www.wiselib.org/browser/applications/example_app/example_app.cpp">http://www.wiselib.org/browser/applications/example_app/example_app.cpp</a>
 * @author Dennis Boldt
 */
public class CppModuleHandler extends FabricDefaultHandler {

    private CppSourceFile file = null;
    private Workspace workspace = null;

    public CppModuleHandler(Properties properties, Workspace workspace) {
        String fileName = properties.get(CppModule.CPP_OUTFILE).toString();
        this.file = workspace.getC().getCppSourceFile(fileName);
        this.workspace = workspace;
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        this.codeGen_example_app();
    }

    private void codeGen_example_app() throws Exception {

        // Get an header file
        CppSourceFile header = this.workspace.getC().getCppHeaderFile("header");
        this.file.addInclude(header);

        // Generate the application
        CppClass example = CppClass.factory.create("ExampleApplication", this.file);
        header.add(example);

        // Generate the types
        CppTypeGenerator os = new CppTypeGenerator("Os");

        // typedef wiselib::OSMODEL Os;
        CppTypeDef typeDef = CppTypeDef.getInstance();
        typeDef.addTypeDef("wiselib::OSMODEL", os);
        // TODO: Add typedef to class

        // Os::Radio::self_pointer_t radio_;
        CppTypeGenerator radio = new CppTypeGenerator("Radio");
        CppTypeGenerator radio_self_pointer_t = new CppTypeGenerator("self_pointer_t");
        CppVar radio_ = CppVar.factory.create(radio_self_pointer_t, "radio_", os, radio);

        // Os::Timer::self_pointer_t timer_;
        CppTypeGenerator timer = new CppTypeGenerator("Timer");
        CppTypeGenerator timer_self_pointer_t = new CppTypeGenerator("self_pointer_t");
        CppVar timer_ = CppVar.factory.create(timer_self_pointer_t, "timer_", os, timer);

        // Os::Debug::self_pointer_t debug_;
        CppTypeGenerator debug = new CppTypeGenerator("Debug");
        CppTypeGenerator debug_self_pointer_t = new CppTypeGenerator("self_pointer_t");
        CppVar debug_ = CppVar.factory.create(debug_self_pointer_t, "debug_", os, debug);

        // void init( Os::AppMainParameter& value )
        CppTypeGenerator appMainParameter = new CppTypeGenerator("AppMainParameter", Cpp.REFERENCE);
        CppVar value = CppVar.factory.create(appMainParameter, "value", os);
        CppFun fun_init = CppFun.factory.create(example, Cpp.VOID, "init", value);

        // Content of the function
        fun_init.appendCode("radio_ = &wiselib::FacetProvider<Os, Os::Radio>::get_facet( value );");
        fun_init.appendCode("timer_ = &wiselib::FacetProvider<Os, Os::Timer>::get_facet( value );");
        fun_init.appendCode("debug_ = &wiselib::FacetProvider<Os, Os::Debug>::get_facet( value );");
        fun_init.appendCode("");
        fun_init.appendCode("debug_->debug( \"Hello World from Example Application!\\n\" );");
        fun_init.appendCode("");
        fun_init.appendCode("radio_->reg_recv_callback<ExampleApplication, &ExampleApplication::receive_radio_message>( this );");
        fun_init.appendCode("timer_->set_timer<ExampleApplication, &ExampleApplication::start>( 5000, this, 0 );");

        // void start( void* )
        CppVar void_pointer = CppVar.factory.create("void*");
        CppFun fun_start = CppFun.factory.create(example, Cpp.VOID, "start", void_pointer);

        // Content of the function
        fun_start.appendCode("debug_->debug( \"broadcast message at %d \\n\", radio_->id() );");
        fun_start.appendCode("Os::Radio::block_data_t message[] = \"hello world!\\0\";");
        fun_start.appendCode("radio_->send( Os::Radio::BROADCAST_ADDRESS, sizeof(message), message );");

        // Add all variables and functions
        example.add(Cpp.PRIVATE, radio_, timer_, debug_);
        example.add(Cpp.PUBLIC, fun_init, fun_start);

        // ####################################################################
        // Example with nested templates -- not in example_app.cpp
        // ####################################################################
        // void fun_temp(wiselib::ExampleApplication<Os, Timer<Os, Radio>> var_temp);

        CppTemplateHelper template1 = new CppTemplateHelper();
        template1.add(os);
        template1.add(radio);
        CppTypeGenerator gen_timer = new CppTypeGenerator(timer, template1);

        CppTemplateHelper template2 = new CppTemplateHelper();
        template2.add(os);
        template2.add(gen_timer);
        CppTypeGenerator gen_app = new CppTypeGenerator(example, template2);

        CppTypeGenerator gen_wiselib = new CppTypeGenerator("wiselib");
        CppVar var_temp = CppVar.factory.create(gen_app, "var_temp", gen_wiselib);
        CppFun fun_temp = CppFun.factory.create(example, Cpp.VOID, "temp", var_temp);

        // Content of the function
        fun_temp.appendCode("/** Do nothing here */");
        fun_temp.appendCode("return;");

        example.add(Cpp.PUBLIC, fun_temp);
    }

    @Override
    public void endSchema(FSchema schema) throws Exception {
        // doesn't do anything
    }

    @Override
    public void startTopLevelElement(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelElement(FElement element) {
        // doesn't do anything
    }

    @Override
    public void startLocalElement(FElement element, FComplexType parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalElement(FElement element, FComplexType parent) {
        // doesn't do anything
    }

    @Override
    public void startElementReference(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endElementReference(FElement element) throws Exception {
        // doesn't do anything
    }

    @Override
    public void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelSimpleType(FSimpleType type, FElement parent) {
        // doesn't do anything
    }

    @Override
    public void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalSimpleType(FSimpleType type, FElement parent) {
        // doesn't do anything
    }

    @Override
    public void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endTopLevelComplexType(FComplexType type, FElement parent) {
        // doesn't do anything
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalComplexType(FComplexType type, FElement parent) {
        // doesn't do anything
    }

}
