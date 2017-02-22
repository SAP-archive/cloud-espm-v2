package com.sap.espm.model.AuthorizationManagement;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sap.cloud.security.password.PasswordStorageException;
import com.sap.espm.model.AuthorizationManagement.Handler.AuthApiHandler;
import com.sap.espm.model.AuthorizationManagement.Handler.OauthHandler;
import com.sap.espm.model.keystore.passwordKeyStore;
import com.sap.espm.model.util.Util;

/**
 * {@link HttpServlet} used for check the roles of the input user, based on the
 * Authorization token passed as the input.
 * <p>
 * Refer to the {@link AuthApiHandler} for the methods exposed on how to fetch
 * the roles of an input user.
 */
public class AuthorizationApiRoleManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * The {@link Gson} instance used for parsing the HttpRequest any particular
	 * data.
	 */
	private Gson gson = new Gson();

	/**
	 * {@link AuthApiHandler} instance. Refer to the JavaDoc of
	 * {@link AuthApiHandler} for more information.
	 */
	AuthApiHandler authApiHandler = new AuthApiHandler();

	/**
	 * {@link Logger} instance used for logging messages.
	 */
	private static Logger logger = LoggerFactory.getLogger(AuthorizationApiRoleManagement.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.debug("Entering AuthorizationApiRoleManagement.doGet Method");
		String oAuthResponse, userList, accessToken;

		JsonElement element;
		JsonObject jsonObj;
		char[] pass = null;
		try {
			if ((pass = passwordKeyStore.getInstance().getPassword("accessToken")) != null) {
				try {
					logger.debug("Accessing Key From KeyStore");
					String passString = new String(pass);
					userList = authApiHandler.getRoles(passString);
					response.getWriter().append(userList);
					return;
				} catch (IOException e) {
					logger.debug("Token Expired");

				}
			}
			logger.debug("Accessing Key From OAuth Api");
			try {
				oAuthResponse = OauthHandler.oauthApiCaller();
			} catch (IOException e) {

				Util.exceptionResponseHandler("Invalid Token from oAuth Api", 401, e, response);
				return;
			}
			element = gson.fromJson(oAuthResponse, JsonElement.class);
			jsonObj = element.getAsJsonObject();
			accessToken = jsonObj.get("access_token").getAsString();
			passwordKeyStore.getInstance().setPassword("accessToken", accessToken.toCharArray());
			try {
				String roleName = request.getParameter("roleName");
				userList = authApiHandler.getRoles(accessToken);
				response.getWriter().append(userList);
				return;
			} catch (IOException e) {
				Util.exceptionResponseHandler("Invalid Token at getRoles", 401, e, response);
				return;
			}

		} catch (PasswordStorageException e) {
			Util.exceptionResponseHandler("Invalid Token at PasswordStorage", 500, e, response);

		} catch (NamingException e) {
			Util.exceptionResponseHandler("Naming Exception", 500, e, response);
		}
		logger.debug("Exiting AuthorizationApiRoleManagement.doGet Method");

	}

}
