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

/**
 * 
 */
package fabric.wsdlschemaparser.schema;

import org.slf4j.LoggerFactory;

/**
 * @author Marco Wegner
 */
public abstract class FSchemaObject {

	// --------------------------------------------------------------------
	// Static members
	// --------------------------------------------------------------------

	private final org.slf4j.Logger log = LoggerFactory.getLogger(FSchemaObject.class);

	private FSchema fschema = null;

	/**
	 * Static counter for consecutively numbering the IDs.
	 */
	private static int s_id = 0;

	/**
	 * This object's name.
	 */
	private String name;

	/**
	 * This type's ID. The ID is unique for every Schema file.
	 */
	private int id;

	/**
	 * This object's namespace (e.g. "http://www.foo.org/bar/")
	 */
	private String namespace = null;

	// --------------------------------------------------------------------

	/**
	 * 
	 */
	public FSchemaObject() {
		this(null);
	}

	// --------------------------------------------------------------------

	/**
	 * @param name
	 */
	public FSchemaObject(String name) {
		this.id = ++s_id;
		setName(name);
	}

	// --------------------------------------------------------------------

	/**
	 * @param name
	 */
	public void setName(String name) {
		String n = name;
		if (n != null && n.equals(""))
			n = null;
		this.name = n;
	}

	public void setFSchema(FSchema fschema) {
		this.fschema = fschema;
	}

	public FSchema getFSchema() {
		return this.fschema;
	}

	// --------------------------------------------------------------------

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * DANIEL: javadoc, test
	 * 
	 * @param other
	 * @return
	 * @author Daniel Bimschas
	 */
	public abstract boolean equals(FSchemaObject other);

	/**
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace
	 *            the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public int getNamespaceCount() {
		return this.fschema.getNamespaces().size();
	}

	/**
	 * Returns the prefix of this object type.
	 * 
	 * the prefix uniquely identifies a namespace and can (via the coresponding FSchema object) be bijectivly mapped to
	 * the coresponding namespace.
	 * 
	 * note that this prefix has *nothing* to do with the ns-prefix used in xml (like "xsd:").
	 * 
	 * @return the prefix of this schemaobject.
	 */
	public String getInternalNamespacePrefix() {

		if (this.fschema == null) {
			throw new RuntimeException("reference to FSchema object is undefined (at " + this.getName() + ")!");
		}

		if (this.namespace == null)
			log.error("namespace is undefined (at " + this.getName() + ")!");

		return this.fschema.mapNamespaceToNSPrefix(this.namespace);
	}
}
