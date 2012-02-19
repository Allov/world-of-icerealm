package ca.qc.icerealm.bukkit.plugins.advancedcompass.data;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializablePlayerCompassData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2851123869525605930L;

	private ArrayList<LocationData> playersData = new ArrayList<LocationData>();
	
	public void addPlayerLocationData(String playerName, double x, double y, double z)
	{
		LocationData data = new LocationData();
		data.setLocationX(x);
		data.setLocationY(y);
		data.setLocationZ(z);
		data.setPlayerName(playerName);
		playersData.add(data);
	}
	
	public ArrayList<LocationData> getPlayerLocationDataList()
	{
		return playersData;
	}
	
    // inner class implements the Iterator pattern
    public class LocationData implements Serializable
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -213835520244045750L;

		private double locationX = 0;
    	private double locationY = 0;
    	private double locationZ = 0;
    	
    	private String playerName = null;
    	

    	public double getLocationX() 
    	{
    		return locationX;
    	}
    	
    	public void setLocationX(double locationX) 
    	{
    		this.locationX = locationX;
    	}
    	
    	public double getLocationY() 
    	{
    		return locationY;
    	}
    	
    	public void setLocationY(double locationY) 
    	{
    		this.locationY = locationY;
    	}
    	
    	public double getLocationZ() 
    	{
    		return locationZ;
    	}
    	
    	public void setLocationZ(double locationZ) 
    	{
    		this.locationZ = locationZ;
    	}

		public String getPlayerName() 
		{
			return playerName;
		}

		public void setPlayerName(String playerName) 
		{
			this.playerName = playerName;
		}
    }
}
