package com.sap.espm.model.util;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class oAuthDetails {
	private static final String JNDI_KEY_CONNECTIVITY_CONFIG = "java:comp/env/ConnectivityConfiguration";
	private static final String DESTINATION_OAUTHAS_TOKEN = ReadProperties.getInstance().getValue("OAuthDestinationName");
	
	public static Map<String,String> getOAuthDetails(){
		String accessToken = null;
	
			Context ctx;
			try {
				ctx = new InitialContext();
				ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx.lookup(JNDI_KEY_CONNECTIVITY_CONFIG);

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
