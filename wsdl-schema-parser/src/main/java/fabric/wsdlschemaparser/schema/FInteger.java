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

import java.math.BigInteger;

import org.apache.xmlbeans.SchemaType;



/**
 * This class represents the Fabric variation of an integer value declaration,
 * as specified by xs:integer in XML Schema.
 *
 * @author Marco Wegner
 */
public class FInteger extends FDecimal {

    // --------------------------------------------------------------------
    // Constructors and object initialisation
    // --------------------------------------------------------------------

	/**
	 *
	 */
	public FInteger( ) {
		this(null);
	}

    // --------------------------------------------------------------------

	/**
	 * @param typeName
	 */
	public FInteger(String typeName) {
		super(typeName);
		addRestriction(SchemaType.FACET_MIN_INCLUSIVE, BigInteger.valueOf(Long.MIN_VALUE));
        addRestriction(SchemaType.FACET_MAX_INCLUSIVE, BigInteger.valueOf(Long.MAX_VALUE));
	}

    // --------------------------------------------------------------------

	/**
	 * @param bits
	 * @param signed
	 */
	protected void initializeInteger(int bits, boolean signed) {
		initializeRestrictions(bits, signed);
	}

	/**
	 * @param bits
	 * @param signed
	 * @throws IllegalArgumentException
	 */
	public void initializeRestrictions(int bits, boolean signed) throws IllegalArgumentException {

        if (bits <= 0)
            throw new IllegalArgumentException("Number of bits must be greater than zero: " + bits);

        // BigInteger's methods don't seems to alter an objects state
        // but rather return the result

        BigInteger max;
        BigInteger range = BigInteger.ONE.shiftLeft(bits); // 2^bits

        if (signed) {
            BigInteger halfRange = range.shiftRight(1);
            max = halfRange.subtract(BigInteger.ONE);
            addRestriction(SchemaType.FACET_MIN_INCLUSIVE, halfRange.negate( ));
        } else {
            addRestriction(SchemaType.FACET_MIN_INCLUSIVE, 0);
            max = range.subtract(BigInteger.ONE);
        }

        addRestriction(SchemaType.FACET_MAX_INCLUSIVE, max);
	}
}
