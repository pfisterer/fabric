/*
 * Test case for XSD attributes
 */

package fabric.testsuite.java;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Waehrung2{

  @Attribute
  private String name;

  @Attribute
  private String waehrungsCode;

  public Waehrung2() {
        super();
  }

  public Waehrung2(String name, String waehrungsCode) {
        this.name = name;
        this.waehrungsCode = waehrungsCode;
  }

  public String getName(){
    return this.name;
  }

  public String getWaehrungsCode(){
    return this.waehrungsCode;
  }

}
