package com.sap.espm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ESPM_SUPPLIER")
public class Supplier {
	/* Supplier ids are generated within a number range starting with 2 */
	@TableGenerator(name = "SupplierGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Customer", initialValue = 100000000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SupplierGenerator")
	@Column(name = "SUPPLIER_ID", length = 10)
	private String supplierId;

	@Column(name = "EMAIL_ADDRESS", unique = true)
	private String emailAddress;

	@Column(name = "PHONE_NUMBER", length = 30)
	private String phoneNumber;

	@Column(name = "CITY", length = 40)
	private String city;

	@Column(name = "POSTAL_CODE", length = 10)
	private String postalCode;

	@Column(name = "STREET", length = 60)
	private String street;

	@Column(name = "HOUSE_NUMBER", length = 10)
	private String houseNumber;

	@Column(name = "COUNTRY", length = 3)
	private String country;

	@Column(name = "SUPPLIER_NAME", length = 80)
	private String supplierName;

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String id) {
		this.supplierId = id;
	}

	public void setEmailAddress(String param) {
		this.emailAddress = param;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setPhoneNumber(String param) {
		this.phoneNumber = param;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setCity(String param) {
		this.city = param;
	}

	public String getCity() {
		return city;
	}

	public void setPostalCode(String param) {
		this.postalCode = param;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setStreet(String param) {
		this.street = param;
	}

	public String getStreet() {
		return street;
	}

	public void setHouseNumber(String param) {
		this.houseNumber = param;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setCountry(String param) {
		this.country = param;
	}

	public String getCountry() {
		return country;
	}

	public void setSupplierName(String param) {
		this.supplierName = param;
	}

	public String getSupplierName() {
		return supplierName;
	}

}