/*
 * Test case for XSD Elements
 */

package classes.java.archive;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Waehrung{

  @Element
  private String name;

  @Element
  private String waehrungsCode;

  public Waehrung() {
        super();
  }

  public Waehrung(String name, String waehrungsCode) {
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
