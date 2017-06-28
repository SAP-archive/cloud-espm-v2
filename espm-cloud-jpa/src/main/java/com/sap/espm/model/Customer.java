package com.sap.espm.model;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ESPM_CUSTOMER")
@NamedQueries({
	@NamedQuery(name = "Customer.getCustomerByEmailAddress", query = "SELECT c FROM Customer c WHERE c.emailAddress = :emailAddress"),
	@NamedQuery(name = "Customer.getCustomerByCustomerId", query = "SELECT cid FROM Customer cid WHERE cid.customerId= :customerId"),
	@NamedQuery(name = "Customer.getAllCustomers", query = "SELECT c FROM Customer c")
})
public class Customer {

	/* Customer ids are generated within a number range starting with 1 */
	@TableGenerator(name = "CustomerGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Customer", initialValue = 100000000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CustomerGenerator")
	@Column(name = "CUSTOMER_ID", length = 10)
	private String customerId;

	@Column(name = "EMAIL_ADDRESS", unique = true)
	private String emailAddress;

	@Column(name = "PHONE_NUMBER", length = 30)
	private String phoneNumber;

	@Column(name = "FIRST_NAME", length = 40)
	private String firstName;

	@Column(name = "LAST_NAME", length = 40)
	private String lastName;

	@Column(name = "DATE_OF_BIRTH", nullable = false)
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;

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
	
	@Column(name = "TWITTER_ID", unique=true)
	private String twitterid;
	
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

	public String getTwitterid() {
		return twitterid;
	}

	public void setTwitterid(String twitterid) {
		this.twitterid = twitterid;
	}
	
	public Map<String, String> getCustomerReportData() {

		Map<String, String> customerMapData = new LinkedHashMap<String, String>(7);
		customerMapData.put("firstName", firstName);
		customerMapData.put("lastName", lastName);
		customerMapData.put("emailAddress", emailAddress);
		customerMapData.put("phoneNumber", phoneNumber);
		customerMapData.put("houseNumber", houseNumber);
		customerMapData.put("city", city);
		customerMapData.put("street", street);
		customerMapData.put("country", country);
		return customerMapData;

	}
}