package com.sap.espm.model.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public Map<String, String> getData(String url, String authorization) throws IOException {
		logger.debug("Entering Method HttpClientUtil.getData");
		Map<String, String> extractedResult = null;
		HttpURLConnection connection = createConnection("GET", url, authorization);
		try {
			extractedResult = connectionResponseHandler(connection);
		} finally {
			connection.disconnect();
		}
		logger.debug("Exiting Method HttpClientUtil.getData");
		return extractedResult;
	}

	public Map<String, String> postData(String url, String body, String authorization) throws IOException {
		logger.debug("Entering Method HttpClientUtil.postData");
		Map<String, String> extractedResult = null;
		HttpURLConnection connection = createConnection("POST", url, authorization);
		try {
			setRequestBody(connection, body);
			extractedResult = connectionResponseHandler(connection);
		} finally {
			connection.disconnect();
		}
		logger.debug("Exiting Method HttpClientUtil.postData");
		return extractedResult;
	}

	public Map<String, String> putData(String url, String body, String authorization) throws IOException {
		logger.debug("Entering Method HttpClientUtil.putData");
		Map<String, String> extractedResult = null;
		HttpURLConnection connection = createConnection("PUT", url, authorization);
		try {
			setRequestBody(connection, body);
			extractedResult = connectionResponseHandler(connection);

		} finally {
			connection.disconnect();
		}
		logger.debug("Exiting Method HttpClientUtil.putData");
		return extractedResult;
	}

	public Map<String, String> deleteData(String url, String authorization) throws IOException {
		logger.debug("Entering HttpClientUtil.deleteData Method");
		Map<String, String> extractedResult = null;
		HttpURLConnection connection = createConnection("GET", url, authorization);
		try {
			extractedResult = connectionResponseHandler(connection);
		} finally {
			connection.disconnect();
		}
		logger.debug("Exiting Method HttpClientUtil.deleteData");
		return extractedResult;

	}

	private HttpURLConnection createConnection(String method, String url, String authorization)
			throws MalformedURLException, IOException {
		logger.debug("Entering Method HttpClientUtil.createConnection");
		URL requestUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		connection.setRequestMethod(method);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Authorization", authorization);
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		logger.debug("Exiting Method HttpClientUtil.createConnection");
		return connection;
	}

	private Map<String, String> connectionResponseHandler(HttpURLConnection connection) throws IOException {
		logger.debug("Entering Method HttpClientUtil.connectionResponseHandler");

		Map<String, String> extractedResult = Collections.<String, String> emptyMap();
		if (connection == null) {
			return extractedResult;
		}
		extractedResult = new HashMap<String, String>();
		extractedResult.put("status", String.valueOf(connection.getResponseCode()));

		InputStream stream = connection.getInputStream();
		if (stream == null) {
			return extractedResult;
		}

		StringBuilder str = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		String response = str.toString();
		extractedResult.put("body", response);
		logger.debug("Exiting Method HttpClientUtil.connectionResponseHandler");
		return extractedResult;

	}

	private void setRequestBody(HttpURLConnection connection, String body) throws IOException {
		logger.debug("Entering Method HttpClientUtil.setRequestBody");
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		try {
			outStream.writeBytes(body);
			outStream.close();

		} finally {
			outStream.close();
		}
		logger.debug("Exiting Method HttpClientUtil.setRequestBody");
	}

}
