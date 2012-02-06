package ca.qc.icerealm.bukkit.plugins.raredrops;

import org.bukkit.Material;

import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentsOdds;

public class RareDropsOddsItem 
{
	public RareDropsOddsItem()
	{
		
	}
	
	public RareDropsOddsItem(double oodsPercentage, Material oddsItem)
	{
		setPercentage(oodsPercentage);
		setItem(oddsItem);
	}
	
	public RareDropsOddsItem(double oodsPercentage, Material oddsItem, EnchantmentsOdds enchodds)
	{
		setPercentage(oodsPercentage);
		setItem(oddsItem);
		setEnchantmentsOdds(enchodds);
	}

	private double percentage = 0;	
	private Material item = null;
	private EnchantmentsOdds enchantmentsOdds = null;
	
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

	public EnchantmentsOdds getEnchantmentsOdds() 
	{
		return enchantmentsOdds;
	}
	
	public void setEnchantmentsOdds(EnchantmentsOdds enchantmentsOdds) 
	{
		this.enchantmentsOdds = enchantmentsOdds;
	}
}
