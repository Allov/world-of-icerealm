package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.CompassToggler;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.CompassMode;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.CompassPlayersInfo;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.data.PlayerCompassData;

public class CustomCompassManager 
{
	private Player player = null;
	private String key = null;
	
	public CustomCompassManager(String key, Player player)
	{
		setPlayer(player);
		this.key = key;
	}
	
	public void setCustomLocation(Location location)
	{
		setCustomLocation(location, null);
	}

    public void setCustomLocation(Location location, String toggleMessage)
    {
		CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
		PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
		
		compassData.setCurrentCustomModeLocation(location);
		compassData.setCurrentCompassMode(CompassMode.Custom);
		compassData.setCurrentCustomLocationKey(key);
		
		CompassToggler toggler = new CompassToggler(player);
		toggler.setCustomLocationMode();
    }
    
    public String getCurrentCustomLocationKey()
    {
    	CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
    	PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
    	return compassData.getCurrentCustomLocationKey();
    }
    
    public Location getCurrentCustomLocation()
    {
    	CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
    	PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
    	return compassData.getCurrentCustomModeLocation();
    }

	public void removeCustomLocation()
	{
		CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
		PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
		
		if (compassData.getCurrentCustomLocationKey() != null && compassData.getCurrentCustomLocationKey().equals(key))
		{
			compassData.setCurrentCustomLocationKey(null);
			compassData.setCurrentCustomModeLocation(null);
		}
	}
    
	public Player getPlayer() 
	{
		return player;
	}

	public void setPlayer(Player player) 
	{
		this.player = player;
	}
}
