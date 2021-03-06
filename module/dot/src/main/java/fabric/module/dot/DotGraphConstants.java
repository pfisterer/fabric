/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
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
package fabric.module.dot;

/**
 * Constants used in connection with dot graph creation.
 * 
 * @author Marco Wegner
 */
public final class DotGraphConstants {

    /**
     * The default attributes for top-level-element nodes.
     */
    public static final String DEFAULT_TOP_LEVEL_ELEMENT_ATTRIBUTES = "shape = ellipse, style = filled";
    /**
     * The default attributes for local-element nodes.
     */
    public static final String DEFAULT_LOCAL_ELEMENT_ATTRIBUTES = "shape = ellipse";
    /**
     * The default attributes for top-level-simple-type nodes.
     */
    public static final String DEFAULT_TOP_LEVEL_SIMPLE_TYPE_ATTRIBUTES = "shape = box, style = filled";
    /**
     * The default attributes for local-simple-type nodes.
     */
    public static final String DEFAULT_LOCAL_SIMPLE_TYPE_ATTRIBUTES = "shape = box";
    /**
     * The default attributes for top-level-complex-type nodes.
     */
    public static final String DEFAULT_TOP_LEVEL_COMPLEX_TYPE_ATTRIBUTES = "shape = box, style = filled";
    /**
     * The default attributes for local-complex-type nodes.
     */
    public static final String DEFAULT_LOCAL_COMPLEX_TYPE_ATTRIBUTES = "shape = box";
    /**
     * The default attribute for edges which signify references to top-level
     * types.
     */
    public static final String DEFAULT_EDGE_TOP_LEVEL_TYPE = "style = dashed";

}
