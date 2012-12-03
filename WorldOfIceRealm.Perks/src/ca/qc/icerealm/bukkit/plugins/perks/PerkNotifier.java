package ca.qc.icerealm.bukkit.plugins.perks;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class PerkNotifier implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerLevelChangeEvent evt) {
		if (evt.getNewLevel() % 5 == 0) {
			evt.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Hey " + ChatColor.YELLOW + evt.getPlayer().getName() + ChatColor.LIGHT_PURPLE + "! Consider buying a perk using " + ChatColor.GREEN + "/perk" + ChatColor.LIGHT_PURPLE + " command!");
		}
	}	
}
