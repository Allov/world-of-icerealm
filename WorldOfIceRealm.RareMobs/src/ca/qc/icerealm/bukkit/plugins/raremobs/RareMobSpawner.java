package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;

public class RareMobSpawner 
{
	private RareMob rareMob = null;
	private Server bukkitServer;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public RareMobSpawner(Server bukkitServer, RareMob rareMob)
	{
		setRareMob(rareMob); 
		this.bukkitServer = bukkitServer;
	}
	
	public void spawnMobWithSubordinates()
	{
		// Choose a player randomly
		List<Player> players = bukkitServer.getWorld("World").getPlayers(); 
		
		// Don's spawn if all players left
		if (players.size() != 0)
		{
			// Spawn near a player randomly
			Collections.shuffle(players);
			Player p = players.get(0);
			
			ArrayList<String> spawnedLocations = new ArrayList<String>();
			
			Location locPlayer = p.getLocation();
			//Location loc = new Location(bukkitServer.getWorld("World"), locPlayer.getX() + 10, locPlayer.getY(), locPlayer.getZ() + 10);
			
			WorldZone spawnZone = new WorldZone(locPlayer, 10);
			Location loc = spawnZone.getRandomHighestLocation(bukkitServer.getWorld("World"));
			spawnedLocations.add(loc.toString());
			
			// Cancel if no spawning loc was found
			if (loc != null)
			{
				//logger.info("spawned a mob near: " + loc.toString());
				LivingEntity mob = bukkitServer.getWorld("World").spawnCreature(loc, rareMob.getCreatureType());
				
				CurrentRareMob currentRareMob = CurrentRareMob.getInstance();
				currentRareMob.setRareMob(rareMob);
				currentRareMob.setRareMobEntityId(mob.getEntityId());
				currentRareMob.setTimeSpawned(System.currentTimeMillis());
				
				// Cancel base raredrops for this type of monster, we handle the drops ourselves
				RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(mob.getEntityId(), 0);
				List<Integer> subordinatesIds = new Vector<Integer>();
					
				if (rareMob.getSubordinates().size() != 0)
				{
					double radius = Math.ceil(Math.sqrt(rareMob.getSubordinates().size())) * 2;
					WorldZone subordinatesZone = new WorldZone(loc, radius);
	
					// Spawn his subordinates	
					for (int i = 0; i < rareMob.getSubordinates().size(); i++)
					{
						// Found a location where a mob didn't spawn yet, try 5 times.. if there's still a mob at that position, make it spawn at same location
						Location locSub = subordinatesZone.getRandomHighestLocation(bukkitServer.getWorld("World"));
						int tries = 0;
						
						if (locSub != null)						
						{
							while (spawnedLocations.contains(locSub) && tries < 5)
							{
								//logger.info("try: " + tries);
								locSub = subordinatesZone.getRandomHighestLocation(bukkitServer.getWorld("World"));
								tries++;
							}
						}
						else
						{
							logger.warning("Couldn't find a location for subordinate, radius: " + radius);
							locSub = loc;
						}
						//logger.info("spawned a subordinate near: " + loc.toString());
						
						LivingEntity subordinate = bukkitServer.getWorld("World").spawnCreature(locSub, rareMob.getSubordinates().get(i).getCreatureType());	
						spawnedLocations.add(locSub.toString());
						subordinatesIds.add(subordinate.getEntityId());
					}
				}
				
				currentRareMob.setSubordinatesEntityId(subordinatesIds);
				
				mob.getServer().broadcastMessage(ChatColor.RED + "You hear rumors about a rare monster in nearby areas.");
				
				// Set the compass so it points on our raremob
				
				for (Player player : mob.getWorld().getPlayers())
				{
		        	CustomCompassManager manager = new CustomCompassManager("RareMobs", player);
		        	manager.setCustomLocation(player.getLocation(), "Your compass is now pointing at " + ChatColor.RED + rareMob.getMobName());	
				}
			}
		}
	}
	
	
	public static void clearCompass(World world)
	{
    	for (Player player : world.getPlayers())
		{
        	CustomCompassManager manager = new CustomCompassManager("RareMobs", player);
        	manager.removeCustomLocation();
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
