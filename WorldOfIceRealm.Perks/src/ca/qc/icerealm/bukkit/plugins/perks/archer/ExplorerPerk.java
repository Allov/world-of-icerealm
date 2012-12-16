package ca.qc.icerealm.bukkit.plugins.perks.archer;

import java.util.Calendar;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.WarriorTree;

public class ExplorerPerk implements Listener {
	private final PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerFoodLevelChange(FoodLevelChangeEvent evt) {
		
		if (evt.getEntity() instanceof Player) {
			Player player = (Player)evt.getEntity();
			
			if (perkService.playerHasPerk(player, ArcherTree.ExplorerId)) {
				
				if (player.getFoodLevel() > evt.getFoodLevel() && new Random(Calendar.getInstance().getTimeInMillis()).nextInt(2) == 1) {
					evt.setCancelled(true);
				}
			}
		}
	}
}
