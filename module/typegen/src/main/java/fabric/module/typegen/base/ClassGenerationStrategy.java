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
package fabric.module.typegen.base;

import java.util.ArrayList;

import de.uniluebeck.sourcegen.WorkspaceElement;
import fabric.module.typegen.AttributeContainer;

/**
 * Interface for class generation strategies. It defines a method
 * to convert AttributeContainer objects to language-specific
 * class objects (e.g. JClass or CppClass).
 *
 * @author seidel
 */
public interface ClassGenerationStrategy
{
  /**
   * Generate language-specific class object from AttributeContainer.
   *
   * @param container AttributeContainer for class creation
   *
   * @return Created class object (WorkspaceElement is base type
   * of both JClass and CppClass)
   *
   * @throws Exception Error during class object creation
   */
  public WorkspaceElement generateClassObject(AttributeContainer container) throws Exception;

  /**
   * Get list of dependencies that are required to save the class
   * object as a valid source file. Depending on the implementation,
   * the method may return imports (Java) or includes (C++), for
   * example.
   *
   * @return List of required dependencies
   */
  public ArrayList<String> getRequiredDependencies();
}
