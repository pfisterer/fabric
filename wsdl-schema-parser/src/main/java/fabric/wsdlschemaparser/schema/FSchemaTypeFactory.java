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
package fabric.wsdlschemaparser.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleRestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Marco Wegner
 */
public class FSchemaTypeFactory {
	private final org.slf4j.Logger log = LoggerFactory.getLogger(FSchemaTypeFactory.class);

	private FSchema fschema = null;

	private String namespace = null;

	private SchemaHelper schemaHelper;

	private List<FSchemaType> topLevelTypes;

	/**
	 * The backtrace of complex types. It is used for detecting cyclic dependencies in the Schema files. Stored in this
	 * object are the complex types' names.
	 */
	private Stack<String> typeTrace;

	private Stack<String> elemTrace;

	public FSchemaTypeFactory(FSchema fschema, Schema schema) {
		this.fschema = fschema;
		Set<Schema> schemas = new HashSet<Schema>();
		schemas.add(schema);
		schemaHelper = new SchemaHelper(schemas);
		typeTrace = new Stack<String>();
		elemTrace = new Stack<String>();
	}

	public FSchemaTypeFactory(FSchema fschema, Collection<Schema> schema) {
		this.fschema = fschema;
		schemaHelper = new SchemaHelper(schema);
		typeTrace = new Stack<String>();
		elemTrace = new Stack<String>();
	}

	/**
	 * @param elem
	 * @return
	 */
	public FElement generate(TopLevelElement elem) {
		log.debug("Generating TopLevelElement");

		String name = elem.getName();

		// cycle detection
		if (isMaxDepthReached(elemTrace, name)) {
			log.error("Cycle detected (elements), maxdepth reached");
			log.debug("{}", elemTrace);
			return null;
		}

		elemTrace.push(name);

		FElement element = generateElement(elem);
		FSchemaType schemaType = element.getSchemaType();
		if (!schemaType.isTopLevel()) {
			schemaType.setName(name);
		}
		elemTrace.pop();

		element.setFSchema(this.fschema);

		element.setTopLevel(true);
		return element;
	}

	/**
	 * @param stype
	 * @return
	 */
	public FSimpleType generate(TopLevelSimpleType stype) {

		String name = stype.getName();
		log.debug("Generating TopLevelSimpleType: " + name);

		FSchemaType ftype = getTopLevelType(name);
		if (ftype != null) {
			// type's already in the list (forward reference)
			if (ftype instanceof FSimpleType) {
				return (FSimpleType) ftype;
			}
		}

		FSimpleType fst = null;
		if (stype.isSetRestriction()) {
			fst = generateSimpleRestrictionType(stype.getRestriction());
		}

		if (fst != null) {
			fst.setName(name);
			fst.setTopLevel(true);
			addTopLevelType(fst);

			fst.setFSchema(this.fschema);
		}

		return fst;
	}

	/**
	 * @param ctype
	 * @return
	 */
	public FComplexType generate(TopLevelComplexType ctype) {

		String name = ctype.getName();
		log.debug("Generating TopLevelComplexType: " + name);

		FSchemaType ftype = getTopLevelType(name);
		if (ftype != null) {
			// type's already in the list (due to forward reference)
			if (ftype instanceof FComplexType) {
				return (FComplexType) ftype;
			}
		}

		// cycle detection
		if (isMaxDepthReached(typeTrace, name)) {
			log.error("Cycle detected (types), maxdepth reached");
			log.debug("{}", typeTrace);
			return null;
		}

		typeTrace.push(name);

		FComplexType fct = generateComplexType(ctype);

		typeTrace.pop();

		if (fct != null) {
			fct.setName(name);
			fct.setTopLevel(true);
			addTopLevelType(fct);

			fct.setFSchema(this.fschema);
		}

		return fct;
	}

	public Collection<FElement> generateAll(TopLevelElement[] elements) {
		Collection<FElement> list = new ArrayList<FElement>();
		for (TopLevelElement elem : elements) {
			elem.setName(ReservedNames.instance().getNewName(elem.getName()));
			FElement tle = generate(elem);
			if (tle != null) {
				log.info(tle.getNamespace() + ":" + tle.getName() + " created.");
				list.add(tle);
			} else {
				log.warn("Top-level element was null after generating it: " + elem.getName());
			}
		}
		return list;
	}

	public Collection<FSimpleType> generateAll(TopLevelSimpleType[] types) {
		Collection<FSimpleType> list = new ArrayList<FSimpleType>();
		for (TopLevelSimpleType stype : types) {
			FSimpleType tlst = generate(stype);
			if (tlst != null) {
				list.add(tlst);
			} else {
				log.error("Top-level simple type was null after generating it: " + stype.getName());
			}
		}
		return list;
	}

	// --------------------------------------------------------------------

	public Collection<FComplexType> generateAll(TopLevelComplexType[] types) {
		Collection<FComplexType> list = new ArrayList<FComplexType>();
		for (TopLevelComplexType ctype : types) {
			FComplexType tlct = generate(ctype);
			if (tlct != null) {
				list.add(tlct);
			} else {
				log.error("Top-level complex type was null after generating it: " + ctype.getName());
			}
		}
		return list;
	}

	// --------------------------------------------------------------------

	/**
	 * @param elem
	 * @return
	 */
	private FElement generateElement(Element elem) {
		elem.setName(ReservedNames.instance().getNewName(elem.getName()));
		String elemName = elem.getName();
		log.debug("Element: " + elemName);
		FSchemaType ftype = null;
		QName type = elem.getType();
		if (type == null) {
			ftype = getTopLevelType(elemName);
			if (ftype == null) {
				if (elem.isSetSimpleType()) {
					ftype = generateLocalSimpleType(elem.getSimpleType());
				} else if (elem.isSetComplexType()) {
					ftype = generateComplexType(elem.getComplexType());
				} else if (elem.isSetRef()) {
					QName ref = elem.getRef();
					String elemRefName = ref.getLocalPart();
					log.debug("Resolved reference (" + elemRefName + ")");
					ftype = getReferencedType(ref);
				} else {
					throw new UnknownElementException(elem.toString());
				}
			}
		} else {
			// try to get a top-level type...
			String typeName = type.getLocalPart();
			ftype = getTopLevelType(typeName);
			// if that has not been successful, then generate a new one
			if (ftype == null) {
				if (schemaHelper.isXMLSchemaName(type)) {
					SchemaType st = SchemaHelper.getSchemaType(type);
					if (st.getSimpleVariety() == SchemaType.ATOMIC) {
						ftype = generateSimpleTypeFromBTC(st.getBuiltinTypeCode());
					} else {
						throw new UnhandledSimpleVarietyException("SimpleVariety not handled: " + st.getSimpleVariety());
					}
				} else {
					XmlObject xo = schemaHelper.getByName(type);
					if (xo instanceof SimpleType) {
						ftype = generate((TopLevelSimpleType) xo);
					} else if (xo instanceof ComplexType) {
						ftype = generate((TopLevelComplexType) xo);
					} else {
						throw new RuntimeException();
					}
				}
			}
		}

		if (ftype == null) {
			return null;
		}

		int ecount = getElemNameCount(elemName);
		if (ecount > 1) {
			elemName += "_" + ecount;
		}
		FElement schemaElement = new FElement(elemName, ftype);
		
		// check the element for minOccurs, ...
		if (elem.isSetMinOccurs()) {
			Object o = elem.getMinOccurs();
			if (o instanceof Number)
				schemaElement.setMinOccurs(((Number) o).intValue());
		}
		
		// ... maxOccurs, ...
		if (elem.isSetMaxOccurs()) {
			Object o = elem.getMaxOccurs();
			if (o instanceof Number)
				schemaElement.setMaxOccurs(((Number) o).intValue());
			else if (o instanceof String && ((String) o).equals("unbounded"))
				schemaElement.setMaxOccursUnbounded();
		}

                // ... fixed value ...
                if (elem.isSetFixed()) {
                    schemaElement.setFixedValue(elem.getFixed());
                }

                // ... and default value
                else if (elem.isSetDefault()) {
                    schemaElement.setDefaultValue(elem.getDefault());
                }

		if (elem.isSetRef()) {
			schemaElement.setReference(true);
			schemaElement.setName(elem.getRef().getLocalPart());
		}

		initObject(schemaElement);
		return schemaElement;
	}

	/**
	 * @param any
	 * @return
	 */
	private FElement generateElement(Any any) {
		String elemName = "any";

		FSchemaType ftype = new FAny("String", getPriviousNodeNames(any), getNextNodeNames(any));

		int ecount = getElemNameCount(elemName);
		if (ecount > 1) {
			elemName += "_" + ecount;
		}
		FElement schemaElement = new FElement(elemName, ftype);
		// check the element for minOccurs...
		if (any.isSetMinOccurs()) {
			Object o = any.getMinOccurs();
			if (o instanceof Number)
				schemaElement.setMinOccurs(((Number) o).intValue());
		}
		// ... and maxOccurs
		if (any.isSetMaxOccurs()) {
			Object o = any.getMaxOccurs();
			if (o instanceof Number)
				schemaElement.setMaxOccurs(((Number) o).intValue());
			else if (o instanceof String && ((String) o).equals("unbounded"))
				schemaElement.setMaxOccursUnbounded();
		}

		initObject(schemaElement);
		return schemaElement;
	}

	/**
	 * Returns the value of the name attribute of the previous neighboring node of the given any-node
	 * 
	 * @param any
	 * @return Name of previous the neighboring node
	 */
	private HashSet<String> getPriviousNodeNames(Any any) {
		HashSet<String> nodeNames = new HashSet<String>();
		Node n = any.getDomNode().getPreviousSibling();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getLocalName().equals("any")) {
					break;
				}
				if (n.hasAttributes()) {
					NamedNodeMap nodeAttr = n.getAttributes();
					if (nodeAttr.getNamedItem("name") != null) {
						nodeNames.add(nodeAttr.getNamedItem("name").getNodeValue());
					}
				}
			}
			n = n.getPreviousSibling();
		}
		return nodeNames;
	}

	/**
	 * Returns the value of the name attribute of the next neighboring node of the given any-node
	 * 
	 * @param any
	 * @return Name of next the neighboring node
	 */
	private HashSet<String> getNextNodeNames(Any any) {
		HashSet<String> nodeNames = new HashSet<String>();
		Node n = any.getDomNode().getNextSibling();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getLocalName().equals("any")) {
					break;
				}
				if (n.hasAttributes()) {
					NamedNodeMap nodeAttr = n.getAttributes();
					if (nodeAttr.getNamedItem("name") != null) {
						nodeNames.add(nodeAttr.getNamedItem("name").getNodeValue());
					}
				}
			}
			n = n.getNextSibling();
		}
		return nodeNames;
	}

	private FSchemaType getReferencedType(QName reference) {
		String elemRefName = reference.getLocalPart();
		FSchemaType ftype = getTopLevelType(elemRefName);
		if (ftype == null) {
			TopLevelElement tle = (TopLevelElement) schemaHelper.getByName(reference);
			FElement fse = generate(tle);
			if (fse == null) {
				return null;
			}
			ftype = fse.getSchemaType();
			ftype.setName(elemRefName);
			addTopLevelType(ftype);
		}
		return ftype;
	}

	/**
	 * @param stype
	 * @return
	 */
	private FSimpleType generateLocalSimpleType(LocalSimpleType stype) {
		FSimpleType fst = null;
		log.debug("Generating LocalSimpleType");
		if (stype.isSetRestriction()) {
			fst = generateSimpleRestrictionType(stype.getRestriction());
		}
		return fst;
	}

	/**
	 * @param restriction
	 * @return
	 */
	private FSimpleType generateSimpleRestrictionType(Restriction restriction) {
		QName base = restriction.getBase();
		System.out.println(base);
		SchemaType st_base = SchemaHelper.getSchemaType(base);
		FSimpleType fst = generateSimpleTypeFromBTC(st_base.getBuiltinTypeCode());
		fst.getRestrictions().parse(restriction);
		return fst;
	}

	/**
	 * @param restriction
	 * @return
	 * @throws UnsupportedRestrictionException
	 */
	private FSimpleType generateSimpleRestrictionType(RestrictionType restriction)
			throws UnsupportedRestrictionException {
		QName base = restriction.getBase();
		System.out.println(base);
		SchemaType st_base = SchemaHelper.getSchemaType(base);
		FSimpleType fst = generateSimpleTypeFromBTC(st_base.getBuiltinTypeCode());
		fst.getRestrictions().parse(restriction);
		return fst;
	}

	/**
	 * @param ctype
	 * @return
	 */
	private FComplexType generateComplexType(ComplexType ctype) {

		String name = ctype.getName();
		if (name == null) {
			log.debug("Generating LocalComplexType");
		} else {
			log.debug("Generating ComplexType: " + name);
		}

		FComplexType fct = null;
		if (ctype.isSetSequence()) {
			fct = generateSequence(ctype);
		} else if (ctype.isSetAll()) {
			fct = generateAll(ctype);
		} else if (ctype.isSetChoice()) {
			fct = generateChoice(ctype);
		} else if (ctype.isSetSimpleContent()) {
			fct = generateSimpleContentCT(ctype);
		} else if (ctype.isSetComplexContent()) {
			fct = generateComplexContentCT(ctype);
		}

		if (fct == null && ctype.sizeOfAttributeArray() > 0) {
			fct = new FSequence();
			handleComplexTypeAttributes(fct, ctype.getAttributeArray());
		}
		if (ctype.isSetAnyAttribute()) {
			fct.setAnyAttribute(true);
		}

		if (fct == null) {
			log.error("Could not generate complex type...");
			throw new RuntimeException();
		}

		initObject(fct);
		return fct;
	}

	/**
	 * @param ctype
	 * @return
	 */
	private FSequence generateSequence(ComplexType ctype) {
		FSequence fs = new FSequence(ctype.getName());
		handleComplexTypeChildElements(fs, schemaHelper.getSequenceElements(ctype));
		handleComplexTypeChildElements(fs, schemaHelper.getSequenceAny(ctype));
		if (ctype.sizeOfAttributeArray() > 0) {
			handleComplexTypeAttributes(fs, ctype.getAttributeArray());
		}
		handleComplexChildTypes(fs, ctype);

		initObject(fs);
		return fs;
	}

	private FAll generateAll(ComplexType ctype) {
		FAll fa = new FAll(ctype.getName());
		handleComplexTypeChildElements(fa, schemaHelper.getAllElements(ctype));
		if (ctype.sizeOfAttributeArray() > 0) {
			handleComplexTypeAttributes(fa, ctype.getAttributeArray());
		}
		initObject(fa);
		return fa;
	}

	private FChoice generateChoice(ComplexType ctype) {
		FChoice fc = new FChoice(ctype.getName());
		handleComplexTypeChildElements(fc, schemaHelper.getChoiceElements(ctype));
		handleComplexTypeChildElements(fc, schemaHelper.getChoiceAny(ctype));
		if (ctype.sizeOfAttributeArray() > 0) {
			handleComplexTypeAttributes(fc, ctype.getAttributeArray());
		}
		handleComplexChildTypes(fc, ctype);
		initObject(fc);
		return fc;
	}

	/**
	 * @param fct
	 * @param elements
	 */
	private void handleComplexTypeChildElements(FComplexType fct, Element[] elements) {
		if (elements != null) {
			for (Element elem : elements) {
				FElement ee = generateElement(elem);
				if (ee != null) {
					fct.addChildObject(ee);
				}
			}
		}
	}

	/**
	 * @param fct
	 * @param anys
	 */
	private void handleComplexTypeChildElements(FComplexType fct, Any[] anys) {
		if (anys != null) {
			for (Any a : anys) {
				elemTrace.push("any");
				FElement ee = generateElement(a);
				if (ee != null) {
					String prev = null;
					Node n = a.getDomNode().getPreviousSibling();
					while (n != null) {
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							prev = n.getLocalName();
							break;
						}
						n = n.getPreviousSibling();
					}
					fct.addChildObjectAfter(ee, prev);
				}
			}
			while (!elemTrace.isEmpty()) {
				if (elemTrace.peek().equals("any")) {
					elemTrace.pop();
				} else {
					break;
				}
			}
		}
	}

	/**
	 * @param fct
	 * @param ctype
	 */
	private void handleComplexChildTypes(FComplexType fct, ComplexType ctype) {
		for (ExplicitGroup eg : schemaHelper.getChildSequences(ctype)) {
			fct.addChildObject(initObject(new FSequence(eg.getName())));
		}
		for (ExplicitGroup eg : schemaHelper.getChildChoices(ctype)) {
			fct.addChildObject(initObject(new FChoice(eg.getName())));
		}
	}

	/**
	 * @param fct
	 * @param attributes
	 */
	private void handleComplexTypeAttributes(FComplexType fct, Attribute[] attributes) {
		for (Attribute attr : attributes) {
			SchemaType st = SchemaHelper.getSchemaType(attr.getType());

			FSchemaType ft;
			if (st.getSimpleVariety() == SchemaType.ATOMIC) {
				ft = generateSimpleTypeFromBTC(st.getBuiltinTypeCode());
			} else {
				throw new UnhandledSimpleVarietyException("SimpleVariety not handled: " + st.getSimpleVariety());
			}

			FSchemaAttribute a = new FSchemaAttribute(attr.getName(), ft);
			initObject(a);
			a.setRequired(attr.isSetUse() && attr.getUse().toString().equals("required"));
			fct.addAttribute(a);
		}
	}

	/**
	 * @param ctype
	 * @return
	 */
	private FComplexType generateSimpleContentCT(ComplexType ctype) {
		SimpleContent simpleContent = ctype.getSimpleContent();
		FComplexType fct = null;
		if (simpleContent.isSetExtension()) {
			fct = generateSimpleContentExtension(simpleContent);
		} else if (simpleContent.isSetRestriction()) {
			fct = generateSimpleContentRestriction(simpleContent);
		}

		if (fct != null) {
			fct.setSimpleContent(true);
		}

		return fct;
	}

	/**
	 * @param simpleContent
	 * @return
	 */
	private FComplexType generateSimpleContentExtension(SimpleContent simpleContent) {

		FComplexType fct = new FSequence();
		SimpleExtensionType extension = simpleContent.getExtension();
		SchemaType st_base = SchemaHelper.getSchemaType(extension.getBase());
		FSimpleType fst = generateSimpleTypeFromBTC(st_base.getBuiltinTypeCode());
		fct.addChildObject(initObject(new FElement("value", fst)));

		if (extension.sizeOfAttributeArray() > 0) {
			handleComplexTypeAttributes(fct, extension.getAttributeArray());
		}

		initObject(fct);
		return fct;
	}

	/**
	 * @param simpleContent
	 * @return
	 */
	private FComplexType generateSimpleContentRestriction(SimpleContent simpleContent) {
		FComplexType fct = new FSequence();
		SimpleRestrictionType restriction = simpleContent.getRestriction();
		FSimpleType fst = generateSimpleRestrictionType(restriction);
		fct.addChildObject(initObject(new FElement("value", fst)));
		initObject(fct);
		return fct;
	}

	/**
	 * @param ctype
	 * @return
	 */
	private FComplexType generateComplexContentCT(ComplexType ctype) {
		// TODO: Marco: xs:complexContent: Not yet implemented!
		log.error("xs:complexContent: Not yet implemented!");
		return null;
	}

	/**
	 * @param ftype
	 */
	private void addTopLevelType(FSchemaType ftype) {
		// lazy instantiation
		if (topLevelTypes == null) {
			topLevelTypes = new LinkedList<FSchemaType>();
		}
		topLevelTypes.add(ftype);
	}

	// --------------------------------------------------------------------

	/**
	 * @param typeName
	 * @return
	 */
	private FSchemaType getTopLevelType(String typeName) {
		FSchemaType ftype = null;
		if (topLevelTypes != null) {
			for (FSchemaType f : topLevelTypes) {
				if (f.getName().equals(typeName)) {
					ftype = f;
					break;
				}
			}
		}
		return ftype;
	}

	private boolean isMaxDepthReached(Stack<String> trace, String name) {
		int count = getNameCount(trace, name);
		return (count >= DefaultValues.CYCLE_MAX_DEPTH);
	}

	private int getElemNameCount(String elemName) {
		return getNameCount(elemTrace, elemName);
	}

	/**
	 * @param trace
	 * @param name
	 * @return
	 */
	private int getNameCount(Stack<String> trace, String name) {
		int count = 0;
		for (String s : trace) {
			if (s.equals(name)) {
				count++;
			}
		}
		return count;
	}

	public FSimpleType generateSimpleTypeFromBTC(int builtinTypeCode) {
		FSimpleType fst = null;

		switch (builtinTypeCode) {

		case SchemaType.BTC_BASE_64_BINARY:
			fst = new FBase64Binary( );
			break;

		case SchemaType.BTC_BOOLEAN:
			fst = new FBoolean();
			break;

		case SchemaType.BTC_BYTE:
			fst = new FByte();
			break;

		case SchemaType.BTC_DATE:
			fst = new FDate( );
			break;

		case SchemaType.BTC_DATE_TIME:
			fst = new FDateTime();
			break;

		case SchemaType.BTC_DECIMAL:
			fst = new FDecimal();
			break;

		case SchemaType.BTC_DOUBLE:
			fst = new FDouble();
			break;

		case SchemaType.BTC_DURATION:
			fst = new FDuration( );
			break;

		case SchemaType.BTC_FLOAT:
			fst = new FFloat();
			break;

		case SchemaType.BTC_G_DAY:
			fst = new FDay( );
			break;

		case SchemaType.BTC_G_MONTH:
			fst = new FMonth( );
			break;

		case SchemaType.BTC_G_MONTH_DAY:
			 fst = new FMonthDay();
			 break;

		case SchemaType.BTC_G_YEAR:
			fst = new FYear( );
			break;

		case SchemaType.BTC_G_YEAR_MONTH:
		        fst = new FYearMonth( );
			break;

		case SchemaType.BTC_HEX_BINARY:
			fst = new FHexBinary( );
			break;

		case SchemaType.BTC_INT:
			fst = new FInt();
			break;

		case SchemaType.BTC_INTEGER:
			fst = new FInteger();
			break;

		case SchemaType.BTC_LONG:
			fst = new FLong();
			break;

		case SchemaType.BTC_NEGATIVE_INTEGER:
			fst = new FNegativeInteger();
			break;

		case SchemaType.BTC_NON_NEGATIVE_INTEGER:
			fst = new FNonNegativeInteger();
			break;

		case SchemaType.BTC_NON_POSITIVE_INTEGER:
			fst = new FNonPositiveInteger();
			break;

		case SchemaType.BTC_POSITIVE_INTEGER:
			fst = new FPositiveInteger();
			break;

		case SchemaType.BTC_SHORT:
			fst = new FShort();
			break;

		case SchemaType.BTC_STRING:
			fst = new FString();
			break;

		case SchemaType.BTC_NORMALIZED_STRING:
			fst = new FNormalizedString();
			break;

		case SchemaType.BTC_TOKEN:
			fst = new FToken();
			break;

		case SchemaType.BTC_NAME:
			fst = new FName();
			break;

		case SchemaType.BTC_NCNAME:
			fst = new FNCName();
			break;

		case SchemaType.BTC_NMTOKEN:
			fst = new FNMTOKEN();
			break;

		case SchemaType.BTC_ANY_URI:
			fst = new FAnyURI();
			break;

		case SchemaType.BTC_QNAME:
			fst = new FQName();
			break;

		case SchemaType.BTC_NOTATION:
      fst = new FNOTATION();
      break;

		case SchemaType.BTC_TIME:
			fst = new FTime( );
			break;

		case SchemaType.BTC_UNSIGNED_BYTE:
			fst = new FUnsignedByte();
			break;

		case SchemaType.BTC_UNSIGNED_INT:
			fst = new FUnsignedInt();
			break;

		case SchemaType.BTC_UNSIGNED_LONG:
			fst = new FUnsignedLong();
			break;

		case SchemaType.BTC_UNSIGNED_SHORT:
			fst = new FUnsignedShort();
			break;

		default:
			throw new UnhandledBuiltinTypeException(
					"This shouldn't happen. If it does, someone has forgotten to add a case statement here");
		}

		initObject(fst);

		return fst;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * This Method sets some important fields in each fschemaObject It must be applied to *every* created FSchemaObject.
	 * 
	 * @param object
	 * @return
	 */
	private FSchemaObject initObject(FSchemaObject object) {
		object.setNamespace(this.namespace);
		object.setFSchema(this.fschema);
		return object;
	}
}
