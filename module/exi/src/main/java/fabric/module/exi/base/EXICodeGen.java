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
/** 11.04.2012 22:26 */
package fabric.module.exi.base;

import java.util.ArrayList;

import fabric.wsdlschemaparser.schema.FElement;

import fabric.module.exi.java.FixValueContainer.ElementData;
import fabric.module.exi.java.FixValueContainer.ArrayData;
import fabric.module.exi.java.FixValueContainer.ListData;

/**
 * Public interface for EXICodeGen implementations.
 * 
 * @author seidel
 */
public interface EXICodeGen
{
  /**
   * Generate code to serialize and deserialize Bean objects with EXI.
   * 
   * @param fixElements XML elements, where value-tags need to be fixed
   * @param fixArrays XML arrays, where value-tags need to be fixed
   * @param fixLists XML lists, where value-tags need to be fixed
   * 
   * @throws Exception Error during code generation
   */
  public void generateCode(final ArrayList<ElementData> fixElements,
                           final ArrayList<ArrayData> fixArrays,
                           final ArrayList<ListData> fixLists) throws Exception;

  /**
   * Create source file and write it to language-specific workspace.
   * 
   * @throws Exception Error during source file write-out
   */
  public void writeSourceFile() throws Exception;

  /**
   * Handle end of XML Schema document, i.e. build EXI grammar.
   * 
   * @param pathToSchemaDocument Path to XML Schema document
   * 
   * @throws Exception Error during event handling
   */
  public void handleEndOfSchema(final String pathToSchemaDocument) throws Exception;

  /**
   * Handle top level element from XML Schema document.
   * 
   * @param element Top level element to handle
   */
  public void handleTopLevelElement(final FElement element);

  /**
   * Handle local element from XML Schema document.
   * 
   * @param element Local element to handle
   * @param parentName Name of parent XML element
   */
  public void handleLocalElement(final FElement element, final String parentName);
}
