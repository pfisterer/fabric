/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the
 * name of the University of Luebeck nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package fabric.module.api;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.Workspace;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaObject;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSimpleType;

public abstract class TreeWalkerBase implements Module {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(TreeWalkerBase.class);

	private Workspace workspace;

	@Override
	public final void handle(Workspace workspace, FSchema schema) throws Exception {
		this.workspace = workspace;

		for (FElement schemaElement : schema.getTopLevelObjectList().getTopLevelElements()) {
			log.debug("Now handling top level schema element {}", schemaElement.getName());
			handle(schemaElement);
		}
	}

	protected Workspace getWorkspace() {
		return workspace;
	}

	
	public final void handle(List<? extends FSchemaObject> objects) throws Exception {
		for (FSchemaObject o : objects) {
			if (o instanceof FElement) {
				handle((FElement) o);
			} else if (o instanceof FSchemaType) {
				handle((FSchemaType) o);
			}
		}
	}

	public final void handle(FSchemaType t) throws Exception {
		startTopLevelType();
		if (t instanceof FSimpleType) {
			handle((FSimpleType) t);
		} else if (t instanceof FComplexType) {
			handle((FComplexType) t);
		}
	}

	protected abstract void handle(FElement e) throws Exception;

	protected abstract void handle(FSimpleType t) throws Exception;

	protected abstract void handle(FComplexType t) throws Exception;

	protected abstract void startTopLevelType() throws Exception;

}
