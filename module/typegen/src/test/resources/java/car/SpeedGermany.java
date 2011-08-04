package java.car;

import org.simpleframework.xml.Element;

public class SpeedGermany
{
  @Element
  private int SpeedGermany;

  public SpeedGermany(final int SpeedGermany)
  {
    this.setSpeedGermany(SpeedGermany);
  }

  public void setSpeedGermany(final int SpeedGermany)
  {
    this.SpeedGermany = SpeedGermany;
  }

  public int getSpeedGermany()
  {
    return this.SpeedGermany;
  }
}
