package com.sap.espm.model.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.Random;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.sap.espm.model.web.util.HttpResponse;
import com.sap.espm.model.web.util.ReSTExecutionHelper;
import com.sap.espm.model.web.util.RequestExecutionHelper;
import com.sap.espm.model.web.util.StreamHelper;

public class StockODataIT extends AbstractODataIT {

	private final String ENTITY_NAME = "Stocks";
	private Random rand = new Random();
	private static final String FILENAME = "com/sap/espm/model/web/stock.xml";

	/**
	 * Test if Stocks are preloaded.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testPreloadedStocksExists() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$inlinecount=allpages&$format=json", true);
		assertEquals("Mismatch in the number of preloaded stocks", 106,
				RequestExecutionHelper.getInlineCount(resp.getBody()));
	}

	/**
	 * Test Top and Orderby Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testStockUrlTopOrderby() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$orderby=ProductId&$top=1", true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		jo = (JSONObject) ja.get(0);
		assertEquals("stock not orderedby Product Id in ascending order",
				"AD-1000", jo.getString("ProductId"));
	}

	/**
	 * Test filter Service Query option via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testStockUrlFilter() throws IOException, JSONException {
		JSONObject jo;
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$filter=ProductId%20eq%20'HT-1037'&$top=1",
						true);
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
	public void testStockUrlSelect() throws IOException, JSONException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(
						ENTITY_NAME
								+ "?$format=json&$orderby=ProductId%20desc&$skip=1&$top=1&$select=MinStock,Quantity",
						true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(resp
				.getBody());
		assertNotNull("Unable to parse JSON response", ja);
		assertTrue(
				"Selected property Quantity does not exist in the odata service",
				resp.getBody().contains("Quantity"));
		assertTrue(
				"Non selected property LotSize still exists in the odata service",
				!resp.getBody().contains("LotSize"));
	}

	/**
	 * Test Update Stock via PUT request
	 * @throws Exception 
	 */
	@Test
	public void testUpdateStock() throws Exception {
		String productId = "HX-" + rand.nextInt(200);
		BigDecimal minStock = BigDecimal.valueOf(10.0);
		String stockXml = StreamHelper.readFromFile(FILENAME);
		stockXml = stockXml.replace("<d:ProductId>ES-1000</d:ProductId>",
				"<d:ProductId>" + productId + "</d:ProductId>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				stockXml, true);
	CloseableHttpResponse resp = ReSTExecutionHelper.executeGetRequest(
				ENTITY_NAME + "?$format=json&$filter=ProductId%20eq%20'"
						+ productId + "'", true);
		assertEquals("Stock not persisted", HttpURLConnection.HTTP_OK,resp.getStatusLine().getStatusCode());
		stockXml = stockXml.replace(
				"<d:MinStock m:type=\"Edm.Decimal\">6.000</d:MinStock>",
				"<d:MinStock m:type=\"Edm.Decimal\">" + minStock
						+ "</d:MinStock>");
		resp = ReSTExecutionHelper.executePutRequest(ENTITY_NAME + "('" + id
				+ "')", stockXml, true);
		assertEquals("Unable to update Stock via REST. Response Message:"
				+ resp.getStatusLine().getReasonPhrase(), HttpURLConnection.HTTP_NO_CONTENT,
				resp.getStatusLine().getStatusCode());
		resp = ReSTExecutionHelper.executeGetRequest(ENTITY_NAME
				+ "?$format=json&$filter=ProductId%20eq%20'" + productId + "'",
				true);
		JSONArray ja = RequestExecutionHelper.getJSONArrayofResults(ReSTExecutionHelper.readResponse(resp));
		assertNotNull("Unable to parse JSON response", ja);
		JSONObject jo = (JSONObject) ja.get(0);
		assertEquals("Updated Stock via REST not persisted in db", minStock,
				BigDecimal.valueOf(Double.valueOf(jo.getString("MinStock"))));
		resp = ReSTExecutionHelper.executeDeleteRequest(ENTITY_NAME + "('"
				+ productId + "')", true);
		assertEquals(
				"Unable to delete Stock via REST or incorrect HTTP Response Code:"
						+ resp.getStatusLine().getReasonPhrase(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getStatusLine().getStatusCode());

	}
}