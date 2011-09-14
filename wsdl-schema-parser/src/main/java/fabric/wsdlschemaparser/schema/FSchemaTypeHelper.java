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
package fabric.wsdlschemaparser.schema;

import java.math.BigInteger;
import java.util.Set;

import org.apache.xmlbeans.SchemaType;


/**
 * @author Marco Wegner
 *
 */
public final class FSchemaTypeHelper {

    public static BigInteger getMinimum(FSchemaRestrictions restr) throws UnsupportedRestrictionException {
    	BigInteger min = BigInteger.valueOf(Long.MIN_VALUE);
    	if (restr.hasRestriction(SchemaType.FACET_MIN_INCLUSIVE)) {
    		min = restr.getBigIntegerValue(SchemaType.FACET_MIN_INCLUSIVE);
    	} else if (restr.hasRestriction(SchemaType.FACET_MIN_EXCLUSIVE)) {
    		min = restr.getBigIntegerValue(SchemaType.FACET_MIN_EXCLUSIVE).add(BigInteger.ONE);
    	}
    	return min;
    }

    // --------------------------------------------------------------------

    public static BigInteger getMaximum(FSchemaRestrictions restr) throws UnsupportedRestrictionException {
    	BigInteger max = BigInteger.valueOf(Long.MAX_VALUE);
    	if (restr.hasRestriction(SchemaType.FACET_MAX_INCLUSIVE)) {
    		max = restr.getBigIntegerValue(SchemaType.FACET_MAX_INCLUSIVE);
    	} else if (restr.hasRestriction(SchemaType.FACET_MAX_EXCLUSIVE)) {
    		max = restr.getBigIntegerValue(SchemaType.FACET_MAX_EXCLUSIVE).subtract(BigInteger.ONE);
    	}
    	return max;
    }

    // --------------------------------------------------------------------

    public static int calculateIntegerBits(FInteger integer) throws UnsupportedRestrictionException {
    	FSchemaRestrictions restr = integer.getRestrictions( );
    
    	BigInteger min, max;
    	if (isUnsignedInteger(integer)) {
    		min = BigInteger.ZERO;
    		max = getMaximum(restr);
    	} else {
    		min = getMinimum(restr);
    		max = getMaximum(restr);
    	}
    
    	return TypeHelper.calculateBits(min, max);
    }

    // --------------------------------------------------------------------

    public static int calculateStringLength(FString fs) {
    	FSchemaRestrictions r = fs.getRestrictions( );
    
    	if (r.hasRestriction(SchemaType.FACET_LENGTH)) {
    	    return r.getIntegerValue(SchemaType.FACET_LENGTH);
    	}
    
    	return 0;
    }

    // --------------------------------------------------------------------

    public static boolean isOptional(FElement e) {
        return (e.getMinOccurs( ) == 0 && e.getMaxOccurs( ) == 1);
    }

    // --------------------------------------------------------------------

    public static boolean isArray(FElement e) {
    	return (e.getMinOccurs( ) != 1 || e.getMaxOccurs( ) != 1) && !isOptional(e);
    }

    // --------------------------------------------------------------------

    public static boolean isEnum(FSchemaType ft) {
    	boolean hasEnum = false;
    	if (ft.isSimple( )) {
    		FSchemaRestrictions r = ft.getRestrictions( );
    		if (r != null) {
    			hasEnum = r.hasRestriction(SchemaType.FACET_ENUMERATION);
    		}
    	}
    	return hasEnum;
    }

    // --------------------------------------------------------------------

    /**
     * Returns whether the given element has a default value.
     *
     * @param e FElement object
     * @return True, if the element has a default value, false otherwise.
     */
    public static boolean hasDefaultValue(FElement e) {
        return e.getDefaultValue() != null;
    }
    
    // --------------------------------------------------------------------

    /**
     * Returns whether the given element has a fixed value.
     *
     * @param e FElement object
     * @return True, if the element has a fixed value, false otherwise.
     */
    public static boolean hasFixedValue(FElement e) {
        return e.getFixedValue() != null;
    }

    // --------------------------------------------------------------------

    public static Object[] extractEnumArray(FSimpleType st) {
        Object[] oarray = null;
        Object value = st.getRestrictions( ).getValue(SchemaType.FACET_ENUMERATION);
        if (value instanceof Object[]) {
            oarray = (Object[])value;
        }
        return oarray;
    }

    // --------------------------------------------------------------------

    public static boolean isUnsignedInteger(FInteger fi) {
        return (fi instanceof FNonNegativeInteger);
    }

    // --------------------------------------------------------------------

    public static boolean containsInteger(Set<String> typeNames) {
        return typeNames.contains("FByte")
                || typeNames.contains("FUnsignedByte")
                || typeNames.contains("FShort")
                || typeNames.contains("FUnsignedShort")
                || typeNames.contains("FInt")
                || typeNames.contains("FUnsignedInt")
                || typeNames.contains("FLong")
                || typeNames.contains("FUnsignedLong")
                || typeNames.contains("FPositiveInteger")
                || typeNames.contains("FNonPositiveInteger")
                || typeNames.contains("FNegativeInteger")
                || typeNames.contains("FNonNegativeInteger")
                || typeNames.contains("FInteger");
    }
    
    // --------------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------------
    
    /**
     * No instantiation permitted.
     */
    private FSchemaTypeHelper( ) {
        //
    }
}
