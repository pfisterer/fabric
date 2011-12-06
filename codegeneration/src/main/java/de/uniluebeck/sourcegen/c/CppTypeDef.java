package de.uniluebeck.sourcegen.c;


public class CppTypeDef {

    // Singeton pattern: STATIC
    public static CppTypeDef factory = new CppTypeDef();

    public CTypeDef create(long qualifier, String alias) {
    	return this.create(new CppTypeGenerator(qualifier), alias);
    }

    public CTypeDef create(String type, String alias) {
    	return this.create(new CppTypeGenerator(type), alias);
    }

    public CTypeDef create(CppTypeGenerator type, String alias) {
        return new CTypeDef(alias, type);

    	/*
    	// Ignore the array
    	if(alias.indexOf("[") > 0) {
    		String aliasName = alias.substring(0, alias.indexOf("["));
    		if (!this.types.containsKey(aliasName)) {
    			CTypeDef c = new CTypeDef(alias, type);
    			this.types.put(aliasName, c);
                return c;
            } else {
                throw new Error("Typedef of key '" + alias + "'is already existing.");
            }
    	}

        if (!this.types.containsKey(alias)) {
			CTypeDef c = new CTypeDef(alias, type);
			this.types.put(alias, c);
            return c;

        } else {
            throw new Error("Typedef of key '" + alias + "'is already existing.");
        }
        */
    }
/*
    public CTypeDef getTypeDef(String key) {
        if(this.types.containsKey(key)) {
            return this.types.get(key);
        } else {
            throw new Error("Typedef '" + key + "' does not exist.");
        }
    }
*/
}
