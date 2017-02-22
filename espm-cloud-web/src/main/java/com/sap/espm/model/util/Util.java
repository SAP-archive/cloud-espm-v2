package com.sap.espm.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/**
 * This is a static utility class that is used to Fetch the OAuth Connection
 * details and properties.
 * <p>
 * This assumes that you have a JNDI object {@link ConnectivityConfiguration}
 * configured on your JEE compliant Web Server. Using this object we can fetch
 * the "OAuthDestinationName".
 *
 */
public class Util {

	/**
	 * This string constant represents the JNDI Look up value for the
	 * Connectivity Configuration.
	 */
	private static final String JNDI_KEY_CONNECTIVITY_CONFIG = "java:comp/env/ConnectivityConfiguration";

	/**
	 * This string constant reads the value of the OAuthDestinationName. This is
	 * set in the "config.properties" file that in the resources folder. The
	 * property is read via the {@link ReadProperties} utility class.
	 */
	private static final String DESTINATION_OAUTHAS_TOKEN = ReadProperties.getInstance()
			.getValue("OAuthDestinationName");

	/**
	 * The {@link Logger} instance for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

	/**
	 * This main method is used to convert the entire {@link HttpServletRequest}
	 * input to a string object.
	 * <p>
	 * This string may be useful for debugging purposes or in cases where a
	 * String representation of the {@link HttpServletRequest} is required.
	 * 
	 * @param request
	 *            - The input {@link HttpServletRequest} object.
	 * @return - The {@link String} representation of the
	 *         {@link HttpServletRequest}.
	 */
	public static String getHttpRequestAsString(HttpServletRequest request) {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			stringBuilder.append("");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					stringBuilder.append("");
				}
			}
		}
		return stringBuilder.toString();
	}
	/*
	 * public static String getAccountName() {
	 * 
	 * TenantContext context = null; try { context = (TenantContext) (new
	 * InitialContext()).lookup("java:comp/env/tenantContext"); } catch
	 * (NamingException ne) { LOGGER.error("Failed to get tenant context", ne);
	 * } return context.getTenant().getAccount().getId(); }
	 */

	/**
	 * This method is used to fetch the OAuthConection details. For this, first
	 * we fetch the {@link ConnectivityConfiguration} via JNDI using the key
	 * "java:comp/env/ConnectivityConfiguration".
	 * <p>
	 * Once the {@link ConnectivityConfiguration} is obtained, we use this
	 * object to fetch the "OAuthDestinationName". This is a
	 * {@link DestinationConfiguration} object and using this object we retrieve
	 * all the properties which are returned as part of method response.
	 * 
	 * @return - A {@link Map} of the properties of the
	 *         {@link DestinationConfiguration}
	 */
	public static Map<String, String> getOAuthDetails() {
		String accessToken = null;

		Context ctx;
		try {
			ctx = new InitialContext();
			ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx
					.lookup(JNDI_KEY_CONNECTIVITY_CONFIG);

			// get destination configuration for "oauthasTokenEndpoint"
			DestinationConfiguration destConfiguration = configuration.getConfiguration(DESTINATION_OAUTHAS_TOKEN);

			// get all destination properties
			Map<String, String> allDestinationPropeties = destConfiguration.getAllProperties();
			return allDestinationPropeties;
		} catch (NamingException e) {
			LOGGER.error("OAuth Destination Name Error");
			return null;
		}

	}

	/**
	 * Generic utility method to log exceptions and process the exception as
	 * part of the response.
	 * 
	 * @param errorMessage
	 *            The String error message.
	 * @param responseStatus
	 *            - The HTTP Response status code.
	 * @param e
	 *            - The actual {@link Exception}
	 * @param response
	 *            - The {@link HttpServletResponse} to add the response status
	 *            and error code.
	 * @throws IOException
	 *             - In case of any exception while adding the error code to the
	 *             {@link HttpServletResponse} object.
	 */
	public static void exceptionResponseHandler(String errorMessage, int responseStatus, Throwable e,
			HttpServletResponse response) throws IOException {

		LOGGER.debug(errorMessage);
		e.printStackTrace();
		LOGGER.debug(e.getMessage());
		response.setStatus(responseStatus);
		response.getWriter().append(errorMessage);
	}

}
