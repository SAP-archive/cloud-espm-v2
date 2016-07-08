/**
 * 
 */
package com.sap.espm.model.exception;

/**
 * Global Exception handler
 *
 */
public class BaseESPMRunTimeException extends RuntimeException {

	public BaseESPMRunTimeException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
