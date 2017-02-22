package com.sap.espm.model.AuthorizationManagement.Handler;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.util.HttpClientUtil;
import com.sap.espm.model.util.ReadProperties;

/**
 * This class is used as a Wrapper to invoke the Authorization API for the
 * particular landscape.
 * <p>
 * Using this Authorization API, we can add/delete or modify Roles for a
 * particular user based on the OAuthToken.
 */
public class AuthApiHandler {

	/**
	 * The landscape parameter of the application, fetched from the
	 * "config.properties" file.
	 */
	private static String landscape = ReadProperties.getInstance().getValue("LandscapeHost");
	/**
	 * The application name parameter of the application, fetched from the
	 * "config.properties" file.
	 */
	private static String appName = ReadProperties.getInstance().getValue("AppName");
	/**
	 * The Account name parameter of the application, fetched from the
	 * "config.properties" file.
	 */
	private static String accountName = ReadProperties.getInstance().getValue("AccountName");

	/**
	 * {@link Logger} instance for logging.
	 */
	private static Logger logger = LoggerFactory.getLogger(AuthApiHandler.class);

	/**
	 * This method is used to get the list of users pertaining to a particular
	 * role.
	 * <p>
	 * For example, if we wish to list the users for a role "Admin", this method
	 * will list all the users for role Admin.
	 * 
	 * @param oAuthToken
	 *            - The input OAuthToken token
	 * @param role
	 *            - The role to fetch the users.
	 * @return - The HTTP response of the body (the list of users)
	 * @throws IOException
	 *             - In case of any exceptions while invoking the API.
	 */
	public String getUsers(String oAuthToken, String role) throws IOException {
		logger.debug("Entering AuthApiHandler.getUsers Method");
		HttpClientUtil client = new HttpClientUtil();
		String url = new StringBuilder("https://api.").append(landscape).append("/authorization/v1/accounts/")
				.append(accountName).append("/apps/").append(appName).append("/roles/users?roleName=").append(role)
				.toString();
		Map<String, String> response = client.getData(url, "Bearer " + oAuthToken);

		if (!response.get("status").equals("200")) {
			logger.error("HTTP getUsers GET Request at AuthApiHandler failed");
			throw new IOException("HTTP getUsers GET Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.getUsers Method");
		return response.get("body");

	}

	/**
	 * This method is used to add a new role for a particular user.
	 * 
	 * @param oAuthToken
	 *            - The OAuthToken for the user.
	 * @param role
	 *            - The new role to be added.
	 * @param users
	 *            - The user (this variable is not used)
	 * @return - The Body of the HTTP response.
	 * @throws IOException
	 *             - In case of any exception while invoking the API to add the
	 *             role for the user.
	 */
	public String addUsers(String oAuthToken, String role, String users) throws IOException {
		logger.debug("Entering AuthApiHandler.addUsers Method");
		HttpClientUtil client = new HttpClientUtil();

		String url = new StringBuilder("https://api.").append(landscape).append("/authorization/v1/accounts/")
				.append(accountName).append("/apps/").append(appName).append("/roles/users?roleName=").append(role)
				.toString();

		Map<String, String> response = client.putData(url, users, "Bearer " + oAuthToken);
		if (!response.get("status").equals("201")) {
			logger.error("HTTP addUsers PUT Request at AuthApiHandler failed");
			throw new IOException("HTTP addUsers PUT Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.addUsers Method");
		return response.get("body");

	}

	/**
	 * This method is used to delete a role of a user.
	 * 
	 * @param oAuthToken
	 *            - The OAuthToken input token
	 * @param role
	 *            - Role to delete.
	 * @param users
	 *            - The user parameter (not used)
	 * @return - The Http Response after invoking the API to delete the role of
	 *         the user.
	 * @throws IOException
	 *             - In case of any exception while delete the user's role.
	 */
	public String deleteUsers(String oAuthToken, String role, String users) throws IOException {

		logger.debug("Entering AuthApiHandler.deleteUsers Method");
		HttpClientUtil client = new HttpClientUtil();
		String url = new StringBuilder("https://api.").append(landscape).append("/authorization/v1/accounts/")
				.append(accountName).append("/apps/").append(appName).append("/roles/users?roleName=").append(role)
				.toString();

		Map<String, String> response = client.deleteData(url, "Bearer " + oAuthToken);

		if (!response.get("status").equals("200")) {
			logger.error("HTTP deleteUsers DELETE Request at AuthApiHandler failed");
			throw new IOException("HTTP deleteUsers DELETE Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.deleteUsers Method");
		return response.get("body");

	}

	/**
	 * This method is used to fetch the roles of a particular user, based on the
	 * input OAuthToken.
	 * <p>
	 * This method in turns calls the HCP API to fetch the roles of the user.
	 * This is a web service call that returns the data via a Map.
	 * 
	 * @param oAuthToken
	 *            - The input OAuthToken token
	 * @return - The body of the response.
	 * @throws IOException
	 *             - In case of any exception while invoking the API to fetch
	 *             the roles of the user.
	 */
	public String getRoles(String oAuthToken) throws IOException {
		logger.debug("Entering AuthApiHandler.getRoles Method");
		HttpClientUtil client = new HttpClientUtil();

		String url = new StringBuilder("https://api.").append(landscape).append("/authorization/v1/accounts/")
				.append(accountName).append("/apps/").append(appName).append("/roles").toString();

		Map<String, String> response = client.getData(url, "Bearer " + oAuthToken);
		if (!response.get("status").equals("200")) {
			logger.error("HTTP addUsers PUT Request at AuthApiHandler failed");
			throw new IOException("HTTP addUsers PUT Request at AuthApiHandler failed");
		}
		logger.debug("Exiting AuthApiHandler.addUsers Method");
		return response.get("body");

	}
}