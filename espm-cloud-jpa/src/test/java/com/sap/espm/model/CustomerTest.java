package com.sap.espm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.sap.espm.model.data.DataLoader;
import com.sap.espm.model.util.TestFactory;

/**
 * Junits for Business Partner JPA Entity
 * 
 */
public class CustomerTest extends AbstractTest {
	Customer bupa = null;

	/**
	 * Test if a single Business Partner can be added and checks if it exists
	 * via entitymanager.find.
	 */
	@Test
	public void testExistingCustomerSearchFind() {
		String bupaId = "99999";
		Customer bupaAct = null;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Business Partner
			assertTrue("Business Partner not created",
					tf.createCustomer(em, bupaId));
			// Search for Business Partner
			bupaAct = em.find(Customer.class, bupaId);
			assertNotNull("Search via find method: Added Business Partner "
					+ bupaId + " not persisted in database", bupaAct);
			if (bupaAct != null) {
				assertEquals(
						"Added Business Partner not persisted in the database ",
						bupaId, bupaAct.getCustomerId());
				tf.deleteCustomer(em, bupaId);
			}
		} finally {
			em.close();
		}

	}

	/**
	 * Test if a single Business Partner can be added and checks if it exists
	 * via TypedQuery-SetParamter
	 */
	@Test
	public void testExistingCustomerSearchTyped() {
		Customer bupaAct = null;
		String bupaId = "11111";
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Business Partner
			assertTrue("Business Partner not created",
					tf.createCustomer(em, bupaId));
			// Search for Business Partner
			TypedQuery<Customer> query = em.createQuery(
					"SELECT bp FROM Customer bp WHERE bp.customerId=:id",
					Customer.class);

			bupaAct = query.setParameter("id", bupaId).getSingleResult();
			assertEquals(
					"Search via typed query for existing Business Partner: Added Business Partner not persisted in the database",
					bupaId, bupaAct.getCustomerId());
			tf.deleteCustomer(em, bupaId);
		} finally {
			em.close();
		}

	}

	/**
	 * Test if BusinesPartner gets updated in database
	 */
	@Test
	public void testUpdateCustomer() {
		Customer bupaAct = null;
		String bupaId = "111110";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createCustomer(em, bupaId)) {
				fail("Unable to create Address");
				return;
			}
			em.getTransaction().begin();
			// Find Business Partner for update
			bupa = em.find(Customer.class, bupaId);
			// Update Business Partner
			bupa.setPhoneNumber("009140345689");
			em.persist(bupa);
			em.getTransaction().commit();
			// Find Business Partner after update
			bupaAct = em.find(Customer.class, bupaId);
			assertEquals(
					"Update Business Partner: Business Partner attribute phone number not updated in the database",
					"009140345689", bupaAct.getPhoneNumber());
			tf.deleteCustomer(em, bupaId);
		} finally {
			em.close();
		}

	}

	/**
	 * Test removing of a Business Partner.
	 */
	@Test
	public void testRemoveCustomer() {
		Customer bupaRes = null;
		String bupaId = "11111";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createCustomer(em, bupaId)) {
				fail("Unable to create Business Partner");
				return;
			}
			em.getTransaction().begin();
			// Remove Business Partner
			assertTrue("Business Partner not deleted",
					tf.deleteCustomer(em, bupaId));
			// Search for deleted business partner.
			bupaRes = em.find(Customer.class, bupaId);
			assertNull(
					"Search via find method for removed Customer: Removed Customer with ID 11111 still exists ",
					bupaRes);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple existing Business Partners added from
	 * Business_Partners.xml via Typed Queries
	 */
	@Test
	public void testExistingCustomersSearchTyped() {
		EntityManager em = emf.createEntityManager();
		try {
			DataLoader dl = new DataLoader(emf);
			dl.loadCustomers();
			// Search for mutiple Business Partners.
			TypedQuery<Customer> query = em.createQuery(
					"SELECT bp FROM Customer bp", Customer.class);
			List<Customer> result = query.getResultList();
			assertTrue(
					"Search for mutiple existing Business Partners: Multiple Business Partners not added",
					result.size() > 2);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing Business Partners via Typed Queries
	 */
	@Test
	public void testNotExistingCustomersSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<Customer> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple Customers.
			TypedQuery<Customer> query = em
					.createQuery(
							"SELECT bp FROM Customer bp WHERE bp.customerId = :customerId",
							Customer.class);
			result = query.setParameter("customerId", "104").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple Business Partners: Business Partners exists in database",
					0, result.size());
		} finally {
			em.close();
		}
	}

}
