package com.example.car;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;
import org.openexi.fujitsu.proc.EXIDecoder;
import org.openexi.fujitsu.proc.common.EXIEvent;
import org.openexi.fujitsu.proc.common.EventType;
import org.openexi.fujitsu.proc.grammars.GrammarCache;
import org.openexi.fujitsu.proc.io.Scanner;
import org.openexi.fujitsu.sax.Transmogrifier;
import org.openexi.fujitsu.schema.EXISchema;
import org.openexi.fujitsu.schema.EmptySchema;
import org.xml.sax.InputSource;

/**
 * The EXI de-/serializer class.
 */
public class EXIConverter {

	/**
	 * The OpenEXI encoder member variable.
	 */
	private static Transmogrifier encoder;

	/**
	 * The OpenEXI schema member variable.
	 */
	private static EXISchema exiSchema = new EXISchema();

	/**
	 * The OpenEXI grammar cache member variable.
	 */
	private static GrammarCache grammarCache = null;

	/**
	 * The OpenEXI decoder member variable.
	 */
	private static EXIDecoder exiDecoder = new EXIDecoder();

	/**
	 * The OpenEXI scanner member variable.
	 */
	private static Scanner exiScanner;

	static {
		EXIConverter.setupEXISchemaFactory();
	}

	/**
	 * Setup OpenEXI schema factory member variable.
	 */
	private static void setupEXISchemaFactory() {
		try {
			EXIConverter.exiSchema = EmptySchema.getEXISchema();
		
			// Set grammar for current XML schema
			EXIConverter.grammarCache = new GrammarCache(EXIConverter.exiSchema);
		
			// Initialize and set grammar cache for the OpenEXI transmogrifier (encoder)
			EXIConverter.encoder = new Transmogrifier();
			EXIConverter.encoder.setEXISchema(EXIConverter.grammarCache);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compress XML document with EXI.
	 */
	public static byte[] serialize(final String xmlDocument) throws Exception {
		// Prepare objects for serialization
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// Parse XML document and serialize
		try {
			EXIConverter.encoder.setOutputStream(baos);
		
			// Parse XML document and serialize
			EXIConverter.encoder.encode(new InputSource(new StringReader(xmlDocument)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Write output to EXI byte stream
		byte[] result = baos.toByteArray();
		baos.close();
		
		return result;
	}

	/**
	 * Uncompress EXI byte stream to XML document.
	 */
	public static String deserialize(final byte[] openEXIStream) throws Exception {
		// The resulting XML string
		String result = "";
		
		// Prepare objects for deserialization
		try {
			// Set grammar cache for the OpenEXI decoder
			EXIConverter.exiDecoder.setEXISchema(EXIConverter.grammarCache);
		
			// Set the input stream for the OpenEXI Decoder
			EXIConverter.exiDecoder.setInputStream(new ByteArrayInputStream(openEXIStream));
		
			// Create the scanner object for deserialization
			EXIConverter.exiScanner = EXIConverter.exiDecoder.processHeader();
		
			// create the EXIEvent list for later processing
			ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
			EXIEvent exiEvent;
			while ((exiEvent = EXIConverter.exiScanner.nextEvent()) != null) {
				exiEventList.add(exiEvent);
			}
			EventType eventType;
			Stack<String> s = new Stack<String>();
		
			// Process the event list
			for(int i=0; i<exiEventList.size(); i++){
				exiEvent = exiEventList.get(i);
				eventType = exiEvent.getEventType();
				// Start Element
				if(exiEvent.getEventVariety() == EXIEvent.EVENT_SE){
					result += "<" + exiEvent.getName();
					if(eventType.getURI() != null && eventType.getURI() != ""){
						result += " xmlns=" + eventType.getURI();
					}
					result += ">";
					s.push(exiEvent.getName());
				}
		
				// Characters between tags
				if(exiEvent.getEventVariety() == EXIEvent.EVENT_CH){
					if(exiEvent.getCharacters() != null){
						result += exiEvent.getCharacters().makeString();
					}
				}
		
				// End Element
				if(exiEvent.getEventVariety() == EXIEvent.EVENT_EE){
					result += "</" + s.pop() + ">";
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
