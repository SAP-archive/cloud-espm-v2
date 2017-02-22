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

/**
 * This is a utility class that acts as a wrapper to invoke a service/URL for
 * HTTP operations like GET/POST/PUT/DELETE. Along with the HTTP request, the
 * Authorization token will be sent in the request header (if provided in the
 * input).
 * <p>
 * Kindly refer to the javaDocs for the individual methods for what information
 * to provide along with the HTTP Request.
 *
 */
public class HttpClientUtil {

	/**
	 * The {@link Logger} instance for logging.
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * This method is used to perform HTTP GET operations based on the URL
	 * passed as input string format and the Authorization token. This (token)
	 * will be passed as request parameter.
	 * 
	 * @param url
	 *            - The URL to access for the GET Operation
	 * @param authorization
	 *            - The Authorization token
	 * @return - A {@link Map} implementation containing the overall status of
	 *         the operation.
	 * @throws IOException
	 *             - In case of any exception while invoking the service.
	 */
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

	/**
	 * This method is used to perform HTTP POST operations based on the input
	 * URL along with the Authorization token passed as input.
	 * 
	 * @param url
	 *            - The URL to invoke
	 * @param body
	 *            - The Body content passed in the form of a string. This will
	 *            be sent as part of the POST request.
	 * @param authorization
	 *            - The authorization token.
	 * @return - A {@link Map} implementation containing the status and the
	 *         result.
	 * @throws IOException
	 *             - In case of any exception while invoking the service.
	 */
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

	/**
	 * This method is used to perform HTTP PUT operations based on the input URL
	 * along with the Authorization token passed as input.
	 * 
	 * @param url
	 *            - The URL to invoke.
	 * @param body
	 *            - The Body content to be sent along with the PUT request.
	 * @param authorization
	 *            - The authorization token.
	 * @return - The {@link Map} implementation containing the status and the
	 *         result.
	 * @throws IOException
	 *             - In case of any exception while accessing the URL.
	 */
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

	/**
	 * This method is used to perform HTTP DELETE operations based on the input
	 * URL along with the Authorization token passed as input.
	 * 
	 * @param url
	 *            - The URL to invoke.
	 * @param authorization
	 *            - The authorization token to be sent along with the request.
	 * @return - The {@link Map} implementation that contains the status of the
	 *         method.
	 * @throws IOException
	 *             - In case of any exception while invoking the service.
	 */
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

	/**
	 * This private method is used to generate the request headers what will be
	 * sent along with the request. This method generates a
	 * {@link HttpURLConnection} object that sets the Authorization token as
	 * part of the request header.
	 * 
	 * @param method
	 *            - The HTTP method (example GET, PUT, POST, etc).
	 * @param url
	 *            - The URl to invoke.
	 * @param authorization
	 *            - The Authorization token
	 * @return - The {@link HttpURLConnection} with the header parameters set.
	 * @throws MalformedURLException
	 *             - In case of any exception if the input data is not correct.
	 * @throws IOException
	 *             - In case of any exception while invoking the service.
	 */
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

	/**
	 * This method is used to handle the response from service. This method
	 * stores the response from the service (data along with response code) and
	 * stores the data in a {@link Map} implementation.
	 * 
	 * @param connection
	 *            - The {@link HttpURLConnection} to invoke the service.
	 * @return - A {@link Map} implementation containing the response code and
	 *         other information.
	 * @throws IOException
	 *             - In case of any exception while invoking the service.
	 */
	private Map<String, String> connectionResponseHandler(HttpURLConnection connection) throws IOException {
		logger.debug("Entering Method HttpClientUtil.connectionResponseHandler");

		Map<String, String> extractedResult = Collections.<String, String>emptyMap();
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		String response = str.toString();
		extractedResult.put("body", response);
		logger.debug("Exiting Method HttpClientUtil.connectionResponseHandler");
		return extractedResult;

	}

	/**
	 * This method is called to set the HTTP Body as part of the request.
	 * 
	 * @param connection
	 *            - The {@link HttpURLConnection} object.
	 * @param body
	 *            - The Body parameter, this could be in XML or JSON project.
	 * @throws IOException
	 *             - In case of any exceptions while setting the body.
	 */
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
