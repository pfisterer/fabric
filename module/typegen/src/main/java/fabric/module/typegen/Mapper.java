package fabric.module.typegen;

import fabric.wsdlschemaparser.schema.FSchemaType;

public interface Mapper {

	public String lookup(FSchemaType type);
	
	/**
	 * Creates the mapping for the specified language.
	 */
	abstract void createMapping();
	
	
}
