package com.hal.jws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.hal.jws package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _SayHello_QNAME = new QName("http://jws.hal.com/", "sayHello");
	private final static QName _SayHelloResponse_QNAME = new QName("http://jws.hal.com/", "sayHelloResponse");
	private final static QName _SayToUser_QNAME = new QName("http://jws.hal.com/", "sayToUser");
	private final static QName _SayToUserResponse_QNAME = new QName("http://jws.hal.com/", "sayToUserResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.hal.jws
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SayToUserResponse }
	 * 
	 */
	public SayToUserResponse createSayToUserResponse() {
		return new SayToUserResponse();
	}

	/**
	 * Create an instance of {@link SayToUser }
	 * 
	 */
	public SayToUser createSayToUser() {
		return new SayToUser();
	}

	/**
	 * Create an instance of {@link SayHelloResponse }
	 * 
	 */
	public SayHelloResponse createSayHelloResponse() {
		return new SayHelloResponse();
	}

	/**
	 * Create an instance of {@link SayHello }
	 * 
	 */
	public SayHello createSayHello() {
		return new SayHello();
	}

	/**
	 * Create an instance of {@link User }
	 * 
	 */
	public User createUser() {
		return new User();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://jws.hal.com/", name = "sayHello")
	public JAXBElement<SayHello> createSayHello(SayHello value) {
		return new JAXBElement<SayHello>(_SayHello_QNAME, SayHello.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SayHelloResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://jws.hal.com/", name = "sayHelloResponse")
	public JAXBElement<SayHelloResponse> createSayHelloResponse(SayHelloResponse value) {
		return new JAXBElement<SayHelloResponse>(_SayHelloResponse_QNAME, SayHelloResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SayToUser }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://jws.hal.com/", name = "sayToUser")
	public JAXBElement<SayToUser> createSayToUser(SayToUser value) {
		return new JAXBElement<SayToUser>(_SayToUser_QNAME, SayToUser.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SayToUserResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://jws.hal.com/", name = "sayToUserResponse")
	public JAXBElement<SayToUserResponse> createSayToUserResponse(SayToUserResponse value) {
		return new JAXBElement<SayToUserResponse>(_SayToUserResponse_QNAME, SayToUserResponse.class, null, value);
	}

}
