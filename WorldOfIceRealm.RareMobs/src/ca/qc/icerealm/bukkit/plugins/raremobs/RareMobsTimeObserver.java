package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Entity;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.RareMob;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

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
			if (current.getRareMobEntityId() == -1)
			{
				spawnedRareMob = randomizer.randomizeSpawn();
	
				if (spawnedRareMob != null)
				{
					RareMobSpawner spawner = new RareMobSpawner(bukkitServer, spawnedRareMob);
					logger.info("attempt to spawn mob");
					spawner.spawnMobWithSubordinates();
				}
			}
		}

		// Attempt to remove a mob if it was alive for more than 12 minecraft hours
		if (current.getRareMobEntityId() != -1)
		{
			// Remove it
			if (System.currentTimeMillis() > current.getTimeSpawned() + 30000)
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
