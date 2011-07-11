package fabric.module.typegen;

import java.util.HashMap;

import fabric.wsdlschemaparser.schema.FSchemaType;

public class LanguageMapper implements Mapper {
	
	///////////////
	// STATICS
	///////////////
	
	public static final HashMap<FSchemaType, String> types;
	
	static {
		types = new HashMap<FSchemaType, String>();
	}
	

	
	///////////////
	// DYNAMICS
	///////////////
	
	
	private LanguageMapper instance;
	
		
	protected LanguageMapper() {
		createMapping();
	}
	
	public synchronized LanguageMapper getInstance() {
		if (null == instance) {
			instance = new LanguageMapper();
		}
		
		return instance;
	}
	
	public String lookup(FSchemaType type) throws IllegalArgumentException {
		if(types.containsKey(type)) {
            return types.get(type);
        }
		
		throw new IllegalArgumentException("unknown type: " +type.getName());
	}
	
	/**
	 * Creates the mapping for the specified language.
	 */
	public void createMapping() {}

}
