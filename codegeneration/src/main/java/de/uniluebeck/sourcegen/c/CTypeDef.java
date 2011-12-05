package de.uniluebeck.sourcegen.c;

public class CTypeDef {
	String alias = null;
	CppTypeGenerator type = null;

	public CTypeDef(String fullAlias, CppTypeGenerator type) {
		this.alias = fullAlias;
		this.type = type;
	}

	@Override
	public String toString() {
		return "typedef " + type.toString() + " " + alias + ";" + Cpp.newline;
	}

	public CppTypeGenerator getType() {
		return type;
	}

	public String getAlias() {
    	if(alias.indexOf("[") > 0) {
    		return alias.substring(0, alias.indexOf("["));
    	}
    	return alias;
	}

}
