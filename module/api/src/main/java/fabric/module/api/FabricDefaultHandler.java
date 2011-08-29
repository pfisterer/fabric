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
/**
 * 
 */
package fabric.module.api;

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Default handler class for XML Schema object trees. This implementation
 * actually doesn't do anything.
 * 
 * @author Marco Wegner
 */
public class FabricDefaultHandler implements FabricSchemaTreeItemHandler {

    /**
     * Constructs a new handler for a tree of objects parsed from an XML Schema
     * document.
     */
    public FabricDefaultHandler( ) {
        super( );
    }

    @Override
    public void startSchema(FSchema schema) throws Exception {
        // doesn't do anything
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
    public void endTopLevelComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void startLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }

    @Override
    public void endLocalComplexType(FComplexType type, FElement parent) throws Exception {
        // doesn't do anything
    }
}
