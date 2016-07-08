package com.sap.espm.model.function.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType.Type;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImportParameter;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.espm.model.CustomerReview;
import com.sap.espm.model.Product;
import com.sap.espm.model.util.Utility;

public class CustomerReviewProcessor {

	/**
	 * Function Import implementation for getting customer reviews created
	 * 
	 * @param productId productId of the reviewed product
	 * @param firstName firstname of the reviewer
	 * @param lastName lastname of the reviewer
	 * @param rating rating for the product
	 * @param creationDate date of creation of the review
	 * @param comment comments for the review
	 * @return customer entity.
	 * @throws ODataException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "CreateCustomerReview", entitySet = "CustomerReviews", returnType = @ReturnType(type = Type.ENTITY, isCollection = false))
	public CustomerReview createCustomerReview(
			@EdmFunctionImportParameter(name = "ProductId") String productId, @EdmFunctionImportParameter(name = "FirstName") String firstName, @EdmFunctionImportParameter(name = "LastName") String lastName, @EdmFunctionImportParameter(name = "Rating") String rating, @EdmFunctionImportParameter(name = "CreationDate") String creationDate, @EdmFunctionImportParameter(name = "Comment") String comment)
			throws ODataException, ParseException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		Product prod = null;
		CustomerReview customerReview = null;
		try {
				em.getTransaction().begin();
				prod = em.find(Product.class, productId);
			try {				
				customerReview = new CustomerReview();
				customerReview.setComment(comment);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(Long.parseLong(creationDate)));
				customerReview.setCreationDate(cal);
				customerReview.setFirstName(firstName);
				customerReview.setLastName(lastName);
				customerReview.setRating(Integer.parseInt(rating));
				customerReview.setProductId(productId);
				customerReview.setProduct(prod);
				em.persist(customerReview);
				prod.addReview(customerReview);
				em.getTransaction().commit();
				return customerReview;

			} catch (NoResultException e) {
				throw new ODataApplicationException(
						"Error creating customer review:" , Locale.ENGLISH,
						HttpStatusCodes.BAD_REQUEST);
			}
		} finally {
			em.close();
		}
	}
}

