package com.sap.espm.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.sap.espm.model.util.Utility;

@Entity
@Table(name = "ESPM_STOCK")
public class Stock {

	@Id
	@Column(name = "PRODUCT_ID")
	private String productId;
	@Column(precision = 13, scale = 3)
	private BigDecimal quantity;
	@Column(name = "MIN_STOCK", precision = 13, scale = 3)
	private BigDecimal minStock;
	@Column(name = "LOT_SIZE", precision = 13, scale = 3)
	private BigDecimal lotSize;
	@Column(name = "QUANTITY_LESS_MIN")
	private boolean quantityLessMin;

	@OneToOne
	private Product product;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String id) {
		this.productId = id;
	}

	public void setQuantity(BigDecimal param) {
		this.quantity = param;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setMinStock(BigDecimal param) {
		this.minStock = param;
	}

	public BigDecimal getMinStock() {
		return minStock;
	}

	public void setLotSize(BigDecimal param) {
		this.lotSize = param;
	}

	public BigDecimal getLotSize() {
		return lotSize;
	}

	public void setQuantityLessMin(boolean param) {
		this.quantityLessMin = param;
	}

	public boolean getQuantityLessMin() {
		return quantityLessMin;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}

	@PrePersist
	@PreUpdate
	private void persist() {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			if (this.quantity.compareTo(this.minStock) < 0) {
				this.quantityLessMin = true;
			} else {
				this.quantityLessMin = false;
			}

			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

}
