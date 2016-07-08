package com.sap.espm.model.data;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.Customer;
import com.sap.espm.model.CustomerReview;
import com.sap.espm.model.Product;
import com.sap.espm.model.ProductCategory;
import com.sap.espm.model.Stock;
import com.sap.espm.model.Supplier;

/**
 * Data Loader tool for loading business partners and products into the db.
 * 
 */
public class DataLoader {

	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	private EntityManagerFactory emf;

	public DataLoader(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Load products to db from Products.xml
	 */
	public List<Product> loadProducts(List<Supplier> suppliers) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Product> queryProd;
		List<Product> resProd = null;
		try {
			em.getTransaction().begin();
			queryProd = em
					.createQuery("SELECT p FROM Product p", Product.class);
			resProd = queryProd.getResultList();
			if (resProd.size() > 5) {
				logger.info(resProd.size()
						+ " Products already available in the db");
			} else {
				new XMLParser().readProduct(em,
						"com/sap/espm/model/data/Products.xml", suppliers);
				em.getTransaction().commit();
				queryProd = em.createQuery("SELECT p FROM Product p",
						Product.class);
				resProd = queryProd.getResultList();
				logger.info(resProd.size() + " Products loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
		return resProd;
	}

	/**
	 * Load CustomerReviews to db from CustomerReivews.xml
	 */
	public List<CustomerReview> loadCustomerReviews(List<Product> products) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<CustomerReview> queryReviews;
		List<CustomerReview> resReview = null;
		try {
			em.getTransaction().begin();
			queryReviews = em
					.createQuery("SELECT p FROM CustomerReview p", CustomerReview.class);
			resReview = queryReviews.getResultList();
			if (resReview.size() > 5) {
				logger.info(resReview.size()
						+ " Customer Reviews already available in the db");
			} else {
				new XMLParser().readCustomerReview(em,
						"com/sap/espm/model/data/CustomerReviews.xml", products);
				em.getTransaction().commit();
				queryReviews = em.createQuery("SELECT p FROM CustomerReview p",
						CustomerReview.class);
				resReview = queryReviews.getResultList();
				logger.info(resReview.size() + " Products loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
		return resReview;
	}
	
	/**
	 * Load Customers to db from Business_Partners.xml.
	 */
	public void loadCustomers() {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Customer> queryBP;
		List<Customer> resBP;
		try {
			em.getTransaction().begin();
			queryBP = em
					.createQuery("SELECT c FROM Customer c", Customer.class);
			resBP = queryBP.getResultList();
			if (resBP.size() > 5) {
				logger.info(resBP.size()
						+ " Customers already available in the db");
			} else {
				new XMLParser().readCustomers(em,
						"com/sap/espm/model/data/Business_Partners.xml");
				em.getTransaction().commit();
				queryBP = em.createQuery("SELECT c FROM Customer c",
						Customer.class);
				resBP = queryBP.getResultList();
				logger.info(resBP.size() + " customers loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
	}

	/**
	 * Load Customers to db from Business_Partners.xml.
	 */
	public List<Supplier> loadSuppliers() {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Supplier> queryBP;
		List<Supplier> resBP = null;
		try {
			em.getTransaction().begin();
			queryBP = em
					.createQuery("SELECT s FROM Supplier s", Supplier.class);
			resBP = queryBP.getResultList();
			if (resBP.size() > 5) {
				logger.info(resBP.size()
						+ " Suppliers already available in the db");
			} else {
				new XMLParser().readSuppliers(em,
						"com/sap/espm/model/data/Business_Partners.xml");
				em.getTransaction().commit();
				queryBP = em.createQuery("SELECT s FROM Supplier s",
						Supplier.class);
				resBP = queryBP.getResultList();
				logger.info(resBP.size() + " suppliers loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
		return resBP;
	}

	/**
	 * Load Product Categories to db from Business_Partners.xml.
	 * 
	 * @param products
	 *            TODO
	 */
	public void loadProductCategories(List<Product> products) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<ProductCategory> queryPC;
		List<ProductCategory> resPC;
		try {
			em.getTransaction().begin();
			queryPC = em.createQuery("SELECT pc FROM ProductCategory pc",
					ProductCategory.class);
			resPC = queryPC.getResultList();
			if (resPC.size() > 5) {
				logger.info(resPC.size()
						+ " Product Categories already available in the db");
			} else {
				new XMLParser().readProductCategory(em,
						"com/sap/espm/model/data/Product_Categories.xml",
						products);
				em.getTransaction().commit();
				queryPC = em.createQuery("SELECT pc FROM ProductCategory pc",
						ProductCategory.class);
				resPC = queryPC.getResultList();
				logger.info(resPC.size()
						+ " Product Categories loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
	}
	
	/**
	 * Load stock into db based on an algorithm
	 * 
	 * @param products
	 *            List of products to be added.
	 */
	public void loadStock(List<Product> products) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Stock> queryStock;
		List<Stock> resStock;
		BigDecimal quantity;
		BigDecimal minStock;
		BigDecimal lotSize;
		Product prod = null;
		int lenProdName;
		try {
			em.getTransaction().begin();
			queryStock = em.createQuery("SELECT st FROM Stock st", Stock.class);
			resStock = queryStock.getResultList();
			if (resStock.size() > 5) {
				logger.info(resStock.size()
						+ " Stock already available in the db");
			} else {
				Stock st = null;
				for (int count = 0; count < products.size(); count++) {
					st = new Stock();
					st.setProductId(products.get(count).getProductId());
					lenProdName = products.get(count).getName().length();
					quantity = BigDecimal.valueOf(lenProdName * 5);
					if (lenProdName % 17 == 0) {
						minStock = BigDecimal.valueOf(17);
					} else {
						minStock = BigDecimal.valueOf(lenProdName - 1);
					}
					lotSize = minStock.multiply(BigDecimal.valueOf(3));
					st.setQuantity(quantity);
					st.setMinStock(minStock);
					st.setLotSize(lotSize);
					prod = em.find(Product.class, products.get(count)
							.getProductId());
					st.setProduct(prod);
					em.persist(st);
				}

				em.getTransaction().commit();
				queryStock = em.createQuery("SELECT st FROM Stock st",
						Stock.class);
				resStock = queryStock.getResultList();
				logger.info(resStock.size() + " Stocks loaded into the db");
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		} finally {
			em.close();
		}
	}

	
/**
	 * Load Products, Customers, Suppliers, Product Categories, customer reviews to db from
	 * respective xml's and Generate Stock.
	 */
	public void loadData() {
		loadCustomers();
		List<Supplier> suppliers = loadSuppliers();
		List<Product> products = loadProducts(suppliers);
		List<CustomerReview> customerReviews = loadCustomerReviews(products);
		loadStock(products);
		loadProductCategories(products);


	}

}
