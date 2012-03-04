package ca.qc.icerealm.bukkit.plugins.raredrops.randomizer;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOddsItem;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentResult;

public class MultipleRareDropsRandomizer implements RareDropsRandomizer
{
	private RareDropsOdds odds = null;;
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public void setOdds(RareDropsOdds odds) 
	{
		this.odds = odds;
	}
	
	public MultipleRareDropsRandomizer()
	{
		
	}
	
	public MultipleRareDropsRandomizer(RareDropsOdds odds)
	{
		this.setOdds(odds);
	}
	
	public ArrayList<RareDropResult> randomize()
	{
		ArrayList<RareDropResult> stackList = new ArrayList<RareDropResult>();
		
		if (odds != null)
		{
			// Looping in all item odds
			for (int i = 0; i < odds.getOddsItems().size(); i++)
			{
				RareDropsOddsItem item = odds.getOddsItems().get(i);			
				double result = Math.random() * 100;
				
				if (result <= item.getPercentage())
				{
					ItemStack stack = new ItemStack(item.getItem(), 1);

					// Apply chances of getting an enchanted item 
					if (item.getEnchantmentsOdds() != null)
					{
						EnchantmentsRandomizer enchRandomizer = new EnchantmentsRandomizer(item.getEnchantmentsOdds(), item.getEnchantmentsAs() == null ? item.getItem() : item.getEnchantmentsAs() );		
						ArrayList<EnchantmentResult> enchantments = enchRandomizer.randomize();

						// Apply enchantements
						for (int j = 0; j < enchantments.size(); j++)
						{	
							EnchantmentResult enchantment = enchantments.get(j);
						//	this.logger.info(item.getItem().name() + ": adding enchantment: " + enchantment.getEnchantment().toString() + " " + enchantment.getLevel());
							stack.addUnsafeEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
						}
					}
					
					// Apply cooked meat behavior
					if (odds.getMonster() != null)
					{
						if (MaterialUtil.isCookable(stack.getType()) && odds.getMonster().getFireTicks() > 0)
						{
							stack.setType(MaterialUtil.getCookableEquivalence(stack.getType()));
						}	
					}
				
					RareDropResult stackResult = new RareDropResult();
					stackResult.setItemStack(stack);
					stackResult.setCustomName(item.getCustomName());
					
					stackList.add(stackResult);	
				}
			}
		}
		
		return stackList;
	}
}
