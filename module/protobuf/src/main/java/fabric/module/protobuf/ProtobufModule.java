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
package fabric.module.protobuf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniluebeck.sourcegen.Workspace;
import de.uniluebeck.sourcegen.protobuf.PSourceFile;
import de.uniluebeck.sourcegen.protobuf.types.PComplexTypeField;
import de.uniluebeck.sourcegen.protobuf.types.PMessage;
import de.uniluebeck.sourcegen.protobuf.types.PSimpleTypeField;
import de.uniluebeck.sourcegen.protobuf.types.PSimpleTypeField.SimpleType;
import fabric.module.api.Module;
import fabric.wsdlschemaparser.schema.FAnyURI;
import fabric.wsdlschemaparser.schema.FBoolean;
import fabric.wsdlschemaparser.schema.FByte;
import fabric.wsdlschemaparser.schema.FChoice;
import fabric.wsdlschemaparser.schema.FComplexType;
import fabric.wsdlschemaparser.schema.FDecimal;
import fabric.wsdlschemaparser.schema.FDouble;
import fabric.wsdlschemaparser.schema.FElement;
import fabric.wsdlschemaparser.schema.FFloat;
import fabric.wsdlschemaparser.schema.FInt;
import fabric.wsdlschemaparser.schema.FInteger;
import fabric.wsdlschemaparser.schema.FLong;
import fabric.wsdlschemaparser.schema.FNegativeInteger;
import fabric.wsdlschemaparser.schema.FNonNegativeInteger;
import fabric.wsdlschemaparser.schema.FNonPositiveInteger;
import fabric.wsdlschemaparser.schema.FNormalizedString;
import fabric.wsdlschemaparser.schema.FPositiveInteger;
import fabric.wsdlschemaparser.schema.FQName;
import fabric.wsdlschemaparser.schema.FSchema;
import fabric.wsdlschemaparser.schema.FSchemaObject;
import fabric.wsdlschemaparser.schema.FSchemaType;
import fabric.wsdlschemaparser.schema.FShort;
import fabric.wsdlschemaparser.schema.FSimpleType;
import fabric.wsdlschemaparser.schema.FString;
import fabric.wsdlschemaparser.schema.FToken;
import fabric.wsdlschemaparser.schema.FUnsignedByte;
import fabric.wsdlschemaparser.schema.FUnsignedInt;
import fabric.wsdlschemaparser.schema.FUnsignedLong;
import fabric.wsdlschemaparser.schema.FUnsignedShort;

/**
 * @author Dennis Pfisterer
 * 
 */
public class ProtobufModule implements Module {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ProtobufModule.class);

	private PSourceFile sourceFile;

	private Map<FElement, PMessage> generatedTopLevelElements = new HashMap<FElement, PMessage>();

	public ProtobufModule(File dotFile, Workspace workspace) throws Exception {
		sourceFile = workspace.getProtobuf().getDefaultSourceFile();
	}

	@Override
	public void handle(Workspace workspace, FSchema schema) throws Exception {

		for (FElement topLevelElement : schema.getTopLevelObjectList().getTopLevelElements()) {
			getOrCreateTopLevelElementMessage(topLevelElement);
		}

	}

	private PMessage getOrCreateTopLevelElementMessage(FElement element) {
		// TODO do infinite loop detection for recursive data types

		if (element.isReference())
			element = element.getReferencedTopLevelElement();

		PMessage message = this.generatedTopLevelElements.get(element);

		if (message != null)
			return message;

		log.debug("Now handling top level schema element {}", element.getName());
		handleSchemaObject(element, element.getName(), null, 0);

		return this.generatedTopLevelElements.get(element);
	}

	private void handleSchemaObject(FSchemaObject element, String elementName, PMessage parent, int uniqueNumberTag) {
		log.debug("Handling type " + element.getName() + " of type " + element.getClass());
		
		if (element instanceof FElement) {
			FElement schemaElement = (FElement) element;
			handleElement(schemaElement, parent, uniqueNumberTag);
	
		} else if (element instanceof FComplexType) {
			FComplexType complexType = (FComplexType) element;
			handleComplexType(complexType, elementName, parent);

		} else {
			throw new RuntimeException("Unhandled Type: " + element);
		}

	}

	private void handleElement(FElement element, PMessage parent, int uniqueNumberTag) {
		log.debug("Handling element {}, parent message {}", element.getName(), parent);
		log.debug("Dispatching to type handler for type {}", element.getSchemaType());

		if (element.isReference()) {
			log.debug("It's a reference.");

			if (element.getSchemaType().isSimple()) {
				handleSimpleType((FSimpleType) element.getSchemaType(), element, parent, uniqueNumberTag);

			} else {
				int min = element.getMinOccurs();
				int max = element.getMaxOccurs();
				boolean optional = false;
				boolean repeated = false;
				boolean required = false;

				if (min == 0 && max == 1)
					optional = true;
				else if (min == 1 && max == 1)
					required = true;
				else
					repeated = true;
				
				PComplexTypeField complexTypeField = new PComplexTypeField(getOrCreateTopLevelElementMessage(element),
						element.getName(), optional, required, repeated, uniqueNumberTag);
				parent.add(complexTypeField);

			}

		} else if (element.isTopLevel()) {
			log.debug("It's a top level type.");

			PMessage messageType = new PMessage(element.getName());

			if (element.getSchemaType().isSimple())
				handleSimpleType((FSimpleType) element.getSchemaType(), element, messageType, uniqueNumberTag);
			else
				handleComplexType((FComplexType) element.getSchemaType(), element.getName(), messageType);

			this.generatedTopLevelElements.put(element, messageType);

			if (parent == null)
				sourceFile.add(messageType);
			else
				parent.add(messageType);
			
		} else {
			log.debug("It's not a reference and not a top level type {}", element.getSchemaType());
			
			FSchemaType schemaType = element.getSchemaType();
			
			if ( schemaType instanceof FSimpleType )
				handleSimpleType((FSimpleType) schemaType, element, parent, uniqueNumberTag);
			else if (schemaType instanceof FComplexType)
				handleComplexType((FComplexType) schemaType, element.getName(), parent);
			else throw new RuntimeException("Unhandled: " + schemaType);
			
		}

	}

	private void handleSimpleType(FSimpleType type, FElement element, PMessage parent, int uniqueNumberTag) {
		log.debug("Handling simple type {}, parent message {}", type, parent);
		
		int min = element.getMinOccurs();
		int max = element.getMaxOccurs();
		boolean optional = false;
		boolean repeated = false;
		boolean required = false;

		if (min == 0 && max == 1)
			optional = true;
		else if (min == 1 && max == 1)
			required = true;
		else
			repeated = true;
		
		String name = generateName(type, element.getName());
		SimpleType simpleType = convertType(type);
		
		PSimpleTypeField simpleTypeField = new PSimpleTypeField(simpleType, name, optional, required, repeated, uniqueNumberTag);
		parent.add(simpleTypeField);
	}

	private void handleComplexType(FComplexType type, String elementName, PMessage parent) {
		log.debug("Handling complex type {}, parent message {}", type, parent);

		// Types: FAll, FSequence, FChoice
		if (type instanceof FChoice) {
			// TODO Support by making the children optional
			log.error("Choices are buggy, they are treated as sequence currently");
		}

		log.debug("Handling {} all/sequence/choice(s) of complex type {}", type.getChildObjects().size(), type);
		
		int uniqueNumberTag = 0;
		for (FSchemaObject child : type.getChildObjects()) {
			log.debug("Handling child {} of type {}", child.getName(), child.getClass().getName());
			handleSchemaObject(child, elementName, parent, ++uniqueNumberTag);
		}
	}

	private String generateName(FSchemaObject object, String elementName) {
		if (object.getNamespaceCount() > 1)
			return object.getInternalNamespacePrefix() + "_" + elementName;
		else
			return elementName;
	}

	@Override
	public void done() throws Exception {
		// Nothing to do, everything is handled by the Workspace code generation
	}

	private PSimpleTypeField.SimpleType convertType(FSimpleType simpleType) {

		Map<Class<? extends FSchemaType>, SimpleType> map = new HashMap<Class<? extends FSchemaType>, SimpleType>();

		//TODO Find optimal data type based on restrictions
		
		map.put(FBoolean.class, SimpleType.BOOL);
		map.put(FDecimal.class, SimpleType.INT64);
		map.put(FInteger.class, SimpleType.INT64);
		map.put(FLong.class, SimpleType.INT64);
		map.put(FInt.class, SimpleType.INT32);
		map.put(FShort.class, SimpleType.INT32);
		map.put(FByte.class, SimpleType.INT32);
		map.put(FNonNegativeInteger.class, SimpleType.UINT64);
		map.put(FPositiveInteger.class, SimpleType.UINT64);
		map.put(FUnsignedLong.class, SimpleType.UINT64);
		map.put(FUnsignedInt.class, SimpleType.UINT32);
		map.put(FUnsignedShort.class, SimpleType.UINT32);
		map.put(FUnsignedByte.class, SimpleType.UINT32);
		map.put(FNonPositiveInteger.class, SimpleType.SFIXED64);
		map.put(FNegativeInteger.class, SimpleType.SFIXED64);
		map.put(FDouble.class, SimpleType.DOUBLE);
		map.put(FFloat.class, SimpleType.FLOAT);
		map.put(FString.class, SimpleType.STRING);
		map.put(FAnyURI.class, SimpleType.STRING);
		map.put(FNormalizedString.class, SimpleType.STRING);
		map.put(FToken.class, SimpleType.STRING);
		map.put(FQName.class, SimpleType.STRING);

		return map.get(simpleType.getClass());
	}
}
