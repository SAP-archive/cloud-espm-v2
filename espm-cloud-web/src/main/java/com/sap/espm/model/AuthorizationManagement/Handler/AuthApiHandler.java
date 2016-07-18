package com.sap.espm.model.AuthorizationManagement.Handler;


import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.sap.espm.model.util.HttpClientUtil;
import com.sap.espm.model.util.ReadProperties;

public class AuthApiHandler{

		private static String landscape = ReadProperties.getInstance().getValue("LandscapeHost");
		private static String appName = ReadProperties.getInstance().getValue("AppName");
		private static String accountName = ReadProperties.getInstance().getValue("AccountName");
		

	private static Logger logger = LoggerFactory.getLogger(AuthApiHandler.class);
	
	public String getUsers(String oAuthToken, String role) throws IOException{
		logger.debug("Entering AuthApiHandler.getUsers Method");
		HttpClientUtil client = new HttpClientUtil();
		String url =  new StringBuilder("https://api.")
		         .append(landscape)
		         .append("/authorization/v1/accounts/")
		         .append(accountName)
		         .append("/apps/")
		         .append(appName)
		         .append("/roles/users?roleName=")
		         .append(role).toString();	
		Map<String,String> response = client.getData(url ,"Bearer "+ oAuthToken);
		
		if(!response.get("status").equals("200")){
			logger.error("HTTP getUsers GET Request at AuthApiHandler failed");
			throw new IOException("HTTP getUsers GET Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.getUsers Method");
		return response.get("body");
		
		
	}
	
  public String addUsers(String oAuthToken, String role, String users) throws IOException{
	    logger.debug("Entering AuthApiHandler.addUsers Method");
		HttpClientUtil client = new HttpClientUtil();
		
		String url =  new StringBuilder("https://api.")
		         .append(landscape)
		         .append("/authorization/v1/accounts/")
		         .append(accountName)
		         .append("/apps/")
		         .append(appName)
		         .append("/roles/users?roleName=")
		         .append(role).toString();		
		
		Map<String,String> response = client.putData(url ,users,"Bearer "+ oAuthToken);
		if(!response.get("status").equals("201")){
			logger.error("HTTP addUsers PUT Request at AuthApiHandler failed");
			throw new IOException("HTTP addUsers PUT Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.addUsers Method");
		return response.get("body");
		
		
	}

public String deleteUsers(String oAuthToken, String role, String users) throws IOException{
	
	logger.debug("Entering AuthApiHandler.deleteUsers Method");
	HttpClientUtil client = new HttpClientUtil();
	String url =  new StringBuilder("https://api.")
	         .append(landscape)
	         .append("/authorization/v1/accounts/")
	         .append(accountName)
	         .append("/apps/")
	         .append(appName)
	         .append("/roles/users?roleName=")
	         .append(role).toString();
	
	Map<String,String> response = client.deleteData(url ,"Bearer "+ oAuthToken);
	
	if(!response.get("status").equals("200")){
		logger.error("HTTP deleteUsers DELETE Request at AuthApiHandler failed");
		throw new IOException("HTTP deleteUsers DELETE Request at AuthApiHandler failed");
	}
	logger.debug("Exiting AuthApiHandler.deleteUsers Method");
	return response.get("body");
	
}

public String getRoles(String oAuthToken) throws IOException{
	logger.debug("Entering AuthApiHandler.getRoles Method");
	HttpClientUtil client = new HttpClientUtil();
	
		
	String url =  new StringBuilder("https://api.")
	         .append(landscape)
	         .append("/authorization/v1/accounts/")
	         .append(accountName)
	         .append("/apps/")
	         .append(appName)
	         .append("/roles")
	         .toString();
	
	Map<String,String> response = client.getData(url ,"Bearer "+ oAuthToken);
	if(!response.get("status").equals("200")){
		logger.error("HTTP addUsers PUT Request at AuthApiHandler failed");
		throw new IOException("HTTP addUsers PUT Request at AuthApiHandler failed");
	}
	logger.debug("Exiting AuthApiHandler.addUsers Method");
	return response.get("body");
	
}
}