package ca.qc.icerealm.bukkit.plugins.raremobs;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;

public class RareMobSpawner 
{
	private RareMob rareMob = null;
	private Server bukkitServer;
	
	public RareMobSpawner(Server bukkitServer, RareMob rareMob)
	{
		setRareMob(rareMob); 
		this.bukkitServer = bukkitServer;
	}
	
	public void spawnMobWithSubordinates()
	{
		// Choose a player randomly
		// TODO 
		
		Player p = bukkitServer.getPlayer("draskus");
		
		Location locPlayer = p.getLocation();
		Location loc = new Location(bukkitServer.getWorld("World"), locPlayer.getX() + 10, locPlayer.getY(), locPlayer.getZ() + 10);
		
		bukkitServer.getWorld("World").spawnCreature(loc, rareMob.getCreatureType());
		
		// Spawn his subordinates
		for (int i = 0; i < rareMob.getSubordinates().size(); i++)
		{
			loc.setX(loc.getX() - (3 * (i + 1)));
			bukkitServer.getWorld("World").spawnCreature(loc, rareMob.getSubordinates().get(i).getCreatureType());
		}
	}

	public RareMob getRareMob() 
	{
		return rareMob;
	}

	public void setRareMob(RareMob rareMob) 
	{
		this.rareMob = rareMob;
	}
}
