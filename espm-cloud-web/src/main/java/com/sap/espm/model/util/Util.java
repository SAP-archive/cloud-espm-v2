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

import com.sap.cloud.account.TenantContext;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class Util {

	private static final String JNDI_KEY_CONNECTIVITY_CONFIG = "java:comp/env/ConnectivityConfiguration";
	private static final String DESTINATION_OAUTHAS_TOKEN = ReadProperties.getInstance().getValue("OAuthDestinationName");
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	
	public static String getHttpRequestAsString(HttpServletRequest request){
		String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
		try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
	public static String getAccountName() {
		
		TenantContext context = null;
		try {
			context = (TenantContext) (new InitialContext()).lookup("java:comp/env/tenantContext");
		}
		catch (NamingException ne) {
			LOGGER.error("Failed to get tenant context", ne);
		}
		return context.getTenant().getAccount().getId();
	}
	
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
				LOGGER.error("OAuth Destination Name Error");
				return null;
			}

	}
	
	public static void exceptionResponseHandler(String errorMessage,int responseStatus, Throwable e,HttpServletResponse response) throws IOException{
		
		LOGGER.debug(errorMessage);
		e.printStackTrace();
		LOGGER.debug(e.getMessage());
		response.setStatus(responseStatus);
		response.getWriter().append(errorMessage);
	}
	
	
	
	
}
