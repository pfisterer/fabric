package fabric.module.typegen;

//import fabric.codegeneration.*;
import de.uniluebeck.sourcegen.java.JClass;
import fabric.wsdlschemaparser.schema.FSchemaType;

public interface TypeGen {
  public JClass parseType(FSchemaType type);
}
