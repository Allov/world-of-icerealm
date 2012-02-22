package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.List;

import org.bukkit.entity.CreatureType;

public class RareMob 
{
	private String mobName;
	private double spawnTimeStart;  // based on 24 hours
	private double spawnTimeEnd;	// based on 24 hours
	private double spawnRate; // Each hour between spawn time range
	private CreatureType creatureType;
	
	private List<Subordinate> subordinates;

	public String getMobName() 
	{
		return mobName;
	}

	public void setMobName(String mobName) 
	{
		this.mobName = mobName;
	}

	public double getSpawnTimeStart() 
	{
		return spawnTimeStart;
	}

	public void setSpawnTimeStart(double spawnTimeStart) 
	{
		this.spawnTimeStart = spawnTimeStart;
	}

	public double getSpawnTimeEnd() 
	{
		return spawnTimeEnd;
	}

	public void setSpawnTimeEnd(double spawnTimeEnd) 
	{
		this.spawnTimeEnd = spawnTimeEnd;
	}

	public double getSpawnRate() 
	{
		return spawnRate;
	}

	public void setSpawnRate(double spawnRate) 
	{
		this.spawnRate = spawnRate;
	}

	public CreatureType getCreatureType() 
	{
		return creatureType;
	}

	public void setCreatureType(CreatureType creatureType) 
	{
		this.creatureType = creatureType;
	}

	public List<Subordinate> getSubordinates() 
	{
		return subordinates;
	}

	public void setSubordinates(List<Subordinate> subordinates) 
	{
		this.subordinates = subordinates;
	}
}
