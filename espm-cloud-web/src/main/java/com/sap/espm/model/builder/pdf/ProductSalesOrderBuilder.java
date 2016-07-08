/**
 * 
 */
package com.sap.espm.model.builder.pdf;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.sap.espm.model.Product;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.SalesOrderReportField;
import com.sap.espm.model.builder.ReportBuilder;
import com.sap.espm.model.exception.ReportGenerationException;
import com.sap.espm.model.util.ReflectionUtil;

/**
 * 
 * Function Import processor class for getting product information
 * 
 */
public class ProductSalesOrderBuilder implements ReportBuilder<SalesOrderItem, StringBuffer> {

	@Override
	public StringBuffer generateReportData(List<SalesOrderItem> soiList) {
		StringBuffer reportData=new StringBuffer();
		// get the Product Information from the SalesOrderItem.
		if (soiList != null && !soiList.isEmpty()) {
			try {
				reportData.append("Product Data:");
				reportData.append("|");
				for (SalesOrderItem salesOrderItem : soiList) {
					Product product = salesOrderItem.getProduct();
					Map<String,String> reportMap =  ReflectionUtil.generateReportData(product.getClass(), SalesOrderReportField.class, product);
					reportData.append("|");
					ReflectionUtil.processReportMap(reportMap, reportData);
					reportData.append("Quantity" + " : " + salesOrderItem.getQuantity());
					reportData.append("|");
				}
			} catch (IllegalAccessException illegalAccessException) {
				throw new ReportGenerationException("Error Generating the Product Data for the Sales Order Report");
			}

		}
		return reportData;
	}

	private String processProduct(Product product) throws IllegalArgumentException, IllegalAccessException {
		return ReflectionUtil.generateEntityData(product, SalesOrderReportField.class);

	}

}
