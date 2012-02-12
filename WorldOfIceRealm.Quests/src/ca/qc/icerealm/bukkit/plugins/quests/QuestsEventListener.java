package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class QuestsEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		player.sendMessage(	ChatColor.DARK_GREEN + "Type " + ChatColor.YELLOW + " /q " + ChatColor.DARK_GREEN + "to get a " + ChatColor.GREEN + "random quest " + 
							ChatColor.DARK_GREEN + "or " + ChatColor.YELLOW + "/q help " + ChatColor.DARK_GREEN + "for help.");
	}
}
