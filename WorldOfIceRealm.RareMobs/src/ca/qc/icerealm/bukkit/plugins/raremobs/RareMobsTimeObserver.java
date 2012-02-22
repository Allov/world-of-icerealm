package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.Server;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class RareMobsTimeObserver implements TimeObserver
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
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
		logger.info("time has come");
		if (bukkitServer.getOnlinePlayers().length > 0)
		{
			spawnedRareMob = randomizer.randomizeSpawn();
			logger.info("randomized");
			if (spawnedRareMob != null)
			{
				RareMobSpawner spawner = new RareMobSpawner(bukkitServer, spawnedRareMob);
				logger.info("attempt to spawn mob");
				spawner.spawnMobWithSubordinates();
			}
		}

		TimeServer.getInstance().addListener(this, 10000);
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
