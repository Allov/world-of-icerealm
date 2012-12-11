package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;
import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class RareMobsTimeObserver implements TimeObserver
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	private ConfigWrapper configWrapper = null;
	
	public ConfigWrapper getConfigWrapper() 
	{
		return configWrapper;
	}

	public void setConfigWrapper(ConfigWrapper configWrapper) 
	{
		this.configWrapper = configWrapper;
	}
	
	private RareMob spawnedRareMob = null;
	//private long spawnedTimeRareMob = 0;
	
	private long _alarm;
	private RareMobsRandomizer randomizer;
	private Server bukkitServer;
	
	public RareMobsTimeObserver(Server bukkitServer, RareMobsRandomizer randomizer)  
	{
		this.randomizer = randomizer;
		this.bukkitServer = bukkitServer;
	}
	
	@Override
	public void timeHasCome(long time) 
	{
		CurrentRareMob current = CurrentRareMob.getInstance(); 
		if (bukkitServer.getOnlinePlayers().length > 0)
		{
			// Attempt to spawn a rare mob only if there's no living rare mob at the moment
			if (current.getRareMobLocation() == null)
			{
				spawnedRareMob = randomizer.randomizeSpawn();
	
				if (spawnedRareMob != null)
				{					
					// Choose a player randomly
					List<Player> players = bukkitServer.getWorld("World").getPlayers(); 
					
					// Don's spawn if all players left
					if (players.size() != 0)
					{	
						bukkitServer.broadcastMessage(ChatColor.RED + "You hear rumors about a rare monster in nearby areas.");
								
						// Spawn near a player randomly
						Collections.shuffle(players);
						Player p = players.get(0);
						
						Location locPlayer = p.getLocation();
					
						WorldZone spawnZone = new WorldZone(locPlayer, RareMobs.SPAWN_AROUND_X, RareMobs.SPAWN_AROUND_Y, RareMobs.SPAWN_AROUND_Z);
						Location loc = spawnZone.getRandomLocation(bukkitServer.getWorld("World"));
						
						// Add a buffer of 1 block in height to make sure it doesn't spawn in a block
						loc.setY(loc.getY() + 1);
						
						// Set the compass so it points on our raremob	
						
						if (bukkitServer.getPluginManager().isPluginEnabled("WoI.AdvancedCompass")) 
						{							
							for (Player player : bukkitServer.getWorld("World").getPlayers())
							{
					        	CustomCompassManager manager = new CustomCompassManager("RareMobs", player);
					        	manager.setCustomLocation(loc, "Your compass is now pointing at " + ChatColor.RED + spawnedRareMob.getMobName());	
							}
						}
						current.setRareMobLocation(loc);
						current.setRareMob(spawnedRareMob);
						current.setTimeSpawned(System.currentTimeMillis());
						
						WorldZone spawnedZone = new WorldZone(loc, 20);
						RareMobZone rareMobZone = new RareMobZone(bukkitServer);
						rareMobZone.setWorldZone(spawnedZone);
						ZoneServer.getInstance().addListener(rareMobZone);	
					}
				}
			}
		}

		// Attempt to remove a mob if it was alive for more than 12 minecraft hours
		if (current.getRareMobLocation() != null)
		{
			// Remove it
			if (System.currentTimeMillis() > current.getTimeSpawned() + 600000)
			{
				for (Entity entity : bukkitServer.getWorld("World").getEntities())
				{
					if (entity.getEntityId() == current.getRareMobEntityId())
					{
						entity.remove();
			        	break;
					}
				}
				
				// Reset current mob
	        	bukkitServer.broadcastMessage(ChatColor.GREEN + current.getRareMob().getMobName() + " is no more. ");
	        	current.clear();
	        	RareMobSpawner.clearCompass(bukkitServer.getWorld("World"));
			}
		}

		TimeServer.getInstance().addListener(this, RareMobs.SPAWN_CHECK_INTERVAL);
	}

	@Override
	public void setAlaram(long time) 
	{
		_alarm = time;
	}

	@Override
	public long getAlarm() 
	{
		return _alarm;
	}
}
