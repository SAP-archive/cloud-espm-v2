package com.sap.espm.model.oAuth.web;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.espm.model.oAuth.impl.TwitterLogin;

import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

/**
 * 
 * WebService for Twitter login
 * 
 */


@WebServlet("/TwitterLogin")
public class TwitterLoginWs extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public TwitterLoginWs() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getSession().setAttribute("twitter", TwitterLogin.loginReqApi());
		try {
			StringBuffer callbackURL = request.getRequestURL();
			System.out
					.println("TwitterLoginServlet:callbackURL:" + callbackURL);
			int index = callbackURL.lastIndexOf("/");
			callbackURL.replace(index, callbackURL.length(), "").append(
					"/TwitterCallbackWs");
			String callUrl = callbackURL.toString();
			
			//If You are behind the proxy then replace localhost in callUrl with your ipv4 proxy address
			//callUrl = callUrl.replaceAll("localhost", "10.53.214.185");
			RequestToken requestToken = TwitterLogin.loginReqApi().getOAuthRequestToken(callUrl);
			request.getSession().setAttribute("requestToken", requestToken);
			response.sendRedirect(requestToken.getAuthenticationURL());
		} catch (TwitterException e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
