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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;


public class FTopLevelObjectList {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(FTopLevelObjectList.class);

    private final String targetNamespace;

	private List<FElement> topLevelElements;

	private List<FSchemaType> topLevelTypes;

	/**
	 * @param targetNamespace 
	 */
	public FTopLevelObjectList(String targetNamespace) {
		this.targetNamespace = targetNamespace;
        topLevelElements = new LinkedList<FElement>( );
		topLevelTypes = new LinkedList<FSchemaType>( );
	}

	/**
     * @return the targetNamespace
     */
    public String getTargetNamespace( ) {
        return targetNamespace;
    }

    public void add(FSchemaObject o) {
    	if (o instanceof FElement) {
    		add((FElement)o);
    	} else if (o instanceof FSchemaType) {
    		add((FSchemaType)o);
    	}
    }

    public void add(FElement fse) {
        if (!fse.isTopLevel( )) {
            log.warn("FElement " + fse.getName( ) + " doesn't appear to a top-level one!");
        }
        topLevelElements.add(fse);
    }

    public void add(FSchemaType ft) {
        if (!ft.isTopLevel( )) {
            log.warn("FSchemaType " + ft.getName( ) + " (ID:" + ft.getID( ) + ") doesn't appear to a top-level one!");
        }
        topLevelTypes.add(ft);
    }

    public void addAll(Collection<? extends FSchemaObject> list) {
        for (FSchemaObject o : list) {
            add(o);
        }
    }

	public List<FElement> getTopLevelElements( ) {
		return topLevelElements;
	}

	public FSchemaType getTopLevelType(String typeName) {
		FSchemaType fst = null;
		for (FSchemaType t : topLevelTypes) {
			if (t.getName( ).equals(typeName)) {
				fst = t;
				break;
			}
		}
		return fst;
	}
	
	public FElement getTopLevelElement(String elemName) {
		FElement fse = null;
		for (FElement e : topLevelElements) {
			if (e.getName( ).equals(elemName)) {
				fse = e;
				break;
			}
		}
		return fse;
	}

	public List<FSchemaType> getTopLevelTypes( ) {
		return topLevelTypes;
	}

	public List<String> getUniqueNames( ) {
	    List<String> names = new ArrayList<String>( );
	    for (FElement e : topLevelElements) {
	        if (e.getSchemaType( ).isTopLevel( )) {
	            names.add(e.getSchemaType( ).getName( ));
	        } else {
	            names.add(e.getName( ));
	        }
	    }
	    return names;
	}

    /**
     * @param t
     * @return
     */
    public int getDataTypeCode(FSchemaType t) {
        List<String> names = getUniqueNames( );
        for (int i = 0; i < names.size( ); ++i) {
            if (t.getName( ).equals(names.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
