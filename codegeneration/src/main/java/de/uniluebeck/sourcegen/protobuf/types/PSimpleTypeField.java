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
package de.uniluebeck.sourcegen.protobuf.types;

public class PSimpleTypeField extends PAbstractField {

	private SimpleType type;

	public PSimpleTypeField(SimpleType type, String name, boolean optional, boolean required, boolean repeated, int uniqueNumberTag) {
		super(name, optional, required, repeated, uniqueNumberTag);
		this.type = type;
	}

	public SimpleType getType() {
		return type;
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		StringBuffer b = new StringBuffer();

		if (isOptional())
			b.append("optional ");

		if (isRepeated())
			b.append("repeated ");

		if (isRequired())
			b.append("required ");

		b.append(type.toTypeString());
		b.append(" ");
		b.append(getName());
		b.append(" = ");
		b.append(getUniqueNumberTag());
		b.append(";");
		
		addLine(buffer, tabCount, b.toString());
	}
	
	
	public enum SimpleType {
		DOUBLE, FLOAT, INT32, INT64, UINT32, UINT64, SINT32, SINT64, FIXED32, FIXED64, SFIXED32, SFIXED64, BOOL, STRING, BYTES;

		public String toTypeString() {

			switch (this) {
			case DOUBLE:
				return "double";
			case FLOAT:
				return "double";
			case INT32:
				return "int32";
			case INT64:
				return "int64";
			case UINT32:
				return "uint32";
			case UINT64:
				return "uint64";
			case SINT32:
				return "sint32";
			case SINT64:
				return "sint64";
			case FIXED32:
				return "fixed32";
			case FIXED64:
				return "fixed64";
			case SFIXED32:
				return "sfixed32";
			case SFIXED64:
				return "sfixed64";
			case BOOL:
				return "bool";
			case STRING:
				return "string";
			case BYTES:
				return "bytes";
			}

			throw new RuntimeException("Unimplemented type: " + this);
		}
	}

}
