package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.Location;

public class PlayerCompassData 
{
    private String currentPlayerModePlayerName = null;
    private Location currentFixedModeLocation = null;
    private CompassMode currentCompassMode = null;
   
    public void setCurrentPlayerModePlayerName(String playerName)
    {
	    currentPlayerModePlayerName = playerName;
    }
   
    public String getCurrentPlayerModePlayerName()
    {
	    return currentPlayerModePlayerName;
    }

	public Location getCurrentFixedModeLocation() 
	{
		return currentFixedModeLocation;
	}

	public void setCurrentFixedModeLocation(Location currentFixedModeLocation) 
	{
		this.currentFixedModeLocation = currentFixedModeLocation;
	}

	public CompassMode getCurrentCompassMode() 
	{
		CompassMode mode = currentCompassMode;
		   
	    if (mode != null)
	    {
	  	    return mode;
	    }
	   
	   return CompassMode.SpawnPoint;
	}

	public void setCurrentCompassMode(CompassMode currentCompassMode) 
	{
		this.currentCompassMode = currentCompassMode;
	}
}
