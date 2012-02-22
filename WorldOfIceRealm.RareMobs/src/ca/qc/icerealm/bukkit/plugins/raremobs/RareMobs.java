package ca.qc.icerealm.bukkit.plugins.raremobs;

import org.bukkit.entity.CreatureType;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class RareMobs extends JavaPlugin
{
	@Override
	public void onDisable() 
	{
				
	}

	@Override
	public void onEnable() 
	{
		RareMobsData data = new RareMobsData();

		RareMob mob = new RareMob();
		mob.setMobName("The butcher");
		mob.setCreatureType(CreatureType.ZOMBIE);
		data.addRareMob(mob);
		
		RareMobsRandomizer randomizer = new RareMobsRandomizer(data);
		RareMobsTimeObserver observer = new RareMobsTimeObserver(getServer(), randomizer);
		TimeServer.getInstance().addListener(observer, 10000);
		//getServer().getPluginManager().registerEvents(new DropsListener(), this);
	}
}
