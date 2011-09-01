/*
 * Test case for XSD elements and attributes combined
 */

package classes.java.archive;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Waehrung3{

  @Element
  private String name;

  @Attribute
  private String waehrungsCode;

  public Waehrung3() {
        super();
  }

  public Waehrung3(String name, String waehrungsCode) {
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
