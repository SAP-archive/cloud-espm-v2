package com.sap.espm.model.oAuth.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.espm.model.oAuth.impl.TwitterLogin;
import com.sap.espm.model.oAuth.impl.TwitterUpdate;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * 
 * WebService for Twitter Call Back 
 * 
 */
@WebServlet("/TwitterCallBack")
public class TwitterCallBackWs extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static AccessToken accessToken;
	
    public TwitterCallBackWs() {
        super();
        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Twitter twitter = (Twitter) request.getSession()
				.getAttribute("twitter");
		RequestToken requestToken = (RequestToken) request.getSession()
				.getAttribute("requestToken");
		String verifier = request.getParameter("oauth_verifier");
		AccessToken at = TwitterLogin.callBackApi(twitter, requestToken, verifier);
		accessToken = at;
		response.sendRedirect(request.getContextPath() + "/webshop?screenName="+at.getScreenName());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
