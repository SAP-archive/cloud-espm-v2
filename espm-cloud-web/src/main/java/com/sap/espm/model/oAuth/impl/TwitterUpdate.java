package com.sap.espm.model.oAuth.impl;

import java.util.Map;

import com.sap.espm.model.util.oAuthDetails;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * This class is used to tweet via ESPM. Via this class
 * customer can tweet about the product he/she bought.
 * 
 */

public class TwitterUpdate {

	/**
	 * Get the OauthDetails from destination and store as key value pair.
	 */
	static Map<String, String> twitterDetails = oAuthDetails.getOAuthDetails();

	/**
	 * Client credentials in form of Consumer Application key and Consumer
	 * application Secret.
	 */
	static String consumerKeyStr = twitterDetails.get("consumerApplicationKey");
	static String consumerSecretStr = twitterDetails.get("consumerApplicationSecret");

	/**
	 * This method is used to get the {@link Twitter} instance by passing
	 * parameters
	 * 
	 * @param at
	 *            - {@link AccessToken}
	 * @param status
	 *            - Message/ Status for tweet (the actual tweet)
	 * @return
	 */

	public static int tweet(AccessToken at, String status) {
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			twitter.setOAuthAccessToken(at);
			twitter.updateStatus(status);
			return 200;
		} catch (TwitterException te) {
			return 500;
		}
	}
}
