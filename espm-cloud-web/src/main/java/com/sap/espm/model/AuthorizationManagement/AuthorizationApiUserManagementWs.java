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

public class AuthorizationApiUserManagementWs extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	AuthApiHandler authApiHandler = new AuthApiHandler();
	private static Logger logger = LoggerFactory.getLogger(AuthorizationApiUserManagementWs.class);

	public AuthorizationApiUserManagementWs() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("Entering AuthorizationApiUserManagementWs.doGet Method");
		String oAuthResponse, userList, accessToken;

		JsonElement element;
		JsonObject jsonObj;
		char[] pass = null;
		String roleName = request.getParameter("roleName");
		try {
			if ((pass = passwordKeyStore.getInstance().getPassword("accessToken")) != null) {
				try {
					logger.debug("Accessing Key From KeyStore");
					String passString = new String(pass);

					if (roleName == null) {
						logger.error("roleName Parameter Not Provided");
						response.setStatus(400);
						response.getWriter().append("roleName Parameter Missing");
						return;
					}
					userList = authApiHandler.getUsers(passString, request.getParameter("roleName"));
					response.getWriter().append(userList);
					return;
				} catch (IOException e) {
					logger.debug("Token in PasswordStore Expired");
				}
			}
			logger.debug("Accessing Key From OAuth Api");
			try {
				oAuthResponse = OauthHandler.oauthApiCaller();
			} catch (IOException e) {

				Util.exceptionResponseHandler("Exception occured when oAuthAPI Called", 500, e, response);
				return;
			}
			element = gson.fromJson(oAuthResponse, JsonElement.class);
			jsonObj = element.getAsJsonObject();
			accessToken = jsonObj.get("access_token").getAsString();
			passwordKeyStore.getInstance().setPassword("accessToken", accessToken.toCharArray());
			try {
				userList = authApiHandler.getUsers(accessToken, (roleName != null) ? roleName : "");
				response.getWriter().append(userList);
				return;
			} catch (IOException e) {
				Util.exceptionResponseHandler("authApiHandler.getUsers IOException Error", 500, e, response);
				return;
			}

		} catch (PasswordStorageException e) {

			Util.exceptionResponseHandler("Internal Error", 500, e, response);
		} catch (NamingException e) {

			Util.exceptionResponseHandler("Internal Error", 500, e, response);
		}

		logger.debug("Exiting AuthorizationApiUserManagementWs.doGet Method");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String oAuthResponse, recc, accessToken;
		String body = Util.getHttpRequestAsString(request);
		AuthApiHandler aah = new AuthApiHandler();
		JsonElement element;
		JsonObject jsonObj;
		char[] pass = null;
		String roleName = request.getParameter("roleName");
		try {
			if ((pass = passwordKeyStore.getInstance().getPassword("accessToken")) != null) {
				try {
					logger.debug("Accessing Key From KeyStore");
					String passString = new String(pass);
					if (roleName == null) {
						logger.error("roleName Parameter Not Provided");
						response.setStatus(400);
						response.getWriter().append("roleName Parameter Missing");
						return;
					}

					authApiHandler.addUsers(passString, (roleName != null) ? roleName : "", body);
					return;
				} catch (IOException e) {

					logger.debug("Token Expired");

				}
			}
			logger.debug("Accessing Key From OAuth Api");
			try {
				oAuthResponse = OauthHandler.oauthApiCaller();
			} catch (IOException e) {
				Util.exceptionResponseHandler("Invalid Token", 401, e, response);
				return;
			}
			element = gson.fromJson(oAuthResponse, JsonElement.class);
			jsonObj = element.getAsJsonObject();
			accessToken = jsonObj.get("access_token").getAsString();
			passwordKeyStore.getInstance().setPassword("accessToken", accessToken.toCharArray());
			try {
				recc = aah.addUsers(accessToken, request.getParameter("roleName"), body);
				response.getWriter().append(recc);
				return;
			} catch (IOException e) {
				Util.exceptionResponseHandler("Invalid Token addUsers", 401, e, response);
				return;
			}

		} catch (PasswordStorageException e) {
			Util.exceptionResponseHandler("Internal Error", 500, e, response);
		} catch (NamingException e) {
			Util.exceptionResponseHandler("Internal Error", 500, e, response);
		}

	}
}
