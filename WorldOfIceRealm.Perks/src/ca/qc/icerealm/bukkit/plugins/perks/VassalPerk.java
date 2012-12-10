package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class VassalPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent evt) {
		 
		if ((evt.getBlock().getType().equals(Material.CROPS) ||
			 evt.getBlock().getType().equals(Material.POTATO) ||
			 evt.getBlock().getType().equals(Material.CARROT) ||
			 evt.getBlock().getType().equals(Material.PUMPKIN) ||
			 evt.getBlock().getType().equals(Material.MELON_BLOCK) ||
			 evt.getBlock().getType().equals(Material.COCOA) ||
			 evt.getBlock().getType().equals(Material.NETHER_WARTS) ||
			 evt.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) && 
			perkService.playerHasPerk(evt.getPlayer(), SettlerPerks.VassalId)) {
			
			ItemStack stack = null;
			
			if (evt.getBlock().getType().equals(Material.CROPS) && evt.getBlock().getData() == 7) {
				stack = new ItemStack(Material.WHEAT, 1);
			} else if (evt.getBlock().getType().equals(Material.POTATO) && evt.getBlock().getData() == 7) {
				stack = new ItemStack(Material.POTATO_ITEM, 1);
			} else if (evt.getBlock().getType().equals(Material.CARROT) && evt.getBlock().getData() == 7) {
				stack = new ItemStack(Material.CARROT_ITEM, 1);
			} else if (evt.getBlock().getType().equals(Material.PUMPKIN)) {
				stack = new ItemStack(Material.PUMPKIN, 1);
			} else if (evt.getBlock().getType().equals(Material.MELON_BLOCK)) {
				stack = new ItemStack(Material.MELON, 1);
			} else if (evt.getBlock().getType().equals(Material.COCOA)) {
				stack = new ItemStack(Material.COCOA, 1);
			} else if (evt.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) {
				stack = new ItemStack(Material.SUGAR_CANE, 1);
			} else if (evt.getBlock().getType().equals(Material.NETHER_WARTS)) {
				stack = new ItemStack(Material.NETHER_STALK, 1);
			}
			
			if (stack != null) {
				evt.getBlock().getWorld().dropItemNaturally(evt.getBlock().getLocation(), stack);
			}
		}		
	}
}
