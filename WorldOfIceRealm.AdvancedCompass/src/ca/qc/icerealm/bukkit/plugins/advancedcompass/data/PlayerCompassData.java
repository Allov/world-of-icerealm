package ca.qc.icerealm.bukkit.plugins.advancedcompass.data;

import org.bukkit.Location;

public class PlayerCompassData 
{
    private String currentPlayerModePlayerName = null;
    private Location currentFixedModeLocation = null;
    private Location currentCustomModeLocation = null;
    private String currentCustomToggleMessage = null;
    private String currentCustomLocationKey = null;
    private CompassMode currentCompassMode = CompassMode.SpawnPoint;
   
    public void setCurrentPlayerModePlayerName(String playerName)
    {
	    this.currentPlayerModePlayerName = playerName;
    }
   
    public String getCurrentPlayerModePlayerName()
    {
	    return this.currentPlayerModePlayerName;
    }

	public Location getCurrentFixedModeLocation() 
	{
		return this.currentFixedModeLocation;
	}

	public void setCurrentFixedModeLocation(Location currentFixedModeLocation) 
	{
		this.currentFixedModeLocation = currentFixedModeLocation;
	}

	public CompassMode getCurrentCompassMode() 
	{
	  	return this.currentCompassMode;
	}

	public void setCurrentCompassMode(CompassMode currentCompassMode) 
	{
		this.currentCompassMode = currentCompassMode;
	}

	public Location getCurrentCustomModeLocation() 
	{
		return currentCustomModeLocation;
	}

	public void setCurrentCustomModeLocation(Location currentCustomModeLocation) 
	{
		this.currentCustomModeLocation = currentCustomModeLocation;
	}

	public String getCurrentCustomToggleMessage() 
	{
		return currentCustomToggleMessage;
	}

	public void setCurrentCustomToggleMessage(String currentCustomToggleMessage) 
	{
		this.currentCustomToggleMessage = currentCustomToggleMessage;
	}

	public String getCurrentCustomLocationKey() 
	{
		return currentCustomLocationKey;
	}

	public void setCurrentCustomLocationKey(String currentCustomLocationKey) 
	{
		this.currentCustomLocationKey = currentCustomLocationKey;
	}
}
