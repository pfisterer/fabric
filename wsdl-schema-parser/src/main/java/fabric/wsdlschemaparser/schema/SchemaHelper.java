/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck
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

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.slf4j.LoggerFactory;

public class SchemaHelper {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(SchemaHelper.class);
	
	protected Collection<Schema> schema = null;

	// -------------------------------------------------------
	/**
	 * 
	 * @param fabric.schema
	 */
	public SchemaHelper(Collection<Schema> schema) {
		this.schema = schema;
	}

	// -------------------------------------------------------
	/**
	 * 
	 * @param ct
	 * @return
	 */
	public ComplexType getExtensionBaseType(ComplexType ct) {
		ComplexContent cc = ct.getComplexContent();

		// Extensions must be inside a complex content tag
		if (cc == null)
			return null;

		// Check the derivation type EXTENSION
		if (cc.schemaType().getDerivationType() != SchemaType.DT_EXTENSION)
			return null;

		// Get the name of the complex extension type
		QName parentQName = cc.getExtension().getBase();

		// Extension base types are alway top level types
		return getComplexTypeByName(parentQName);
	}

	// -------------------------------------------------------
	/**
	 * XXX: I adapted this method to cope with multiple schemata.
	 * Not sure that a understood the meaning of this method correctly,
	 * so bear any changes in functionality -- Dariush
	 */
	public boolean isLocalName(QName name) {

		if (name == null)
			log.error("Supplied name[" + name + "] null");
		else if (schema == null)
			log.error("Local fabric.schema[" + schema + "] is null");
		else if (schema.size()==0)
			log.error("Local fabric.schema contains no schemas");
		else if (name.getNamespaceURI() == null)
			log.error("Supplied name.getNamespaceURI[" + name.getNamespaceURI() + "] is null");
		else
		{
			for (Schema s : this.schema)
			{
				if (s.getTargetNamespace() == null)
					log.error("Local fabric.schema.getTargetNamespace[" + s.getTargetNamespace() + "] in schema "+s+" is null");
				else if (s.getTargetNamespace().equals(name.getNamespaceURI()))
					return true;	
			}
			
		}
		log.error("isLocalName(QName name[" + name + "]): Invalid parameters supplied");
		return false;
	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	public boolean isLocalComplexType(QName name) {
		return getComplexTypeByName(name) != null;
	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	public boolean isLocalSimpleType(QName name) {
		return getSimpleTypeByName(name) != null;
	}

	// -------------------------------------------------------
	/**
	 * 
	 * @param name
	 * @return
	 * @todo resolve other namespaces too
	 */
	public XmlObject getByName(QName name) {
		String ln = name.getLocalPart();
		
		for (Schema s : this.schema)
		{
			TopLevelComplexType[] cta = s.getComplexTypeArray();
			for (int i = 0; i < cta.length; ++i)
				if (cta[i].getName().equals(ln))
					return cta[i];
		}
		for (Schema s : this.schema)
		{
			TopLevelSimpleType[] sta = s.getSimpleTypeArray();
			for (int i = 0; i < sta.length; ++i)
				if (sta[i].getName().equals(ln))
					return sta[i];
		}
		for (Schema s : this.schema)
		{
			TopLevelElement[] tlea = s.getElementArray();
			for (int i = 0; i < tlea.length; ++i)
				if (tlea[i].getName().equals(ln))
					return tlea[i];
		}

//		log.debug("local name not found [" + ln + "], namespace [" + name.getNamespaceURI() + "], local namespace["
//				+ schema.getTargetNamespace() + "]");

		log.debug("local name not found [" + ln + "], namespace [" + name.getNamespaceURI() + "]");
		return null;
	}

	// -------------------------------------------------------
	public ComplexType getComplexTypeByName(QName name) {
		XmlObject o = getByName(name);
		if (o instanceof ComplexType)
			return (ComplexType) o;
		return null;
	}

	// -------------------------------------------------------
	public SimpleType getSimpleTypeByName(QName name) {
		XmlObject o = getByName(name);
		if (o instanceof SimpleType)
			return (SimpleType) o;
		return null;
	}

	// -------------------------------------------------------
	public static SchemaType getSchemaType(QName name) {
		return XmlBeans.getContextTypeLoader().findType(name);
	}

	// -------------------------------------------------------
	public boolean isXMLSchemaName(QName name) {
		if (name == null)
			return false;

		String xmlSchemaURI = DefaultValues.XML_SCHEMA_NAMESPACE_URI;
		return xmlSchemaURI.equals(name.getNamespaceURI());
	}

	// -------------------------------------------------------
	/**
	 * 
	 * @param ct
	 * @return
	 */
	public LinkedList<ComplexType> getExtensionHierarchy(ComplexType ct) {
		LinkedList<ComplexType> ll = new LinkedList<ComplexType>();
		getExtensionHierarchy(ct, ll);
		return ll;
	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	private void getExtensionHierarchy(ComplexType ct, LinkedList<ComplexType> ll) {
		ComplexType bt = getExtensionBaseType(ct);

		ll.add(ct);
		if (bt != null)
			getExtensionHierarchy(bt, ll);
	}

	// -------------------------------------------------------
	/**
	 * Elements (either directly in a sequence, or in extension sequence)
	 * 
	 */
	protected ExplicitGroup getSequence(ComplexType ct) {
		ExplicitGroup s = null;

		if (ct.isSetSequence())
			s = ct.getSequence();
		else if (ct.isSetComplexContent())
			if (ct.getComplexContent().getExtension() != null)
				if (ct.getComplexContent().getExtension().isSetSequence())
					s = ct.getComplexContent().getExtension().getSequence();

		return s;
	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	public Element[] getSequenceElements(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<Element> els = new Vector<Element>();

		for (ComplexType t : ll) {
			ExplicitGroup g = getSequence(t);
			if (g == null)
				continue;

			for (Element e : g.getElementArray())
				els.add(e);
		}

		return els.toArray(new Element[els.size()]);
		//
		//		for (int i = 0; i < els.size(); ++i)
		//			ea[i] = els.elementAt(i);
		//		return ea;
		//
	}
	
	public Any[] getSequenceAny(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<Any> els = new Vector<Any>();

		for (ComplexType t : ll) {
			ExplicitGroup g = getSequence(t);
			if (g == null)
				continue;

			for (Any a : g.getAnyArray())
				els.add(a);
		}

		return els.toArray(new Any[els.size()]);
	}

	public ExplicitGroup[] getChildSequences(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<ExplicitGroup> egs = new Vector<ExplicitGroup>();

		for (ComplexType t : ll) {
			ExplicitGroup g = null;
			if (t.isSetSequence()) {
				g = getSequence(t);
			} else if (t.isSetChoice()) {
				g = getChoice(t);
			}

			if (g == null)
				continue;

			for (ExplicitGroup eg : g.getSequenceArray()) {
				egs.add(eg);
			}
		}

		return egs.toArray(new ExplicitGroup[egs.size()]);
	}

	// -------------------------------------------------------
	/**
	 * @param ct
	 * @return
	 */
	protected ExplicitGroup getChoice(ComplexType ct) {
		ExplicitGroup s = null;

		if (ct.isSetChoice())
			s = ct.getChoice();
		else if (ct.isSetComplexContent())
			if (ct.getComplexContent().getExtension() != null)
				if (ct.getComplexContent().getExtension().isSetChoice())
					s = ct.getComplexContent().getExtension().getChoice();

		return s;
	}

	// -------------------------------------------------------
	/**
	 * @param ct
	 * @return
	 */
	public Element[] getChoiceElements(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<Element> els = new Vector<Element>();

		for (Iterator<ComplexType> it = ll.iterator(); it.hasNext();) {
			ComplexType t = it.next();
			ExplicitGroup g = getChoice(t);
			if (g == null)
				continue;

			Element[] elems = g.getElementArray();
			for (int i = 0; i < elems.length; ++i)
				els.add(elems[i]);
		}

		Element[] ea = new Element[els.size()];
		for (int i = 0; i < els.size(); ++i)
			ea[i] = els.elementAt(i);
		return ea;

	}
	
	// -------------------------------------------------------
	/**
	 * @param ct
	 * @return
	 */
	public Any[] getChoiceAny(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<Any> els = new Vector<Any>();

		for (Iterator<ComplexType> it = ll.iterator(); it.hasNext();) {
			ComplexType t = it.next();
			ExplicitGroup g = getChoice(t);
			if (g == null)
				continue;

			Any[] elems = g.getAnyArray();
			for (int i = 0; i < elems.length; ++i)
				els.add(elems[i]);
		}

		Any[] ea = new Any[els.size()];
		for (int i = 0; i < els.size(); ++i)
			ea[i] = els.elementAt(i);
		return ea;

	}

	//-------------------------------------------------------
	/**
	 * 
	 * @param ct
	 * @return
	 */
	public ExplicitGroup[] getChildChoices(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<ExplicitGroup> egs = new Vector<ExplicitGroup>();

		for (ComplexType t : ll) {
			ExplicitGroup g = null;
			if (t.isSetSequence()) {
				g = getSequence(t);
			} else if (t.isSetChoice()) {
				g = getChoice(t);
			}

			if (g == null)
				continue;

			for (ExplicitGroup eg : g.getChoiceArray()) {
				egs.add(eg);
			}
		}

		return egs.toArray(new ExplicitGroup[egs.size()]);
	}

	// -------------------------------------------------------
	/**
	 * @param ct
	 * @return
	 */
	protected ExplicitGroup getAll(ComplexType ct) {
		ExplicitGroup s = null;

		if (ct.isSetAll())
			s = ct.getAll();
		else if (ct.isSetComplexContent())
			if (ct.getComplexContent().getExtension() != null)
				if (ct.getComplexContent().getExtension().isSetAll())
					s = ct.getComplexContent().getExtension().getAll();

		return s;
	}

	// -------------------------------------------------------
	/**
	 * @param ct
	 * @return
	 */
	public Element[] getAllElements(ComplexType ct) {
		LinkedList<ComplexType> ll = getExtensionHierarchy(ct);
		Vector<Element> els = new Vector<Element>();

		for (Iterator<ComplexType> it = ll.iterator(); it.hasNext();) {
			ComplexType t = it.next();
			ExplicitGroup g = getAll(t);
			if (g == null)
				continue;

			Element[] elems = g.getElementArray();
			for (int i = 0; i < elems.length; ++i)
				els.add(elems[i]);
		}

		Element[] ea = new Element[els.size()];
		for (int i = 0; i < els.size(); ++i)
			ea[i] = els.elementAt(i);
		return ea;

	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	public int getMinOccurs(Element e) {
		int min = 1;

		if (e.isSetMinOccurs())
			min = e.getMinOccurs().intValue();

		log.debug("Min occurs of element [" + e.getName() + "] is " + min);
		return min;
	}

	// -------------------------------------------------------
	/**
	 * 
	 */
	public int getMaxOccurs(Element e) {
		int max = -1;
		String debug = "Max occurs of element [" + e.getName() + "] is ";

		if (e.isSetMaxOccurs()) {
			Object o = e.getMaxOccurs();

			if (o instanceof BigInteger) {
				BigInteger i = (BigInteger) o;
				max = i.intValue();
				log.debug(debug + max);
			} else if (o instanceof String && "unbounded".equals(o)) {
				max = Integer.MAX_VALUE;
				log.debug(debug + "unbounded [" + max + "]");
			}

		}

		if (max == -1) {
			max = 1;
			log.debug(debug + max + " (default)");
		}

		return 1;
	}

}

/*+---------------------------------------------------------------+
 *| Source  $Source: /cvs/shawn/shawn/sys/worlds/save_world_task.cpp,v $                                           
 *| Version $Revision: 4 $ modified by $Author: pfisterer $
 *| Date    $Date: 2006-09-13 16:48:40 +0200 (Mi, 13 Sep 2006) $
 *+---------------------------------------------------------------
 *| $Log: save_world_task.cpp,v $
 *+---------------------------------------------------------------*/
