package de.uniluebeck.sourcegen.c;

public class CppInclude {

    public CppInclude(String beforeDirective, String afterDirective, String... includes) {
        this.beforeDirective = beforeDirective;
        this.afterDirective = afterDirective;
        this.include = includes;
    }

    public String beforeDirective;
    public String afterDirective;
    public String[] include;
}
