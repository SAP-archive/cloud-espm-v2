package com.sap.espm.model.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.Customer;
import com.sap.espm.model.Product;
import com.sap.espm.model.ProductCategory;
import com.sap.espm.model.SalesOrderHeader;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.SalesOrderItemId;
import com.sap.espm.model.CustomerReview;
import com.sap.espm.model.Stock;
import com.sap.espm.model.Supplier;

public class TestFactory {
	public static final String TEST_JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String TEST_JDBC_URL_IN_MEMORY = "jdbc:derby:memory:DemoDB;create=true";
	public static final String TEST_JDBC_URL = "jdbc:derby:DemoDB;create=true";
	public static final String TEST_JDBC_USER = "demo";
	public static final String TEST_JDBC_PASSWORD = "demo";
	private static final String TEST_TARGET_DATABASE = "Derby";
	private static final String TEST_JPA_LOG_LEVEL = "INFO";
	public static final String PERSISTENCE_UNIT = "com.sap.espm.model";

	/*
	 * Indicates whether the test database instance is in-memory or file system
	 * based
	 */
	private static boolean inMemory = true;
	private static Logger logger = LoggerFactory.getLogger(TestFactory.class);
	private static Map<String, String> defaultProperties = null;
	protected static EntityManagerFactory emf;

	/**
	 * Create an EntityManagerFactory instance for the named persistence unit
	 * for accessing a local in-memory Derby test database instance.
	 * <p>
	 * 
	 * @param persistenceUnitName
	 *            the persistence unit name
	 * @return an EntityManagerFactory instance for accessing a local in-memory
	 *         Derby test database instance
	 */
	public static EntityManagerFactory createEntityManagerFactory(
			String persistenceUnitName) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				persistenceUnitName, getDefaultTestProperties());
		Utility.setEntityManagerFactory(emf);
		return emf;
	}

	/**
	 * Helper method to create Product
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createProduct(EntityManager em, String productId) {
		Boolean status = true;
		Product product = new Product();
		try {
			em.getTransaction().begin();
			product.setProductId(productId);
			product.setCategory("SMARTPHONE");
			product.setQuantityUnit("EA");
			product.setWeightUnit("KG");
			product.setPrice(BigDecimal.valueOf(Double.parseDouble("500")));
			em.persist(product);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of product. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to delete Product
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteProduct(EntityManager em, String productId) {
		Boolean status = true;
		Product product = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			product = em.find(Product.class, productId);
			if (product != null) {
				em.remove(product);
				em.getTransaction().commit();
			} else {
				logger.info("Product " + productId
						+ " does not exist in the db");
				status = false;
			}

		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of product. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to create Business Partner
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createCustomer(EntityManager em, String customerId) {
		Boolean status = true;
		Customer bupa = null;
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("yyyymmdd");
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}

			Calendar cal = Calendar.getInstance();
			date = formatter.parse("19770707");
			cal.setTime(date);
			bupa = new Customer();
			bupa.setCustomerId(customerId);
			bupa.setPhoneNumber("009180437980098");
			bupa.setDateOfBirth(cal);
			em.persist(bupa);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of Business Partner. Detailed info: "
					+ e);
		}

		return status;
	}

	/**
	 * Helper method to delete Business Partner
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteCustomer(EntityManager em, String id) {
		Boolean status = true;
		Customer bupa = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			bupa = em.find(Customer.class, id);
			em.remove(bupa);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Business Partner. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to create Sales Order Header.
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createSalesOrderHeader(EntityManager em, String salesOrderId) {
		boolean status = true;
		SalesOrderHeader soHeader = null;
		try {
			// Create SO Header
			em.getTransaction().begin();
			soHeader = createSalesOrderHeader(salesOrderId);
			em.persist(soHeader);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of Sales Order Header. Detailed info: "
					+ e);
		}

		return status;
	}

	/**
	 * Helper method to delete Sales Order Header.
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteSalesOrderHeader(EntityManager em, String id) {
		boolean status = true;
		SalesOrderHeader soHeader = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			soHeader = em.find(SalesOrderHeader.class, id);
			em.remove(soHeader);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Sales Order Header. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to create Sales Order Item.
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createSalesOrderItem(EntityManager em, String salesOrderId) {
		boolean status = true;
		SalesOrderHeader soHeader = null;
		SalesOrderItem soItem = null;
		SalesOrderItemId id = new SalesOrderItemId(salesOrderId, 1);
		try {
			// Create SO Item
			em.getTransaction().begin();
			soHeader = createSalesOrderHeader(salesOrderId);
			soItem = new SalesOrderItem();
			/*
			 * soItem.setSalesOrderId(salesOrderId); soItem.setItemNumber(1);
			 */
			soItem.setId(id);
			soItem.setCurrencyCode("INR");
			soItem.setGrossAmount(BigDecimal.valueOf(13224));
			soItem.setNetAmount(BigDecimal.valueOf(11113));
			soItem.setTaxAmount(BigDecimal.valueOf(2111));
			soHeader.addItem(soItem);
			soItem.setSalesOrderHeader(soHeader);
			em.persist(soItem);
			em.getTransaction().commit();

		} catch (Exception e) {
			status = false;
			logger.error("Error occured during create of Sales Order Item. Detailed info: "
					+ e);
		}
		return status;

	}

	/**
	 * Helper method to delete Sales Order Item.
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteSalesOrderItem(EntityManager em, String id) {
		boolean status = true;
		SalesOrderItem soItem = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			soItem = em.find(SalesOrderItem.class, new SalesOrderItemId(id, 1));
			em.remove(soItem);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Sales Order Item. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to create Stock
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */

	public Boolean createStock(EntityManager em, String productId) {
		Boolean status = true;
		Stock stock = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			stock = new Stock();
			stock.setProductId(productId);
			stock.setQuantity(BigDecimal.valueOf(10));
			stock.setLotSize(BigDecimal.valueOf(15));
			stock.setMinStock(BigDecimal.valueOf(5));
			em.persist(stock);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of Stock. Detailed info: "
					+ e);
		}

		return status;
	}

	/**
	 * Helper method to delete Stock
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteStock(EntityManager em, String id) {
		Boolean status = true;
		Stock stock = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			stock = em.find(Stock.class, id);
			em.remove(stock);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Stock. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to create Supplier
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createSupplier(EntityManager em, String supplierId) {
		Boolean status = true;
		Supplier supplier = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			supplier = new Supplier();
			supplier.setSupplierId(supplierId);
			supplier.setPhoneNumber("009180437980098");
			em.persist(supplier);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of Supplier. Detailed info: "
					+ e);
		}

		return status;
	}

	/**
	 * Helper method to delete Supplier
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product db id.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteSupplier(EntityManager em, String id) {
		Boolean status = true;
		Supplier supplier = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			supplier = em.find(Supplier.class, id);
			em.remove(supplier);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Supplier. Detailed info: "
					+ e);
		}
		return status;
	}


	/**
	 * Helper method to create Stock
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product category .
	 * @return the true/false status of the success/failure of operation.
	 */

	public Boolean createProductCategory(EntityManager em,
			String productCategory) {
		Boolean status = true;
		ProductCategory prodcat = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			prodcat = new ProductCategory();
			prodcat.setCategory(productCategory);
			prodcat.setCategoryName(productCategory);
			em.persist(prodcat);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of Product category . Detailed info: "
					+ e);
		}

		return status;
	}

	/**
	 * Helper method to delete Product Category
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key product category.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteProductCategory(EntityManager em,
			String productCategory) {
		Boolean status = true;
		ProductCategory prodcat = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			prodcat = em.find(ProductCategory.class, productCategory);
			em.remove(prodcat);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during delete of Product category. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Create SalesOrderHeader entity
	 * 
	 * @param salesOrderId
	 *            sales order id of created entity
	 * @return created entity.
	 */
	private SalesOrderHeader createSalesOrderHeader(String salesOrderId) {
		SalesOrderHeader soHeader = new SalesOrderHeader();
		soHeader.setSalesOrderId(salesOrderId);
		soHeader.setCurrencyCode("INR");
		soHeader.setGrossAmount(BigDecimal.valueOf(13224));
		soHeader.setNetAmount(BigDecimal.valueOf(11113));
		soHeader.setTaxAmount(BigDecimal.valueOf(2111));
		return soHeader;
	}

	/**
	 * Get persistence.xml properties
	 * 
	 * @return properties as Map
	 */
	private static Map<String, String> getDefaultTestProperties() {
		if (defaultProperties == null) {
			defaultProperties = new HashMap<String, String>();
			defaultProperties.put(PersistenceUnitProperties.JDBC_DRIVER,
					TEST_JDBC_DRIVER);
			if (inMemory) {
				defaultProperties.put(PersistenceUnitProperties.JDBC_URL,
						TEST_JDBC_URL_IN_MEMORY);
			} else {
				defaultProperties.put(PersistenceUnitProperties.JDBC_URL,
						TEST_JDBC_URL);
			}
			defaultProperties.put(PersistenceUnitProperties.JDBC_USER,
					TEST_JDBC_USER);
			defaultProperties.put(PersistenceUnitProperties.JDBC_PASSWORD,
					TEST_JDBC_PASSWORD);
			defaultProperties.put(PersistenceUnitProperties.TARGET_DATABASE,
					TEST_TARGET_DATABASE);
			defaultProperties.put(PersistenceUnitProperties.LOGGING_LEVEL,
					TEST_JPA_LOG_LEVEL);
			defaultProperties.put(PersistenceUnitProperties.DDL_GENERATION,
					PersistenceUnitProperties.DROP_AND_CREATE);
		}
		return defaultProperties;
	}
	
	/**
	 * Helper method to create CustomerReview
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key of customer review entity in db.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean createCustomerReview(EntityManager em,
			String customerReviewId) {
		Boolean status = true;
		Date date = null;
		CustomerReview customerReview = new CustomerReview();
		Calendar cal = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("yyyymmdd");

		try {
			date = formatter.parse("19770707");
			cal.setTime(date);
			em.getTransaction().begin();

			customerReview.setCustomerReviewId(customerReviewId);
			customerReview
					.setComment("This product is really great. I like especially the design, speed and performance");
			customerReview.setRating(5);
			customerReview.setFirstName("John");
			customerReview.setLastName("Smith");
			customerReview.setProductId("HT-2001");
			customerReview.setCreationDate(cal);
			/*
			 * customerReview.setCreationDate(new GregorianCalendar(2012, 3, 17,
			 * 13, 23, 11));
			 */

			em.persist(customerReview);
			em.getTransaction().commit();
		} catch (Exception e) {
			status = false;
			logger.error("Error occured during creation of customer review. Detailed info: "
					+ e);
		}
		return status;
	}

	/**
	 * Helper method to delete CustomerReview
	 * 
	 * @param em
	 *            the entity manager instance
	 * @param id
	 *            the primary key of customer review entity in db.
	 * @return the true/false status of the success/failure of operation.
	 */
	public Boolean deleteCustomerReview(EntityManager em,
			String customerReviewId) {
		Boolean status = true;
		CustomerReview customerReview = null;
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			customerReview = em.find(CustomerReview.class, customerReviewId);
			if (customerReview != null) {
				em.remove(customerReview);
				em.getTransaction().commit();
			} else {
				logger.info("CustomerReview " + customerReviewId
						+ " does not exist in the db");
				status = false;
			}

		} catch (Exception e) {
			status = false;
			logger.error("Error occured during deletion of customer review. Detailed info: "
					+ e);
		}
		return status;
	}

}
