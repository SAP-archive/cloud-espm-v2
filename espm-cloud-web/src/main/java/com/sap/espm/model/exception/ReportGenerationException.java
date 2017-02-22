/**
 * 
 */
package com.sap.espm.model.exception;

/**
 * This Exception will be used for any error scenarios while generating a
 * customer sales order.
 *
 */
public class ReportGenerationException extends BaseESPMRunTimeException {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 4501161376995698587L;

	/**
	 * Constructor taking error message as input parameter.
	 * 
	 * @param errorMessage
	 *            - The error message.
	 */
	public ReportGenerationException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Constructor taking a {@link Throwable} as input parameter
	 * 
	 * @param throwable
	 *            - The {@link Throwable} to wrap.
	 */
	public ReportGenerationException(Throwable throwable) {
		super(throwable);
	}

}
