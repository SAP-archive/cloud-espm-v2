package com.sap.espm.model.oAuth.impl;

import java.util.Map;

import com.sap.espm.model.util.oAuthDetails;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is used to Login into Twitter by passing Consumer Key and Consumer
 * Secret Key as parameters to the Twitter API from the destination.
 * <p>
 * To refer to the Twitter Official Documentation for Twitter Client, refer to link:
 * <p>
 * https://dev.twitter.com/oauth
 * <p>
 * For Twitter4J implementation and other information, refer to the link:
 * <p>
 * http://twitter4j.org/en/configuration.html
 * 
 */
public class TwitterLogin {

	/**
	 * Get the OauthDetails from destination stored as key value pair.
	 */
	static Map<String, String> twitterDetails = oAuthDetails.getOAuthDetails();

	/**
	 * Client credentials in form of Consumer Application key and Consumer
	 * application Secret.
	 */
	static String consumerKeyStr = twitterDetails.get("consumerApplicationKey");
	static String consumerSecretStr = twitterDetails.get("consumerApplicationSecret");
	public static String token;
	public static String tokenSecret;

	/**
	 * This method will be used to configure the Twitter factory by setting
	 * OAuthConsumerKey,OAuthConsumerSecret
	 * 
	 * @return {@link Twitter}
	 */
	public static Twitter loginReqApi() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKeyStr).setOAuthConsumerSecret(consumerSecretStr);
		// cb.setUseSSL(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

	/**
	 * This method is used to get the accessToken from Twitter API by passing
	 * parameters:
	 * 
	 * @param twitter
	 *            - Instance of {@link Twitter}
	 * @param requestToken
	 *            - {@link RequestToken} from session
	 * @param verifier
	 *            - AccessToken with the oauth_verifier parameter which will be
	 *            added to the Callback URL upon callback
	 * @return {@link AccessToken}
	 */

	public static AccessToken callBackApi(Twitter twitter, RequestToken requestToken, String verifier) {
		AccessToken accessToken;
		try {

			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			token = accessToken.getToken();
			tokenSecret = accessToken.getTokenSecret();
			return accessToken;

		} catch (TwitterException e) {
			e.getMessage();
			return null;

		}

	}

}
