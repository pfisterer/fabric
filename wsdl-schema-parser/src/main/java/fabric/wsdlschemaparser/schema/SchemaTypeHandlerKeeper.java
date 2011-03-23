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

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.google.common.base.Preconditions;

public class SchemaTypeHandlerKeeper<HandlerType> {

	/** */
	private static Collection<String> schemaTypenames = new Vector<String>();

	private HashMap<String, HandlerType> keeper = new HashMap<String, HandlerType>();

	//-------------------------------------------------------------------------
	static {
		//Built-in
		schemaTypenames.add("string");
		schemaTypenames.add("boolean");
		schemaTypenames.add("decimal");
		schemaTypenames.add("precisionDecimal");
		schemaTypenames.add("float");
		schemaTypenames.add("double");
		schemaTypenames.add("duration");
		schemaTypenames.add("dateTime");
		schemaTypenames.add("time");
		schemaTypenames.add("date");
		schemaTypenames.add("gYearMonth");
		schemaTypenames.add("gYear");
		schemaTypenames.add("gMonthDay");
		schemaTypenames.add("gDay");
		schemaTypenames.add("gMonth");
		schemaTypenames.add("hexBinary");
		schemaTypenames.add("base64Binary");
		schemaTypenames.add("anyURI");
		schemaTypenames.add("QName");
		schemaTypenames.add("NOTATION");

		//Other built-in
		schemaTypenames.add("normalizedString");
		schemaTypenames.add("token");
		schemaTypenames.add("language");
		schemaTypenames.add("NMTOKEN");
		schemaTypenames.add("NMTOKENS");
		schemaTypenames.add("Name");
		schemaTypenames.add("NCName");
		schemaTypenames.add("ID");
		schemaTypenames.add("IDREF");
		schemaTypenames.add("IDREFS");
		schemaTypenames.add("ENTITY");
		schemaTypenames.add("ENTITIES");
		schemaTypenames.add("integer");
		schemaTypenames.add("nonPositiveInteger");
		schemaTypenames.add("negativeInteger");
		schemaTypenames.add("long");
		schemaTypenames.add("int");
		schemaTypenames.add("short");
		schemaTypenames.add("byte");
		schemaTypenames.add("nonNegativeInteger");
		schemaTypenames.add("unsigned");
		schemaTypenames.add("unsignedInt");
		schemaTypenames.add("unsignedShort");
		schemaTypenames.add("unsignedByte");
		schemaTypenames.add("positiveInteger");
		schemaTypenames.add("yearMonthDuration");
		schemaTypenames.add("dayTimeDuration");
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 * @param schemaType
	 * @param handler
	 */
	public void register(Collection<String> schemaTypes, HandlerType handler) {

		for (String schemaType : schemaTypes) {
			Preconditions.checkArgument(schemaTypenames.contains(schemaType), "Schematype [" + schemaType + "] unknown");
			keeper.put(schemaType, handler);
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public HandlerType get(String schemaType) {
		HandlerType h = keeper.get(schemaType);
		Preconditions.checkArgument(h != null, "No handler for fabric.schema type [" + schemaType + "] known");
		return h;
	}

}
