package com.hal.exception;

public class ValidationException extends Exception {
	private static String VALIDATION_EXCEPTION = "An errour ocourred during the request validation!";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4971793003399386238L;

	public ValidationException() {
		super(VALIDATION_EXCEPTION);
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable e) {
		super(e);
	}

}
