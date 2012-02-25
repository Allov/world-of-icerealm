package ca.qc.icerealm.bukkit.plugins.Twitter;

import twitter4j.conf.ConfigurationBuilder;

public class Register {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurationBuilder cb;
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("YEIaTiL6YtrC76M95Qt6g")
		.setOAuthConsumerSecret("NX3zl9OCIu4eRqWvsIMVJWoW1IeTqrhaqvU4yDdD0")
		.setOAuthAccessToken("490863314-RJBxwxSTh77zqW2Ur38RpZIQ6SrUX272wKyTeVTM")
		.setOAuthAccessTokenSecret("ncefxX3S6FpM0Mkw3XSep7eTFU0F8nV6Druqkh4RPg");
	}

}
