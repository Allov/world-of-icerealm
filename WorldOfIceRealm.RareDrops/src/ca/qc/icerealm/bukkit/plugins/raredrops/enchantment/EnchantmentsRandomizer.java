package ca.qc.icerealm.bukkit.plugins.raredrops.enchantment;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;

public class EnchantmentsRandomizer 
{
	private Material item = null;
	
	public Material getItem() 
	{
		return item;
	}
	
	public void setItem(Material item) 
	{
		this.item = item;
	}
	
	private EnchantmentsOdds odds = null;;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public void setOdds(EnchantmentsOdds odds) 
	{
		this.odds = odds;
	}
	
	public EnchantmentsRandomizer()
	{
		
	}
	
	public EnchantmentsRandomizer(EnchantmentsOdds odds)
	{
		this.setOdds(odds);
	}
	
	public EnchantmentsRandomizer(EnchantmentsOdds odds, Material item)
	{
		this.setOdds(odds);
		this.setItem(item);
	}
	
	public ArrayList<EnchantmentResult> randomize()
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		if (odds != null)
		{
			if (MaterialUtil.isArmor(item))
			{
				if (MaterialUtil.isHelmet(item))
				{
					// Custom logic
					enchantmentList = randomizeHelmet();
				}
				else if (MaterialUtil.isBoots(item))
				{
					enchantmentList = randomize(MaterialUtil.getBootsEnchantments());
				}
				else
				{
					enchantmentList = randomize(MaterialUtil.getArmorProtectionEnchantments());
				}
			}
			else if (MaterialUtil.isSword(item))
			{
				// Custom logic
				enchantmentList = randomizeSword();
			}
			else if (MaterialUtil.isBow(item))
			{
				enchantmentList = randomize(MaterialUtil.getBowEnchantments());
			}
			else if (MaterialUtil.isEnchantableTool(item))
			{
				enchantmentList = randomize(MaterialUtil.getToolsEnchantments());
			}
		}
		
		return enchantmentList;
	}
	
	private ArrayList<EnchantmentResult> randomizeHelmet()
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		// Start from maximum level
		for (int level = odds.getPercentageEnchantments().length; level > 0; level--)
		{
			for (int i = 0; i < MaterialUtil.getArmorProtectionEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getHelmetOnlyEnchantments().length + MaterialUtil.getArmorProtectionEnchantments().length))) 
					 && !enchantmentExists(enchantmentList, MaterialUtil.getArmorProtectionEnchantments()[i])
					 && level <= MaterialUtil.getArmorProtectionEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getArmorProtectionEnchantments()[i], level));
					// Only one protection is allowed, we get out of here!
					break;
				}
			}
			
			for (int i = 0; i < MaterialUtil.getHelmetOnlyEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getHelmetOnlyEnchantments().length + MaterialUtil.getArmorProtectionEnchantments().length)))
						&& !enchantmentExists(enchantmentList, MaterialUtil.getHelmetOnlyEnchantments()[i])
						&& level <= MaterialUtil.getHelmetOnlyEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getHelmetOnlyEnchantments()[i], level));
				}
			}
		}
		
		return enchantmentList;
	}
	
	
	
	private ArrayList<EnchantmentResult> randomizeSword()
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		// Start from maximum level
		for (int level = odds.getPercentageEnchantments().length; level > 0; level--)
		{
			for (int i = 0; i < MaterialUtil.getSwordDamageEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getSwordDamageEnchantments().length + MaterialUtil.getSwordNonDamageEnchantments().length)))
					 && !enchantmentExists(enchantmentList, MaterialUtil.getSwordDamageEnchantments()[i])
					 && level <= MaterialUtil.getSwordDamageEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getSwordDamageEnchantments()[i], level));
					// Only one damage is allowed, we get out of here!
					break;
				}
			}
			
			for (int i = 0; i < MaterialUtil.getSwordNonDamageEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getSwordNonDamageEnchantments().length + MaterialUtil.getSwordDamageEnchantments().length)))
						&& !enchantmentExists(enchantmentList, MaterialUtil.getSwordNonDamageEnchantments()[i])
						&& level <= MaterialUtil.getSwordNonDamageEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getSwordNonDamageEnchantments()[i], level));
				}
			}
		}
		
		return enchantmentList;
	}
	
	private ArrayList<EnchantmentResult> randomize(Enchantment[] enchantments)
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		// Start from maximum level
		for (int level = odds.getPercentageEnchantments().length; level > 0; level--)
		{		
			for (int i = 0; i < enchantments.length; i++)
			{	
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (enchantments.length)))
						&& !enchantmentExists(enchantmentList, enchantments[i])
						&& level <= enchantments[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(enchantments[i], level));
				}
			}
		}
		
		return enchantmentList;
	}
	
	public static boolean enchantmentExists(ArrayList<EnchantmentResult> enchantmentList, Enchantment enchantment)
	{
		for (int i = 0; i < enchantmentList.size(); i++)
		{
			if (enchantmentList.get(i).getEnchantment().equals(enchantment))
			{
				return true;
			}
		}
		
		return false;
	}
}
