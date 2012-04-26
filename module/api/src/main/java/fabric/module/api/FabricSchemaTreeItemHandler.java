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

import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSimpleType;

/**
 * Handler interface for specific items of the Schema object tree. This handler
 * supplies callback methods which are called when encountering specific Schema
 * object tree items during a Schema tree-walk.
 * 
 * @author Marco Wegner
 */
public interface FabricSchemaTreeItemHandler {

    /**
     * Signifies that walking the Schema object tree is about to start.
     * 
     * @param schema The root element of the Schema object tree to be walked.
     * @throws Exception If an error occurs.
     */
    public abstract void startSchema(FSchema schema) throws Exception;

    /**
     * Signifies that walking the Schema object tree has just finished.
     * 
     * @param schema The root element of the recently walked Schema object tree.
     * @throws Exception If an error occurs.
     */
    public abstract void endSchema(FSchema schema) throws Exception;

    /**
     * @param element
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelElement(FElement element) throws Exception;

    /**
     * @param element
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalElement(FElement element, FComplexType parent) throws Exception;

    /**
     * @param element
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalElement(FElement element, FComplexType parent) throws Exception;

    /**
     * Signifies that handling the specified element reference is about to
     * start.
     * 
     * @param element The element reference to be handled.
     * @throws Exception If an error occurs.
     */
    public abstract void startElementReference(FElement element) throws Exception;

    /**
     * Signifies that handling the specified element reference has just
     * finished.
     * 
     * @param element The recently handled element reference.
     * @throws Exception If an error occurs.
     */
    public abstract void endElementReference(FElement element) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalSimpleType(FSimpleType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startTopLevelComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endTopLevelComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void startLocalComplexType(FComplexType type, FElement parent) throws Exception;

    /**
     * @param type
     * @param parent TODO
     * @throws Exception If an error occurs.
     */
    public abstract void endLocalComplexType(FComplexType type, FElement parent) throws Exception;

}
