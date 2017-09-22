package com.sap.espm.services;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sap.espm.services.Utility;

import java.io.*;
import java.util.HashMap;

public class OdataClient {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(OdataClient.class);
	private String query;

	public OdataClient() {
		super();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	//
	// public static void main(String[] args) {
	// Logger logger = Logger.getLogger(OdataClient.class.getName());
	// Utility util = new Utility();
	// OdataClient client = new OdataClient();
	// String jamResponse = client.getDataFromService("TR_middle-aged", util);
	// logger.info("jamResponse " + jamResponse);
	// }

	public String getDataFromService(String classPredicted, Utility util) throws UnhandledException {
		// Create an instance of HttpClient.
		util.getLogger().info("inside getDataFromService" + classPredicted);
		String query = util.get(classPredicted);
		util.getLogger().info("inside getDataFromService query from utils " + query);
		this.setQuery(query);
		HttpClient httpClient = createClient();
		String jamResponse = queryData(httpClient, util, classPredicted);
		util.getLogger().info("jamresponse in getDataFrom Service " + jamResponse);
		return jamResponse;
	}

	private HttpClient createClient() {
		HttpClient client = new HttpClient();
		return client;
	}

	private String queryData(HttpClient httpClient, Utility util, String classPredicted) throws UnhandledException {
		String jamResponse = "";
		if (this.getQuery() != null && !this.getQuery().isEmpty()) {
			util.getLogger().info("inside this.getQuery " + this.getQuery());
			String url = util.get("espmendpoint");
			url = url + this.getQuery();
			GetMethod getConnection = new GetMethod(url);
			util.getLogger().info("url in query data odata client " + url);
			try {
				// Execute the method.
				int statusCode = httpClient.executeMethod(getConnection);
				if (statusCode != HttpStatus.SC_OK) {
					util.getLogger().info("Status " + getConnection.getStatusLine());
				}
				// Read the response body.
				byte[] responseBody = getConnection.getResponseBody();
				jamResponse = parseResponse(responseBody, util, classPredicted);
			} catch (HttpException e) {
				util.getLogger().info("Fatal protocol violation: " + e.getMessage());
				throw new UnhandledException("Fatal protocol violation: " + e.getMessage());
			} catch (IOException e) {
				util.getLogger().info("Fatal transport error: " + e.getMessage());
				throw new UnhandledException("Fatal transport error: " + e.getMessage());
			} finally {
				// Release the connection.
				util.getLogger().info("Releasing the Connection");
				getConnection.releaseConnection();
			}
		} else {
			util.getLogger().info("Inside default Jam response");
			jamResponse = util.get("defaultjamresponse");
		}
		return jamResponse;
	}

	private String parseResponse(byte[] responseBody, Utility util, String classPredicted) throws UnhandledException {
		String jamResponse = "";
		String jsonObjectString = "{\"Text\":\"" + util.get(classPredicted + "_text");
		try {
			JSONParser parser = new JSONParser();
			util.getLogger().info("Inside parser " + parser.parse(new String(responseBody)));
			JSONObject response = (JSONObject) parser.parse(new String(responseBody.clone()));
			JSONObject tempResponse = (JSONObject) response.get("d");
			JSONArray resultSet = (JSONArray) tempResponse.get("results");
			HashMap<String, String> products = new HashMap<>();
			HashMap<String, String> values = new HashMap<String, String>();
			for (int i = 0; i < resultSet.size(); i++) {
				JSONObject object = (JSONObject) resultSet.get(i);
				String[] keyProperties = (util.get("keys")).split(",");
				String[] valueProperties = (util.get("values")).split(",");
				for (int j = 0; j < valueProperties.length; j++) {
					for (int k = 0; k < keyProperties.length; k++) {
						String id = getValue(keyProperties[k], null, object);
						id = id.replaceAll("\"", "inches"); 
						String value = values.get(id + valueProperties[j]);
						if (value == null) {
							value = getValue(valueProperties[j], value, object);
						} else {
							String tempValue = getValue(valueProperties[j], value, object);
							if (!value.equals(tempValue)) {
								value = value + "; " + getValue(valueProperties[j], value, object);
							}
						}
						values.put(id + valueProperties[j], value);
						products.put(id + " (" + valueProperties[j], values.get(id + valueProperties[j]));
					}
				}
			}
			jsonObjectString = jsonObjectString + products.toString().substring(1).replaceAll("=", " - ")
					.replaceAll(",", "), ").replaceAll(";", ",").replaceAll("}", ")") + "\"}";
			util.getLogger().info("json Object String " + jsonObjectString);
			jamResponse = jsonObjectString;
		} catch (ParseException e) {
			util.getLogger().error("Parser Error: " + e.getMessage());
			throw new UnhandledException("Parser Error: " + e.getMessage());
		} finally {
			if (!jamResponse.equals("") && jamResponse != null) {
				util.getLogger().error("Inside finally in odata client " + jamResponse);
			} else {
				jamResponse = util.get("defaultjamresponse");
			}
		}
		return jamResponse;
	}

	private String getValue(String valueProperties, String value, JSONObject object) {
		if (valueProperties.contains(".")) {
			String valueName = valueProperties;
			String[] splittedValueProperties = valueName.split("\\.");
			String tempObject = "";
			for (int l = 0; l < splittedValueProperties.length; l++) {
				tempObject = (String) ((JSONObject) object.get(splittedValueProperties[l]))
						.get(splittedValueProperties[l + 1]);
				l = l + 1;
			}
			value = tempObject;
		} else {
			value = object.get(valueProperties).toString();
		}
		return value;
	}

}
