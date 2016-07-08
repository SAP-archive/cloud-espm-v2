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
 * JUnits for Product JPA Entity
 * 
 */
public class ProductTest extends AbstractTest {
	Product prod = null;

	/**
	 * Test if a single product can be added and checks if it exists via
	 * entitymanager.find
	 */
	@Test
	public void testExistingProductSearchFind() {
		Product prodAct = null;
		String prodId = "HT-9000";
		EntityManager em = emf.createEntityManager();
		TestFactory tf = new TestFactory();
		try {
			// Add Product
			assertTrue("Product not created", tf.createProduct(em, prodId));
			em.getTransaction().begin();
			// Search for Product
			prodAct = em.find(Product.class, prodId);
			assertNotNull(
					"Search via find method: Added product HT-9000 not persisted in database",
					prodAct);
			if (prodAct != null) {
				assertEquals("Added Product not persisted in the database ",
						"HT-9000", prodAct.getProductId());
				tf.deleteProduct(em, prodId);
			}

		} finally {
			em.close();
		}
	}

	/**
	 * Test if a single product can be added and checks if it exists via
	 * TypedQuery-SetParamter
	 */
	@Test
	public void testExistingProductSearchTyped() {
		Product prodResult = null;
		String prodId = "HT-9001";
		EntityManager em = emf.createEntityManager();

		TestFactory tf = new TestFactory();
		try {
			// Add Product
			assertTrue("Product not created", tf.createProduct(em, prodId));
			em.getTransaction().begin();

			// Search for Product
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE p.productId=:id",
					Product.class);

			prodResult = query.setParameter("id", prodId).getSingleResult();

			assertEquals(
					"Search via typed query for existing product: Added Product not persisted in the database",
					prodId, prodResult.getProductId());
		} finally {
			em.close();
		}
	}

	/**
	 * Test if product category gets updated in database
	 */
	@Test
	public void testUpdateProduct() {

		Product prodAct = null;
		String prodId = "HT-9002";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		if (!tf.createProduct(em, prodId)) {
			fail("Unable to create product");
			return;
		}
		em.getTransaction().begin();
		try {
			// Find product for update
			prod = em.find(Product.class, prodId);
			// Update product
			prod.setCategory("Tablet");
			em.persist(prod);
			em.getTransaction().commit();
			// Find product after update.
			prodAct = em.find(Product.class, prodId);
			assertEquals(
					"Update product: Product category not updated in the database",
					"Tablet", prodAct.getCategory());
		} finally {
			em.close();
		}
	}

	/**
	 * Test removing of a product
	 */
	@Test
	public void testRemoveProduct() {
		Product prodRes = null;
		String prodId = "HT-9003";
		EntityManager em = emf.createEntityManager();
		TestFactory tf = new TestFactory();

		try {
			if (!tf.createProduct(em, prodId)) {
				fail("Unable to create product");
				return;
			}
			em.getTransaction().begin();

			// Remove product
			assertTrue("Product not deleted", tf.deleteProduct(em, prodId));
			prodRes = em.find(Product.class, prodId);
			assertNull(
					"Search via find method for removed Product: Removed product with ID "
							+ prodId + " still exists ", prodRes);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple existing products added from Products.xml via Typed
	 * Queries
	 */
	@Test
	public void testExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		try {
			DataLoader dl = new DataLoader(emf);
			dl.loadProducts(null);
			// Search for mutiple Products.
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p", Product.class);
			List<Product> result = query.getResultList();

			assertTrue(
					"Search for mutiple existing products: Multiple products not added",
					result.size() > 2);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing products via Typed Queries
	 */
	@Test
	public void testNotExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<Product> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple Products.
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE p.category = :category",
					Product.class);
			result = query.setParameter("category", "Trackpad").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple products: Product exists in database",
					0, result.size());
		} finally {
			em.close();
		}
	}
}
