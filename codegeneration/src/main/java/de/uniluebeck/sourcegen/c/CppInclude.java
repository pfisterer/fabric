package de.uniluebeck.sourcegen.c;

/**
 * TODO: Dummy implementation.
 */
public class CppInclude
{
  public String beforeDirective;
  public String afterDirective;
  public String[] include;
  
  public CppInclude(String beforeDirective, String afterDirective, String... includes)
  {
    this.beforeDirective = beforeDirective;
    this.afterDirective = afterDirective;
    this.include = includes;
  }
}
