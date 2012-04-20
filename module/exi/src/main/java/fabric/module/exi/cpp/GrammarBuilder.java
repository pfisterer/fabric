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
