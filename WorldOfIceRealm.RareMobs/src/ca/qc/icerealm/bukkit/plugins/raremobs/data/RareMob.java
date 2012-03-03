package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.List;

import org.bukkit.entity.CreatureType;

import ca.qc.icerealm.bukkit.plugins.raredrops.RareDropsOdds;

public class RareMob 
{
	private String mobName = null;
	private double spawnOdds; // Each hour between spawn time range
	private CreatureType creatureType = null;
	private int health = 0;
	private double strengthMultiplier = 0.00;
	private int experience = 0;
	private List<Subordinate> subordinates;
	private RareDropsOdds raredropsOdds;

	public String getMobName() 
	{
		return mobName;
	}

	public void setMobName(String mobName) 
	{
		this.mobName = mobName;
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

	public double getSpawnOdds() 
	{
		return spawnOdds;
	}

	public void setSpawnOdds(double spawnOdds) 
	{
		this.spawnOdds = spawnOdds;
	}

	public int getHealth() 
	{
		return health;
	}

	public void setHealth(int health) 
	{
		this.health = health;
	}

	public double getStrengthMultiplier() 
	{
		return strengthMultiplier;
	}

	public void setStrengthMultiplier(double strengthMultiplier) 
	{
		this.strengthMultiplier = strengthMultiplier;
	}

	public int getExperience() 
	{
		return experience;
	}

	public void setExperience(int experience) 
	{
		this.experience = experience;
	}

	public RareDropsOdds getRaredropsOdds() 
	{
		return raredropsOdds;
	}

	public void setRaredropsOdds(RareDropsOdds raredropsOdds) 
	{
		this.raredropsOdds = raredropsOdds;
	}
}
