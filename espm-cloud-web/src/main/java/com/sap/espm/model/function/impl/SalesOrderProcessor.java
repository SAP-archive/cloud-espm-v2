package com.sap.espm.model.function.impl;

import java.util.ArrayList;
import java.util.Collections;
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

import com.sap.espm.model.Customer;
import com.sap.espm.model.Product;
import com.sap.espm.model.SalesOrderHeader;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.pdf.generator.InvoicePDFGenerator;
import com.sap.espm.model.util.Utility;

/**
 * 
 * Function Import processor class for Sales Orders
 * 
 */
public class SalesOrderProcessor {

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
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {

			Query query = em
					.createQuery("SELECT s FROM SalesOrderHeader s WHERE s.salesOrderId ="
							+ salesOrderId);
			try {
				SalesOrderHeader so = (SalesOrderHeader) query
						.getSingleResult();
				em.getTransaction().begin();
				so.setLifeCycleStatus("P");
				so.setLifeCycleStatusName("In Process");
				em.persist(so);
				em.getTransaction().commit();
				List<SalesOrderHeader> salesorderlist = null;

				query = em
						.createQuery("SELECT s FROM SalesOrderHeader s WHERE s.salesOrderId ='"
								+ salesOrderId + "'");
				salesorderlist = query.getResultList();
				return salesorderlist;

			} catch (NoResultException e) {
				throw new ODataApplicationException(
						"No Sales Order with Sales Order Id:" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
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
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {

			Query query = em
					.createQuery("SELECT s FROM SalesOrderHeader s WHERE s.salesOrderId ="
							+ salesOrderId);
			try {
				SalesOrderHeader so = (SalesOrderHeader) query
						.getSingleResult();
				em.getTransaction().begin();
				so.setLifeCycleStatus("X");
				so.setLifeCycleStatusName("Cancelled");
				em.persist(so);
				em.getTransaction().commit();
				List<SalesOrderHeader> salesOrderList = null;
				query = em
						.createQuery("SELECT s FROM SalesOrderHeader s WHERE s.salesOrderId ='"
								+ salesOrderId + "'");
				salesOrderList = query.getResultList();
				return salesOrderList;
			} catch (NoResultException e) {
				throw new ODataApplicationException(
						"No Sales Order with Sales Order Id:" + salesOrderId,
						Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
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
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<SalesOrderItem> soiList = null;
		try {

			
			Query query = em.createQuery("SELECT soi FROM SalesOrderItem soi where soi.id.salesOrderId='"+ salesOrderId +"'"); 
			
			try {

				soiList = query.getResultList();
				if(soiList!=null && soiList.size()>=1){
					query = em.createQuery("SELECT soh FROM SalesOrderHeader soh where soh.salesOrderId='"+ salesOrderId +"'");
					List<SalesOrderHeader> salesOrderHeaderList = query.getResultList();
					
					for(SalesOrderHeader salesOrderHeader : salesOrderHeaderList){
						List<SalesOrderItem> salesOrderItemList = salesOrderHeader.getSalesOrderItems();
						for(SalesOrderItem salesOrderItem : salesOrderItemList){
							query = em.createQuery("SELECT product FROM Product product where product.productId = :productId");
							query.setParameter("productId", salesOrderItem.getProductId());
							Product product = (Product) query.getSingleResult();
							salesOrderItem.setProduct(product);
							
						}
						
					}
					// if the sales order are fetched successfully, generate the pdf report data.
					InvoicePDFGenerator generator = new InvoicePDFGenerator();
					String reportPath = generator.generateInvoicePdf(soiList);
					updateSalesOrderHeader(reportPath, soiList, em);
					
				}

			} catch (NoResultException e) {
				throw new ODataApplicationException(
						"No matching Sales Order with Sales Order Id:"
								+ salesOrderId, Locale.ENGLISH,
						HttpStatusCodes.BAD_REQUEST);
			} catch(Exception exception){
				exception.printStackTrace();
			} catch(Throwable throwable){
				throwable.printStackTrace();
			}
			return soiList;
		} finally {
			em.close();
		}
	}
	/**
	 * Function Import implementation for updating SalesOrderHeader 
	 */
	private void updateSalesOrderHeader(String reportPath,
			List<SalesOrderItem> soiList, EntityManager em) {
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

				transaction.commit();
			} finally {

			}
		}
	}
	
/*	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetSalesOrderInvoiceByEmail", entitySet = "SalesOrderHeaders", returnType = @ReturnType(type = Type.COMPLEX, isCollection = true))
	public List<Object> getSalesOrderInvoiceByEmail(
			@EdmFunctionImportParameter(name = "EmailAddress") String emailAddress,
			@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<Object> Invoice = new ArrayList<Object>();
		List<SalesOrderHeader> invoiceList = Collections.emptyList();
		List<SalesOrderItem> itemList = Collections.emptyList();
		List<Customer> custList = null;
		try {
			Query query = em
					.createQuery("SELECT soh FROM SalesOrderHeader soh JOIN Customer c WHERE c.emailAddress= :emailAddress ");
			query.setParameter("emailAddress", emailAddress);
			Query query1 = em
					.createQuery("SELECT soi FROM SalesOrderItem soi where soi.id.salesOrderId='"+ salesOrderId +"'");
			
			Query query2 = em
					.createQuery("SELECT c FROM Customer WHERE c.emailAddress= :emailAddress ");
			query2.setParameter("emailAddress", emailAddress);
			invoiceList = query.getResultList();
			itemList=query1.getResultList();
			custList=query2.getResultList();
			Invoice.addAll(invoiceList);
			Invoice.addAll(itemList);
			Invoice.addAll(custList);
			
			
		} catch (NoResultException e) {
			throw new ODataApplicationException(
					"No Sales Order Invoices with emailId Id:" + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
		} catch (Exception exception) {
			throw new ODataApplicationException(
					"No Sales Order Invoices with emailId Id:" + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
		}

		finally {
			em.close();
		}

		return Invoice;

	}
*/
	
	/**
	 * Function Import implementation for getting all the Sales Order invoices by email Address
	 * under a Sales Order Header
	 * 
	 * @param emailAddreaa
	 *            
	 * @return SalesOrderHeader entity.
	 * @throws ODataException
	 */
	//@EdmFunctionImportParameter(name = "SalesOrderId") String salesOrderId
	@SuppressWarnings("unchecked")
	@EdmFunctionImport(name = "GetSalesOrderInvoiceByEmail", entitySet = "SalesOrderHeaders", returnType = @ReturnType(type = Type.ENTITY, isCollection = true))
	public List<SalesOrderHeader> getSalesOrderInvoiceByEmail(
			@EdmFunctionImportParameter(name = "EmailAddress") String emailAddress)
			throws ODataException {
		EntityManagerFactory emf = Utility.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		List<SalesOrderHeader> orderList = new ArrayList<>();
		List<SalesOrderHeader> salesOrderHeaderList = new ArrayList<>();
		List<SalesOrderItem> itemList = new ArrayList<>();
		//SalesOrderHeader soh;
		try {
			/*Query querySOHeader = em
					.createQuery("SELECT soh FROM SalesOrderHeader soh JOIN Customer c WHERE c.emailAddress= :emailAddress ");
			querySOHeader.setParameter("emailAddress", emailAddress);
			Query querySOItems; 			
			Query queryCustomer = em
					.createQuery("SELECT c FROM Customer WHERE c.emailAddress= :emailAddress");
			queryCustomer.setParameter("emailAddress", emailAddress);
			orderList = querySOHeader.getResultList();
			Customer cm = (Customer) queryCustomer.getSingleResult();
			for( SalesOrderHeader salesOrderHeader : orderList){
				querySOItems = em
						.createQuery("SELECT soi FROM SalesOrderItem soi where soi.id.salesOrderId= :salesOrderId");
				querySOItems.setParameter("salesOrderId", salesOrderId);
				salesOrderHeader.setSalesOrderItems(querySOItems.getResultList());
				salesOrderHeader.setCustomer(cm);
				orderList.add(salesOrderHeader);
				
			}*/		
			
			Query querySOItems; ;
			Query queryCustomer = em.createQuery("SELECT c FROM Customer c where c.emailAddress= :emailAddress");
			queryCustomer.setParameter("emailAddress", emailAddress);
			Customer c = (Customer) queryCustomer.getSingleResult();
			String customerId = c.getCustomerId();
			Query querySOHeader = em
					.createQuery("SELECT soh FROM SalesOrderHeader soh where soh.customerId= :customerId");
			querySOHeader.setParameter("customerId", customerId);
			orderList = querySOHeader.getResultList();
			for( SalesOrderHeader salesOrderHeader : orderList){
				querySOItems = em
						.createQuery("SELECT soi FROM SalesOrderItem soi where soi.id.salesOrderId= :salesOrderId");
				querySOItems.setParameter("salesOrderId", salesOrderHeader.getSalesOrderId());
				itemList = querySOItems.getResultList();
				salesOrderHeader.setSalesOrderItems(itemList);
				salesOrderHeader.setCustomer(c);
				salesOrderHeaderList.add(salesOrderHeader);
				
			}
			
			
			
		} catch (NoResultException e) {
			throw new ODataApplicationException(
					"No Sales Order Invoices with emailId Id:......." + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
		} catch (Exception exception) {
			throw new ODataApplicationException(
					"No Sales Order Invoices with emailId Id:" + emailAddress,
					Locale.ENGLISH, HttpStatusCodes.BAD_REQUEST);
		} catch (Throwable throwable){
			//TODO: remove this
			throwable.printStackTrace();
		}

		finally {
			em.close();
		}

		return salesOrderHeaderList;

	}
	

}

