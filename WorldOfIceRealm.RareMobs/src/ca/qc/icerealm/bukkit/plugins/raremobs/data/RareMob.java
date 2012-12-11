package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.List;

import org.bukkit.entity.EntityType;

import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;

public class RareMob 
{
	private String mobName = null;
	private double spawnOdds; // Each hour between spawn time range
	private EntityType creatureType = null;
	private int health = 0;
	private double strengthMultiplier = 0.00;
	private int experienceLevels = 0;
	private int money = 0;
	private double subordinatesDamageMultiplier = 0;
	private double subordinatesHealthMultiplier = 0;
	private List<Subordinate> subordinates;
	private RareDropsOdds raredropsOdds;
	private double distanceMultiplier = 1.00;

	public String getMobName() 
	{
		return mobName;
	}

	public void setMobName(String mobName) 
	{
		this.mobName = mobName;
	}

	public EntityType getCreatureType() 
	{
		return creatureType;
	}

	public void setCreatureType(EntityType creatureType) 
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

	public int getExperienceLevels() 
	{
		return experienceLevels;
	}

	public void setExperienceLevels(int experienceLevels) 
	{
		this.experienceLevels = experienceLevels;
	}

	public RareDropsOdds getRaredropsOdds() 
	{
		return raredropsOdds;
	}

	public void setRaredropsOdds(RareDropsOdds raredropsOdds) 
	{
		this.raredropsOdds = raredropsOdds;
	}

	public int getMoney() 
	{
		return money;
	}

	public void setMoney(int money) 
	{
		this.money = money;
	}

	public double getSubordinatesDamageMultiplier() 
	{
		return subordinatesDamageMultiplier;
	}

	public void setSubordinatesDamageMultiplier(double subordinatesDamageMultiplier) 
	{
		this.subordinatesDamageMultiplier = subordinatesDamageMultiplier;
	}

	public double getSubordinatesHealthMultiplier() 
	{
		return subordinatesHealthMultiplier;
	}

	public void setSubordinatesHealthMultiplier(double subordinatesHealthMultiplier) 
	{
		this.subordinatesHealthMultiplier = subordinatesHealthMultiplier;
	}

	public double getDistanceMultiplier() 
	{
		return distanceMultiplier;
	}

	public void setDistanceMultiplier(double distanceMultiplier) 
	{
		this.distanceMultiplier = distanceMultiplier;
	}
}
