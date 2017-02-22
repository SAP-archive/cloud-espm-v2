package com.sap.espm.model.exception;

/**
 * This class is used as a wrapper for exceptions while connecting to the
 * Document Storage via the CMIS library.
 */
public class CMISConnectionException extends BaseESPMRunTimeException {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor taking error message as input parameter.
	 * 
	 * @param errorMessage
	 *            - The error message.
	 */
	public CMISConnectionException(String errorMessage) {
		super(errorMessage);

	}

	/**
	 * Constructor taking a {@link Throwable} as input parameter
	 * 
	 * @param throwable
	 *            - The {@link Throwable} to wrap.
	 */
	public CMISConnectionException(Throwable throwable) {
		super(throwable);
	}

}
