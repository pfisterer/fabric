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
/** 20.04.2012 02:11 */
package fabric.module.exi.cpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.GrammarFactory;
import com.siemens.ct.exi.grammar.Grammar;
import com.siemens.ct.exi.helpers.DefaultEXIFactory;

import fabric.module.exi.exceptions.FabricEXIException;

/**
 * This class is a grammar builder for EXI. It can read
 * an XML Schema document from a file and then build a
 * schema-informed EXI grammar using the Java EXI library
 * EXIficient. The grammar can later be used for EXI event
 * code determination in the C++ EXI module.
 *
 * @author seidel
 */
public class GrammarBuilder
{
  /** Logger object */
  private static final Logger LOGGER = LoggerFactory.getLogger(GrammarBuilder.class);

  /** Schema-informed EXI grammar */
  private Grammar schemaInformedGrammar;

  /**
   * Parameterless constructor.
   */
  public GrammarBuilder()
  {
    this.schemaInformedGrammar = null;
  }

  /**
   * Build schema-informed EXI grammar from given XML Schema
   * document. This method uses the EXIFactory object, which
   * is part of the EXIficient library, to initialize the
   * internal EXI grammar object.
   *
   * This method must be called once before the EXI grammar
   * can be retrieved with the getGrammar() method.
   *
   * @param schemaLocation Path to XML Schema document
   *
   * @throws Exception Error while creating EXI grammar
   */
  public void buildGrammar(final String schemaLocation) throws Exception
  {
    if (null == schemaLocation)
    {
      throw new FabricEXIException("Cannot build EXI grammar. Schema location is null.");
    }
    else
    {
      // Create schema-informed EXI grammar
      GrammarFactory grammarFactory = GrammarFactory.newInstance();
      this.schemaInformedGrammar = grammarFactory.createGrammar(schemaLocation);
      LOGGER.debug(String.format("Schema-informed EXI grammar was built for '%s'.", schemaLocation));

      // Do NOT use XSDGrammarBuilder to create grammar directly!

      // Create EXIFactory and enable fidelity option for strict mode
      EXIFactory exiFactory = DefaultEXIFactory.newInstance();
      exiFactory.setGrammar(this.schemaInformedGrammar);
      exiFactory.setFidelityOptions(FidelityOptions.createStrict());
    }
  }

  /**
   * Retrieve schema-informed EXI grammar for current XML Schema
   * document. Before you can grab the grammar, it must be build
   * by calling buildGrammar() once. In case the grammar was not
   * built yet, a call to getGrammar() will throw an exception.
   *
   * @return Schema-informed EXI grammar
   *
   * @throws Exception EXI grammar was not built yet
   */
  public Grammar getGrammar() throws Exception
  {
    // Check if grammar was already built
    if (null == this.schemaInformedGrammar)
    {
      throw new IllegalStateException("Grammar was not built yet. Call buildGrammar() first!");
    }

    return this.schemaInformedGrammar;
  }
}
