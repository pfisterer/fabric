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

package de.uniluebeck.sourcegen.exceptions;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author  bimschas
 */
public class ValidationException extends Exception {

	/** */
	private static final long serialVersionUID = -8793049965421226870L;
	
	/** */
	private LinkedList<Exception> exceptions = new LinkedList<Exception>();

	/***
	 * 
	 * @param message
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param e
	 */
	public ValidationException(Exception e) {
		super(e.getMessage());
		this.exceptions.add(e);
	}

	/**
	 * 
	 * @param c
	 */
	public ValidationException(Collection<? extends Exception> c) {
		super();
		this.exceptions.addAll(c);
	}
	
	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException() {
		super();
	}

	/**
	 * 
	 * @param e
	 */
	public void append(Exception e) {
		this.exceptions.add(e);
	}
	
	/**
	 * 
	 * @param c
	 */
	public void appendAll(Collection<? extends Exception> c) {
		this.exceptions.addAll(c);
	}

	/**
	 * @return
	 */
	public Collection<Exception> getExceptions() {
		return exceptions;
	}

}
