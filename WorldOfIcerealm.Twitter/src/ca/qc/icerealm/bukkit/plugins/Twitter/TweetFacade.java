package ca.qc.icerealm.bukkit.plugins.Twitter;

import org.bukkit.plugin.java.JavaPlugin;

public class TweetFacade extends JavaPlugin {
	
	private static TwitterService tw;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		tw = new TwitterService();
	}
	
	
	public static void UpdateStatus(String status)
	{
		tw.UpdateStatus(status);
	}
	

}
