package com.example.car;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

/**
 * The 'MyString' container class.
 */
@XmlRootElement(name = "MyString")
@XmlAccessorType(XmlAccessType.NONE)
public class MyString {

	/**
	 * The 'value' element.
	 */
	@XmlElement
	private String value;

	/**
	 * Set the 'value' member variable.
	 */
	public void setValue(final String value) {
		// Check the 'minLength' restriction
		if (value.length() < 0)
		{
			throw new IllegalArgumentException("Restriction 'minLength' violated for member variable 'value'.");
		}
		
		// Check the 'maxLength' restriction
		if (value.length() > 65535)
		{
			throw new IllegalArgumentException("Restriction 'maxLength' violated for member variable 'value'.");
		}
		
		this.value = value;
	}

	/**
	 * Get the 'value' member variable.
	 */
	public String getValue() {
		return this.value;
	}

}
