/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
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



/**
 * @author Marco Wegner
 */
public abstract class FSimpleType extends FSchemaType {

    // --------------------------------------------------------------------
    // Constructors and object initialisation
    // --------------------------------------------------------------------
	
	/**
	 * @param typeName
	 */
	protected FSimpleType(String typeName) {
		super(typeName);
	}

    // --------------------------------------------------------------------

	/**
	 * @see fabric.wsdlschemaparser.schema.FSchemaType#isSimple()
	 */
	@Override
	public final boolean isSimple( ) {
		return true;
	}
	
	/**
	 * @see fabric.wsdlschemaparser.schema.FSchemaObject#equals(fabric.wsdlschemaparser.schema.FSchemaObject)
	 * @author Daniel Bimschas
	 */
	@Override
	public boolean equals(FSchemaObject other) {
		
		if (!(other instanceof FSimpleType))
			return false;
		
		if (getClass().getCanonicalName().equals(other.getClass().getCanonicalName()))
			return false;
		
		if (getRestrictions() == null && ((FSimpleType)other).getRestrictions() != null)
			return false;
		
		if (getRestrictions() != null && ((FSimpleType)other).getRestrictions() == null)
			return false;
			
		return
			getRestrictions().equals(((FSimpleType)other).getRestrictions());
		
	}

    public boolean isList() {
        return false;
    }
}
