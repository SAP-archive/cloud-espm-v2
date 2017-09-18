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
 * Junits for Address Odata services.
 * 
 */
public class ProductCategoryODataIT extends AbstractODataIT {

	private final String ENTITY_NAME = "ProductCategories";

	/**
	 * Test if Product Categories are preloaded.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testPreloadedProductCategoriesExists() throws IOException,
			JSONException {
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$inlinecount=allpages&$format=json", false);
		assertEquals("Mismatch in the number of preloaded product categories",
				22, RequestExecutionHelper.getInlineCount(resp.getBody()));
	}

	/**
	 * Test Top and Orderby Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductCategoryUrlTopOrderby() throws IOException,
			JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$orderby=Category&$top=1", false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals(
				"Product Category not orderedby Category in ascending order",
				"Beamers", jo.getString("Category"));
	}

	/**
	 * Test filter Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductCategoryUrlFilter() throws IOException,
			JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$filter=Category%20eq%20'Mice'&$top=1",
						false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("Product Category not filtered Category", "Mice",
				jo.getString("Category"));
	}

	/**
	 * Test Skip Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */

	/**
	 * Test Select Service Query Option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testProductCategoryUrlSelect() throws IOException,
			JSONException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$orderby=Category&$skip=1&$top=1&$select=Category,CategoryName",
						false);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		assertTrue(
				"Selected property category does not exist in the odata service",
				resp.getBody().contains("Category"));
		assertTrue(
				"Non selected property Main Category still exists in the odata service",
				!resp.getBody().contains("MainCategory"));
	}

}
