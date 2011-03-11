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

package fabric.wsdlschemaparser.wsdl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;

import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.ibm.wsdl.xml.WSDLReaderImpl;

import fabric.wsdlschemaparser.schema.FSchema;

public class FWSDL {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(FWSDL.class);

	private Set<FMethod> methods = new HashSet<FMethod>();

	private FSchema schema = new FSchema();

	public FWSDL(File wsdlFile) throws MalformedURLException, Exception {
		log.info("Reading WSDL definition from " + wsdlFile);

		Definition wsdlDef = new WSDLReaderImpl().readWSDL(wsdlFile.getAbsolutePath());
		Map<?, ?> wsdlNamespaces = wsdlDef.getNamespaces();

		if (wsdlDef.getTypes() != null) {
			List<?> exEls = wsdlDef.getTypes().getExtensibilityElements();
			for (int i = 0; i < exEls.size(); i++) {
				Object a = exEls.get(i);

				/* Ignore Types != an XML Schema */
				if (javax.wsdl.extensions.schema.Schema.class.isAssignableFrom(a.getClass())) {
					javax.wsdl.extensions.schema.Schema containedSchema = (javax.wsdl.extensions.schema.Schema) a;

					log.debug("Found XMLSchema in WSDL: " + containedSchema);

					/* This is the root node of the XML Schema tree */
					Element schemaElement = containedSchema.getElement();

					/*
					 * Go through all parent namespace declarations and copy
					 * them into the tree of the XMLSchema.
					 * (probably a little hacky)
					 */
					for (Object key : wsdlNamespaces.keySet()) {
						String nsShort = (String) key;
						String nsLong = (String) wsdlNamespaces.get(key);

						if (schemaElement.getAttribute("xmlns:" + nsShort).length() == 0) {
							schemaElement.setAttribute("xmlns:" + nsShort, nsLong);
						}
					}

					/* Fall back on global defininiton */
					if (schemaElement.getAttribute("targetNamespace").length() == 0) {
						schemaElement.setAttribute("targetNamespace", wsdlDef.getTargetNamespace());
					}

					/*
					 * TODO: more things to take care of when cutting out the
					 * schema?
					 */
					Schema schemaDocument = SchemaDocument.Factory.parse(schemaElement).getSchema();
					log.debug("Adding schema to FSchema: " + schemaDocument);
					schema.addSchema(schemaDocument, wsdlFile.toURI());
				}

				// log.debug("Ignoring unknown ExtensibilityElement in WSDL (type: " + a.getClass() + ") --> " + a);
			}
		}

	}

	// -------------------------------------------------------------------------
	/**
	 *
	 */
	public void add(FMethod m) {
		methods.add(m);
	}

	// -------------------------------------------------------------------------
	/**
	 *
	 */
	public Collection<FMethod> getMethods() {
		return new HashSet<FMethod>(methods);
	}

	// -------------------------------------------------------------------------
	/**
	 *
	 */
	public FSchema getSchema() {
		return schema;
	}

	/**
	 * Gives back the method definitions in human readable form (not all information is included)
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		String ret = "";

		for (FMethod method : this.getMethods()) {
			String opString = "Operation: " + method.getName() + "(";

			opString += method.getParameters().getSchemaType().getName() + " " + method.getParameters().getName()
					+ ", ";
			opString += ") --> (";
			opString += method.getReturnVal().getSchemaType().getName() + " " + method.getReturnVal().getName() + ", ";
			opString += ");";
			ret += opString;
		}

		ret += "Schema: " + this.getSchema();
		
		return ret;
	}
}
