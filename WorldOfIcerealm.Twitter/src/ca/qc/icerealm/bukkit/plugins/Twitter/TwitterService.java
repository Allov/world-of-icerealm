package ca.qc.icerealm.bukkit.plugins.Twitter;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterService {
	
	private ConfigurationBuilder cb;
	TwitterFactory tf;
	
	public TwitterService()
	{
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("YEIaTiL6YtrC76M95Qt6g")
		.setOAuthConsumerSecret("NX3zl9OCIu4eRqWvsIMVJWoW1IeTqrhaqvU4yDdD0")
		.setOAuthAccessToken("490863314-RJBxwxSTh77zqW2Ur38RpZIQ6SrUX272wKyTeVTM")
		.setOAuthAccessTokenSecret("ncefxX3S6FpM0Mkw3XSep7eTFU0F8nV6Druqkh4RPg");
		tf = new TwitterFactory(cb.build());
	}
	
	public void UpdateStatus(String status)
	{
		Twitter twitter = tf.getInstance();
		
		try {
			twitter.updateStatus(new StatusUpdate(status));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public void SendDirectMessage(String username, String message)
	{
		Twitter twitter = tf.getInstance();
		
		try {
			twitter.sendDirectMessage(username, message);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}
