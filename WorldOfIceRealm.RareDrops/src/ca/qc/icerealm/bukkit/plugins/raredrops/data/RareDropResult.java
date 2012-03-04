package ca.qc.icerealm.bukkit.plugins.raredrops.data;

import org.bukkit.inventory.ItemStack;

public class RareDropResult 
{
	private ItemStack itemStack = null;
	private String customName = null;

	public ItemStack getItemStack() 
	{
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) 
	{
		this.itemStack = itemStack;
	}

	public String getCustomName() 
	{
		return customName;
	}

	public void setCustomName(String customName) 
	{
		this.customName = customName;
	}
	
}
