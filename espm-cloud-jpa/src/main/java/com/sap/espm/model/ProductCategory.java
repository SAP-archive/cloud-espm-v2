package com.sap.espm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESPM_PRODUCT_CATEGORY")
public class ProductCategory {

	@Id
	@Column(length = 40)
	private String category;

	@Column(name = "CATEGORY_NAME", length = 40)
	private String categoryName;

	@Column(name = "MAIN_CATEGORY", length = 40)
	private String mainCategory;

	@Column(name = "MAIN_CATEGORY_NAME", length = 40)
	private String mainCategoryName;

	@Column(name = "NUMBER_OF_PRODUCTS")
	long numberOfProducts;

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String param) {
		this.category = param;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getMainCategoryName() {
		return mainCategoryName;
	}

	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}

	public long getNumberOfProducts() {
		return this.numberOfProducts;
	}

	public void setNumberOfProducts(long numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}

}