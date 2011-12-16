package de.uniluebeck.sourcegen.c;


public class CppTypeGenerator {

   // private Long qualifier = null;
    private Long qualifiedType = null;
    private String typeName = null;
    private CppClass clazz = null;

    public CppTypeGenerator(String name) {
        this.typeName = name;
    }

    public CppTypeGenerator(CppClass clazz) {
        this.clazz = clazz;
    }

    public CppTypeGenerator(Long qualifier) {
    	this.qualifiedType = qualifier;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        if (this.qualifiedType != null) {
            buffer.append(Cpp.toString(this.qualifiedType));
        } else if (this.typeName != null) {
            buffer.append(this.typeName);
        } else if (this.clazz != null) {
            buffer.append(this.getParents() + this.clazz.getName());
        }

        if(buffer.length() == 0) {
        	buffer = null;
            return "$UNKNOWN_TYPE_BY_CPP_TYPE_GENERATOR$";
        }

        return buffer.toString();
    }

    private String getParents(){
    	StringBuffer myParents = new StringBuffer();
    	if(this.clazz != null) {
	    	for (CppClass p : this.clazz.getParents()) {
	    		myParents.append(p.getName()+ "::");
			}
    	}
    	return myParents.toString();
    }

}
