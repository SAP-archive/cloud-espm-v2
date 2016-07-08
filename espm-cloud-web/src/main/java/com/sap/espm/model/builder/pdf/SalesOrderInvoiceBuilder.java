package com.sap.espm.model.builder.pdf;

import java.util.List;

import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.builder.GenericInvoiceBuilder;
import com.sap.espm.model.builder.ReportBuilder;
/**
 * 
 * Function Import processor class for building Sales Order Invoice
 * 
 */
public class SalesOrderInvoiceBuilder extends
		GenericInvoiceBuilder<SalesOrderItem, StringBuffer> {

	public SalesOrderInvoiceBuilder(
			List<ReportBuilder<SalesOrderItem, StringBuffer>> builderList) {
		this.reportBuilders = builderList;
	}

	@Override
	public StringBuffer generateInvoice(List<SalesOrderItem> inputList) {
		StringBuffer reportBuffer = new StringBuffer();
		if (inputList != null && !inputList.isEmpty() && reportBuilders != null
				&& !reportBuilders.isEmpty()) {
			for (ReportBuilder<SalesOrderItem, StringBuffer> reportBuilder : reportBuilders) {
				reportBuffer
						.append(reportBuilder.generateReportData(inputList));
				reportBuffer.append("|");

			}
		}
		return reportBuffer;
	}

}
