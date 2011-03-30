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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;



/**
 * 
 *
 * @author Dennis Pfisterer, pfisterer@farberg.de, 2005
 */
public class SchemaInfoDumper {
	Schema schema = null;
	SchemaHelper sh = null;

	/**
	 * 
	 * @param fabric.schema
	 */
	public SchemaInfoDumper(Schema schema) {
		this.schema = schema;
		ArrayList<Schema> list = new ArrayList<Schema>();
		list.add(schema);
		sh = new SchemaHelper(list);
	}

	/**
	 * 
	 * @param fabric.schema
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public SchemaInfoDumper(String file) throws XmlException, IOException {
		File schemaFile = new File(file);
		SchemaDocument sd = SchemaDocument.Factory.parse(schemaFile);
		schema = sd.getSchema();
		ArrayList<Schema> list = new ArrayList<Schema>();
		list.add(schema);
		sh = new SchemaHelper(list);
	}

	/**
	 * 
	 * @param out
	 * @throws Exception
	 */
	public void dumpTo(PrintWriter out) throws Exception {

		out.println();
		out.println("------------------ " + schema.getTargetNamespace() + " ------------------");
		out.println();

		dumpTopLevelComplexTypes(out);
		dumpTopLevelSimpleTypes(out);
		dumpTopLevelElements(out);

	}

	/**
	 * 
	 * @param out
	 * @param ann
	 */
	private void dumpTopLevelElements(PrintWriter out) {
		TopLevelElement[] tles = schema.getElementArray();
		
		for(int i = 0 ; i < tles.length ; ++i)
		{
			TopLevelElement tle = tles[i];
			out.println("Element: " + tle.getName());
			
			QName qn = tle.getType();
			
			if( sh.isLocalComplexType(qn) )
				out.println("\t Type (Local complex type): " + qn.getLocalPart());
			else if( sh.isLocalSimpleType(qn) )
				out.println("\t Type (Local simple type) : " + qn.getLocalPart());
			else if( sh.isXMLSchemaName(qn) )
				out.println("\t Type (Schema type) : " + qn.getLocalPart());
		}
		
	}


	/**
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void dumpTopLevelComplexTypes(PrintWriter out) throws Exception {
		TopLevelComplexType[] cta = schema.getComplexTypeArray();
		for (int i = 0; i < cta.length; ++i) {
			TopLevelComplexType ct = cta[i];
			out.println("CT:" + ct.getName());

			// Bridle annotation
			//TODO Dennis: Annotationen ausgeben?
			/*
			SchemaAnnotationParser sp = new SchemaAnnotationParser(spec, ct.getAnnotation());
			if (sp.hasFabricAnnotationDocument())
				dumpBridleAnnotation(out, sp.getTypeAnnotation().getFabricAnnotation());
			 */
			// Extension stuff
			LinkedList<ComplexType> ll = sh.getExtensionHierarchy(ct);
			for (int t = 0; t < ll.size() && ll.size() > 1; ++t) {
				if (t == 0)
					out.print("\t ");

				out.print(((TopLevelComplexType) ll.get(t)).getName());

				if (t == ll.size() - 1)
					out.println();
				else
					out.print(" extends ");

			}

			// Elements
			dumpElements(out, ct);
		}
	}

	/**
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void dumpTopLevelSimpleTypes(PrintWriter out) throws Exception {
		TopLevelSimpleType[] sta = schema.getSimpleTypeArray();
		for (int i = 0; i < sta.length; ++i) {
			TopLevelSimpleType st = sta[i];

			out.println("ST:" + st.getName());

			//TODO Dennis: Annotationen ausgeben
			/*
			SchemaAnnotationParser sp = new SchemaAnnotationParser(null, st.getAnnotation());
			if (sp.hasFabricAnnotationDocument())
				out.println("\t\t Annotation: " + sp.getTypeAnnotation());
			 */
			Restriction r = st.getRestriction();
			if (r != null) {
				if (sh.isXMLSchemaName(r.getBase())) {
					out.println("\t\t Base restriction: " + SchemaHelper.getSchemaType(r.getBase()));
				}
			}
		}
	}

	/**
	 * 
	 * @param out
	 * @param ct
	 * @throws Exception
	 */
	private void dumpElements(PrintWriter out, TopLevelComplexType ct) throws Exception {
		// Elements
		Element[] elems = sh.getSequenceElements(ct);
		for (int t = 0; t < elems.length; ++t) {
			Element e = elems[t];
			out.println("\t Element: " + e.getName());
			QName q = e.getType();

			if (q != null && sh.isLocalName(q)) {
				ComplexType ctype = sh.getComplexTypeByName(q);
				SimpleType stype = sh.getSimpleTypeByName(q);

				if (ctype != null)
					out.println("\t\t LocalComplexType(" + q + "):" + ctype.getName());
				else if (stype != null)
					out.println("\t\t LocalSimpleType(" + q + "):" + stype.getName());
				else
					throw new Exception("Neither complex, nor simple type for qname[" + q + "] found");

			} else if (q != null && sh.isXMLSchemaName(q))
				out.println("\t\t SchemaType(" + q + "): " + SchemaHelper.getSchemaType(q));
			else
				out.println("\t\t ERROR: NO TYPE SPECIFIED");

			//TODO Dennis: Einfach die Annotationsdokumente ausgeben?
			/*
			out.println("\t\t min: " + e.getMinOccurs() + ", max: " + e.getMaxOccurs());
			SchemaAnnotationParser esp = new SchemaAnnotationParser(null, e.getAnnotation());
			if (esp.hasFabricAnnotationDocument())
				dumpBridleAnnotation(out, esp.getTypeAnnotation().getFabricAnnotation());
			*/
		}
	}

}

/*+---------------------------------------------------------------+
 *| Source  $Source: /cvs/shawn/shawn/sys/worlds/save_world_task.cpp,v $  
 *| Version $Revision: 6 $ modified by $Author: pfisterer $
 *| Date    $Date: 2006-09-20 11:12:01 +0200 (Mi, 20 Sep 2006) $
 *+---------------------------------------------------------------
 *| $Log: save_world_task.cpp,v $
 *+---------------------------------------------------------------*/