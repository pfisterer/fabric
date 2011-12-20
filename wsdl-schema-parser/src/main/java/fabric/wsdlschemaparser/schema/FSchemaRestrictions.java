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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.slf4j.LoggerFactory;

/**
 * Utility class for conveniently storing and retrieving the facets for a
 * Schema type.
 *
 * @author Marco Wegner
 */
public class FSchemaRestrictions extends FSchemaObject implements Cloneable {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(FSchemaTypeFactory.class);

    // --------------------------------------------------------------------
    // Attributes
    // --------------------------------------------------------------------

    /**
     * The data type this facet storage belongs to.
     */
    private FSchemaType type;

    /**
     * Used for mapping the facets to their values.
     */
    private HashMap<Integer, Object> restrictions;

    // --------------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------------

    /**
     * Creates a new facets map for the containing data type.
     *
     * @param type The data type this facet storage belongs to.
     */
    public FSchemaRestrictions(FSchemaType type) {
        this.type = type;
        initRestrictions( );
    }

    // --------------------------------------------------------------------
    // Initialisation
    // --------------------------------------------------------------------

    /**
     * Initialises the facets' {@link HashMap}.
     */
    private void initRestrictions( ) {
        restrictions = new HashMap<Integer, Object>( );
    }

    // --------------------------------------------------------------------

    /**
     * Return the number of defined facets
     *
     * @return The number of facets.
     */
    public int getCount( ) {
        return restrictions.size( );
    }

    // --------------------------------------------------------------------
    // Methods for setting and retrieving values
    // --------------------------------------------------------------------

    /**
     * Sets a (valid) facet's value.
     *
     * @param facetCode The facet's code.
     * @param value The facet's value.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public void setRestriction(int facetCode, Object value)
            throws UnsupportedRestrictionException {
        checkValidFacet(facetCode);
        sanitize(facetCode);
        restrictions.put(facetCode, value);
    }

    public void removeRestriction(int facetCode) {
        checkValidFacet(facetCode);
        restrictions.remove(facetCode);
    }

    // --------------------------------------------------------------------

    /**
     * @param facetCode
     * @return
     */
    public boolean hasRestriction(int facetCode) {
        try {
            checkValidFacet(facetCode);
        } catch (UnsupportedRestrictionException ex) {
            return false;
        }

        return restrictions.containsKey(facetCode);
    }

    // --------------------------------------------------------------------

    /**
     * @param r
     */
    public void parse(RestrictionType r) {

        List<Integer> validFacets = type.getValidFacets( );
        if (validFacets != null) {
            for (int fcode : validFacets) {
                Facet[] facets = getRestriction(r, fcode);
                if (facets == null || facets.length == 0) {
                    continue;
                }

                Object o;
                if (fcode == SchemaType.FACET_ENUMERATION) {
                    List<String> olist = new ArrayList<String>( );
                    for (Facet f : facets) {
                        olist.add(f.getValue( ).getStringValue( ));
                    }
                    o = olist.toArray( );
                } else {
                    o = facets[0].getValue( ).getStringValue( );
                }
                setRestriction(fcode, o);
            }
        }
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>byte</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public byte getByteValue(int facetCode)
            throws UnsupportedRestrictionException {
        byte result = 0;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).byteValue( );
            } else if (o instanceof String) {
                try {
                    result = Byte.parseByte((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>short</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public short getShortValue(int facetCode)
            throws UnsupportedRestrictionException {
        short result = 0;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).shortValue( );
            } else if (o instanceof String) {
                try {
                    result = Short.parseShort((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>int</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public int getIntegerValue(int facetCode)
            throws UnsupportedRestrictionException {
        int result = 0;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).intValue( );
            } else if (o instanceof String) {
                try {
                    result = Integer.parseInt((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>long</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public long getLongValue(int facetCode)
            throws UnsupportedRestrictionException {
        long result = 0L;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).longValue( );
            } else if (o instanceof String) {
                try {
                    result = Long.parseLong((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>float</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public float getFloatValue(int facetCode)
            throws UnsupportedRestrictionException {
        float result = 0.0f;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).floatValue( );
            } else if (o instanceof String) {
                try {
                    result = Float.parseFloat((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as <code>double</code>.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public double getDoubleValue(int facetCode)
            throws UnsupportedRestrictionException {
        double result = 0.0;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof Number) {
                result = ((Number)o).doubleValue( );
            } else if (o instanceof String) {
                try {
                    result = Double.parseDouble((String)o);
                } catch (NumberFormatException e) {
                    // nothing to do here
                }
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as {@link BigInteger}.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public BigInteger getBigIntegerValue(int facetCode)
            throws UnsupportedRestrictionException {
        BigInteger result = null;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof BigInteger) {
                result = (BigInteger)o;
            } else if (o instanceof Number) {
                long longValue = ((Number)o).longValue( );
                result = BigInteger.valueOf(longValue);
            } else if (o instanceof String) {
                result = new BigInteger((String)o);
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as {@link BigDecimal}.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public BigDecimal getBigDecimalValue(int facetCode)
            throws UnsupportedRestrictionException {
        BigDecimal result = null;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof BigDecimal) {
                result = (BigDecimal)o;
            } else if (o instanceof Number) {
                result = BigDecimal.valueOf(((Number)o).doubleValue( ));
            } else if (o instanceof String) {
                result = new BigDecimal((String)o);
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as {@link String}.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public String getStringValue(int facetCode)
            throws UnsupportedRestrictionException {
        String result = null;
        Object o = getValue(facetCode);

        if (o != null) {
            if (o instanceof String) {
                result = (String)o;
            } else {
                result = o.toString( );
            }
        }
        return result;
    }

    // --------------------------------------------------------------------

    /**
     * Returns a facet's value as {@link Object}.
     *
     * @param facetCode The facet's code.
     * @return The facet's value or <code>null</code> if the facet is not
     *         valid for the containing type.
     * @throws UnsupportedRestrictionException If the facet is not a valid one
     *         for containing data type.
     */
    public Object getValue(int facetCode)
            throws UnsupportedRestrictionException {
        checkValidFacet(facetCode);
        return restrictions.get(facetCode);
    }

    /**
     * This method is called prior to actually adding a restriction. It is
     * meant to execute some preparatory stuff like e.g. removing facets which
     * would clash with the new one or other sanity checks.
     *
     * At this point is it certain that the facet is a valid one for this type
     * and that the restriction map is instantiated.
     *
     * @param facetCode The facet to be added.
     */
    private void sanitize(int facetCode) {
        if (type instanceof FNumber) {
            if (facetCode == SchemaType.FACET_MIN_INCLUSIVE) {
                removeRestriction(SchemaType.FACET_MIN_EXCLUSIVE);
            } else if (facetCode == SchemaType.FACET_MIN_EXCLUSIVE) {
                removeRestriction(SchemaType.FACET_MIN_INCLUSIVE);
            } else if (facetCode == SchemaType.FACET_MAX_INCLUSIVE) {
                removeRestriction(SchemaType.FACET_MAX_EXCLUSIVE);
            } else if (facetCode == SchemaType.FACET_MAX_EXCLUSIVE) {
                removeRestriction(SchemaType.FACET_MAX_INCLUSIVE);
            }
        } else if (type instanceof FString || type instanceof FList) {
            if (facetCode == SchemaType.FACET_LENGTH) {
                removeRestriction(SchemaType.FACET_MIN_LENGTH);
                removeRestriction(SchemaType.FACET_MAX_LENGTH);
            } else if (facetCode == SchemaType.FACET_MIN_LENGTH || facetCode == SchemaType.FACET_MAX_LENGTH) {
                removeRestriction(SchemaType.FACET_LENGTH);
            }
        }
    }

    /**
     * @param restriction
     * @param facetCode
     * @return
     */
    private static Facet[] getRestriction(RestrictionType restriction,
            int facetCode) {
        Facet[] facets = null;

        switch (facetCode) {

            case SchemaType.FACET_MIN_INCLUSIVE:
                facets = restriction.getMinInclusiveArray( );
                break;

            case SchemaType.FACET_MAX_INCLUSIVE:
                facets = restriction.getMaxInclusiveArray( );
                break;

            case SchemaType.FACET_MIN_EXCLUSIVE:
                facets = restriction.getMinExclusiveArray( );
                break;

            case SchemaType.FACET_MAX_EXCLUSIVE:
                facets = restriction.getMaxExclusiveArray( );
                break;

            case SchemaType.FACET_LENGTH:
                facets = restriction.getLengthArray( );
                break;

            case SchemaType.FACET_MIN_LENGTH:
                facets = restriction.getMinLengthArray( );
                break;

            case SchemaType.FACET_MAX_LENGTH:
                facets = restriction.getMaxLengthArray( );
                break;

            case SchemaType.FACET_ENUMERATION:
                facets = restriction.getEnumerationArray( );
                break;

            case SchemaType.FACET_WHITE_SPACE:
                facets = restriction.getWhiteSpaceArray( );
                break;

            case SchemaType.FACET_PATTERN:
                facets = restriction.getPatternArray( );
                break;

            case SchemaType.FACET_TOTAL_DIGITS:
                facets = restriction.getTotalDigitsArray( );
                break;

            case SchemaType.FACET_FRACTION_DIGITS:
                facets = restriction.getFractionDigitsArray( );
                break;
        }

        return facets;
    }

    /**
     * @param facetCode
     * @throws UnsupportedRestrictionException
     */
    private void checkValidFacet(int facetCode)
            throws UnsupportedRestrictionException {
        if (!type.getValidFacets( ).contains(facetCode))
            throw new UnsupportedRestrictionException("" + facetCode);
    }

    /*
     * (non-Javadoc)
     *
     * @see fabric.schema.types.FSchemaObject#equals(fabric.schema.types.FSchemaObject)
     *      @author Daniel Bimschas
     */
    @Override
    public boolean equals(FSchemaObject other) {

        if (!(other instanceof FSchemaRestrictions))
            return false;

        FSchemaRestrictions otherRes = (FSchemaRestrictions)other;

        if (restrictions.size( ) != otherRes.restrictions.size( ))
            return false;

        for (Object o : restrictions.keySet( )) {
            if (!equalsArrayRecursive(restrictions.get(o), otherRes.restrictions.get(o))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests two Object instances (which may be Object[] or Object[][] or ...)
     * for equality in the sense of every element of <code>aObj</code> has an
     * equal element in <code>bObj</code>. The order of the elements inside
     * the (possibly) arrays is not considered.<br/> <br/> <b>Example:</b><br/>
     * <br/>
     * <code>Object a = new Object[] {new String[] {"one","two"}, "three"};</code><br/>
     * <code>Object b = new Object[] {"three", new String[] {"two","one"}};</code><br/>
     * <code>Object c = new Object[] {"three", new String[] {"two"}};</code><br/>
     * <code>Object d = new Object[] {new String[] {"two", "one"}};</code><br/>
     * <br/> <code>equalsArrayRecursive(a, b)</code> yields <code>true</code><br/>
     * <code>equalsArrayRecursive(a, a)</code> yields <code>true</code><br/>
     * <code>equalsArrayRecursive(a, c)</code> yields <code>false</code><br/>
     * <code>equalsArrayRecursive(a, d)</code> yields <code>false</code><br/>
     * and so on...<br/>
     *
     * @param aObj
     * @param bObj
     * @return
     * @author Daniel Bimschas
     */
    private static boolean equalsArrayRecursive(Object aObj, Object bObj) {
        boolean aArray = aObj instanceof Object[];
        boolean bArray = bObj instanceof Object[];
        if (aArray && bArray) {

            Object[] a = (Object[])aObj;
            Object[] b = (Object[])bObj;

            if (a.length != b.length)
                return false;

            for (int i = 0; i < a.length; i++) {
                boolean contains = false;
                for (int j = 0; j < b.length; j++) {

                    if (a[i] instanceof Object[]) {
                        if (equalsArrayRecursive(a[i], b[j])) {
                            contains = true;
                            break;
                        }
                    } else {
                        if (a[i].equals(b[j])) {
                            contains = true;
                            break;
                        }
                    }

                }
                if (!contains) {
                    return false;
                }
            }
            return true;
        } else if (aArray && !bArray)
            return false;
        else if (!aArray && bArray)
            return false;
        return aObj.equals(bObj);
    }

    /**
     * @param r
     */
    public void parse(Restriction r) {
        List<Integer> validFacets = type.getValidFacets( );
        if (validFacets != null) {
            for (int fcode : validFacets) {
                Facet[] facets = getRestriction(r, fcode);
                if (facets == null || facets.length == 0) {
                    continue;
                }

                Object o;
                if (fcode == SchemaType.FACET_ENUMERATION) {
                    List<String> olist = new ArrayList<String>( );
                    for (Facet f : facets) {
                        olist.add(f.getValue( ).getStringValue( ));
                    }
                    o = olist.toArray( );
                } else {
                    o = facets[0].getValue( ).getStringValue( );
                }
                setRestriction(fcode, o);
            }
        }
    }

    /**
     * @param restriction
     * @param facetCode
     * @return
     */
    private Facet[] getRestriction(Restriction restriction, int facetCode) {
        Facet[] facets = null;

        switch (facetCode) {

            case SchemaType.FACET_MIN_INCLUSIVE:
                facets = restriction.getMinInclusiveArray( );
                break;

            case SchemaType.FACET_MAX_INCLUSIVE:
                facets = restriction.getMaxInclusiveArray( );
                break;

            case SchemaType.FACET_MIN_EXCLUSIVE:
                facets = restriction.getMinExclusiveArray( );
                break;

            case SchemaType.FACET_MAX_EXCLUSIVE:
                facets = restriction.getMaxExclusiveArray( );
                break;

            case SchemaType.FACET_LENGTH:
                facets = restriction.getLengthArray( );
                break;

            case SchemaType.FACET_MIN_LENGTH:
                facets = restriction.getMinLengthArray( );
                break;

            case SchemaType.FACET_MAX_LENGTH:
                facets = restriction.getMaxLengthArray( );
                break;

            case SchemaType.FACET_ENUMERATION:
                facets = restriction.getEnumerationArray( );
                break;

            case SchemaType.FACET_WHITE_SPACE:
                facets = restriction.getWhiteSpaceArray( );
                break;

            case SchemaType.FACET_PATTERN:
                facets = restriction.getPatternArray( );
                break;

            case SchemaType.FACET_TOTAL_DIGITS:
                facets = restriction.getTotalDigitsArray( );
                break;

            case SchemaType.FACET_FRACTION_DIGITS:
                facets = restriction.getFractionDigitsArray( );
                break;

            default:
                log.warn("Restriction not supported!");
        }

        return facets;
    }

    @SuppressWarnings("unchecked")
    public FSchemaRestrictions clone(FSchemaType fst) {
        FSchemaRestrictions ret = null;
        try {
            ret = (FSchemaRestrictions) super.clone();
            ret.restrictions = (HashMap<Integer, Object>) restrictions.clone();
            ret.type = fst;
        } catch (CloneNotSupportedException e) {
            log.error("Unable to clone FSchemaRestrictions object " + this.toString());
        }
        return ret;
    }
}
