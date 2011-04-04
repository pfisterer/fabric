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
package fabric.wsdlschemaparser.schema;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class FSchema {
    /** */
    private final org.slf4j.Logger log = LoggerFactory.getLogger(FSchema.class);

    /** */
    private Collection<SchemaDocument.Schema> schemata = new ArrayList<SchemaDocument.Schema>();

    /** This Hashmap maps from namespaces to prefixes. It must be bijective! */
    private HashMap<String, String> prefixMap = new HashMap<String, String>();

    private HashMap<String, URI> targetNamespaceToFileLocationMap = new HashMap<String, URI>();

    FTopLevelObjectList topLevelObjectList = null;

    public FSchema() {

    }

    public FSchema(File xsdFile) throws Exception {
        log.info("Parsing XML Schema from file: " + xsdFile);
        addSchema(xsdFile);
        topLevelObjectList = generateSchemaTrees(this.schemata);
    }

    /**
     * This method generatea new prefix
     * 
     * TODO: Make this user-configurable (so users can define their own mappings).
     * 
     * @param namespace
     * @return a currently unused prefix
     */
    private String findUnusedNamespacePrefix(String namespace) {
        int i = 0;

        String prefix = "ns" + i;
        while (this.prefixMap.containsValue(prefix)) {
            i++;
            prefix = "ns" + i;
        }

        return prefix;
    }

    public void addSchema(File xsdFile) throws Exception {
        Schema schema = SchemaDocument.Factory.parse(xsdFile).getSchema();
        addSchema(schema, xsdFile.toURI());
    }

    public void addSchema(Schema schema, URI location) throws Exception {
        log.debug("Adding schema with target namespace {} and location {}", schema.getTargetNamespace(), location);

        schemata.add(schema);
        targetNamespaceToFileLocationMap.put(schema.getTargetNamespace(), location);

        if (this.prefixMap.containsKey(schema.getTargetNamespace()))
            throw new Exception("Namespace " + schema.getTargetNamespace() + " already defined by another schema!");

        String newPrefix = this.findUnusedNamespacePrefix(schema.getTargetNamespace());
        this.prefixMap.put(schema.getTargetNamespace(), newPrefix);

        loadAllReferencedSchemata(schema);
    }

    /**
     * Returns a collection of all namespaces known to this FSchema object.
     * 
     * @return
     */
    public Collection<String> getNamespaces() {
        ArrayList<String> list = new ArrayList<String>();

        for (SchemaDocument.Schema schema : this.schemata) {
            list.add(schema.getTargetNamespace());
        }

        return list;
    }

    /**
     * Maps an Namespace prefix (used for code generation to uniquely identify a namespace in compiler-friendly way)
     * back to a namespace.
     * 
     * @return the coresponding namespace or null if the prefix is unknown
     * @param prefix
     *            a prefix
     */
    public String mapNSPrefixToNamespace(String prefix) {
        for (String namespace : this.getNamespaces()) {
            if (this.prefixMap.get(namespace).equals(prefix))
                return namespace;
        }

        return null;
    }

    /**
     * Maps an namespace to a prefix.
     * 
     * @return the coresponding prefix or null if the namespace is unknown
     * @param namespace
     *            a namespace known to this FSchema object
     */
    public String mapNamespaceToNSPrefix(String namespace) {
        String ret = this.prefixMap.get(namespace);

        if (ret == null) {
            ret = findUnusedNamespacePrefix(namespace);
            this.prefixMap.put(namespace, ret);
        }

        return ret;
    }

    /**
     * This methods goes through the list of schemata and dereferences all import-declarations it finds.
     * 
     * it tries to load files, if a schemaLocation is given, otherwise it will try to download the schema from the
     * namespace URI.
     * 
     * @param xmlSchema
     * 
     * @throws Exception
     */
    private void loadAllReferencedSchemata(Schema xmlSchema) throws Exception {
        Import[] imports = xmlSchema.getImportArray();

        if (imports.length <= 0) {
            log.debug("No schemas referenced in " + xmlSchema.getTargetNamespace() + ". Returning.");
            return;
        }

        log.debug("XML Schema {} has {} import declarations. Resolving them.", xmlSchema.getTargetNamespace(),
                imports.length);

        /* Try to load the referenced schema */
        for (Import importElement : imports) {
            URI enclosingSchemaLocationUrl = getSchemaLocation(xmlSchema);
            File enclosingSchemaLocation = new File(enclosingSchemaLocationUrl.toURL().getFile()).getParentFile();

            String importLocation = importElement.getSchemaLocation();

            if (importLocation == null) {
                log.debug("No import location supplied, trying to use the namespace URI {}",
                        importElement.getNamespace());
                try {
                    URI uri = new URI(importElement.getNamespace());
                    uri.toURL();
                    importLocation = importElement.getNamespace();
                } catch (MalformedURLException e) {
                    log.debug("Unable to use {} as location: not a URL", importElement.getNamespace());
                }
            }

            Preconditions.checkNotNull(importLocation, "Import location must not be null");

            URI importLocationURI = new URI(importLocation);

            if ("file".equals(importLocationURI.getScheme()))
                importLocationURI = new File(enclosingSchemaLocation, importLocation).toURI();

            log.debug("Imported schema suspected to be at {}", importLocationURI);

            if (!isKnownTargetNamespace(importElement.getNamespace())) {
                log.debug("Importing schema with namespace {}", importElement.getNamespace());

                SchemaDocument doc = SchemaDocument.Factory.parse(importLocationURI.toURL());

                addSchema(doc.getSchema(), importLocationURI);
            }

        }
    }

    public FTopLevelObjectList getTopLevelObjectList() {
        return topLevelObjectList;
    }

    private FTopLevelObjectList generateSchemaTrees(Collection<Schema> schemata) {

        /*
         * Defining one Namespace for all Schemata doesnt make much sense if you are dealing with multiple Schemata. But
         * for backwart compatiblity I will set it to the namespace of the first schema anyway. -- Dfo
         */
        FTopLevelObjectList topLevelObjects = new FTopLevelObjectList(schemata.iterator().next().getTargetNamespace());

        FSchemaTypeFactory factory = new FSchemaTypeFactory(this, schemata);

        for (Schema schema : schemata) {

            /* Set current namespace */
            factory.setNamespace(schema.getTargetNamespace());

            TopLevelSimpleType[] simpleTypeArray = schema.getSimpleTypeArray();
            log.debug("Number of top-level simple types: " + simpleTypeArray.length);
            topLevelObjects.addAll(factory.generateAll(simpleTypeArray));

            TopLevelComplexType[] complexTypeArray = schema.getComplexTypeArray();
            log.debug("Number of top-level complex types: " + complexTypeArray.length);
            topLevelObjects.addAll(factory.generateAll(complexTypeArray));

            TopLevelElement[] elementArray = schema.getElementArray();
            log.debug("Number of top-level elements: " + elementArray.length);
            topLevelObjects.addAll(factory.generateAll(elementArray));

        }

        return topLevelObjects;
    }

    private boolean isKnownTargetNamespace(String namespace) {
        for (Schema schema : this.schemata) {
            if (schema.getTargetNamespace().equals(namespace)) {
                return true;
            }
        }
        return false;
    }

    private URI getSchemaLocation(SchemaDocument.Schema schema) throws Exception {
        URI location = this.targetNamespaceToFileLocationMap.get(schema.getTargetNamespace());

        if (location == null)
            throw new Exception("No location for Schema with target namespace " + schema.getTargetNamespace()
                    + " found.");

        return location;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for (SchemaDocument.Schema schema : schemata) {
            out.append("----------------------\n");

            out.append("Schema: \n");

            out.append("\t Namespace\t: " + schema.getTargetNamespace() + "\n");
            try {
                out.append("\t Location\t: " + getSchemaLocation(schema) + "\n");
            } catch (Exception e) {
                out.append("\t Location\t: Unknown\n");
            }

            out.append("\t Content\t: \n" + schema + "\n");
            out.append("----------------------\n\n");
        }

        return out.toString();
    }

}
