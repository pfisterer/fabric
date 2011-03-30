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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.LoggerFactory;

public class ResourceLoader {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ResourceLoader.class);

	/**
	 * 
	 * @param location
	 * @return
	 */
	public static URL getURL(String location) {
		URL u = ResourceLoader.class.getClassLoader().getResource(location);
		
		if( u != null ) 
			return u;
		
		File f = new File(location);
		if ( f.exists() ){
			try {
				return f.toURI().toURL( );
			} catch (MalformedURLException e) {
				//
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public static boolean exists(String location) {

		if (getURL(location) == null) {
			log.debug("Resource["+location+"] not found");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStream(String location) throws Exception {
		if( ! exists(location) )
			throw new Exception("Resource at location["+location+"] not found");
		
		return getURL(location).openStream();
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public static Reader getReader(String location) throws Exception {
		return new InputStreamReader( getInputStream(location) );
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getBufferedReader(String location) throws Exception {
		return new BufferedReader( getReader(location));
	}
	
	
}

/*+---------------------------------------------------------------+
 *| Source  $Source: /cvs/shawn/shawn/sys/worlds/save_world_task.cpp,v $  
 *| Version $Revision: 4 $ modified by $Author: pfisterer $
 *| Date    $Date: 2006-09-13 16:48:40 +0200 (Mi, 13 Sep 2006) $
 *+---------------------------------------------------------------
 *| $Log: save_world_task.cpp,v $
 *+---------------------------------------------------------------*/