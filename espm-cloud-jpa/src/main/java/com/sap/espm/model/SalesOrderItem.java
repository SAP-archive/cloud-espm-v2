package com.sap.espm.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ESPM_SALES_ORDER_ITEM")
public class SalesOrderItem {

	private static final BigDecimal TAX_AMOUNT_FACTOR = new BigDecimal("0.19");
	private static final BigDecimal GROSS_AMOUNT_FACTOR = new BigDecimal("1.19");

	@EmbeddedId
	private SalesOrderItemId id;

	@Column(name = "PRODUCT_ID", length = 10)
	private String productId;

	@Column(name = "CURRENCY_CODE", length = 5)
	private String currencyCode = "EUR";

	@Column(name = "GROSS_AMOUNT", precision = 15, scale = 3)
	private BigDecimal grossAmount;

	@Column(name = "NET_AMOUNT", precision = 15, scale = 3)
	private BigDecimal netAmount;

	@Column(name = "TAX_AMOUNT", precision = 15, scale = 3)
	private BigDecimal taxAmount;

	@Column(precision = 13, scale = 3)
	private BigDecimal quantity;

	@Column(name = "QUANTITY_UNIT", length = 3)
	private String quantityUnit = "EA";

	@Column(name = "DELIVERY_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar deliveryDate;

	@OneToOne
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "SALES_ORDER_ITEM_ID", referencedColumnName = "SALES_ORDER_ID", insertable = false, updatable = false)
	private SalesOrderHeader salesOrderHeader;

	public SalesOrderItem() {
		this.id = new SalesOrderItemId();
	}

	public SalesOrderItem(String salesOrderId, int itemNumber) {
		this.id = new SalesOrderItemId(salesOrderId, itemNumber);
	}

	public SalesOrderItemId getId() {
		return id;
	}

	public void setId(SalesOrderItemId id) {
		this.id = id;
	}

	public String getSalesOrderId() {
		return this.id.getSalesOrderId();
	}

	public void setSalesOrderId(String salesOrderId) {
		this.id.setSalesOrderId(salesOrderId);
	}

	public int getItemNumber() {
		return this.id.getItemNumber();
	}

	public void setItemNumber(int itemNumber) {
		this.id.setItemNumber(itemNumber);
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String param) {
		this.productId = param;
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

	public BigDecimal getQuantity() {
		return this.quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getQuantityUnit() {
		return this.quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public Calendar getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}

	public SalesOrderHeader getSalesOrderHeader() {
		return salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader param) {
		this.salesOrderHeader = param;
	}

	void persist() {
		BigDecimal price = Product.getPrice(this.getProductId());
		if (price == null) {
			return;
		}
		this.netAmount = price.multiply(this.getQuantity()).setScale(3);
		this.taxAmount = this.netAmount.multiply(TAX_AMOUNT_FACTOR).setScale(3);
		this.grossAmount = this.netAmount.multiply(GROSS_AMOUNT_FACTOR)
				.setScale(3);
	}

}