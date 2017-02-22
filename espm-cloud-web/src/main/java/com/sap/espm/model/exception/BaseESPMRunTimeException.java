/**
 * 
 */
package com.sap.espm.model.exception;

/**
 * This global exception extends the {@link RuntimeException}. It will be used
 * as the parent class for defining {@link RuntimeException} in the application.
 *
 */
public class BaseESPMRunTimeException extends RuntimeException {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor taking the error message.
	 * 
	 * @param errorMessage
	 *            - The error message.
	 */
	public BaseESPMRunTimeException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * The constructor taking the {@link Throwable} instance.
	 * 
	 * @param throwable
	 *            - The {@link Throwable} instance to wrap.
	 */
	public BaseESPMRunTimeException(Throwable throwable) {
		super(throwable);
	}

}
