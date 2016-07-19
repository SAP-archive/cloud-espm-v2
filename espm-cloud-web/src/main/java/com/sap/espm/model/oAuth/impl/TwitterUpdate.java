package com.sap.espm.model.oAuth.impl;


import java.util.Map;

import com.sap.espm.model.util.ReadProperties;
import com.sap.espm.model.util.oAuthDetails;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * 
 * Function Import processor class for Twitter Update
 * 
 */


public class TwitterUpdate {
	static Map<String,String> twitterDetails = oAuthDetails.getOAuthDetails();
	static String consumerKeyStr = twitterDetails.get("consumerApplicationKey");
	static String consumerSecretStr =twitterDetails.get("consumerApplicationSecret");
	
	
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
