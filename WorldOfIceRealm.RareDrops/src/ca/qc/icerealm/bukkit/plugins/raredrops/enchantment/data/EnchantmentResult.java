package ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentResult 
{
	private Enchantment enchantment = null;
	private int level = 1;
	
	public EnchantmentResult() 
	{
		
	}
	
	public EnchantmentResult(Enchantment enchantment, int level) 
	{
		this.enchantment = enchantment;
		this.level = level;
	}
	
	public Enchantment getEnchantment() 
	{
		return enchantment;
	}
	
	public void setEnchantment(Enchantment enchantment) 
	{
		this.enchantment = enchantment;
	}
	
	public int getLevel() 
	{
		return level;
	}
	
	public void setLevel(int level) 
	{
		this.level = level;
	}
}
