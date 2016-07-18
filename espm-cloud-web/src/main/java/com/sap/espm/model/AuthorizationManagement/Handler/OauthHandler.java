package com.sap.espm.model.AuthorizationManagement.Handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.util.HttpClientUtil;
import com.sap.espm.model.util.Util;

public class OauthHandler {

	// static HttpResponse oAuthResponse;
	private static final String CLIENTID = "ClientId";
	private static final String CLIENT_SECRET = "ClientSecret";
	private static final Logger LOGGER = LoggerFactory.getLogger(OauthHandler.class);

	public static String oauthApiCaller() throws IOException {

		Map<String, String> oAuthResponse;
		HttpClientUtil client = new HttpClientUtil();
		Map<String, String> oAuthDetails = Util.getOAuthDetails();
		if (oAuthDetails == null) {
			throw new IOException();
		}

		String clientId = oAuthDetails.get(OauthHandler.CLIENTID);
		String clientSecret = oAuthDetails.get(OauthHandler.CLIENT_SECRET);
		String oAuthDestination = oAuthDetails.get("URL");
		String combinedKeySet = new StringBuilder(clientId).append(":").append(clientSecret).toString();
		String base64String = DatatypeConverter.printBase64Binary(combinedKeySet.getBytes(StandardCharsets.UTF_8));

		oAuthResponse = client.postData(oAuthDestination, "", "Basic " + base64String);

		if (!oAuthResponse.get("status").equals("200")) {
			LOGGER.error("HTTP Request at OauthHandler failed");
			throw new IOException("HTTP Request at OauthHandler failed");
		}
		return oAuthResponse.get("body");
	}
}
