package com.sap.espm.model.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 * 
 * Junits for Supplier OData services
 * 
 */

public class SupplierODataIT extends AbstractODataIT {
	private final String ENTITY_NAME = "Suppliers";
	private Random rand = new Random();
	private static final String FILENAME = "com/sap/espm/model/web/supplier.xml";

	/**
	 * Test if Suppliers are preloaded.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testPreloadedSuppliersExists() throws IOException,
			JSONException {
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$inlinecount=allpages&$format=json", true);
		assertEquals("Mismatch in the number of preloaded suppliers", 9,
				RequestExecutionHelper.getInlineCount(resp.getBody()));
	}

	/**
	 * Test Supplier Url Top and Orderby Service Query option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testSupplierUrlTopOrderby() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$orderby=SupplierId&$top=1", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Suppliers not orderedby Supplier Id in ascending order",
				"100000041", jo.getString("SupplierId"));

	}

	/**
	 * Test Supplier Url filter Service Query option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testSupplierUrlFilter() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$filter=EmailAddress%20eq%20'franklin.jones@pear-computing.com'&$top=1",
						true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Supplier not filtered Supplier Id", "100000045",
				jo.getString("SupplierId"));

	}
	/**
	 * Test if Supplier URL Select Service Query Option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testSupplierUrlSelect() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$orderby=SupplierId&$skip=1&$top=1&$select=SupplierId,Country",
						true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		ja.get(0);
		assertTrue(
				"Selected property Country does not exist in the odata service",
				resp.getBody().contains("Country"));
		assertTrue(
				"Non selected property City still exists in the odata service",
				!resp.getBody().contains("City"));
	}

	/**
	 * Test Create and Read Supplier via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */

	@Test
	public void testCreateSupplierViaREST() throws IOException, JSONException {
		String supplierXml = StreamHelper.readFromFile(FILENAME);
		String email = rand.nextInt(200) + "@sap.com";
		supplierXml = supplierXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				supplierXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SupplierId%20eq%20'" + id
						+ "'", true);
		assertEquals("Supplier not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Supplier via REST not persisted in db", id,
				jo.getString("SupplierId"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Supplier via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test Delete Supplier via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testDeleteSupplierViaREST() throws IOException, JSONException {
		String supplierXml = StreamHelper.readFromFile(FILENAME);
		String email = rand.nextInt(200) + "@sap.com";
		supplierXml = supplierXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				supplierXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SupplierId%20eq%20'" + id
						+ "'", true);
		assertEquals("Supplier not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test Update Supplier via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testUpdateSupplierViaREST() throws IOException, JSONException {
		String supplierXml = StreamHelper.readFromFile(FILENAME);
		String newPhoneNumber = "111111";
		String email = rand.nextInt(200) + "@sap.com";
		supplierXml = supplierXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				supplierXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SupplierId%20eq%20'" + id
						+ "'", true);
		assertEquals("Supplier not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		supplierXml = supplierXml.replace(
				"<d:PhoneNumber>5899428367</d:PhoneNumber>", "<d:PhoneNumber>"
						+ newPhoneNumber + "</d:PhoneNumber>");
		resp = RequestExecutionHelper.executePutRequest(ENTITY_NAME + "('" + id
				+ "')", supplierXml, true);
		assertEquals("Unable to update Supplier via URL Response Message:"
				+ resp.getResponseMessage(), HttpURLConnection.HTTP_NO_CONTENT,
				resp.getResponseCode());
		resp = RequestExecutionHelper.executeGetRequest(ENTITY_NAME
				+ "?$format=json&$filter=SupplierId%20eq%20'" + id + "'", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Updated Supplier via URL not persisted in db",
				newPhoneNumber, jo.getString("PhoneNumber"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Supplier via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

	/**
	 * Test Get Supplier by Anonymous user.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */

	@Test
	public void testGetSupplierByAnonymous() throws IOException, JSONException {
		String supplierXml = StreamHelper.readFromFile(FILENAME);
		String email = rand.nextInt(200) + "@sap.com";
		supplierXml = supplierXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				supplierXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=SupplierId%20eq%20'" + id
						+ "'", false);
		assertEquals("Supplier OData service not secure for HTTP_GET request",
				HttpURLConnection.HTTP_UNAUTHORIZED, resp.getResponseCode());

		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Supplier via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}

}
