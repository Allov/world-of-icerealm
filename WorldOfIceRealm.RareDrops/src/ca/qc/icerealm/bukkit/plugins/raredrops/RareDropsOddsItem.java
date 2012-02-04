package ca.qc.icerealm.bukkit.plugins.raredrops;

import org.bukkit.Material;

public class RareDropsOddsItem 
{
	public RareDropsOddsItem(double oodsPercentage, Material oddsItem)
	{
		setPercentage(oodsPercentage);
		setItem(oddsItem);
	}

	private double percentage = 0;	
	private Material item = null;
	
	public Material getItem() 
	{
		return item;
	}
	
	public void setItem(Material item) 
	{
		this.item = item;
	}
	
	public double getPercentage() 
	{
		return percentage;
	}
	
	public void setPercentage(double oodsPercentage) 
	{
		this.percentage = oodsPercentage;
	}
}
