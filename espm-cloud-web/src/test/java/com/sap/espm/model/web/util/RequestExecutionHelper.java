package com.sap.espm.model.web.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.cxf.common.util.Base64Utility;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Class containing all factory methods.
 * 
 */
public class RequestExecutionHelper {

	public static final String ES_PRODUCT = "Products";
	public static final String ES_CUSTOMER = "Customers";
	public static final String ES_PRODUCT_CATEGORY = "ProductCategories";
	public static final String ES_SALES_ORDER_HEADER = "SalesOrderHeaders";
	public static final String ES_SALES_ORDER_ITEM = "SalesOrderItems";
	public static final String ES_STOCK = "Stocks";
	public static final String ES_PURCHASE_ORDER_HEADER = "PurchaseOrderHeaders";
	public static final String ES_PURCHASE_ORDER_ITEM = "PurchaseOrderItems";
	public static final String ES_SUPPLIER = "Suppliers";

	private static final String ESPM_SERVICE_BASE_URI = "/espm-cloud-web/espm.svc/";
	private static final String INTEGRATION_TEST_SERVER_URL = "integration.test.server.url";
	private static final String SERVICE_ROOT_URI = System
			.getProperty(INTEGRATION_TEST_SERVER_URL) + ESPM_SERVICE_BASE_URI;
	private static final String UPDATE_LINK_PAYLOAD = "<uri  xmlns=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">"
			+ SERVICE_ROOT_URI + "<href></uri>";
	private static final String user = "ret";
	private static final String password = "123";
	private static final String AUTHORIZATION = "Basic "
			+ new String(Base64Utility.encode((user + ":" + password).getBytes()));

	/**
	 * Get JSONArray from a JSON response
	 * 
	 * @param response
	 *            JSON response
	 * @return array of JSON objects
	 * @throws JSONException
	 */
	public static JSONObject getJSONResponseObject(String response)
			throws JSONException {
		JSONObject jo = new JSONObject(response);
		return (JSONObject) jo.get("d");
	}

	/**
	 * Get JSONArray from a JSON response
	 * 
	 * @param response
	 *            JSON response
	 * @return array of JSON objects
	 * @throws JSONException
	 */
	public static JSONArray getJSONArrayofResults(String response)
			throws JSONException {
		JSONArray ja = null;
		JSONObject jo = new JSONObject(response);
		JSONObject jod = (JSONObject) jo.get("d");
		ja = (JSONArray) jod.get("results");
		return ja;
	}

	/**
	 * 
	 * @param entityName
	 *            Odata entity Name
	 * @param payLoad
	 *            id of the entity
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return connection instance of the request/response
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String createEntityViaREST(String entityName, String payLoad,
			boolean secure) throws IOException, JSONException {
		HttpResponse resp = executePostRequest(entityName, payLoad, secure);
		if (resp.getResponseCode() != 201) {
			return null;
		}
		JSONObject jo = getJSONResponseObject(resp.getBody());
		return jo.getString(getPropertyRefByEntityName(entityName));
	}

	/**
	 * 
	 * @throws JSONException
	 * 
	 * @param entityName
	 *            Name of the entity
	 * @param id
	 *            id of the entity
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return connection instance of the request/reponse
	 * @throws IOException
	 * @throws
	 */
	public static String createSalesOrderViaREST(String entityName,
			String salesOrderPayLoad, boolean secure) throws IOException,
			JSONException {
		String salesOrderId = null;
		HttpResponse resp = executePostRequest(entityName, salesOrderPayLoad,
				secure);
		if (resp.getResponseCode() != 201) {
			return null;
		}
		JSONObject jo = getJSONResponseObject(resp.getBody());
		salesOrderId = jo.getString("SalesOrderId");
		String customerDetails = UPDATE_LINK_PAYLOAD;
		customerDetails = (secure) ? customerDetails.replace("<href>",
				"secure/Customers('100000028')") : customerDetails.replace(
				"<href>", "Customers('100000028')");
		resp = executePutRequest("SalesOrderHeaders('" + salesOrderId
				+ "')/$links/Customer", customerDetails, secure);
		if (resp.getResponseCode() != 204) {
			return null;
		}
		String productDetails = UPDATE_LINK_PAYLOAD;
		productDetails = (secure) ? productDetails.replace("<href>",
				"secure/Products('HT-1002')") : productDetails.replace(
				"<href>", "Products('HT-1002')");
		resp = executePutRequest("SalesOrderItems(ItemNumber=10,SalesOrderId='"
				+ salesOrderId + "')/$links/Product", productDetails, secure);
		if (resp.getResponseCode() != 204) {
			return null;
		}
		return salesOrderId;

	}

	/**
	 * 
	 * @throws JSONException
	 * 
	 * @param entityName
	 *            Name of the entity
	 * @param id
	 *            id of the entity
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return connection instance of the request/reponse
	 * @throws IOException
	 * @throws
	 */
	public static String createPurchaseOrderViaREST(String entityName,
			String purchaseOrderPayLoad, boolean secure) throws IOException,
			JSONException {
		String purchaseOrderId = null;
		HttpResponse resp = executePostRequest(entityName,
				purchaseOrderPayLoad, secure);
		if (resp.getResponseCode() != 201) {
			return null;
		}
		JSONObject jo = getJSONResponseObject(resp.getBody());
		purchaseOrderId = jo.getString("PurchaseOrderId");
		String productDetails = UPDATE_LINK_PAYLOAD;
		productDetails = (secure) ? productDetails.replace("<href>",
				"secure/Products('HT-1002')") : productDetails.replace(
				"<href>", "Products('HT-1002')");
		resp = executePutRequest(
				"PurchaseOrderItems(ItemNumber=10,PurchaseOrderId='"
						+ purchaseOrderId + "')/$links/Product",
				productDetails, secure);
		if (resp.getResponseCode() != 204) {
			return null;
		}
		return purchaseOrderId;

	}

	/**
	 * Run a http get request
	 * 
	 * @param serviceEndPoint
	 *            OData service url
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return http response http response of OData get request
	 * @throws IOException
	 */
	public static HttpResponse executeGetRequest(String serviceEndPoint,
			boolean secure) throws IOException {
		URL url = (secure) ? new URL(SERVICE_ROOT_URI + "secure/"
				+ serviceEndPoint)
				: new URL(SERVICE_ROOT_URI + serviceEndPoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (secure) {
			connection.setRequestProperty("Authorization", AUTHORIZATION);
		}
		try {
			return new HttpResponse(connection);
		} finally {
			connection.disconnect();
		}

	}

	/**
	 * Helper method to execute a HTTP PUT request
	 * 
	 * @param serviceEndPoint
	 *            OData service url
	 * @param payLoad
	 *            xml payload for request body
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return http reponse of request
	 * @throws IOException
	 */
	public static HttpResponse executePutRequest(String serviceEndPoint,
			String payLoad, boolean secure) throws IOException {
		URL url = (secure) ? new URL(SERVICE_ROOT_URI + "secure/"
				+ serviceEndPoint)
				: new URL(SERVICE_ROOT_URI + serviceEndPoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("PUT");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			if (secure) {
				connection.setRequestProperty("Authorization", AUTHORIZATION);
			}
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Accept", "application/xml");
			DataOutputStream outStream = new DataOutputStream(
					connection.getOutputStream());
			try {
				outStream.writeBytes(payLoad);
				outStream.close();
				return new HttpResponse(connection);
			} finally {
				outStream.close();
			}
		} finally {
			connection.disconnect();
		}

	}

	/**
	 * Helper method to execute a HTTP POST request
	 * 
	 * @param entityName
	 *            Name of OData entity
	 * @param payLoad
	 *            xml payload for request body
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return http response of the request
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static HttpResponse executePostRequest(String entityName,
			String payLoad, boolean secure) throws MalformedURLException,
			IOException, ProtocolException {
		URL url = (secure) ? new URL(SERVICE_ROOT_URI + "secure/" + entityName)
				: new URL(SERVICE_ROOT_URI + entityName);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			if (secure) {
				connection.setRequestProperty("Authorization", AUTHORIZATION);
			}
			connection.setRequestProperty("Content-Type",
					"application/atom+xml");
			connection.setRequestProperty("Accept", "application/json");
			DataOutputStream outStream = new DataOutputStream(
					connection.getOutputStream());
			try {
				outStream.writeBytes(payLoad);
				outStream.close();
				return new HttpResponse(connection);
			} finally {
				outStream.close();
			}
		} finally {

			connection.disconnect();
		}
	}

	/**
	 * Helper method to execute a HTTP Delete request
	 * 
	 * @param serviceEndPoint
	 *            OData service URL
	 * @param secure
	 *            True if credentials need to be sent with request
	 * @return http response of the request
	 * @throws IOException
	 */
	public static HttpResponse executeDeleteRequest(String serviceEndPoint,
			boolean secure) throws IOException {
		URL url = (secure) ? new URL(SERVICE_ROOT_URI + "secure/"
				+ serviceEndPoint)
				: new URL(SERVICE_ROOT_URI + serviceEndPoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type",
					"application/atom+xml");
			connection.setRequestProperty("Accept", "application/atom+xml");
			connection.setRequestMethod("DELETE");

			if (secure) {
				connection.setRequestProperty("Authorization", AUTHORIZATION);
			}
			connection.connect();
			return new HttpResponse(connection);

		} finally {
			connection.disconnect();
		}
	}

	/**
	 * Get Inline Count from request
	 * 
	 * @param response
	 *            Body of response
	 * @return inline count
	 * @throws JSONException
	 */
	public static int getInlineCount(String response) throws JSONException {
		JSONObject jo = new JSONObject(response);
		JSONObject jod = (JSONObject) jo.get("d");
		return (Integer.parseInt((String) jod.get("__count")));
	}

	/**
	 * Get the PropertyRef name of an Entity
	 * 
	 * @param entityName
	 *            OData entity name
	 * @return PropertyRef name
	 */
	private static String getPropertyRefByEntityName(String entityName) {

		if (entityName.equals(ES_PRODUCT)) {
			return "ProductId";
		} else if (entityName.equals(ES_CUSTOMER)) {
			return "CustomerId";
		} else if (entityName.equals(ES_PRODUCT_CATEGORY)) {
			return "Category";
		} else if (entityName.equals(ES_SALES_ORDER_HEADER)) {
			return "SalesOrderId";
		} else if (entityName.equals(ES_SALES_ORDER_ITEM)) {
			return "SalesOrderId";
		} else if (entityName.equals(ES_STOCK)) {
			return "ProductId";
		} else if (entityName.equals(ES_PURCHASE_ORDER_HEADER)) {
			return "PurchaseOrderId";
		} else if (entityName.equals(ES_PURCHASE_ORDER_ITEM)) {
			return "PurchaseOrderId";
		} else if (entityName.equals(ES_SUPPLIER)) {
			return "SupplierId";
		} else {
			return null;
		}
	}
	
	/**
	 * Run a http get request
	 * 
	 * @param serviceEndPoint
	 * @return http response
	 * @throws IOException
	 */
	public static HttpResponse executeGetRequest(String serviceEndPoint)
			throws IOException {
		URL url = new URL(SERVICE_ROOT_URI + serviceEndPoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept",
				"application/atom+xml");
		try {
			return new HttpResponse(connection);
		} finally {
			connection.disconnect();
		}
	}

	/**
	 * Run a http post request
	 * 
	 * @param entityName
	 * @param body
	 * @return http response
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static HttpResponse executePostRequest(String entityName, String body)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(SERVICE_ROOT_URI + entityName);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Accept",
					"application/atom+xml");
			connection.setRequestProperty("Content-Type",
					"application/atom+xml");
			DataOutputStream outStream = new DataOutputStream(
					connection.getOutputStream());
			try {
				outStream.writeBytes(body);
				outStream.close();
				return new HttpResponse(connection);
			} finally {
				outStream.close();
			}
		} finally {
			connection.disconnect();
		}
	}


}
