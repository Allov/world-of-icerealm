package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.Calendar;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class GreenThumbPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPlanting(BlockPlaceEvent evt) {
		if ((evt.getBlock().getType().equals(Material.CROPS) ||
			 evt.getBlock().getType().equals(Material.POTATO) ||
			 evt.getBlock().getType().equals(Material.CARROT) ||
			 evt.getBlock().getType().equals(Material.NETHER_WARTS)) && 
			perkService.playerHasPerk(evt.getPlayer(), SettlerPerks.GreenThumbId)) {
			
			if (new Random(Calendar.getInstance().getTimeInMillis()).nextInt(5) == 1) {
				
				if (evt.getBlock().getType().equals(Material.NETHER_WARTS)) {
					evt.getBlock().setData((byte)3);
				} else {
					evt.getBlock().setData((byte)7);
				}
			}
		}
	}
	
}
