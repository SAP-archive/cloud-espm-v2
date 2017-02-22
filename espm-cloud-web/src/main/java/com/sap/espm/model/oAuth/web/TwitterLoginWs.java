package com.sap.espm.model.oAuth.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.espm.model.oAuth.impl.TwitterLogin;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

/**
 * This {@link HttpServlet} is used to Login into Twitter by passing Consumer
 * Key and Consumer Secret Key from the destination as parameters to the Twitter
 * API
 * <p>
 * The doGet method of this {@link Servlet} will use the {@link TwitterLogin} to fetch
 * the {@link Twitter} from Twitter API.
 *
 */
@WebServlet("/TwitterLogin")
public class TwitterLoginWs extends HttpServlet {
	/**
	 * Default serial Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TwitterLoginWs() {
		super();

	}

	/**
	 * This is a custom implementation of the doGet method of the
	 * {@link HttpServlet}.
	 * <p>
	 * Here, we will call loginReqApi method of {@link TwitterLogin} class for
	 * getting the {@link Twitter} instance. As a response we will get OAuth
	 * Token from Twitter API.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().setAttribute("twitter", TwitterLogin.loginReqApi());
		try {
			StringBuffer callbackURL = request.getRequestURL();
			int index = callbackURL.lastIndexOf("/");
			callbackURL.replace(index, callbackURL.length(), "").append("/TwitterCallbackWs");
			String callUrl = callbackURL.toString();

			// If You are behind the proxy then replace localhost in callUrl
			// with your ipv4 proxy address
			// callUrl = callUrl.replaceAll("localhost", "10.53.214.185");
			RequestToken requestToken = TwitterLogin.loginReqApi().getOAuthRequestToken(callUrl);
			request.getSession().setAttribute("requestToken", requestToken);
			response.sendRedirect(requestToken.getAuthenticationURL());
		} catch (TwitterException e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
