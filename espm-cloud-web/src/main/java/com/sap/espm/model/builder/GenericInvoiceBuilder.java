package com.sap.espm.model.builder;

import java.util.List;
/**
 * 
 */
public abstract class GenericInvoiceBuilder<INPUT, RETURNTYPE> {
	
	protected List<ReportBuilder<INPUT, RETURNTYPE>> reportBuilders;

	public abstract RETURNTYPE generateInvoice(List<INPUT> inputList);

}
