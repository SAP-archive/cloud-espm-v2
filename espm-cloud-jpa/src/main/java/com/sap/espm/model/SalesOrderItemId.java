package com.sap.espm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SalesOrderItemId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "SALES_ORDER_ITEM_ID", length = 10)
	private String salesOrderId;

	@Column(name = "ITEM_NUMBER")
	private int itemNumber;

	public SalesOrderItemId() {
	}

	public SalesOrderItemId(String salesOrderId, int itemNumber) {
		this.salesOrderId = salesOrderId;
		this.itemNumber = itemNumber;
	}

	public String getSalesOrderId() {
		return this.salesOrderId;
	}

	public void setSalesOrderId(String salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public int getItemNumber() {
		return this.itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public int hashCode() {
		return salesOrderId.hashCode() + itemNumber;
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof SalesOrderItemId)
				&& this.salesOrderId.equals(((SalesOrderItemId) obj)
						.getSalesOrderId()) && (this.itemNumber == ((SalesOrderItemId) obj)
					.getItemNumber()));
	}

}
