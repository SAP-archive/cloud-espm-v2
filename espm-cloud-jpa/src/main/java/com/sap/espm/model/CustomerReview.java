package com.sap.espm.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.sap.espm.model.util.Utility;

/**
 * Entity implementation class for entity CustomerReview
 */

@Entity
@Table(name = "ESPM_EXTENSION_CUSTOMER_REVIEW")
@NamedQuery(name = "CustomerReview.getAllCustomerReviews", query = "SELECT cr FROM CustomerReview cr")
public class CustomerReview implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "CUSTOMER_REVIEW_ID", length = 16)
	private String customerReviewId;

	@Column(name = "COMMENT", length = 1024)
	private String comment;

	@Column(name = "RATING")
	private int rating;
	
	@Column(name = "PRODUCT_ID", length = 10)
	private String productId;

	@Column(name = "DATE_OF_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationDate;

	@Column(name = "FIRST_NAME", length = 40)
	private String firstName;

	@Column(name = "LAST_NAME", length = 40)
	private String lastName;
	
	@Column(name = "AGE_GROUP", length = 40)
	private String ageGroup;

	@ManyToOne(optional=false)
	private Product product;
	
	public CustomerReview() {
		super();
	}

	public String getCustomerReviewId() {
		return this.customerReviewId;
	}

	public void setCustomerReviewId(String customerReviewId) {
		this.customerReviewId = customerReviewId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Calendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
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
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}
	
	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}
	
	private Customer getCustomer(EntityManager em) {
		TypedQuery<Customer> query = em.createQuery(
				"SELECT c FROM Customer c WHERE c.firstName = :firstName AND c.lastName = :lastName",
				Customer.class);
		try {
			return query.setParameter("firstName", this.getFirstName()).setParameter("lastName", this.getLastName())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@PrePersist
	private void persist() {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Customer customer = getCustomer(em);
			
			if (customer == null) {
				this.setAgeGroup("None");
			} else {
				String ageGroup = "";
				LocalDate now = LocalDate.now();
				int dobYear = (customer.getDateOfBirth()).get(Calendar.YEAR);
				int currentYear = now.getYear();
				int years = currentYear - dobYear;
				System.out.println("difference in years "+years);
				if(years < 30){
					ageGroup = "young";
				}else if(years >30 && years < 50){
					ageGroup = "middle-aged";
				}else{
					ageGroup = "old";
				}
				System.out.println("ageGroup "+ageGroup);
				this.setAgeGroup(ageGroup);
			}

			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

}
