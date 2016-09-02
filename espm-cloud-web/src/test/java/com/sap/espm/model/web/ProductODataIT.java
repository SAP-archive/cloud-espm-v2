package com.sap.espm.model.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.sap.espm.model.web.util.HttpResponse;
import com.sap.espm.model.web.util.RequestExecutionHelper;

/**
 * Junits for Product OData services
 * 
 */
public class ProductODataIT extends AbstractODataIT {

	private final String ENTITY_NAME = "Products";

	/**
	 * Test if Products are preloaded.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testPreloadedProductsExists() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$inlinecount=allpages&$format=json", false);
		assertEquals("Mismatch in the number of preloaded products", 106,
				RequestExecutionHelper.getInlineCount(resp.getBody()));
	}

	/**
	 * Test Top and Orderby Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductUrlTopOrderby() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$orderby=ProductId&$top=1", false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Products not orderedby Product Id in ascending order",
				"AD-1000", jo.getString("ProductId"));
	}

	/**
	 * Test filter Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductUrlFilter() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=Price%20eq%201430&$top=1",
				false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Product not filtered Product Id", "HT-1037",
				jo.getString("ProductId"));
	}

	/**
	 * Test Select Service Query Option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductUrlSelect() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$orderby=ProductId%20desc&$skip=1&$top=1&$select=ProductId,Category",
						false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		assertTrue(
				"Selected property category does not exist in the odata service",
				resp.getBody().contains("Category"));
		assertTrue(
				"Non selected property Price still exists in the odata service",
				!resp.getBody().contains("Price"));
	}

	/**
	 * Test Substringof Query Option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductUrlSubstringof() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$filter=substringof('beam',tolower(Category))",
						false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Beamers does not exist in the filtered response",
				"Beamers", jo.getString("Category"));
	}

}
