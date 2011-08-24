/*
 * Testcase for xsd enumerations, decimals and dateTime
 */

package fabric.testsuite.java;

import java.math.BigDecimal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import javax.xml.datatype.XMLGregorianCalendar;

/*
 *  This is mainly an enumeration XSD document including date time objects and decimals
 */
@Root
public class Waehrung4{

  @Element
  private XMLGregorianCalendar Datum;
  @Element
  private Waehrungscodes Waehrungscode;
  @Element
  private String Waehrungsname;
  @Element
  private BigDecimal Wert;

  /*
   * This is a currency code type enumeration
   */
  @Element
  public enum Waehrungscodes{
    AUD,
    BRL,
    CAD,
    CNY,
    EUR,
    GBP,
    INR,
    JPY,
    RUR,
    USD
  }

  public Waehrung4() {
        super();
  }

  public XMLGregorianCalendar getDatum(){
    return this.Datum;
  }

  public Waehrungscodes getWaehrungscode(){
    return this.Waehrungscode;
  }

  public String getWaehrungsnamen(){
    return this.Waehrungsname;
  }

  public BigDecimal getWert(){
    return this.Wert;
  }

}
