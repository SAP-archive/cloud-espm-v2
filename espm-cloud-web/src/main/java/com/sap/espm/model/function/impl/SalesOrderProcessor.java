package com.sap.espm.model.function.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImport.ReturnType.Type;
import org.apache.olingo.odata2.api.annotation.edm.EdmFunctionImportParameter;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.Customer;
import com.sap.espm.model.Product;
import com.sap.espm.model.SalesOrderHeader;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.documentservice.CMISSessionHelper;
import com.sap.espm.model.documentservice.InvoiceBuilder;
import com.sap.espm.model.exception.CMISConnectionException;
import com.sap.espm.model.exception.ReportGenerationException;
import com.sap.espm.model.util.Utility;

/**
 *  * This is a custom Apache Olingo Function Import. For more reference
 * information regarding a Function Import, refer to the official Olingo
 * documentation:
 * <p>
 * https://olingo.apache.org/doc/odata2/tutorials/jpafunctionimport.html
 * <p>
 * http://olingo.apache.org/doc/odata2/ 
 * <p>
 * This class is used to define custom {@link SalesOrderItem} entity Odata functions.
 *
 */
public class SalesOrderProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderProcessor.class);

	/**
	 * Function Import implementation for confirming a sales order
	 * 
	 * @param salesOrderId
	 *            sales order id of sales order to be confirmed
	 * @return SalesOrderHeader entity
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "ConfirmSalesOrder", entitySet = "SalesOrderHeaders", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<SalesOrderHeader> confirmSalesOrder(
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId) throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {

			Query query = em.createNamedQuery("SalesOrderHeader.getSOHBySaledOrderId");
			query.setParameter("salesOrderId", salesOrderId);
			try {
				SalesOrderHeader so = (SalesOrderHeader) query.getSingleResult();
				em.getTransaction().begin();
				so.setLifeCycleStatus("P");
				so.setLifeCycleStatusName("In Process");
				em.persist(so);
				em.getTransaction().commit();
				List<SalesOrderHeader> salesorderlist = null;

				query = em.createNamedQuery("SalesOrderHeader.getSOHBySaledOrderId");
				query.setParameter("salesOrderId", salesOrderId);
				salesorderlist = query.getResultList();
				return salesorderlist;

			} catch (NoResultException e) {
				throw new ODataApplicationException("No Sales Order with Sales Order Id:" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST, e);
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Function Import implementation for cancelling a sales order
	 * 
	 * @param salesOrderId
	 *            sales order id of sales order to be cancelled
	 * @return SalesOrderHeader entity
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "CancelSalesOrder", entitySet = "SalesOrderHeaders", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<SalesOrderHeader> cancelSalesOrder(
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId) throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {

			Query query = em.createNamedQuery("SalesOrderHeader.getSOHBySaledOrderId");
			query.setParameter("salesOrderId", salesOrderId);
			
			try {
				SalesOrderHeader so = (SalesOrderHeader) query.getSingleResult();
				em.getTransaction().begin();
				so.setLifeCycleStatus("X");
				so.setLifeCycleStatusName("Cancelled");
				em.persist(so);
				em.getTransaction().commit();
				List<SalesOrderHeader> salesOrderList = null;
				query = em.createNamedQuery("SalesOrderHeader.getSOHBySaledOrderId");
				query.setParameter("salesOrderId", salesOrderId);
				salesOrderList = query.getResultList();
				return salesOrderList;
			} catch (NoResultException e) {
				throw new ODataApplicationException("No Sales Order with Sales Order Id:" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST , e);
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Function Import implementation for getting all the Sales Order Items
	 * under a Sales Order Header
	 * 
	 * @param SalesOrderId
	 *            Sales Order Id of a Sales Order
	 * @return SalesOrderItem entity.
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetSalesOrderItemsById", entitySet = "SalesOrderItems", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<SalesOrderItem> getSalesOrderById(
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId) throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<SalesOrderItem> soiList = null;
		try {

			Query query = em.createNamedQuery("SalesOrderItem.getSOIBySalesOrderItemId");
			query.setParameter("id", salesOrderId);
			
			try {

				soiList = query.getResultList();
				if (soiList != null && soiList.size() >= 1) {

					for (SalesOrderItem salesOrderItem : soiList) {
						query = em.createNamedQuery("Product.getProductByProductId");
						query.setParameter("productId", salesOrderItem.getProductId());
						Product product = (Product) query.getSingleResult();
						salesOrderItem.setProduct(product);

					}
					// if the sales order are fetched successfully, generate the
					// pdf report data.
					try {
						if (CMISSessionHelper.getInstance().getSession() != null) {
							InvoiceBuilder builder = new InvoiceBuilder();
							String reportPath = builder.generateInvoice(soiList);
							updateSalesOrderHeader(reportPath, soiList, em);
						}
					} catch (CMISConnectionException cmisConnectionException) {
						// There was an exception while generating the report.
						LOGGER.error(cmisConnectionException.getMessage());
					}
					
					

				}

			} catch (NoResultException e) {
				throw new ODataApplicationException("No matching Sales Order with Sales Order Id:" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST, e);
			} catch (ReportGenerationException reportGenerationException) {
				//LOGGER.error("Exception while generating the report : " + reportGenerationException.getMessage());
				reportGenerationException.printStackTrace();
				throw new ODataApplicationException("PDF Report Generation Error for :" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.INTERNAL_SERVER_ERROR, reportGenerationException);
			}

			return soiList;
		} finally {
			em.close();
		}
	}

	/**
	 * Function Import implementation for updating SalesOrderHeader
	 */
	private void updateSalesOrderHeader(String reportPath, List<SalesOrderItem> soiList, EntityManager em) {
		if (soiList != null && !soiList.isEmpty()) {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				// add the path to the SalesOrder

				for (SalesOrderItem orderItem : soiList) {

					orderItem.getSalesOrderHeader().setInvoiceLink(reportPath);
					// save the soiList.
					em.merge(orderItem);

				}


			} finally {
				if(transaction!=null){
					transaction.commit();
				}
				
			}
		}
	}

	/**
	 * Function Import implementation for getting all the Sales Order invoices
	 * by email Address under a Sales Order Header
	 * 
	 * @param emailAddreaa
	 * 
	 * @return SalesOrderHeader entity.
	 * @throws ODataException
	 */
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetSalesOrderInvoiceByEmail", entitySet = "SalesOrderHeaders", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<SalesOrderHeader> getSalesOrderInvoiceByEmail(
			@EdmFunctionImportParameter(name = "EmailAddress") String emailAddress) throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<SalesOrderHeader> orderList = new ArrayList<>();
		List<SalesOrderHeader> salesOrderHeaderList = new ArrayList<>();
		List<SalesOrderItem> itemList = new ArrayList<>();
		try {
			Query querySOItems;
			Query queryCustomer = em.createNamedQuery("Customer.getCustomerByEmailAddress");
			queryCustomer.setParameter("emailAddress", emailAddress);
			Customer c = (Customer) queryCustomer.getSingleResult();
			String customerId = c.getCustomerId();
			Query querySOHeader = em.createNamedQuery("SalesOrderHeader.getSOHByCustomerId");
			querySOHeader.setParameter("customerId", customerId);
			orderList = querySOHeader.getResultList();
			for (SalesOrderHeader salesOrderHeader : orderList) {
				querySOItems = em.createNamedQuery("SalesOrderItem.getSOIBySalesOrderItemId");
				querySOItems.setParameter("id", salesOrderHeader.getSalesOrderId());
				itemList = querySOItems.getResultList();
				salesOrderHeader.setSalesOrderItems(itemList);
				salesOrderHeader.setCustomer(c);
				salesOrderHeaderList.add(salesOrderHeader);

			}

		} catch (NoResultException e) {
			throw new ODataApplicationException("No Sales Order Invoices with emailId Id:......." + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST, e);
		} catch (Exception exception) {
			throw new ODataApplicationException("No Sales Order Invoices with emailId Id:" + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST, exception);
		} finally {
			em.close();
		}

		return salesOrderHeaderList;

	}

}
