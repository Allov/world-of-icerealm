package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.minecraft.server.EnchantmentManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import ca.qc.icerealm.bukkit.plugins.raredrops.*;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentsRandomizer;

public class RareDropsRandomizer 
{
	private RareDropsOdds odds = null;;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public void setOdds(RareDropsOdds odds) 
	{
		this.odds = odds;
	}
	
	public RareDropsRandomizer()
	{
		
	}
	
	public RareDropsRandomizer(RareDropsOdds odds)
	{
		this.setOdds(odds);
	}
	
	public ArrayList<ItemStack> randomize()
	{
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
		
		if (odds != null)
		{
			for (int i = 0; i < odds.getOddsItems().size(); i++)
			{
				RareDropsOddsItem item = odds.getOddsItems().get(i);			
				double result = Math.random() * 100;
				
				//this.logger.info(String.valueOf(result) + " <= " + String.valueOf(item.getPercentage()));
				
				if (result <= item.getPercentage())
				{
					ItemStack stack = new ItemStack(item.getItem(), 1);

					if (item.getEnchantmentsOdds() != null)
					{
						EnchantmentsRandomizer enchRandomizer = new EnchantmentsRandomizer(item.getEnchantmentsOdds(), item.getItem());		
						ArrayList<EnchantmentResult> enchantments = enchRandomizer.randomize();
						
						this.logger.info("enchantment size: " + enchantments.size());
						
						for (int j = 0; j < enchantments.size(); j++)
						{	
							EnchantmentResult enchantment = enchantments.get(j);
							this.logger.info(item.getItem().name() + ": adding enchantment: " + enchantment.getEnchantment().toString() + " " + enchantment.getLevel());
							stack.addEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
						}
					}
				
					stackList.add(stack);	
				}
			}
		}
		
		return stackList;
	}
}
