package com.sap.espm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import com.sap.espm.model.util.Utility;

@Entity
@Table(name = "ESPM_PRODUCT")
public class Product {

	transient private static Map<String, BigDecimal> prices = null;

	@Id
	@Column(name = "PRODUCT_ID", length = 10)
	private String productId;

	private String name;

	@SalesOrderReportField
	@Column(name = "SHORT_DESCRIPTION")
	private String shortDescription;

	@Column(name = "LONG_DESCRIPTION")
	private String longDescription;

	@Column(length = 40)
	private String category;

	@Column(name = "CATEGORY_NAME", length = 40)
	private String categoryName;

	@Column(name = "QUANTITY_UNIT", length = 3)
	private String quantityUnit;

	@Column(name = "WEIGHT", precision = 13, scale = 3)
	private BigDecimal weight;

	@Column(name = "WEIGHT_UNIT", length = 3)
	private String weightUnit;

	@SalesOrderReportField
	@Column(precision = 23, scale = 3)
	private BigDecimal price;

	@Column(name = "CURRENCY_CODE", length = 5)
	private String currencyCode = "EUR";

	@Column(name = "DIMENSION_WIDTH", precision = 13, scale = 4)
	private BigDecimal dimensionWidth;

	@Column(name = "DIMENSION_DEPTH", precision = 13, scale = 4)
	private BigDecimal dimensionDepth;

	@Column(name = "DIMENSION_HEIGHT", precision = 13, scale = 4)
	private BigDecimal dimensionHeight;

	@Column(name = "DIMENSION_UNIT", length = 3)
	private String dimensionUnit;

	@Column(name = "PICTURE_URL")
	private String pictureUrl;

	@Column(name = "SUPPLIER_ID", length = 10)
	private String supplierId;

	@OneToOne
	private Supplier supplier;
	
	@OneToMany(mappedBy="product",targetEntity=CustomerReview.class,fetch=FetchType.EAGER)
	private List<CustomerReview> reviews;   

	public Product() {
		this.reviews = new ArrayList<CustomerReview>();
	}
	
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String param) {
		this.productId = param;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return this.shortDescription;
	}

	public void setShortDescription(String description) {
		this.shortDescription = description;
	}

	public String getLongDescription() {
		return this.longDescription;
	}

	public void setLongDescription(String description) {
		this.longDescription = description;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String param) {
		this.category = param;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getQuantityUnit() {
		return this.quantityUnit;
	}

	public void setQuantityUnit(String param) {
		this.quantityUnit = param;
	}

	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal param) {
		this.weight = param;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String param) {
		this.weightUnit = param;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal param) {
		this.price = param;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String param) {
		this.currencyCode = param;
	}

	public BigDecimal getDimensionWidth() {
		return this.dimensionWidth;
	}

	public void setDimensionWidth(BigDecimal param) {
		this.dimensionWidth = param;
	}

	public BigDecimal getDimensionDepth() {
		return this.dimensionDepth;
	}

	public void setDimensionDepth(BigDecimal param) {
		this.dimensionDepth = param;
	}

	public BigDecimal getDimensionHeight() {
		return this.dimensionHeight;
	}

	public void setDimensionHeight(BigDecimal param) {
		this.dimensionHeight = param;
	}

	public String getDimensionUnit() {
		return this.dimensionUnit;
	}

	public void setDimensionUnit(String param) {
		this.dimensionUnit = param;
	}

	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(String param) {
		this.pictureUrl = param;
	}

	public void setSupplierId(String param) {
		this.supplierId = param;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier param) {
		this.supplier = param;
	}
	
	public void addReview(CustomerReview review){
		this.reviews.add(review);
	}
	
	public List<CustomerReview> getReviews(){
		return this.reviews;
	}

	static BigDecimal getPrice(String productId) {
		if (prices == null) {
			updatePrices();
		}
		return prices.get(productId);
	}

	private static void updatePrices() {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			List<Product> products = em.createQuery("SELECT p FROM Product p",
					Product.class).getResultList();
			prices = new HashMap<String, BigDecimal>();
			for (Product p : products) {
				prices.put(p.getProductId(), p.getPrice());
			}
		} finally {
			em.close();
		}
	}

	private static void invalidatePrices() {
		if (prices != null) {
			prices.clear();
		}
		prices = null;
	}

	@PostLoad
	private void postLoad() {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			ProductText productText = getProductText(em);
			if (productText != null) {
				this.name = productText.getName();
				this.shortDescription = productText.getShortDescription();
				this.longDescription = productText.getLongDescription();
			} else {
				this.name = "";
				this.shortDescription = "";
				this.longDescription = "";
			}
		} finally {
			em.close();
		}
	}

	@PrePersist
	@PreUpdate
	private void persist() {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			ProductText productText = getProductText(em);
			if (productText == null) {
				// INSERT
				productText = new ProductText();
				productText.setProductId(this.getProductId());
				productText.setLanguage("EN");
				productText.setName(this.name);
				productText.setShortDescription(this.shortDescription);
				productText.setLongDescription(this.longDescription);
				em.persist(productText);
			} else {
				// UPDATE
				productText.setName(this.name);
				productText.setShortDescription(this.shortDescription);
				productText.setLongDescription(this.longDescription);
			}

			em.getTransaction().commit();
		} finally {
			// ProductCategory.invalidateNumbers();
			invalidatePrices();
			em.close();
		}
	}

	private ProductText getProductText(EntityManager em) {
		TypedQuery<ProductText> query = em
				.createQuery(
						"SELECT p FROM ProductText p WHERE p.productId = :productId AND p.language = :language",
						ProductText.class);
		try {
			return query.setParameter("productId", this.getProductId())
					.setParameter("language", "EN").getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}