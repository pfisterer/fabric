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
package fabric.module.typegen.cpp;

import fabric.module.typegen.base.Mapper;

/**
 * LanguageMapper implementation for C++. This class will map
 * Fabric's XSD built-in datatypes to its C++ equivalents.
 *
 * @author seidel
 */
public class CppMapper extends Mapper
{
  /**
   * This method populates the map for the datatype mapping
   * with the C++ type names.
   */
  @Override
  public void createMapping()
  {
    types.put("FBoolean", "bool");
    types.put("FFloat", "xsd_float_t");
    types.put("FDouble", "double");
    types.put("FByte", "int8");
    types.put("FUnsignedByte", "uint8");
    types.put("FShort", "int32");
    types.put("FUnsignedShort", "uint16");
    types.put("FInt", "int32");
    types.put("FInteger", "char*");
    types.put("FPositiveInteger", "char*");
    types.put("FUnsignedInt", "uint32");
    types.put("FLong", "int64");
    types.put("FUnsignedLong", "uint64");
    types.put("FDecimal", "char*");
    types.put("FString", "char*");
    types.put("FHexBinary", "xsd_hexBinary_t");
    types.put("FBase64Binary", "xsd_base64Binary_t");
    types.put("FDateTime", "xsd_dateTime_t");
    types.put("FTime", "xsd_time_t");
    types.put("FDate", "xsd_date_t");
    types.put("FDay", "xsd_gDay_t");
    types.put("FMonth", "xsd_gMonth_t");
    types.put("FMonthDay", "xsd_gMonthDay_t");
    types.put("FYear", "xsd_gYear_t");
    types.put("FYearMonth", "xsd_gYearMonth_t");
    types.put("FDuration", "xsd_duration_t");
    types.put("FNOTATION", "xsd_notation_t");
    types.put("FQName", "xsd_qName_t");
    types.put("FName", "char*");
    types.put("FNCName", "char*");
    types.put("FNegativeInteger", "char*");
    types.put("FNMTOKEN", "char*");
    types.put("FNonNegativeInteger", "char*");
    types.put("FNonPositiveInteger", "char*");
    types.put("FNormalizedString", "char*");
    types.put("FToken", "char*");
    types.put("FAnyURI", "char*");
    types.put("FAny", "char*");
  }
}
