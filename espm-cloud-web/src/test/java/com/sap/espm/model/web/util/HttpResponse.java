package com.sap.espm.model.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * 
 * HTTP Response helper
 * 
 */
public class HttpResponse {
	private final String body;
	private final int responseCode;
	private final String responseMessage;

	HttpResponse(HttpURLConnection connection) throws IOException {
		responseCode = connection.getResponseCode();
		responseMessage = connection.getResponseMessage();
		InputStream stream = null;
		try {
			if (responseCode != 401) {
				stream = connection.getInputStream();
				body = StreamHelper.readStreamContent(stream);
			} else {
				body = responseMessage;
			}
		} finally {
			if (responseCode != 401) {
				stream.close();
			}
		}
	}

	/**
	 * Get http response body
	 * 
	 * @return http response body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Get http response code
	 * 
	 * @return http response code
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * Get http response message
	 * 
	 * @return http response message
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

}