package com.sap.espm.model.oAuth.impl;


import com.sap.espm.model.util.ReadProperties;

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
	static String consumerKeyStr = ReadProperties.getInstance().getValue("consumerApplicationKey");
	static String consumerSecretStr =ReadProperties.getInstance().getValue("consumerApplicationSecret");;
	
	
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
