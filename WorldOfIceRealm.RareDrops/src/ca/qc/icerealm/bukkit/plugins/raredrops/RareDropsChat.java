package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;

public class RareDropsChat 
{
	public static void notifyPlayer(ArrayList<RareDropResult> items, Player player, String monsterName)
	{
    	// Notify the player for all obtained rare drops
    	for (int i = 0; i < items.size(); i++)
    	{
    		RareDropResult raredrop = items.get(i);
    		
    		String itemName = raredrop.getItemStack().getType().name();
    		
    		// If no enchantment, this isn't a custom item.
    		if (raredrop.getCustomName() != null && raredrop.getItemStack().getEnchantments() != null && !raredrop.getItemStack().getEnchantments().isEmpty())
    		{
    			itemName = raredrop.getCustomName();
    		}
    		
    		player.sendMessage( ChatColor.YELLOW + monsterName + " dropped a " + ChatColor.DARK_PURPLE + MaterialUtil.getMaterialFriendName(itemName) + (raredrop.getItemStack().getEnchantments().size() != 0 ? " (enchanted)":""));
    	}
	}
	
	public static void notifyPlayers(ArrayList<RareDropResult> items, List<Player> players, String monsterName)
	{
    	for(Player player : players)
    	{
    		notifyPlayer(items, player, monsterName);
    	}
	}
}
