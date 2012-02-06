package ca.qc.icerealm.bukkit.plugins.raredrops.enchantment;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

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
				//this.logger.info("randomize armor");
				enchantmentList = randomizeArmor();
			}
			else if (MaterialUtil.isSword(item))
			{
				//this.logger.info("randomize sword");
				enchantmentList = randomizeSword();
			}
			else if (MaterialUtil.isBow(item))
			{
				//this.logger.info("randomize bow");
				enchantmentList = randomizeBow();
			}
			else if (MaterialUtil.isEnchantableTool(item))
			{
				//this.logger.info("randomize tool");
				enchantmentList = randomizeTool();
			}
		}
		
		return enchantmentList;
	}
	
	private ArrayList<EnchantmentResult> randomizeArmor()
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
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getArmorNonProtectionEnchantments().length + MaterialUtil.getArmorProtectionEnchantments().length))) 
					 && !enchantmentExists(enchantmentList, MaterialUtil.getArmorProtectionEnchantments()[i])
					 && level <= MaterialUtil.getArmorProtectionEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getArmorProtectionEnchantments()[i], level));
					// Only one protection is allowed, we get out of here!
					break;
				}
			}
			
			for (int i = 0; i < MaterialUtil.getArmorNonProtectionEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getArmorNonProtectionEnchantments().length + MaterialUtil.getArmorProtectionEnchantments().length)))
						&& !enchantmentExists(enchantmentList, MaterialUtil.getArmorNonProtectionEnchantments()[i])
						&& level <= MaterialUtil.getArmorNonProtectionEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getArmorNonProtectionEnchantments()[i], level));
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
	
	private ArrayList<EnchantmentResult> randomizeBow()
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		// Start from maximum level
		for (int level = odds.getPercentageEnchantments().length; level > 0; level--)
		{		
			for (int i = 0; i < MaterialUtil.getBowEnchantments().length; i++)
			{			
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getBowEnchantments().length)))
						&& !enchantmentExists(enchantmentList, MaterialUtil.getBowEnchantments()[i])
						&& level <= MaterialUtil.getBowEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getBowEnchantments()[i], level));
				}
			}
		}
		
		return enchantmentList;
	}
	
	private ArrayList<EnchantmentResult> randomizeTool()
	{
		ArrayList<EnchantmentResult> enchantmentList = new ArrayList<EnchantmentResult>();
		
		// Start from maximum level
		for (int level = odds.getPercentageEnchantments().length; level > 0; level--)
		{		
			//this.logger.info("level: " + level);
			for (int i = 0; i < MaterialUtil.getToolsEnchantments().length; i++)
			{	
				// Loop into all type of enchantments for this material
				// divide by total number of enchantment
				double result = Math.random() * 100;
				
			/*	this.logger.info("result: " + result);
				this.logger.info("odds.getPercentageEnchantments()[level]: " +  (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getToolsEnchantments().length))));
				this.logger.info("exists: " + enchantmentExists(enchantmentList, MaterialUtil.getToolsEnchantments()[i]));
				this.logger.info("isNotMaxlevel: " + (level <= MaterialUtil.getToolsEnchantments()[i].getMaxLevel()));
				*/
				if (result <= (odds.getPercentageEnchantments()[level-1] / ((double) (MaterialUtil.getToolsEnchantments().length)))
						&& !enchantmentExists(enchantmentList, MaterialUtil.getToolsEnchantments()[i])
						&& level <= MaterialUtil.getToolsEnchantments()[i].getMaxLevel())
				{
					enchantmentList.add(new EnchantmentResult(MaterialUtil.getToolsEnchantments()[i], level));
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
