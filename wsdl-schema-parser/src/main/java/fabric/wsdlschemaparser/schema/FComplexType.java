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

/**
 *
 */
package fabric.wsdlschemaparser.schema;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Marco Wegner
 */
public abstract class FComplexType extends FSchemaType {

    // --------------------------------------------------------------------
    // Member variables
    // --------------------------------------------------------------------

	/**
	 * The list of this complex type's child objects.
	 */
	private List<FSchemaObject> childObjects;

    // --------------------------------------------------------------------

	/**
	 * The list of this complex type's attributes.
	 */
	private List<FSchemaAttribute> attributes;

    // --------------------------------------------------------------------

	/**
     * States whether this complex type contains simple contents
     * (xs:simpleContent).
     */
	private boolean isSimpleContent = false;
	
	/**
     * States whether this complex type has an AnyAttribute
     * (xs:anyAttribute).
     */
	private boolean hasAnyAttribute = false;

    // --------------------------------------------------------------------
    // Constructor and object initialisation
    // --------------------------------------------------------------------

	/**
	 * @param typeName
	 */
	public FComplexType(String typeName) {
		super(typeName);
		initialize( );
	}

    // --------------------------------------------------------------------

    /**
	 *
	 */
	private void initialize( ) {
		this.childObjects = new LinkedList<FSchemaObject>( );
		this.attributes = new LinkedList<FSchemaAttribute>( );
	}

	// --------------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------------

	public boolean isSimpleContent( ) {
        return isSimpleContent;
    }

    public void setSimpleContent(boolean isSimpleContent) {
        this.isSimpleContent = isSimpleContent;
    }
    
    public boolean hasAnyAttribute()
	{
		return hasAnyAttribute;
	}
	
	public void setAnyAttribute(boolean hasAnyAttribute)
	{
		this.hasAnyAttribute = hasAnyAttribute;
	}

    /**
	 * @param elem
	 */
	public void addChildObject(FSchemaObject fso) {
		if (fso == null)
			throw new IllegalArgumentException("Child objects may not be null");
		this.childObjects.add(fso);
	}
	
	/**
	 * @param elem
	 */
	public void addChildObjectAfter(FSchemaObject fso, String name) {
		if (fso == null)
			throw new IllegalArgumentException("Child objects may not be null");
		int index = 0;
		if(name!=null)
		{
			for(int i=0; i<childObjects.size();i++)
			{
				FSchemaObject fsoLocal = childObjects.get(i);
				if(fsoLocal.getName().equals(name))
				{
					index = i;
				}
			}
		}
		this.childObjects.add(index, fso);
	}


    // --------------------------------------------------------------------

	/**
	 * @return
	 */
	public List<FSchemaObject> getChildObjects( ) {
		return this.childObjects;
	}

    // --------------------------------------------------------------------

	public void addAttribute(FSchemaAttribute attr) {
		if (attr == null)
			throw new IllegalArgumentException("Attribute objects may not be null");
		this.attributes.add(attr);
	}

    // --------------------------------------------------------------------

	public List<FSchemaAttribute> getAttributes( ) {
		return this.attributes;
	}

    // --------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see fabric.schema.types.tng.FSchemaType#isSimple()
	 */
	@Override
	public final boolean isSimple( ) {
		return false;
	}

    // --------------------------------------------------------------------

	private boolean containsAllAttributes(FComplexType a, FComplexType b) {
		for (FElement eA : a.attributes) {
			boolean contained = false;
			for (FElement eB : b.attributes) {
				if (eA.equals(eB)) {
					contained = true;
					break;
				}
			}
			if (!contained)
				return false;
		}
		return true;
	}

	private boolean containsAllChildObjects(FComplexType a, FComplexType b) {
		for (FSchemaObject eObj : a.childObjects) {
			boolean contained = false;
			for (FSchemaObject bObj : b.childObjects) {
				if (eObj.equals(bObj)) {
					contained = true;
					break;
				}
			}
			if (!contained)
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see fabric.schema.types.FSchemaObject#equals(fabric.schema.types.FSchemaObject)
	 * @author Daniel Bimschas
	 */
	@Override
	public boolean equals(FSchemaObject other) {

		if (!(other instanceof FComplexType))
			return false;

		FComplexType otherType = (FComplexType) other;

		// test if this complex type is of the same type anyway
		// since FAll != FChoice != FSequence
		if (!getClass().getCanonicalName().equals(other.getClass().getCanonicalName()))
			return false;

		// test for the same attributes
		boolean equalAttributes =
			containsAllAttributes(otherType, this) &&
			containsAllAttributes(this, otherType);

		if (!equalAttributes)
			return false;

		// test for equality of child objects
		if (this instanceof FSequence) {

			// if this is a sequence the order of the child objects
			// must be equal to otherType
			for (int i=0; i<childObjects.size(); i++)
				if (!childObjects.get(i).equals(otherType.childObjects.get(i)))
					return false;

		}
		else {

			// if this is a FAll or FChoice the order
			// of the elements don't matter
			boolean equalChildObjects =
				containsAllChildObjects(otherType, this) &&
				containsAllChildObjects(this, otherType);

			if (!equalChildObjects)
				return false;

		}

		return otherType.getRestrictions() != null ?
				otherType.getRestrictions().equals(getRestrictions()) :
				getRestrictions() == null;
	}
}
