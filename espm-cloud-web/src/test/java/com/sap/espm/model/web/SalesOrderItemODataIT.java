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
 * Junits for Sales Order Item Odata services
 * 
 */
public class SalesOrderItemODataIT extends AbstractODataIT {
	private final String ENTITY_NAME = "SalesOrderItems";
	private static final String FILENAME = "com/sap/espm/model/web/salesOrderItem.xml";
	private static final String SOH_FILENAME = "com/sap/espm/model/web/salesOrderHeader.xml";
	Random rand = new Random();

	/**
	 * Test creation of Sales Order Item via URL.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	public void testCreateSalesOrderItemViaURL() throws IOException,
			JSONException {
		String salesOrderItemXml = StreamHelper.readFromFile(FILENAME);
		String salesOrderHeaderXml = StreamHelper.readFromFile(SOH_FILENAME);
		String soid = RequestExecutionHelper.createEntityViaREST(
				"SalesOrderHeaders", salesOrderHeaderXml, false);
		salesOrderItemXml = salesOrderItemXml.replace(
				"<d:SalesOrderId>600000001</d:SalesOrderId>",
				"<d:SalesOrderId>" + soid + "</d:SalesOrderId>");
		String id = RequestExecutionHelper.createEntityViaREST(ENTITY_NAME,
				salesOrderItemXml, true);
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
		resp = RequestExecutionHelper.executeDeleteRequest(ENTITY_NAME
				+ "(SalesOrderId='" + id + "',ItemNumber=10)", true);
		assertEquals(
				"Unable to delete Sales Order Header via REST or incorrect HTTP Response Code:"
						+ resp.getResponseMessage(),
				HttpURLConnection.HTTP_NO_CONTENT, resp.getResponseCode());
		resp = RequestExecutionHelper.executeDeleteRequest(
				"SalesOrderHeaders('" + soid + "')", true);
	}
}
