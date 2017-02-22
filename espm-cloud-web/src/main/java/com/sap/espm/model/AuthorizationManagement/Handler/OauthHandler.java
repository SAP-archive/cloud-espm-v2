package com.sap.espm.model.AuthorizationManagement.Handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.util.HttpClientUtil;
import com.sap.espm.model.util.Util;

/**
 * This class acts as a wrapper to get the OAuth Details via JNDI. Refer to
 * class {@link Util} for more information on the implementation on how to fetch
 * the details.
 * <p>
 * Using the OAuthDetails, we use the ClientId and the Client Secret to fetch
 * the OAuthDetails.
 */
public class OauthHandler {

	// static HttpResponse oAuthResponse;
	private static final String CLIENTID = "User";
	private static final String CLIENT_SECRET = "Password";

	/**
	 * The {@link Logger} instance used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OauthHandler.class);

	/**
	 * This method calls the OAuthHandler for the getting the OAuth Token.
	 * 
	 * @return - The String response from the OAuthApi.
	 * @throws IOException
	 *             - In case of any exception while invoking the OAuthApi.
	 */
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
