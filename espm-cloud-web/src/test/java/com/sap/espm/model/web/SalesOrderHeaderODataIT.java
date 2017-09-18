package com.sap.espm.model.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Random;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.sap.espm.model.web.util.HttpResponse;
import com.sap.espm.model.web.util.RequestExecutionHelper;
import com.sap.espm.model.web.util.StreamHelper;

/**
 * Junits for Sales Order Header Odata services
 * 
 */
public class SalesOrderHeaderODataIT extends AbstractODataIT {
	Random rand = new Random();
	public static final String ENTITY_NAME = "SalesOrderHeaders";
	private static final String SO_FILENAME = "com/sap/espm/model/web/salesOrder.xml";
	private static final String SOH_FILENAME = "com/sap/espm/model/web/salesOrderHeader.xml";

	/**
	 * Test Create Sales Order Header via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCreateSalesOrderHeaderViaREST() throws IOException,
			JSONException {
		String salesOrderHeaderXml = StreamHelper.readFromFile(SOH_FILENAME);
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				salesOrderHeaderXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SalesOrderId%20eq%20'"
						+ id + "'", true);
		assertEquals("Sales Order not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Sales Order Header via REST not persisted in db",
				id, jo.getString("SalesOrderId"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test Create Sales Order via Url.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCreateSalesOrderViaREST() throws IOException, JSONException {
		String salesOrderXml = StreamHelper.readFromFile(SO_FILENAME);
		String id = RequestExecutionHelper.createSalesOrderViaREST(ENTITY_NAME,
				salesOrderXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SalesOrderId%20eq%20'"
						+ id + "'", true);
		assertEquals("Sales Order not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Sales Order Header via REST not persisted in db",
				id, jo.getString("SalesOrderId"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test ConfirmSalesOrder Function Import
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	@Test
	public void testConfirmSalesOrder() throws JSONException, IOException {
		String salesOrderXml = StreamHelper.readFromFile(SO_FILENAME);
		String id = RequestExecutionHelper.createSalesOrderViaREST(ENTITY_NAME,
				salesOrderXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SalesOrderId%20eq%20'"
						+ id + "'", true);
		assertEquals("Sales Order not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		resp = RequestExecutionHelper.executeGetRequest(
				"ConfirmSalesOrder?SalesOrderId='" + id + "'&$format=json",
				true);

		assertEquals("Sales Order not confirmed", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		resp = RequestExecutionHelper.executeGetRequest(ENTITY_NAME
				+ "?$format=json&$filter=SalesOrderId%20eq%20'" + id + "'",
				true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Sales Order Header via REST not persisted in db",
				"P", jo.getString("LifeCycleStatus"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());

	}

	/**
	 * Test Cancel Sales Order Function Import
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCancelSalesOrder() throws IOException, JSONException {
		String salesOrderXml = StreamHelper.readFromFile(SO_FILENAME);
		String id = RequestExecutionHelper.createSalesOrderViaREST(ENTITY_NAME,
				salesOrderXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SalesOrderId%20eq%20'"
						+ id + "'", true);
		assertEquals("Sales Order not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		resp = RequestExecutionHelper
				.executeGetRequest("CancelSalesOrder?SalesOrderId='" + id
						+ "'&$format=json", true);

		assertEquals("Sales Order not confirmed", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		resp = RequestExecutionHelper.executeGetRequest(ENTITY_NAME
				+ "?$format=json&$filter=SalesOrderId%20eq%20'" + id + "'",
				true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Sales Order Header via URL not persisted in db",
				"X", jo.getString("LifeCycleStatus"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test Get SalesOrderHeader Details by Anonymous user.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testGetSalesOrderHeaderByAnonymous() throws IOException,
			JSONException {
		String salesOrderXml = StreamHelper.readFromFile(SO_FILENAME);
		String id = RequestExecutionHelper.createSalesOrderViaREST(ENTITY_NAME,
				salesOrderXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SalesOrderId%20eq%20'"
						+ id + "'", false);
		assertEquals(
				"SalesOrderHeader OData service not secured for HTTP GET request",
				HttpURLConnection.HTTP_UNAUTHORIZED, resp.getResponseCode());
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

}
