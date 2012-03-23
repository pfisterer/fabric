package de.uniluebeck.sourcegen.c;

public class CppInclude {
  public String beforeDirective;
  public String afterDirective;
  public String[] include;
  
  public CppInclude(String beforeDirective, String afterDirective, String... includes) {
    this.beforeDirective = beforeDirective;
    this.afterDirective = afterDirective;
    this.include = includes;
  }
}
