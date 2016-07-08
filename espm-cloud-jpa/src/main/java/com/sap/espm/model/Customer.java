package com.sap.espm.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ESPM_CUSTOMER")
public class Customer {

	/* Customer ids are generated within a number range starting with 1 */
	@TableGenerator(name = "CustomerGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Customer", initialValue = 100000000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CustomerGenerator")
	@Column(name = "CUSTOMER_ID", length = 10)
	private String customerId;

	@SalesOrderReportField
	@Column(name = "EMAIL_ADDRESS", unique = true)
	private String emailAddress;

	@Column(name = "PHONE_NUMBER", length = 30)
	private String phoneNumber;
	
	@SalesOrderReportField
	@Column(name = "FIRST_NAME", length = 40)
	private String firstName;

	@SalesOrderReportField
	@Column(name = "LAST_NAME", length = 40)
	private String lastName;

	@SalesOrderReportField
	@Column(name = "DATE_OF_BIRTH", nullable = false)
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;

	@SalesOrderReportField
	@Column(name = "CITY", length = 40)
	private String city;

	@SalesOrderReportField
	@Column(name = "POSTAL_CODE", length = 10)
	private String postalCode;
	@SalesOrderReportField
	@Column(name = "STREET", length = 60)
	private String street;

	@Column(name = "HOUSE_NUMBER", length = 10)
	private String houseNumber;

	@SalesOrderReportField
	@Column(name = "COUNTRY", length = 3)
	private String country;

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String param) {
		this.customerId = param;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String param) {
		this.emailAddress = param;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String param) {
		this.phoneNumber = param;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return this.houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}