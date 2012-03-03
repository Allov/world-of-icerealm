package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.ArrayList;
import java.util.List;


public class RareMobsData 
{
	private List<RareMob> rareMobs = null;

	public List<RareMob> getRareMobs() 
	{
		return rareMobs;
	}

	public void setRareMobs(List<RareMob> rareMobs) 
	{
		this.rareMobs = rareMobs;
	}
	
	public void addRareMob(RareMob mob)
	{
		if (rareMobs == null)
		{
			rareMobs = new ArrayList<RareMob>();
		}
		
		rareMobs.add(mob);
	}
}
