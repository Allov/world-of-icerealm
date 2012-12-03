package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class StoneWorkerPerk implements Listener {
	
	private final PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDestory(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.STONE)) {
			ItemStack stack = new ItemStack(Material.COBBLESTONE, 1);
			Player player = event.getPlayer();
			
			if (player != null && perkService.playerHasPerk(player, SettlerPerks.StoneWorkerId)) {
				player.getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
			}
		}
	}
	
}
