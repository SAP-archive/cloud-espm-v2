package com.sap.espm.model.function.impl;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType.Type;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImportParameter;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.espm.model.Customer;
import com.sap.espm.model.util.Utility;

/**
 * 
 * This is a custom Apache Olingo Function Import. For more reference
 * information regarding a Function Import, refer to the official Olingo
 * documentation:
 * <p>
 * https://olingo.apache.org/doc/odata2/tutorials/jpafunctionimport.html
 * <p>
 * http://olingo.apache.org/doc/odata2/ 
 * <p>
 * This class is used to define custom OData functions for {@link Customer}
 * entity.
 * 
 * 
 */
public class CustomerProcessor {

	/**
	 * Function Import implementation for getting customer by email address
	 * 
	 * @param emailAddress
	 *            email address of the customer
	 * @return customer entity.
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetCustomerByEmailAddress", entitySet = "Customers", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<Customer> getCustomerByEmailAddress(
			@EdmFunctionImportParameter(name = "EmailAddress") String emailAddress) throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<Customer> custList = null;
		try {

			Query query = em.createQuery("SELECT c FROM Customer c WHERE c.emailAddress = :emailAddress");
			query.setParameter("emailAddress", emailAddress);

			try {

				custList = query.getResultList();
				return custList;

			} catch (NoResultException e) {
				throw new ODataApplicationException("No matching customer with Email Address:" + emailAddress,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST, e);
			}
		} finally {
			em.close();
		}
	}
	/**
	 * Function Import implementation for getting customer by Twitter ID
	 * 
	 * @param twitter id
	 *            twitter id of the customer
	 * @return customer entity.
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetCustomerByTwitterId", entitySet = "Customers", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<Customer> getCustomerByTwitterId(
			@EdmFunctionImportParameter(name = "twitterid") String twitterid)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<Customer> custList = null;
		try {

			Query query = em
					.createQuery("SELECT c FROM Customer c WHERE c.twitterid ='"
							+ twitterid + "'");

			try {

				custList = query.getResultList();
				return custList;

			} catch (NoResultException e) {
				throw new ODataApplicationException(
						"No matching customer with twitter ID:"
								+ twitterid, Locale.ENGLISH,
						HttpStatusCodes.BAD_REQUEST, e);
			}
		} finally {
			em.close();
		}
	}	
	
}
