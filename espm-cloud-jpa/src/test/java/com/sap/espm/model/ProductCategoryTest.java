package com.sap.espm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.sap.espm.model.data.DataLoader;
import com.sap.espm.model.util.TestFactory;

/**
 * JUnits for Product JPA Entity
 * 
 */
public class ProductCategoryTest extends AbstractTest {
	Product prod = null;

	/**
	 * Test if a single product can be added and checks if it exists via
	 * entitymanager.find
	 */
	@Test
	public void testExistingProductCategorySearchFind() {
		ProductCategory prodCatAct = null;
		String prodCategory = "LED";
		EntityManager em = emf.createEntityManager();
		TestFactory tf = new TestFactory();
		try {
			// Add Product Category
			assertTrue("Product category not created",
					tf.createProductCategory(em, prodCategory));
			em.getTransaction().begin();
			// Search for Product Category
			prodCatAct = em.find(ProductCategory.class, prodCategory);
			assertNotNull(
					"Search via find method: Added product category PCs not persisted in database",
					prodCatAct);
			if (prodCatAct != null) {
				assertEquals(
						"Added Product category not persisted in the database ",
						prodCategory, prodCatAct.getCategory());
				tf.deleteProductCategory(em, prodCategory);
			}

		} finally {
			em.close();
		}
	}

	/**
	 * Test if a single product category can be added and checks if it exists
	 * via TypedQuery-SetParamter
	 */
	@Test
	public void testExistingProductcategorySearchTyped() {
		ProductCategory prodCatResult = null;
		String prodCategory = "Phablets";
		EntityManager em = emf.createEntityManager();

		TestFactory tf = new TestFactory();
		try {
			// Add Product
			assertTrue("Product category not created",
					tf.createProductCategory(em, prodCategory));
			em.getTransaction().begin();

			// Search for Product category
			TypedQuery<ProductCategory> query = em
					.createQuery(
							"SELECT pc FROM ProductCategory pc WHERE pc.category=:category",
							ProductCategory.class);

			prodCatResult = query.setParameter("category", prodCategory)
					.getSingleResult();

			assertEquals(
					"Search via typed query for existing product category : Added Product category not persisted in the database",
					prodCategory, prodCatResult.getCategory());
		} catch (NoResultException ne) {
			assertNotNull(
					"Search via typed query for existing product category: Added Product category  not persisted in the database",
					prodCatResult);
		} finally {
			em.close();
		}
	}

	/**
	 * Test if product category gets updated in database
	 */
	@Test
	public void testUpdateProduct() {

		ProductCategory prodCatAct = null;
		ProductCategory prodCat = null;
		String prodCategory = "Tablets";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		if (!tf.createProductCategory(em, prodCategory)) {
			fail("Unable to create product category");
			return;
		}
		em.getTransaction().begin();
		try {
			// Find product category for update
			prodCat = em.find(ProductCategory.class, prodCategory);
			// Update product category
			prodCat.setCategoryName("Printers");
			em.persist(prodCat);
			em.getTransaction().commit();
			// Find product category after update.
			prodCatAct = em.find(ProductCategory.class, prodCategory);
			assertEquals(
					"Update product category: Product main category not updated in the database",
					"Printers", prodCatAct.getCategoryName());
		} finally {
			em.close();
		}
	}

	/**
	 * Test removing of a product category
	 */
	@Test
	public void testRemoveProduct() {
		ProductCategory prodCatRes = null;
		String prodCategory = "Joysticks";
		EntityManager em = emf.createEntityManager();
		TestFactory tf = new TestFactory();

		try {
			if (!tf.createProductCategory(em, prodCategory)) {
				fail("Unable to create product category");
				return;
			}
			em.getTransaction().begin();

			// Remove product category
			assertTrue("Product category not deleted",
					tf.deleteProductCategory(em, prodCategory));
			prodCatRes = em.find(ProductCategory.class, prodCategory);
			assertNull(
					"Search via find method for removed Product category: Removed product category with Category "
							+ prodCategory + " still exists ", prodCatRes);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple existing product categories added from
	 * ProductCategories.xml via Typed Queries
	 */
	@Test
	public void testExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		try {
			DataLoader dl = new DataLoader(emf);
			dl.loadProductCategories(null);
			// Search for mutiple Product Categories.
			TypedQuery<ProductCategory> query = em.createQuery(
					"SELECT pc FROM ProductCategory pc", ProductCategory.class);
			List<ProductCategory> result = query.getResultList();

			assertTrue(
					"Search for mutiple existing product categories: Multiple product categories not added",
					result.size() > 2);
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing product categories via Typed Queries
	 */
	@Test
	public void testNotExistingProductsSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<ProductCategory> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple Product Category.
			TypedQuery<ProductCategory> query = em
					.createQuery(
							"SELECT pc FROM ProductCategory pc WHERE pc.categoryName = :categoryName",
							ProductCategory.class);
			result = query.setParameter("categoryName", "Trackpad")
					.getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple products: Product exists in database",
					0, result.size());
		} finally {
			em.close();
		}
	}

}
