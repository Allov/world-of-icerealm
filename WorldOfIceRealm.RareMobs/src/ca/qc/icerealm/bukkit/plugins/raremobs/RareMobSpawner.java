package ca.qc.icerealm.bukkit.plugins.raremobs;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class RareMobSpawner 
{
	private RareMob rareMob = null;
	private Server bukkitServer;
	
	public RareMobSpawner(Server bukkitServer, RareMob rareMo)
	{
		setRareMob(rareMo); 
		this.bukkitServer = bukkitServer;
	}
	
	public void spawnMobWithSubordinates()
	{

		Player p = bukkitServer.getPlayer("draskus");
		
		Location locPlayer = p.getLocation();
		Location loc = new Location(bukkitServer.getWorld("World"), locPlayer.getX() + 10, locPlayer.getY(), locPlayer.getZ() + 10);
		
		bukkitServer.getWorld("World").spawnCreature(loc, rareMob.getCreatureType());
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
