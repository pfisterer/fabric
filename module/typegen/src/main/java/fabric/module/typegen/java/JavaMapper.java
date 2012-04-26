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
package fabric.module.typegen.java;

import fabric.module.typegen.base.Mapper;

/**
 * LanguageMapper implementation for Java. This class will map
 * Fabric's XSD built-in datatypes to its Java equivalents.
 *
 * @author seidel
 */
public class JavaMapper extends Mapper
{
  /**
   * This method populates the map for the datatype mapping with the
   * Java type names. Non-standard datatypes must be fully qualified
   * (e.g. javax.xml.namespace.QName instead of QName).
   */
  @Override
  public void createMapping()
  {
    types.put("FBoolean", "boolean");
    types.put("FFloat", "float");
    types.put("FDouble", "double");
    types.put("FByte", "byte");
    types.put("FUnsignedByte", "short");
    types.put("FShort", "short");
    types.put("FUnsignedShort", "int");
    types.put("FInt", "int");
    types.put("FInteger", "java.math.BigDecimal");
    types.put("FPositiveInteger", "java.math.BigInteger");
    types.put("FUnsignedInt", "java.math.BigInteger");
    types.put("FLong", "java.math.BigInteger");
    types.put("FUnsignedLong", "java.math.BigDecimal");
    types.put("FDecimal", "java.math.BigDecimal");
    types.put("FString", "String");
    types.put("FHexBinary", "byte[]");
    types.put("FBase64Binary", "byte[]");
    types.put("FDateTime", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FTime", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDate", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDay", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FMonth", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FMonthDay", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FYear", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FYearMonth", "javax.xml.datatype.XMLGregorianCalendar");
    types.put("FDuration", "javax.xml.datatype.Duration");
    types.put("FNOTATION", "javax.xml.namespace.QName");
    types.put("FQName", "javax.xml.namespace.QName");
    types.put("FAnyURI", "String");
    types.put("FName", "String");
    types.put("FNCName", "String");
    types.put("FNegativeInteger", "java.math.BigDecimal");
    types.put("FNMTOKEN", "String");
    types.put("FNonNegativeInteger", "java.math.BigDecimal");
    types.put("FNonPositiveInteger", "java.math.BigDecimal");
    types.put("FNormalizedString", "String");
    types.put("FToken", "String");
    types.put("FAny", "Object");
  }
}
