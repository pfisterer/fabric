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
package fabric.module.echo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.xmlbeans.SchemaType;
import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.TreeWalkerBase;
import fabric.wsdlschemaparser.schema.FAll;
import fabric.wsdlschemaparser.schema.FChoice;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FNumber;
import fabric.wsdlschemaparser.schema.FSchemaObject;
import fabric.wsdlschemaparser.schema.FSchemaRestrictions;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FSchemaTypeHelper;
import fabric.wsdlschemaparser.schema.FSequence;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.FString;

/**
 * @author Marco Wegner
 */
public class EchoModule extends TreeWalkerBase {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(EchoModule.class);
	
	private BufferedWriter writer;

	public EchoModule(File dotFile, Workspace workspace) throws Exception {
		//TODO Change dot module to use workspace
		log.debug("New dot writer for output file {}", dotFile);
		log.info("Run dot -Tpng -O {} to generate a PNG output file", dotFile);
		this.writer = new BufferedWriter(new FileWriter(dotFile));
		writeLine("digraph G {");
	}
	
	@Override
	public void done() throws Exception {
		writeLine("}");
		writer.flush();
		writer.close();
		log.debug("Dot writing done.");
	}

	@Override
	protected void startTopLevelType() throws Exception {
	}

	@Override
	public void handle(FElement elem) throws Exception {
		handleElement(elem, null);
	}

	@Override
	public void handle(FSimpleType st) throws Exception {
		writeType(st);
	}

	@Override
	public void handle(FComplexType ct) throws Exception {
		writeType(ct);
		handleComplexTypeChildren(ct);
	}

	private void handleElement(FElement elem, FComplexType parent) throws Exception {
		writeElement(elem, parent);

		FSchemaType type = elem.getSchemaType();

		if (type instanceof FSimpleType) {
			handle((FSimpleType) type);
		} else if (type instanceof FComplexType) {
			handle((FComplexType) type);
		} else {
			throw new Exception("Unknown SimpleType: " + type.toString());
		}

		writeTransition(elem, parent, type);
	}

	private void handleComplexTypeChildren(FComplexType ct) throws Exception {
		for (FSchemaObject o : ct.getChildObjects()) {
			if (o instanceof FElement) {
				FElement e = (FElement) o;
				handleElement(e, ct);
				writeTransition(ct, e);
			} else if (o instanceof FComplexType) {
				FComplexType ctype = (FComplexType) o;
				handle(ctype);
				writeTransition(ct, ctype);
			} else {
				throw new Exception("Unknown type: " + o.toString());
			}
		}
	}

	private void writeElement(FElement fse, FComplexType parent) throws Exception {

		// don't generate anything if the element is just referenced
		if (fse.isReference()) {
			return;
		}

		// element attributes
		String eattr = "shape = ellipse";
		if (fse.isTopLevel()) {
			eattr += ", style = filled";
		}
		eattr = " [" + eattr + "]";

		String source = fse.getName();
		if (parent != null) {
			source = getElementString(fse, parent);
		}
		writeLine(source + eattr + ";");
	}

	private void writeType(FSchemaType type) throws Exception {
		String name = getTypeString(type);

		String attr;
		if (FSchemaTypeHelper.isEnum(type)) {
			attr = "shape = polygon, sides = 4, skew = 0.4";
		} else {
			attr = "shape = box";
		}

		if (type.isTopLevel()) {
			attr += ", style = filled";
		}

		writeLine(name + " [" + attr + "];");
	}

	private void writeTransition(FElement source, FComplexType parent, FSchemaType dest) throws Exception {
		String attr = "";
		if (dest.isTopLevel()) {
			attr = " [style = dashed]";
		}
		writeLine(getElementString(source, parent) + " -> " + getTypeString(dest) + attr);
	}

	private void writeTransition(FComplexType source, FElement dest) throws Exception {
		String attr = "";
		if (dest.isReference()) {
			attr = " [style = dashed]";
		}
		writeLine(getTypeString(source) + " -> " + getElementString(dest, source) + attr);
	}

	private void writeTransition(FComplexType source, FComplexType dest) throws Exception {
		writeLine(getTypeString(source) + " -> " + getTypeString(dest));
	}

	private String getElementString(FElement e, FComplexType parent) {
		String destination = e.getName();
		if (!(e.isTopLevel() || e.isReference())) {
			destination = "\"" + getTypeNameString(parent) + "::\\n" + destination + "\"";
		}
		return destination;
	}

	private String getTypeString(FSchemaType ft) {
		String name = getTypeNameString(ft);

		boolean hasEnum = FSchemaTypeHelper.isEnum(ft);

		String prefix = "";
		if (ft.isSimple()) {
			if (hasEnum) {
				prefix = "<<enum>>\\n";
			}
		} else {
			if (ft instanceof FAll) {
				prefix = "<<all>>\\n";
			} else if (ft instanceof FChoice) {
				prefix = "<<choice>>\\n";
			} else if (ft instanceof FSequence) {
				prefix = "<<sequence>>\\n";
			}
		}

		return "\"" + prefix + name + "\"";
	}

	private String getTypeNameString(FSchemaType ft) {

		String name = ft.getName();
		if (name == null) {
			// locally defined type
			if (ft.isSimple()) {
				name = ft.getClass().getSimpleName() + "[ID:" + ft.getID() + "]";

				String frs = getRestrictionString(ft);
				if (frs.length() > 0) {
					name += "\\n" + frs;
				}
			} else {
				name = "LocalComplexType[ID:" + ft.getID() + "]";
			}
		}
		return name;
	}

	private String getRestrictionString(FSchemaType ft) {

		FSchemaRestrictions r = ft.getRestrictions();
		if (r == null) {
			return "";
		}

		String frs = "";
		// string representation for restrictions (if necessary and/or available)
		if (ft instanceof FNumber) {
			if (r.hasRestriction(SchemaType.FACET_MIN_INCLUSIVE)) {
				frs += "[" + r.getStringValue(SchemaType.FACET_MIN_INCLUSIVE);
			} else if (r.hasRestriction(SchemaType.FACET_MIN_EXCLUSIVE)) {
				frs += "(" + r.getStringValue(SchemaType.FACET_MIN_EXCLUSIVE);
			}
			frs += ", ";
			if (r.hasRestriction(SchemaType.FACET_MAX_INCLUSIVE)) {
				frs += r.getStringValue(SchemaType.FACET_MAX_INCLUSIVE) + "]";
			} else if (r.hasRestriction(SchemaType.FACET_MAX_EXCLUSIVE)) {
				frs += r.getStringValue(SchemaType.FACET_MAX_EXCLUSIVE) + ")";
			}
		} else if (ft instanceof FString) {
			if (r.hasRestriction(SchemaType.FACET_LENGTH)) {
				frs = "[" + r.getStringValue(SchemaType.FACET_LENGTH) + "]";
			} else {
				int minLength = 0;
				if (r.hasRestriction(SchemaType.FACET_MIN_LENGTH)) {
					minLength = r.getIntegerValue(SchemaType.FACET_MIN_LENGTH);
				}

				String max = "unb";
				if (r.hasRestriction(SchemaType.FACET_MAX_LENGTH)) {
					max = r.getStringValue(SchemaType.FACET_MAX_LENGTH);
				}
				frs = "[" + minLength + ":" + max + "]";
			}
		}
		return frs;
	}

	private void writeLine(String line) throws Exception {
		writer.write(line);
		writer.newLine();
	}
}
