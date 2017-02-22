package com.sap.espm.model.oAuth.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.espm.model.oAuth.impl.TwitterLogin;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * This {@link HttpServlet} is used as a Callback to route back to the ESPM
 * application via the Twitter API (on successful login using Twitter
 * credentials).
 * <p>
 * Here we will Get the AccessToken with the oauth_verifier parameter which will
 * be added to the Callback URL
 */
@WebServlet("/TwitterCallBack")
public class TwitterCallBackWs extends HttpServlet {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * {@link AccessToken}
	 */
	public static AccessToken accessToken;

	/**
	 * Public Default Constructor
	 */
	public TwitterCallBackWs() {
		super();

	}

	/**
	 * This is a custom implementation of the doGet method of the
	 * {@link HttpServlet}.
	 * <p>
	 * Here we will call callBackApi method of {@link TwitterLogin} class for
	 * getting the Access token after successful login into Twitter. On
	 * successful login, the response is redirected to ESPM as a callback
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
		String verifier = request.getParameter("oauth_verifier");
		AccessToken at = TwitterLogin.callBackApi(twitter, requestToken, verifier);
		accessToken = at;
		response.sendRedirect(request.getContextPath() + "/webshop?screenName=" + at.getScreenName());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
