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
 * Junits for Supplier JPA Entity
 * 
 */
public class SupplierTest extends AbstractTest {
	Supplier supplier = null;

	/**
	 * Test if a single Business Partner can be added and checks if it exists
	 * via entitymanager.find.
	 */
	@Test
	public void testExistingSupplierSearchFind() {
		String supplierId = "99999";
		Supplier supplierAct = null;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Supplier
			assertTrue("Supplier not created",
					tf.createSupplier(em, supplierId));
			// Search for Supplier
			supplierAct = em.find(Supplier.class, supplierId);
			assertNotNull("Search via find method: Added Supplier "
					+ supplierId + " not persisted in database", supplierAct);
			if (supplierAct != null) {
				assertEquals("Added Supplier not persisted in the database ",
						supplierId, supplierAct.getSupplierId());
				tf.deleteSupplier(em, supplierId);
			}

		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}

	}

	/**
	 * Test if a single Supplier can be added and checks if it exists via
	 * TypedQuery-SetParamter
	 */
	@Test
	public void testExistingSupplierSearchTyped() {
		Supplier supplierAct = null;
		String supplierId = "11111";
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		TestFactory tf = new TestFactory();
		try {
			// Add Supplier
			assertTrue("Supplier not created",
					tf.createSupplier(em, supplierId));
			// Search for Supplier
			TypedQuery<Supplier> query = em.createQuery(
					"SELECT s FROM Supplier s WHERE s.supplierId=:id",
					Supplier.class);

			supplierAct = query.setParameter("id", supplierId)
					.getSingleResult();
			assertEquals(
					"Search via typed query for existing Supplier: Added Supplier not persisted in the database",
					supplierId, supplierAct.getSupplierId());
			tf.deleteSupplier(em, supplierId);
		} catch (NoResultException ne) {
			assertNotNull(
					"Search via typed query for existing Supplier: Added Supplier not persisted in the database",
					supplierAct);
		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}

	}

	/**
	 * Test if Supplier gets updated in database
	 */
	@Test
	public void testUpdateSupplier() {
		Supplier supplierAct = null;
		String supplierId = "111110";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createSupplier(em, supplierId)) {
				fail("Unable to create Supplier");
				return;
			}
			em.getTransaction().begin();
			// Find Supplier for update
			supplier = em.find(Supplier.class, supplierId);
			// Update Supplier
			supplier.setPhoneNumber("009140345689");
			em.persist(supplier);
			em.getTransaction().commit();
			// Find Supplier after update
			supplierAct = em.find(Supplier.class, supplierId);
			assertEquals(
					"Update Supplier: Supplier attribute phone number not updated in the database",
					"009140345689", supplierAct.getPhoneNumber());
			tf.deleteSupplier(em, supplierId);
		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}

	}

	/**
	 * Test removing of a Supplier.
	 */
	@Test
	public void testRemoveSupplier() {
		Supplier supplierRes = null;
		String supplierId = "11111";
		TestFactory tf = new TestFactory();
		EntityManager em = emf.createEntityManager();
		try {
			if (!tf.createSupplier(em, supplierId)) {
				fail("Unable to create Supplier");
				return;
			}
			em.getTransaction().begin();
			// Remove Supplier
			assertTrue("Supplier not deleted",
					tf.deleteSupplier(em, supplierId));
			// Search for deleted Supplier.
			supplierRes = em.find(Supplier.class, supplierId);
			assertNull(
					"Search via find method for removed Supplier: Removed Supplier with ID 11111 still exists ",
					supplierRes);
		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple existing Suppliers added from Business_Partners.xml via
	 * Typed Queries
	 */
	@Test
	public void testExistingSuppliersSearchTyped() {
		EntityManager em = emf.createEntityManager();
		try {
			DataLoader dl = new DataLoader(emf);
			dl.loadSuppliers();
			// Search for mutiple Suppliers.
			TypedQuery<Supplier> query = em.createQuery(
					"SELECT s FROM Supplier s", Supplier.class);
			List<Supplier> result = query.getResultList();
			assertTrue(
					"Search for mutiple existing Suppliers: Multiple Suppliers not added",
					result.size() > 2);
		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}
	}

	/**
	 * Test for multiple not existing Suppliers via Typed Queries
	 */
	@Test
	public void testNotExistingCustomersSearchTyped() {
		EntityManager em = emf.createEntityManager();
		List<Supplier> result = null;
		em.getTransaction().begin();
		try {
			// Search for mutiple Suppliers.
			TypedQuery<Supplier> query = em
					.createQuery(
							"SELECT s FROM Supplier s WHERE s.supplierId = :supplierId",
							Supplier.class);
			result = query.setParameter("supplierId", "104").getResultList();
			assertEquals(
					"Search via typed query for not existing mutiple Suppliers: Suppliers exists in database",
					0, result.size());
		} catch (Exception e) {
			fail("Exception " + e + " occured");
		} finally {
			em.close();
		}
	}

}
