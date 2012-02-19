package ca.qc.icerealm.bukkit.plugins.common;

import org.bukkit.Location;

public class LocationUtil 
{
	public static double getDistanceBetween(Location loc1, Location loc2)
	{
		double dx = loc1.getX() - loc2.getX();        
		double dy = loc1.getY() - loc2.getY();         
		double dz = loc1.getZ() - loc2.getZ();         
		
		double dist = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) ); //distance using Pythagoras theorem
		
		return dist;
	}
}
