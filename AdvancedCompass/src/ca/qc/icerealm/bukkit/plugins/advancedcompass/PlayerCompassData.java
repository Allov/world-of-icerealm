package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.Location;

public class PlayerCompassData 
{
    private String currentPlayerModePlayerName = null;
    private Location currentFixedModeLocation = null;
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
}
