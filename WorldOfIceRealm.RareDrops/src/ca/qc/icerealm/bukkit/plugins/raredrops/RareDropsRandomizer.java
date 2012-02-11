package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
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
			// Boucle dans les items possibles
			for (int i = 0; i < odds.getOddsItems().size(); i++)
			{
				RareDropsOddsItem item = odds.getOddsItems().get(i);			
				double result = Math.random() * 100;
				
				if (result <= item.getPercentage())
				{
					ItemStack stack = new ItemStack(item.getItem(), 1);

					// Appliquer les chances d'avoir un item enchante
					if (item.getEnchantmentsOdds() != null)
					{
						EnchantmentsRandomizer enchRandomizer = new EnchantmentsRandomizer(item.getEnchantmentsOdds(), item.getItem());		
						ArrayList<EnchantmentResult> enchantments = enchRandomizer.randomize();

						// Appliquer les enchantements obtenus
						for (int j = 0; j < enchantments.size(); j++)
						{	
							EnchantmentResult enchantment = enchantments.get(j);
							this.logger.info(item.getItem().name() + ": adding enchantment: " + enchantment.getEnchantment().toString() + " " + enchantment.getLevel());
							stack.addEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
						}
					}
					
					/*if (item.getItem().equals(Material.POTION))
					{

					}*/
					
					// Appliquer le comportement de nourriture cuite
					if (MaterialUtil.isCookable(stack.getType()) && odds.getMonster().getFireTicks() > 0)
					{
						stack.setType(MaterialUtil.getCookableEquivalence(stack.getType()));
					}			
				
					stackList.add(stack);	
				}
			}
		}
		
		return stackList;
	}
}
