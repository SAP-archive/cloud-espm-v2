package com.sap.espm.model.util;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/**
 * This singleton class is used read the destination properties configured via
 * JNDI.
 * <p>
 * We will create destination with required properties as Consumer key and
 * Consumer Secret
 * 
 *
 */
public class oAuthDetails {
	/**
	 * The JNDI Name to fetch the Connectivity Configuration Details.
	 */
	private static final String JNDI_KEY_CONNECTIVITY_CONFIG = "java:comp/env/ConnectivityConfiguration";
	
	/**
	 * The OAuthDestination Name stored in the properties file (config.properties)
	 */
	private static final String DESTINATION_OAUTHAS_TOKEN = ReadProperties.getInstance()
			.getValue("OAuthDestinationName");

	/**
	 * This method is used to fetch the Connectivity Configuration using JNDI
	 * lookup. Once we have the {@link ConnectivityConfiguration}, we use this
	 * object to fetch the details of the Destination.
	 * 
	 * @return  - The destination properties stored in a {@link Map}.
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

			return null;
		}

	}
}
