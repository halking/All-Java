package com.hal.exception;

public class XmlValidationException extends Exception {
	private static String VALIDATION_EXCEPTION = "An errour ocourred during the xml validation!";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4971793003399386238L;

	public XmlValidationException() {
		super(VALIDATION_EXCEPTION);
	}

	public XmlValidationException(String message) {
		super(message);
	}

	public XmlValidationException(Throwable e) {
		super(e);
	}
}
