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

import com.sap.espm.model.util.TestFactory;

/**
 * JUnits for SalesOrderHeader JPA entity.
 * 
 */
public class SalesOrderHeaderTest extends AbstractTest {
	SalesOrderHeader soHeader = null;

	/**
	 * Test if a single Sales Order Header can be added and checks if it exists
	 * via entitymanager.find.
	 */
	@Test
	public void testExisitingSalesOrderHeaderSearchFind() {
		String soHeadId = "99999";
		EntityManager em = emf.createEntityManager();
		TestFactory tf = new TestFactory();
		SalesOrderHeader soHeaderAct = new SalesOrderHeader();
		try {
			// Add Sales Order Header
			assertTrue("Sales Order Header not created",
					tf.createSalesOrderHeader(em, soHeadId));

			// Search Sales Order Header
			soHeaderAct = em.find(SalesOrderHeader.class, soHeadId);

			assertNotNull(
					"Search via find method: Added Sales Order Header not persisted in database",
					soHeaderAct);
			if (soHeaderAct != null) {
				assertEquals(
						"Added Sales Order Header Currency Code not persisted in the database ",
						"INR", soHeaderAct.getCurrencyCode());
				tf.deleteSalesOrderHeader(em, soHeadId);
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Tests for a existing single Sales Order Header via TypedQuery-SetParamter
	 */
	@Test
	public void testExisitingSalesOrderHeaderSearchTyped() {
		EntityManager em = emf.createEntityManager();
		String soHeadId = "11111";
		TestFactory tf = new TestFactory();
		SalesOrderHeader soHeaderAct = null;
		try {
			// Add Sales Order Header
			assertTrue("Sales Order Header not created",
					tf.createSalesOrderHeader(em, soHeadId));
			// Search for Sales Order Header.
			TypedQuery<SalesOrderHeader> query = em
					.createQuery(
							"SELECT so FROM SalesOrderHeader so WHERE so.salesOrderId=:id",
							SalesOrderHeader.class);

			soHeaderAct = query.setParameter("id", soHeadId).getSingleResult();

			assertEquals(
					"Search via typed query for existing sales order header: Added sales order header not persisted in the database",
					soHeadId, soHeaderAct.getSalesOrderId());
			tf.deleteSalesOrderHeader(em, soHeadId);
		} finally {
			em.close();
		}
	}

	/**
	 * Test removing of a Sales Order Header.
	 */
	@Test
	public void testRemoveSalesOrderHeader() {
		SalesOrderHeader soHeaderAct = null;
		String soHeadId = "111112";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createSalesOrderHeader(em, soHeadId)) {
				fail("Unable to create Sales Order Header");
				return;
			}
			em.getTransaction().begin();
			// Remove Sales Order Header.
			assertTrue("Sales Order Header not deleted",
					tf.deleteSalesOrderHeader(em, soHeadId));
			// Check for deleted Sales Order Header
			soHeaderAct = em.find(SalesOrderHeader.class, soHeadId);
			assertNull(
					"Search via find method for removed Sales Order Header: Removed Sales Order Header with ID "
							+ soHeadId + " still exists ", soHeaderAct);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing Sales Order Header via Typed Queries
	 */
	@Test
	public void testNotExistingSaleOrderHeadersSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<SalesOrderHeader> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple SO Headers.
			TypedQuery<SalesOrderHeader> query = em
					.createQuery(
							"SELECT so FROM SalesOrderHeader so WHERE so.customerId = :customerId",
							SalesOrderHeader.class);
			result = query.setParameter("customerId", "98765").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple Sales Order Headers: Sales Order Headers exists in database",
					0, result.size());
		} finally {
			em.close();
		}

	}

}
