package com.sap.espm.model.oAuth.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.oAuth.impl.TwitterUpdate;

import twitter4j.JSONObject;
import twitter4j.Twitter;

/**
 * This {@link HttpServlet} is used to Tweet via the Twitter API.
 *
 */
public class TwitterUpdateWs extends HttpServlet {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterUpdateWs.class);
	
	/**
	 * Defualt Serial Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor.
	 */
	public TwitterUpdateWs() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * This is a custom implementation of the dopPst method of the
	 * {@link HttpServlet}.
	 * <p>
	 * Here, we will convert the {@link InputStream} of the request into a proper format and then
	 * send this to {@link TwitterUpdate} to send out a tweet.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					LOGGER.error(ex.getMessage());
				}
			}
		}

		body = stringBuilder.toString();
		try {
			JSONObject statusObject = new JSONObject(body);
			String statusMessage = statusObject.getString("status");
			TwitterUpdate.tweet(TwitterCallBackWs.accessToken, statusMessage);
		} catch (twitter4j.JSONException e) {
			throw new ServletException(e);

		}

	}

}
