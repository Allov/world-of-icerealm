package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import ca.qc.icerealm.bukkit.plugins.raredrops.*;

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
					stackList.add(new ItemStack(item.getItem(), 1));	
				}
			}
		}
		
		return stackList;
	}
}
