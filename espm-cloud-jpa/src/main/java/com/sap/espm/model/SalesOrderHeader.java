package com.sap.espm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ESPM_SALES_ORDER_HEADER")
public class SalesOrderHeader {

	/* Sales order ids are generated within a number range starting with 5 */
	@TableGenerator(name = "SalesOrderGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "SalesOrder", initialValue = 500000000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SalesOrderGenerator")
	@Column(name = "SALES_ORDER_ID", length = 10)
	private String salesOrderId;

	@Column(name = "CUSTOMER_ID", length = 10)
	private String customerId;

	@Column(name = "CURRENCY_CODE", length = 5)
	private String currencyCode = "EUR";

	@Column(name = "GROSS_AMOUNT", precision = 15, scale = 3)
	private BigDecimal grossAmount;

	@Column(name = "NET_AMOUNT", precision = 15, scale = 3)
	private BigDecimal netAmount;

	@Column(name = "TAX_AMOUNT", precision = 15, scale = 3)
	private BigDecimal taxAmount;

	@Column(name = "LIFE_CYCLE_STATUS", length = 1, nullable = false)
	private String lifeCycleStatus;

	@Column(name = "LIFE_CYCLE_STATUS_NAME", nullable = false)
	private String lifeCycleStatusName;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.DATE)
	private Calendar createdAt;

	@Column(name = "INVOICE_LINK", length = 200)
	private String invoiceLink;

	@OneToOne
	private Customer customer;

	@OneToMany(mappedBy = "salesOrderHeader", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SalesOrderItem> salesOrderItems;

	public SalesOrderHeader() {
		this.salesOrderItems = new ArrayList<SalesOrderItem>();
	}

	public String getSalesOrderId() {
		return this.salesOrderId;
	}

	public void setSalesOrderId(String param) {
		this.salesOrderId = param;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String param) {
		this.currencyCode = param;
	}

	public BigDecimal getGrossAmount() {
		return this.grossAmount;
	}

	public void setGrossAmount(BigDecimal param) {
		this.grossAmount = param;
	}

	public BigDecimal getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(BigDecimal param) {
		this.netAmount = param;
	}

	public BigDecimal getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(BigDecimal param) {
		this.taxAmount = param;
	}

	public List<SalesOrderItem> getItems() {
		return this.salesOrderItems;
	}

	public void setItems(List<SalesOrderItem> items) {
		this.salesOrderItems = items;
	}

	public void addItem(SalesOrderItem item) {
		this.salesOrderItems.add(item);
	}

	public void setLifeCycleStatus(String param) {
		this.lifeCycleStatus = param;
	}

	public String getLifeCycleStatus() {
		return this.lifeCycleStatus;
	}

	public void setLifeCycleStatusName(String param) {
		this.lifeCycleStatusName = param;
	}

	public String getLifeCycleStatusName() {
		return this.lifeCycleStatusName;
	}

	public void setCreatedAt(Calendar param) {
		this.createdAt = param;
	}

	public Calendar getCreatedAt() {
		return this.createdAt;
	}
	
	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public String getInvoiceLink() {
		return this.invoiceLink;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer param) {
		this.customer = param;
	}

	public List<SalesOrderItem> getSalesOrderItems() {
		return this.salesOrderItems;
	}

	public void setSalesOrderItems(List<SalesOrderItem> param) {
		this.salesOrderItems = param;
	}

	@PrePersist
	private void persist() {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		this.lifeCycleStatus = "N";
		this.lifeCycleStatusName = "New";
		int itemNumber = 10;
		this.netAmount = new BigDecimal("0.00");
		this.taxAmount = new BigDecimal("0.00");
		this.grossAmount = new BigDecimal("0.00");
		this.createdAt = cal;
		for (SalesOrderItem item : salesOrderItems) {
			item.setSalesOrderId(this.getSalesOrderId());
			item.setItemNumber(itemNumber);
			itemNumber += 10;
			item.persist();
			this.netAmount = this.netAmount.add(item.getNetAmount())
					.setScale(3);
			this.taxAmount = this.taxAmount.add(item.getTaxAmount())
					.setScale(3);
			this.grossAmount = this.grossAmount.add(item.getGrossAmount())
					.setScale(3);
		}
	}

}