package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.Server;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;
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
		if (bukkitServer.getOnlinePlayers().length > 0)
		{
			spawnedRareMob = randomizer.randomizeSpawn();

			if (spawnedRareMob != null)
			{
				RareMobSpawner spawner = new RareMobSpawner(bukkitServer, spawnedRareMob);
				logger.info("attempt to spawn mob");
				spawner.spawnMobWithSubordinates();
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
