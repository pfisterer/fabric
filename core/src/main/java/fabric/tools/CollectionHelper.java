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
package fabric.tools;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class CollectionHelper<Type> {
	/** */
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(CollectionHelper.class);

	/** */
	private Collection<Collection<Type>> collections = null;

	/** */
	private Comparator<Type> comparator = null;

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public CollectionHelper(Comparator<Type> comparator) {
		this(new LinkedList<Collection<Type>>(), comparator);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public CollectionHelper(Collection<Collection<Type>> collections, Comparator<Type> comparator) {
		this.collections = collections;
		this.comparator = comparator;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public CollectionHelper(Collection<Type> c1, Collection<Type> c2, Comparator<Type> comparator) {
		collections = new Vector<Collection<Type>>();
		collections.add(c1);
		collections.add(c2);
		this.comparator = comparator;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public CollectionHelper(Collection<Type> c1, Collection<Type> c2, Collection<Type> c3, Comparator<Type> comparator) {
		this(c1, c2, comparator);
		collections.add(c3);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public void add(Collection<Type> c) {
		collections.add(c);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public void add(Collection<Type>... cs) {
		for(Collection<Type> c : cs )
			collections.add(c);
	}
	
	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public void clear() {
		collections.clear();		
	}
	
	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public Collection<Type> setunion() {
		TreeSet<Type> result = new TreeSet<Type>(comparator);

		//Add all to one hashset
		for (Collection<Type> c : collections)
			for (Type t : c) {
				result.add(t);
				log.debug("Added: " + t);
			}

		//Debug
		if (log.isDebugEnabled())
			log.debug("Union: " + Tools.toString(result));

		return result;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public Collection<Type> intersection() {
		log.debug("Calculating intersection.");
		dumpDebug();

		TreeSet<Type> result = new TreeSet<Type>(comparator);
		result.addAll(setunion());

		//Remove all items from the hash set that are not in the individual lists
		for (Collection<Type> c : collections)
			for (Iterator<Type> it = result.iterator(); it.hasNext();) {
				Type unionType = it.next();
				if (!contains(c, unionType)) {
					log.debug("Removing: " + unionType + ", not in list[" + Tools.toString(c) + "]");
					it.remove();
				}
			}

		//Debug
		if (log.isDebugEnabled())
			log.debug("Union: " + Tools.toString(result));

		return result;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public Collection<Type> difference() {
		Preconditions.checkArgument(this.collections.size() == 2, "Difference is only available for two sets");
		log.debug("Calculating difference.");
		TreeSet<Type> result = new TreeSet<Type>(comparator);
		
		dumpDebug();
		
		Iterator<Collection<Type>> it = collections.iterator();
		Collection<Type> c1 = it.next();
		Collection<Type> c2 = it.next();

		result.addAll(c1);
		for(Type t : c2)
			result.remove(t);

		//Debug
		if (log.isDebugEnabled())
			log.debug("Difference: " + Tools.toString(result));

		return result;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	private boolean contains(Collection<Type> c, Type type) {
		for (Type t : c)
			if (comparator.compare(t, type) == 0)
				return true;
		return false;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	private void dumpDebug() {
		int i = 0;
		for (Collection<Type> c : collections)
			log.debug("Collection[" + (++i) + "]:" + Tools.toString(c));
	}

}
