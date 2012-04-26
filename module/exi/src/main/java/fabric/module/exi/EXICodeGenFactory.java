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
package fabric.module.exi;

import java.util.Properties;
import java.lang.reflect.Constructor;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.exi.base.EXICodeGen;
import fabric.module.exi.exceptions.UnsupportedEXICodeGenException;

/**
 * Abstract factory for creation of concrete EXICodeGen objects.
 * 
 * @author seidel
 */
public class EXICodeGenFactory
{
  /** Factory instance for Singleton implementation */
  private static EXICodeGenFactory instance;

  /**
   * Private constructor for Singleton implementation.
   */
  private EXICodeGenFactory()
  {
    // Empty implementation
  }

  /**
   * Method for factory access. We either instantiate a new
   * object here or use the existing instance.
   *
   * @return EXICodeGenFactory object
   */
  public static synchronized EXICodeGenFactory getInstance()
  {
    // Instance does not exist, create a new one
    if (null == instance)
    {
      instance = new EXICodeGenFactory();
    }

    return instance;
  }

  /**
   * Factory method for EXICodeGen creation. This method
   * takes the name of a concrete EXICodeGen implementation
   * and tries to instantiate an object of this class.
   * On success, the object is returned, otherwise we
   * throw an exception.
   *
   * @param concreteEXICodeGenName Name of concrete EXICodeGen class
   * @param workspace Workspace object for source code write-out
   * @param properties Properties object with module options
   *
   * @return EXICodeGen object of desired type
   *
   * @throws Exception Error during class instantiation
   */
  public EXICodeGen createEXICodeGen(String concreteEXICodeGenName, Workspace workspace, Properties properties) throws Exception
  {
    EXICodeGen concreteEXICodeGen = null;

    try
    {
      // Try to instantiate concrete EXICodeGen class     
      Class concreteClass = Class.forName(concreteEXICodeGenName);
      Class[] argumentsClass = new Class[] { Workspace.class, Properties.class };

      Constructor constructor = concreteClass.getConstructor(argumentsClass);
      Object[] arguments = new Object[] { workspace, properties };

      concreteEXICodeGen = (EXICodeGen)constructor.newInstance(arguments);
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(String.format("No class found with name '%s'.", concreteEXICodeGenName));
    }
    catch (InstantiationException e)
    {
      throw new Exception(String.format("Could not instantiate class with name '%s'.", concreteEXICodeGenName));
    }
    catch (IllegalAccessException e)
    {
      throw new Exception(String.format("Illegal access to class '%s'.", concreteEXICodeGenName));
    }
    catch (ClassCastException e)
    {
      throw new UnsupportedEXICodeGenException(String.format("Class '%s' is not a valid EXICodeGen implementation.", concreteEXICodeGenName));
    }

    return concreteEXICodeGen;
  }
}
