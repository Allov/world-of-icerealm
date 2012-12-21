package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

class FishingEventListener implements Listener {
	private final FishingTournament tournament;

	public FishingEventListener(FishingTournament tournament) {
		this.tournament = tournament;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFishOn(PlayerFishEvent event) {
		if (tournament.isInProgress()) {
			Entity theCatch = event.getCaught();
			
			if (theCatch instanceof Item) {
				tournament.addCatchToPlayer(event.getPlayer());
			}
		}
	}
}
