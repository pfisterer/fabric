package com.example.car;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The XML converter class.
 */
public class CarConverter {

	/**
	 * Serialize bean object to XML document.
	 */
	public static String instanceToXML(final Car beanObject) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Car.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		StringWriter xmlDocument = new StringWriter();
		marshaller.marshal(beanObject, xmlDocument);
		
		return removeValueTags(xmlDocument.toString());
	}

	/**
	 * Deserialize XML document to bean object.
	 */
	public static Car xmlToInstance(final String xmlDocument) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Car.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		return (Car)unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(addValueTags(xmlDocument).getBytes())));
	}

	/**
	 * Remove unnecessary value-tags from the XML document.
	 */
	private static String removeValueTags(final String xmlDocument) {
		try {
			// Create document
			Document doc = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null).newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));
			// Fix tags in elements
			// Fix tags in element arrays
			// Fix tags in lists
			// Create instances for writing output
			Source source               = new DOMSource(doc);
			StringWriter stringWriter   = new StringWriter();
			Result result               = new StreamResult(stringWriter);
			TransformerFactory factory  = TransformerFactory.newInstance();
			Transformer transformer     = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			// Resulting XML as a string
			String ret = stringWriter.getBuffer().toString();
			stringWriter.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Remove unnecessary value-tag from the XML element.
	 */
	private static void removeTagFromElement(final String element, final Document doc) {
		NodeList rootNodes = doc.getElementsByTagName(element);
		// Length of rootNodes is greater than 1 if we have an element array
		for (int i = 0; i < rootNodes.getLength(); i++) {
			Element root = (Element) rootNodes.item(i);
			// Get all child nodes of root with a value-tag
			NodeList children = root.getElementsByTagName("value");
			String newContent   = "";
			while (children.getLength() > 0) {
				Element value = (Element) children.item(0);
				newContent += value.getTextContent() + " ";
				root.removeChild(value);
			}
			root.setTextContent(newContent.trim());
		}
	}

	/**
	 * Remove unnecessary value-tag from the XML element.
	 */
	private static void removeTagFromList(final String list, final Document doc, final boolean isCustomTyped) {
		NodeList rootNodes = doc.getElementsByTagName(list);
		for (int i = 0; i < rootNodes.getLength(); i++) {
			Element root = (Element) rootNodes.item(i);
			// Get all child nodes of root with a value-tag
			NodeList children = root.getElementsByTagName("values");
			if (children.getLength() == 1) {
				String newContent = children.item(0).getTextContent();
				// Remove value-tag from root element
				root.removeChild(children.item(0));
				root.setTextContent(newContent);
			}
		}
	}

	/**
	 * Add value-tags to the XML document.
	 */
	private static String addValueTags(final String xmlDocument) {
		try {
			// Create document
			Document doc = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null).newDocumentBuilder().parse(new ByteArrayInputStream(xmlDocument.getBytes()));
			// Fix tags in elements
			// Fix tags in element arrays
			// Fix tags in lists
			// Create instances for writing output
			Source source               = new DOMSource(doc);
			StringWriter stringWriter   = new StringWriter();
			Result result               = new StreamResult(stringWriter);
			TransformerFactory factory  = TransformerFactory.newInstance();
			Transformer transformer     = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			// Resulting XML as a string
			String ret = stringWriter.getBuffer().toString();
			stringWriter.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add value-tag to the XML element.
	 */
	private static void addTagToElement(final String element, final Document doc) {
		NodeList rootNodes = doc.getElementsByTagName(element);
		// Length of rootNodes is greater than 1 if we have an element array
		for (int i = 0; i < rootNodes.getLength(); i++) {
			Element root    = (Element) rootNodes.item(i);
			Element child   = doc.createElement("value");
			child.appendChild(root.getFirstChild().cloneNode(true));
			root.replaceChild(child, root.getFirstChild());
		}
	}

	/**
	 * Add values-tag and/or value-tags to the XML list.
	 */
	private static void addTagToList(final String list, final Document doc, final boolean isCustomTyped) {
		if (isCustomTyped) {
			NodeList rootNodes = doc.getElementsByTagName(list);
			for (int i = 0; i < rootNodes.getLength(); i++) {
				Element root    = (Element) rootNodes.item(i);
				Element child   = doc.createElement("values");
				child.setTextContent(root.getTextContent());
				root.removeChild(root.getFirstChild());
				root.appendChild(child);
			}
		}
	}

}
