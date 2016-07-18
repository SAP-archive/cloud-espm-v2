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
 * Junits for Customer OData services
 * 
 */
public class CustomerODataIT extends AbstractODataIT {

	private final String ENTITY_NAME = "Customers";
	private Random rand = new Random();
	private static final String FILENAME = "com/sap/espm/model/web/customer.xml";

	/**
	 * Test if Customers are preloaded.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testPreloadedCustomersExists() throws IOException,
			JSONException {
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$inlinecount=allpages&$format=json", true);
		assertEquals("Mismatch in the number of preloaded customers", 41,
				RequestExecutionHelper.getInlineCount(resp.getBody()));
	}

	/**
	 * Test Customer Url Top and Orderby Service Query option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCustomerUrlTopOrderby() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$orderby=CustomerId&$top=1", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Customers not orderedby Customer Id in descending order",
				prefix0(0), jo.getString("CustomerId"));

	}

	/**
	 * Test Customer Url filter Service Query option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCustomerUrlFilter() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$filter=EmailAddress%20eq%20'mirjam.schmidt@sorali.de'&$top=1",
						true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Customer not filtered CustomerId", prefix0(40),
				jo.getString("CustomerId"));
	}

	/**
	 * Test if Customer URL Skip Service Query Option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
/*	@Test
	public void testCustomerUrlSkip() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(ENTITY_NAME
						+ "?$format=json&$orderby=CustomerId&$skip=1", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Customer not expanded by Address", prefix0(1),
				jo.getString("CustomerId"));
	}*/

	/**
	 * Test if Customer URL Select Service Query Option.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCustomerUrlSelect() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$orderby=CustomerId&$skip=1&$top=1&$select=CustomerId,Country",
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
	 * Test Create and Read Customer via URL.
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	/*@Test
	public void testCreateCustomerViaREST() throws IOException, JSONException {
		String email = rand.nextInt(200) + "@sap.com";
		String customerXml = StreamHelper.readFromFile(FILENAME);
		customerXml = customerXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				customerXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=CustomerId%20eq%20'" + id
						+ "'", true);
		assertEquals("Customer not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Added Customer via REST not persisted in db", id,
				jo.getString("CustomerId"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}*/

	/**
	 * Test Update Customer via URL.
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	/*@Test
	public void testUpdateCustomerViaURL() throws IOException, JSONException {

		String email = rand.nextInt(200) + "@sap.com";
		String newPhoneNumber = "9565470312";
		String customerXml = StreamHelper.readFromFile(FILENAME);
		customerXml = customerXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				customerXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=CustomerId%20eq%20'" + id
						+ "'", true);
		assertEquals("Customer not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		customerXml = customerXml.replace(
				"<d:PhoneNumber>5850428367</d:PhoneNumber>", "<d:PhoneNumber>"
						+ newPhoneNumber + "</d:PhoneNumber>");
		resp = RequestExecutionHelper.executePutRequest(ENTITY_NAME + "('" + id
				+ "')", customerXml, true);
		assertEquals("Unable to update Customer via URL Response Message:"
				+ resp.getResponseMessage(), HttpURLConnection.HTTP_NO_CONTENT,
				resp.getResponseCode());
		resp = RequestExecutionHelper.executeGetRequest(ENTITY_NAME
				+ "?$format=json&$filter=CustomerId%20eq%20'" + id + "'", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Updated Customer via REST not persisted in db",
				newPhoneNumber, jo.getString("PhoneNumber"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}*/

	/**
	 * Test Delete Customer via URL.
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	/*@Test
	public void testDeleteCustomerViaURL() throws IOException, JSONException {
		String email = rand.nextInt(200) + "@sap.com";
		String customerXml = StreamHelper.readFromFile(FILENAME);
		customerXml = customerXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				customerXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=CustomerId%20eq%20'" + id
						+ "'", true);
		assertEquals("Customer not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}*/

	/**
	 * Test Function Import GetCustomerByEmailAddress
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
/*	@Test
	public void testGetCustomerByEmailAddress() throws IOException,
			JSONException {
		String email = rand.nextInt(200) + "@sap.com";
		String customerXml = StreamHelper.readFromFile(FILENAME);
		customerXml = customerXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				customerXml, true);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=CustomerId%20eq%20'" + id
						+ "'", true);
		assertEquals("Customer not persisted", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());

		resp = RequestExecutionHelper.executeGetRequest(
				"GetCustomerByEmailAddress?EmailAddress='" + email
						+ "'&$format=json", false);

		assertEquals("Sales Order not confirmed", HttpURLConnection.HTTP_OK,
				resp.getResponseCode());
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());

		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals(
				"Unable to retreive by function import GetCustomerByEmailAddress",
				email, jo.getString("EmailAddress"));
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}*/

	/**
	 * Test Get Customer Details by Anonymous user.
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
/*	@Test
	public void testGetCustomerByAnonymous() throws IOException, JSONException {
		String email = rand.nextInt(200) + "@sap.com";
		String customerXml = StreamHelper.readFromFile(FILENAME);
		customerXml = customerXml.replace(
				"<d:EmailAddress>email</d:EmailAddress>", "<d:EmailAddress>"
						+ email + "</d:EmailAddress>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				customerXml, false);
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=CustomerId%20eq%20'" + id
						+ "'", false);
		assertEquals(
				"Customers OData service not secured for HTTP_GET Request",
				HttpURLConnection.HTTP_UNAUTHORIZED, resp.getResponseCode());
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ id + "')", true);
		assertEquals(
				"Unable to delete Customer via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
	}*/

	private String prefix0(int value) {
		return String.format("1%08d", value);
	}

}