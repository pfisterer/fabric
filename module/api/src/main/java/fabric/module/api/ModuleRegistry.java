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
/**
 * 
 */
package fabric.module.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Registry for Fabric's modules.
 * 
 * @author Marco Wegner
 */
public final class ModuleRegistry implements Iterable<FabricModule> {

    /**
     * This instance holds the registered Fabric modules mapped to their
     * respective names.
     */
    private final Map<String, FabricModule> modules;

    {
        this.modules = new HashMap<String, FabricModule>( );
    }

    /**
     * Constructs a new module registry.
     */
    public ModuleRegistry( ) {
        //
    }

    /**
     * Registers a single module in this registry.
     * 
     * @param m The module to be registered.
     * @throws Exception If the registry already contains a module with the same
     *         name as the module to be added now.
     */
    public void register(FabricModule m) throws Exception {
        final String moduleName = m.getName( );
        if (this.modules.containsKey(moduleName)) {
            throw new Exception(String.format("registry already contains module with name '%s'",
                    moduleName));
        }
        this.modules.put(moduleName, m);
    }

    /**
     * Returns the module which has the specified name.
     * 
     * @param name The module name to be queried.
     * @return The module.
     * @throws Exception If there is no module registered with the specified
     *         name.
     */
    public FabricModule get(String name) throws Exception {
        if (!this.modules.containsKey(name)) {
            throw new Exception(String.format("no module regsitered with name '%s'", name));
        }
        return this.modules.get(name);
    }

    @Override
    public String toString( ) {
        final StringBuilder sb = new StringBuilder( );
        sb.append("Registered modules:\n-------------------\n");
        for (final Entry<String, FabricModule> e : this.modules.entrySet( )) {
            final FabricModule m = e.getValue( );
            sb.append(String.format("Module[%s]: %s\n", m.getName( ), m.getDescription( )));
        }
        return sb.toString( );
    }

    @Override
    public Iterator<FabricModule> iterator( ) {
        return this.modules.values( ).iterator( );
    }
}
