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
/** 16.12.2011 02:19 */
package fabric.module.typegen.base;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract base class for all language mapper implementations.
 * This class defines various methods, which are being used for
 * mapping creation and datatype look-up.
 *
 * @author seidel
 */
abstract public class Mapper
{
  /** Mapping between Fabric's XSD built-in datatypes and language-specific ones */
  protected HashMap<String, String> types = new HashMap<String, String>();

  /**
   * Constructor initializes the internal datatype mapping.
   */
  public Mapper()
  {
    createMapping();
  }

  /**
   * Look-up the language-specific representation of an XSD built-in type.
   *
   * @param type Fabric's type name for look-up
   *
   * @return String with the name of the corresponding language-specific datatype
   *
   * @throws IllegalArgumentException No matching mapping found
   */
  public String lookup(final String type) throws IllegalArgumentException
  {
    if (types.containsKey(type))
    {
      return types.get(type);
    }

    throw new IllegalArgumentException(String.format("No mapping found for datatype '%s'.", type));
  }

  /**
   * Check whether any XSD built-in type is mapped to the given
   * string. If the return value is true, the type must be an
   * XSD built-in type. Otherwise false is returned.
   *
   * @param type Type to check for built-in status
   *
   * @return True if argument is an XSD built-in type,
   * false otherwise
   */
  public boolean isBuiltInType(final String type)
  {
    boolean isBuiltInType = false;

    Collection collection = this.types.values();
    Iterator iterator = collection.iterator();
    while (iterator.hasNext())
    {
      if (type.equals(iterator.next()))
      {
        isBuiltInType = true;
        break; // Early exit
      }
    }

    return isBuiltInType;
  }

  /**
   * This method populates the internal map with
   * language-specific datatype mappings.
   */
  abstract protected void createMapping();
}
