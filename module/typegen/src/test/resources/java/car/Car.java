package java.car;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

@Root(name = "Car")
public class Car
{
  @Attribute
  private String Name;

  @Element
  private SpeedGermany CurrentSpeed;

  @Element
  private int HorsePower;

  @Element
  private float Milage;

  @ElementArray
  private float PassengerWeights[] = new float[3];

  public Car(final String Name, final SpeedGermany CurrentSpeed, final int HorsePower, final float Milage, final float PassengerWeights[])
  {
    this.setName(Name);
    this.setCurrentSpeed(CurrentSpeed);
    this.setHorsePower(HorsePower);
    this.setMilage(Milage);
    this.setPassengerWeights(PassengerWeights);
  }

  public void setName(final String Name)
  {
    this.Name = Name;
  }

  public void setCurrentSpeed(final SpeedGermany CurrentSpeed)
  {
    this.CurrentSpeed = CurrentSpeed;
  }

  public void setHorsePower(final int HorsePower)
  {
    this.HorsePower = HorsePower;
  }

  public void setMilage(final float Milage)
  {
    this.Milage = Milage;
  }

  public void setPassengerWeights(float[] PassengerWeights)
  {
    if (PassengerWeights.length < 0 || PassengerWeights.length > 3)
    {
      throw new IllegalArgumentException("Restriction violated.");
    }

    this.PassengerWeights = PassengerWeights;
  }

  public String getName()
  {
    return this.Name;
  }

  public SpeedGermany getCurrentSpeed()
  {
    return this.CurrentSpeed;
  }

  public int getHorsePower()
  {
    return this.HorsePower;
  }

  public float getMilage()
  {
    return this.Milage;
  }

  public float[] getPassengerWeights()
  {
    return this.PassengerWeights;
  }
}
