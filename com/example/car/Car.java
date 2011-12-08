package com.example.car;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

/**
 * The 'Car' container class.
 */
@XmlRootElement(name = "Car")
@XmlAccessorType(XmlAccessType.NONE)
public class Car {

	/**
	 * The 'SimpleDefault' element.
	 */
	@XmlElement
	private String SimpleDefault = "Foo";

	/**
	 * The 'SimpleFixed' constant.
	 */
	@XmlElement
	private static final String SimpleFixed = "Bar";

	/**
	 * Set the 'SimpleDefault' member variable.
	 */
	public void setSimpleDefault(final String SimpleDefault) {
		// Check the 'minLength' restriction
		if (SimpleDefault.length() < 0)
		{
			throw new IllegalArgumentException("Restriction 'minLength' violated for member variable 'SimpleDefault'.");
		}
		
		// Check the 'maxLength' restriction
		if (SimpleDefault.length() > 65535)
		{
			throw new IllegalArgumentException("Restriction 'maxLength' violated for member variable 'SimpleDefault'.");
		}
		
		this.SimpleDefault = SimpleDefault;
	}

	/**
	 * Get the 'SimpleDefault' member variable.
	 */
	public String getSimpleDefault() {
		return this.SimpleDefault;
	}

	/**
	 * Get the 'SimpleFixed' member variable.
	 */
	public String getSimpleFixed() {
		return Car.SimpleFixed;
	}

}
