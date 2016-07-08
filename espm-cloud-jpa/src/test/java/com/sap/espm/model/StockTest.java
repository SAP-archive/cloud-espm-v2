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

import com.sap.espm.model.data.DataLoader;
import com.sap.espm.model.util.TestFactory;

/**
 * Junits for Stock JPA Entity
 * 
 */

public class StockTest extends AbstractTest {
	Stock stock = null;

	/**
	 * Test if a single Stock can be added and checks if it exists via
	 * entitymanager.find.
	 */
	@Test
	public void testExistingStockSearchFind() {
		String productId = "HY-1000";
		Stock productAct = null;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Stock
			assertTrue("Stock not created", tf.createStock(em, productId));
			// Search for Stock
			productAct = em.find(Stock.class, productId);
			assertNotNull("Search via find method: Added Stock " + productId
					+ " not persisted in database", productAct);
			if (productAct != null) {
				assertEquals("Added Stock not persisted in the database ",
						productId, productAct.getProductId());
				tf.deleteStock(em, productId);
			}

		} finally {
			em.close();
		}

	}

	/**
	 * Test if a single Stock can be added and checks if it exists via
	 * TypedQuery-SetParamter
	 */
	@Test
	public void testExistingStockSearchTyped() {
		Stock productAct = null;
		String productId = "HY-1001";
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Stock
			assertTrue("Stock not created", tf.createStock(em, productId));
			// Search for Stock
			TypedQuery<Stock> query = em.createQuery(
					"SELECT s FROM Stock s WHERE s.productId=:id", Stock.class);

			productAct = query.setParameter("id", productId).getSingleResult();
			assertEquals(
					"Search via typed query for existing Stock: Added Stock is not persisted in the database",
					productId, productAct.getProductId());
			tf.deleteStock(em, productId);
		} finally {
			em.close();
		}

	}

	/**
	 * Test if Stock gets updated in database
	 */
	@Test
	public void testUpdateStock() {
		Stock stock = null;
		String productId = "HY-1004";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createStock(em, productId)) {
				fail("Unable to create "); // doubt
				return;
			}
			em.getTransaction().begin();
			// Find Stock for update
			stock = em.find(Stock.class, productId);
			// Update Business Partner
			stock.setQuantity(BigDecimal.valueOf(10));
			em.persist(stock);
			em.getTransaction().commit();
			// Find Business Partner after update
			stock = em.find(Stock.class, productId);
			assertEquals(
					"Update Stock: Stock attribute quantity not updated in the database",
					BigDecimal.valueOf(10), stock.getQuantity());
			tf.deleteStock(em, productId);
		} finally {
			em.close();
		}

	}

	/**
	 * Test removing of a Business Partner.
	 */
	@Test
	public void testRemoveStock() {
		Stock stockRes = null;
		String productId = "HY-1000";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createStock(em, productId)) {
				fail("Unable to create Stock");
				return;
			}
			em.getTransaction().begin();
			// Remove Business Partner
			assertTrue("Stock not deleted", tf.deleteStock(em, productId));
			// Search for deleted business partner.
			stockRes = em.find(Stock.class, productId);
			assertNull(
					"Search via find method for removed Stock: Removed Stock with Product HT-1000 still exists ",
					stockRes);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple existing stocks generated via algorithm via Typed
	 * Queries
	 */
	@Test
	public void testExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		try {
			DataLoader dl = new DataLoader(emf);
			List<Product> products = dl.loadProducts(null);
			dl.loadStock(products);
			// Search for mutiple Stocks.
			TypedQuery<Stock> query = em.createQuery("SELECT st FROM Stock st",
					Stock.class);
			List<Stock> result = query.getResultList();

			assertTrue(
					"Search for mutiple existing stocks: Multiple stocks not added",
					result.size() > 2);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing stocks via Typed Queries
	 */
	@Test
	public void testNotExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<Stock> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple stocks.
			TypedQuery<Stock> query = em.createQuery(
					"SELECT st FROM Stock st WHERE st.productId = :productId",
					Stock.class);
			result = query.setParameter("productId", "HZ-1000").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple stocks: Stocks exists in database",
					0, result.size());
		} finally {
			em.close();
		}
	}

}

