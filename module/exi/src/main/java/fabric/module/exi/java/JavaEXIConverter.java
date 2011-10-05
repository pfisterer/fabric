package fabric.module.exi.java;

import java.util.Properties;

import fabric.module.exi.java.lib.exi.EXILibrary;
import fabric.module.exi.java.lib.exi.EXILibraryFactory;

/**
 * @author seidel
 */
public class JavaEXIConverter
{
  private EXILibrary exiLibrary;

  public JavaEXIConverter(Properties properties) throws Exception
  {
    this.exiLibrary = EXILibraryFactory.getInstance().createEXILibrary(null, properties); // TODO
  }

  public String generateSerializeCode()
  {
    return this.exiLibrary.generateSerializeCode();
  }

  public String generateDeserializeCode()
  {
    return this.exiLibrary.generateDeserializeCode();
  }
}
