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





/**
 * @author Marco Wegner
 */
public class TypeHelper {

    // --------------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------------

    protected TypeHelper( ) {
        // nothing to do
    }

    // --------------------------------------------------------------------
    // Methods which query restrictions
    // --------------------------------------------------------------------

	/**
     * @param numElements
     * @return
     */
    public static int calculateBits(long numElements) {
        if (numElements < 1) {
            return 0;
        }
    	return calculateBits(1, numElements);
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int calculateBits(long min, long max) {
    	return calculateBits(BigInteger.valueOf(min), BigInteger.valueOf(max));
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int calculateBits(BigInteger min, BigInteger max) {
    	return max.subtract(min).bitLength( );
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int calculateEightBits(long numElements) {
        if (numElements < 1) {
            return 0;
        }
        return calculateEightBits(1, numElements);
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int calculateEightBits(long min, long max) {
        return calculateEightBits(BigInteger.valueOf(min), BigInteger.valueOf(max));
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static int calculateEightBits(BigInteger min, BigInteger max) {
        int bytes = (int)Math.pow(2, Math.ceil(Math.log((7+calculateBits(min, max))/8)/Math.log(2)));
        return bytes*8;
    }

    /**
     * @return
     */
    public static int calculateYearBits( ) {
    	return calculateBits(DefaultValues.MIN_YEAR, DefaultValues.MAX_YEAR);
    }
}
