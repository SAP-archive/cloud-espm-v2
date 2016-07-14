package com.sap.espm.model.oAuth.impl;

import java.util.Map;

import javax.servlet.ServletException;

import com.sap.espm.model.util.ReadProperties;
import com.sap.espm.model.util.oAuthDetails;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * Function Import processor class for Twitter Login
 * 
 */

public class TwitterLogin {
	static Map<String,String> twitterDetails = oAuthDetails.getOAuthDetails();
	static String consumerKeyStr = twitterDetails.get("consumerApplicationKey");
	static String consumerSecretStr =twitterDetails.get("consumerApplicationSecret");;
	public static String token;
	public static String tokenSecret;
	public static Twitter loginReqApi()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey(consumerKeyStr)
    	  .setOAuthConsumerSecret(consumerSecretStr);
    	//cb.setUseSSL(true);
    	TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}
	
	public static AccessToken callBackApi(Twitter twitter, RequestToken requestToken, String verifier){
		 AccessToken accessToken;
try {
			
			accessToken=twitter.getOAuthAccessToken(requestToken, verifier);
			token=accessToken.getToken();
			tokenSecret=accessToken.getTokenSecret();
			return accessToken;
	
		} catch (TwitterException e) {
			e.getMessage();
			return null;
			
		}

		
	}

}
