package de.uniluebeck.sourcegen.c;


public class CppTypeGenerator implements CppTemplateName {

    private Long qualifier = null;
    private Long qualifiedType = null;
    private CppTemplateName clazz = null;
    private CppTemplateHelper template = null;
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

    public CppTypeGenerator(String name, CppTemplateHelper template) {
        this.typeName = name;
        this.template = template;
    }

    public CppTypeGenerator(Long qualifier, Long qualifiedType, CppTemplateHelper template) {
        this.qualifier = qualifier;
        this.qualifiedType = qualifiedType;
        this.template = template;
    }

    public CppTypeGenerator(CppTemplateName clazz, CppTemplateHelper template) {
        this.clazz = clazz;
        this.template = template;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        if (this.qualifiedType != null) {
            buffer.append(Cpp.toString(this.qualifiedType));
        }

        if(this.clazz != null) {
            buffer.append(this.clazz.getName());
        }

        if (this.typeName != null) {
            buffer.append(this.typeName);
        }

        if(this.qualifier != null) {
            buffer.append(Cpp.toString(this.qualifier));
        }

        if(this.template != null) {
            buffer.append(this.template.toString());
        }

        if(buffer.length() == 0) {
        	buffer = null;
            return "$UNKNOWN_TYPE_BY_CPP_TYPE_GENERATOR$";
        }

        return buffer.toString();
    }

    @Override
    public String getName() {
        return this.toString();
    }
    
    public String getTypeName() {
      return this.typeName;
    }

}
