package com.example.car;

/**
 * The application's main class.
 */
public class CarApplication {

	/**
	 * Main function of the application.
	 */
	public static void main(String[] args) {
		// Instanziate application
		CarApplication application = new CarApplication();
		
		// Create instance of the Java bean class
		Car car = new Car();
		// TODO: Add custom initialization code
		
		car.setSimpleBuiltIn("SimpleBuiltInContent");
		car.setSimpleLocal("SimpleLocalContent");
		MyString ms = new MyString();
		ms.setValue("MyStringContent");
		car.setSimpleCustom(ms);
		try {
			// Convert bean instance to XML document
			String xmlDocument = application.toXML(car);
		
			// Print XML document for debug purposes
			System.out.println(xmlDocument);
		
			System.out.println(application.fromEXIStream(application.toEXIStream(xmlDocument)));
			Car obj = application.toInstance(application.fromEXIStream(application.toEXIStream(xmlDocument)).replaceAll("MyStringContent", "MyAlteredContent"));
			System.out.println(obj.getSimpleBuiltIn() + " " + obj.getSimpleLocal() + " " + obj.getSimpleCustom().getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert Java bean object to XML document.
	 */
	public String toXML(final Car beanObject) throws Exception {
		return CarConverter.instanceToXML(beanObject);
	}

	/**
	 * Convert XML document to Java bean object.
	 */
	public Car toInstance(final String xmlDocument) throws Exception {
		return CarConverter.xmlToInstance(xmlDocument);
	}

	/**
	 * Serialize XML document to EXI byte stream.
	 */
	public byte[] toEXIStream(final String xmlDocument) throws Exception {
		return EXIConverter.serialize(xmlDocument);
	}

	/**
	 * Deserialize EXI byte stream to XML document.
	 */
	public String fromEXIStream(final byte[] exiStream) throws Exception {
		return EXIConverter.deserialize(exiStream);
	}

}
