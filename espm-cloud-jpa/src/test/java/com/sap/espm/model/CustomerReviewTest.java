package com.sap.espm.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.sap.espm.model.util.TestFactory;

/**
 * JUnit test for CustomerReview JPA entity
 * 
 */
public class CustomerReviewTest extends AbstractTest {

	/**
	 * Test if a single customer review can be added and check if added customer
	 * review exists
	 */
	@Test
	public void testAddedCustomerReviewExists() {
		CustomerReview customerReview = null;
		String customerReviewId = "0000000001";

		EntityManager em = emf.createEntityManager();
		TestFactory testFactory = new TestFactory();
		try {
			// add customer review
			assertTrue("Customer review creation failed",
					testFactory.createCustomerReview(em, customerReviewId));

			em.getTransaction().begin();
			// search for customer review
			customerReview = em.find(CustomerReview.class, customerReviewId);
			assertNotNull(
					"Search for customer review failed: no entity with customerReviewID '0000000001' was found in database",
					customerReview);
			testFactory.deleteCustomerReview(em, customerReviewId);
		} finally {
			em.close();
		}
	}
}
