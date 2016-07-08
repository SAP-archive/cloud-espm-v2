/**
 * 
 */
package com.sap.espm.model.builder.pdf;

import java.util.List;
import java.util.Map;

import com.sap.espm.model.Customer;
import com.sap.espm.model.SalesOrderHeader;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.SalesOrderReportField;
import com.sap.espm.model.builder.ReportBuilder;
import com.sap.espm.model.exception.ReportGenerationException;
import com.sap.espm.model.util.ReflectionUtil;

/**
 * 
 * Function Import processor class for getting customer information
 * 
 */
public class CustomerSalesOrderBuilder implements ReportBuilder<SalesOrderItem, StringBuffer> {

	@Override
	public StringBuffer generateReportData(List<SalesOrderItem> soiList) {
		StringBuffer reportData = new StringBuffer();
		if (soiList != null && !soiList.isEmpty()) {
			try {
				
				SalesOrderHeader header = soiList.get(0).getSalesOrderHeader();
				
				if (header != null) {
					Customer customer = header.getCustomer();
					Map<String,String> reportMap =  ReflectionUtil.generateReportData(customer.getClass(), SalesOrderReportField.class, customer);
					reportData.append("Customer Data:");
					reportData.append("|");
					ReflectionUtil.processReportMap(reportMap, reportData);
					reportData.append("|");
					 
				}
			} catch (IllegalAccessException illegalAccessException) {
				throw new ReportGenerationException("Error Generating the Customer Data for the Sales Order Report");
			}

		}
		return reportData;

	}
	
	

}
