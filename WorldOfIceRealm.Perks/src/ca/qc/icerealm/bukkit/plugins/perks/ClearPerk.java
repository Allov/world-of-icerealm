package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ClearPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance(); 
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent evt) {
		String playerName = evt.getEntity().getName();

		Player player = evt.getEntity();
		perkService.clearPerks(player);
	}
}
