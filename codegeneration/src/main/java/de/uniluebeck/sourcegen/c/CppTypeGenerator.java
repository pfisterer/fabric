package de.uniluebeck.sourcegen.c;


public class CppTypeGenerator {

    private Long qualifier = null;
    private Long qualifiedType = null;
    private String typeName = null;

    public CppTypeGenerator(String name) {
        this.typeName = name;
    }

    public CppTypeGenerator(Long qualifier) {
    	this.qualifier = qualifier;
    }

    public CppTypeGenerator(String name, Long qualifier) {
        this.typeName = name;
        this.qualifier = qualifier;
    }

    public CppTypeGenerator(Long qualifiedType, String name) {
        this.typeName = name;
        this.qualifiedType = qualifiedType;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        if (this.qualifiedType != null) {
            buffer.append(Cpp.toString(this.qualifiedType));
        }

        if (this.typeName != null) {
            buffer.append(this.typeName);
        }

        if(this.qualifier != null) {
            buffer.append(Cpp.toString(this.qualifier));
        }

        if(buffer.length() == 0) {
        	buffer = null;
            return "$UNKNOWN_TYPE_BY_CPP_TYPE_GENERATOR$";
        }

        return buffer.toString();
    }

    public String getName() {
      return this.typeName;
    }

}
