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

import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Marco Wegner
 */
public abstract class FSchemaType extends FSchemaObject implements Cloneable {

    // --------------------------------------------------------------------
    // Static attributes
    // --------------------------------------------------------------------

    private final org.slf4j.Logger log = LoggerFactory.getLogger(FSchemaTypeFactory.class);

    /**
     * This type's restrictions.
     */
    private FSchemaRestrictions restrictions = new FSchemaRestrictions(this);

    // --------------------------------------------------------------------

    /**
     * Stores whether this type is a top-level one or not.
     */
    private boolean isTopLevel;

    // --------------------------------------------------------------------

    /**
     * Creates a new Fabric Schema type.
     * 
     * @param name
     *            The new type's name.
     */
    protected FSchemaType(String name) {
        super(name);
        setTopLevel(false);
    }

    // --------------------------------------------------------------------

    /**
     * Returns the list of valid facets for this type
     * 
     * @return The list of valid facets for this type.
     */
    public List<Integer> getValidFacets() {
        return null;
    }

    // --------------------------------------------------------------------

    /**
     * @param facet
     * @param value
     * @throws UnsupportedRestrictionException
     */
    public void addRestriction(int facet, Object value) throws UnsupportedRestrictionException {
        // check if the facet is valid at all
        if (getValidFacets() == null || !getValidFacets().contains(facet) || value == null)
            throw new UnsupportedRestrictionException();

        // finally, add the restriction
        restrictions.setRestriction(facet, value);
    }

    // --------------------------------------------------------------------

    /**
     * Returns the restriction object for this type.
     * 
     * @return The restriction object for this type.
     */
    public FSchemaRestrictions getRestrictions() {
        return restrictions;
    }

    // --------------------------------------------------------------------

    /**
     * @param isTopLevel
     */
    public void setTopLevel(boolean isTopLevel) {
        this.isTopLevel = isTopLevel;
    }

    // --------------------------------------------------------------------

    /**
     * Check whether this type is a top-level one or not.
     * 
     * @return <code>true</code> if this type is a top-level one, else <code>false</code>
     */
    public boolean isTopLevel() {
        return isTopLevel;
    }

    // --------------------------------------------------------------------

    /**
     * Check whether this type is a simple one or not.
     * 
     * @return <code>true</code> if this type is a simple one, else <code>false</code>
     */
    public abstract boolean isSimple();

    // --------------------------------------------------------------------

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "]";
    }

    @Override
    public FSchemaType clone() {
        FSchemaType ret = null;
        try {
            ret = (FSchemaType) super.clone();
            ret.restrictions = restrictions.clone(ret);
        } catch (CloneNotSupportedException e) {
            log.error("Unable to clone FSchemaType object " + this.toString());
        }
        return ret;
    }

}
