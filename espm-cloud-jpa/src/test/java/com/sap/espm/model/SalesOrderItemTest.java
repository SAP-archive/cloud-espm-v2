package com.sap.espm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.sap.espm.model.util.TestFactory;

/**
 * JUnits for Sales Order Item JPA Entity
 * 
 */
public class SalesOrderItemTest extends AbstractTest {
	SalesOrderItem soItem = null;

	/**
	 * Test if a single Sales Order Item can be added and checks if it exists
	 * via entitymanager.find.
	 */
	@Test
	public void testExisitingSalesOrderItemSearchFind() {
		EntityManager em = emf.createEntityManager();
		String soId = "99999";
		TestFactory tf = new TestFactory();
		SalesOrderItem soItemAct = null;
		try {
			// Add Sales Order Item
			assertTrue("Sales order item not created",
					tf.createSalesOrderItem(em, soId));
			// Search Sales Order Item.
			soItemAct = em.find(SalesOrderItem.class, new SalesOrderItemId(
					soId, 1));

			assertNotNull(
					"Search via find method: Added Sales Order Item not persisted in database",
					soItemAct);
			if (soItemAct != null) {
				assertEquals(
						"Added Sales Order Item Currency Code not persisted in the database ",
						"INR", soItemAct.getCurrencyCode());
				tf.deleteSalesOrderItem(em, soId);
			}

		} finally {
			em.close();
		}
	}

	/**
	 * Tests for a existing single Sales Order Item via TypedQuery-SetParamter
	 */
	@Test
	public void testExisitingSalesOrderItemSearchTyped() {
		EntityManager em = emf.createEntityManager();
		String soId = "11111";
		TestFactory tf = new TestFactory();
		SalesOrderItem soItemAct = null;
		try {
			// Add Sales Order Item
			assertTrue("Sales Order Item", tf.createSalesOrderItem(em, soId));
			// Search for Sales Order Item
			TypedQuery<SalesOrderItem> query = em
					.createQuery(
							"SELECT soi FROM SalesOrderItem soi WHERE soi.id.salesOrderId=:id",
							SalesOrderItem.class);

			soItemAct = query.setParameter("id", soId).getSingleResult();

			assertEquals(
					"Search via typed query for existing sales order item: Added sales order item Gross Amount not persisted in the database",
					BigDecimal.valueOf(13224), soItemAct.getGrossAmount());
			tf.deleteSalesOrderItem(em, soId);
		} finally {
			em.close();
		}
	}

	/**
	 * Test if SalesOrderItem gets updated in database.
	 */
	@Test
	public void testUpdateSalesOrderItem() {
		SalesOrderItem soItemAct = null;
		String soId = "111110";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createSalesOrderItem(em, soId)) {
				fail("Unable to create Sales Order Item");
				return;
			}
			em.getTransaction().begin();
			// Find Sales Order Item for update.
			soItem = em.find(SalesOrderItem.class,
					new SalesOrderItemId(soId, 1));
			// Update Sales Order Item.
			soItem.setCurrencyCode("EUR");
			em.persist(soItem);
			em.getTransaction().commit();
			// Find Sales Order Item after update.
			soItemAct = em.find(SalesOrderItem.class, new SalesOrderItemId(
					soId, 1));
			assertEquals(
					"Update Sales Orde Item: Sales Order Item attribute Sales Order Item Position not updated in the database",
					"EUR", soItemAct.getCurrencyCode());
			tf.deleteSalesOrderItem(em, soId);
		} finally {
			em.close();
		}
	}

	/**
	 * Test if Sales Order Item can be removed from database.
	 */
	@Test
	public void testRemoveSalesOrderItem() {
		SalesOrderItem soItemAct = null;
		String soId = "11112";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createSalesOrderItem(em, soId)) {
				fail("Unable to create Sales Order Item");
				return;
			}
			em.getTransaction().begin();
			// Remove Schedule Line
			assertTrue("Sales Order Item not deleted",
					tf.deleteSalesOrderItem(em, soId));
			// Check for deleted Sales Order Item
			soItemAct = em.find(SalesOrderItem.class, new SalesOrderItemId(
					soId, 1));
			assertNull(
					"Search via find method for removed Sales Order Item: Removed Sales Order Item with ID "
							+ soId + " still exists ", soItemAct);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing Sales Order Items via Typed Queries
	 */
	@Test
	public void testNotExistingSalesOrderItemsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<SalesOrderItem> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple SO Item.
			TypedQuery<SalesOrderItem> query = em
					.createQuery(
							"SELECT soi FROM SalesOrderItem soi WHERE soi.currencyCode = :currencyCode",
							SalesOrderItem.class);
			result = query.setParameter("currencyCode", "INV").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple Sales Order Items: Sales Order Items exists in database",
					0, result.size());
		} finally {
			em.close();
		}
	}
}
